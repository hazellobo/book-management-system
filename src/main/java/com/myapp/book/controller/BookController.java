package com.myapp.book.controller;


import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.myapp.book.common.Constants;
import com.myapp.book.dao.BookDao;
import com.myapp.book.dao.GenreDao;
import com.myapp.book.dao.IssueDao;
import com.myapp.book.dao.IssuedBookDao;
import com.myapp.book.exception.BookException;
import com.myapp.book.pojo.Book;
import com.myapp.book.pojo.Issue;
import com.myapp.book.pojo.IssuedBook;
import com.myapp.book.pojo.User;


@Controller
public class BookController {
	@Autowired
	private BookDao bookDao;
	
	@Autowired
	private GenreDao genreDao;
	
	@Autowired
	private IssueDao issueDao;
	
	@Autowired
	private IssuedBookDao issuedBookDao;
	
	
	@GetMapping("/manage-books.htm")
	public String listBooksGet(HttpServletRequest request,ModelMap model,Book book) {
		HttpSession session = request.getSession();
		User u = (User) session.getAttribute(Constants.LOGGED_IN_USER);
		if (u == null) {
			return "user-login";
		} else {
			request.setAttribute("genres",genreDao.list());
			model.addAttribute("book", book);
			if (u.getRole().equals(Constants.ROLE_ADMIN)) {
				request.setAttribute("books", bookDao.list());
			} else if (u.getRole().equals(Constants.ROLE_CUSTOMER)){
				request.setAttribute("books", bookDao.listBookByStatus(Constants.BOOK_AVAILABLE));
			} 
		}
		return "manage-books";
	}
	
	@RequestMapping(value = "/searchBook.htm", method =RequestMethod.POST)
	public String searchBooks(HttpServletRequest request,ModelMap model,Book book) {
		HttpSession session = request.getSession();
		User u = (User) session.getAttribute(Constants.LOGGED_IN_USER);
		if (u == null) {
			return "user-login";
		}else {
			String search= request.getParameter("search");
			List<Book> books= new ArrayList<>();
			books=bookDao.searchBooks(search, u);
			request.setAttribute("books", books);
			request.setAttribute("genres",genreDao.list());
			model.addAttribute("book", book);
		}
		return "manage-books";
	}
	

	@PostMapping("/addbook.htm")
	public String addBookPost(@Valid @ModelAttribute("book") Book book, BindingResult result, SessionStatus status,HttpServletRequest request) {
		try {
			if(book.getImageFile().getSize()!=0) {
				CommonsMultipartFile file=(CommonsMultipartFile) book.getImageFile();
				System.out.println("Original Image Byte Size - " + file.getBytes().length);
				book.setBytePhoto(file.getBytes());
			String b = Base64.getEncoder().encodeToString(book.getBytePhoto());
			book.setImagePath(b);
				if(file.getSize()>100000) {
					FieldError nameError= new FieldError("book", "imageFile", "File size exceeded");
					result.addError(nameError);
				}
			}else {
				FieldError nameError= new FieldError("book", "imageFile", "Please select valid image");
				result.addError(nameError);
			}
			if(result.hasErrors()) {
				return "manage-books";
			}
			book.setGenre(genreDao.getGenre(Long.parseLong(request.getParameter("genre"))));
			 if(request.getAttribute("update")!=null && (boolean)request.getAttribute("update")==true)
			 	bookDao.update(book);
			 else
				 bookDao.save(book); 
			 request.setAttribute("success", book.getTitle());
		} catch (IllegalStateException e1) {
			System.out.println("IllegalStateException: " + e1.getMessage());
		} catch (BookException e) {
			System.out.println("Book could not be Added: " + e.getMessage());
		}
		status.setComplete();
		request.setAttribute("genres",genreDao.list());
		request.setAttribute("books", bookDao.list());
		return "manage-books";
	}
	
	@RequestMapping(value="/book-operation.htm", method = RequestMethod.POST)
	public String bookOperations(HttpServletRequest request) {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute(Constants.LOGGED_IN_USER);
		if(user!=null && Constants.USER_ACTIVE==user.getStatus()) {
		String deleteop = request.getParameter("delete");
		String updateop = request.getParameter("update");
		String borrowop= request.getParameter("borrow");
			if(deleteop!=null) {
				return delete(request);
			}else if(updateop!=null) {
				return update(request);
			}else if(borrowop!=null) {
				return borrowBooks(request);
			}
		}
		return "user-login";
	}
	
	@GetMapping("/update-book.htm")
	public String update(HttpServletRequest request){
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute(Constants.LOGGED_IN_USER);
		if(user!=null && user.getStatus()==Constants.USER_ACTIVE && user.getRole().equalsIgnoreCase(Constants.ROLE_ADMIN)){
			String[] bookIdsArr = request.getParameterValues("bookId");
			ModelMap model=new ModelMap();
			request.setAttribute("genres",genreDao.list());
			request.setAttribute("books", bookDao.list());
			if(bookIdsArr==null || bookIdsArr.length>1) {
				request.setAttribute("checkbox", "Please select only one book at a time");
				request.setAttribute("book", new Book());
				return "manage-books";			
			}
			Book book=bookDao.getBook(Long.parseLong(bookIdsArr[0]));
			request.setAttribute("update",true);
			request.setAttribute("book",book);
			return "manage-books";		
			}
		return "user-login";
	}
	
	public String delete(HttpServletRequest request) {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute(Constants.LOGGED_IN_USER);
		if(user!=null && user.getStatus()==Constants.USER_ACTIVE && user.getRole().equalsIgnoreCase(Constants.ROLE_ADMIN)) {
			String[] bookIdsArr = request.getParameterValues("bookId");
			if(bookIdsArr==null || bookIdsArr.length<1) {
				request.setAttribute("checkbox", "Please select atleast one book");
				request.setAttribute("genres",genreDao.list());
				request.setAttribute("books", bookDao.list());
				request.setAttribute("book", new Book());
				return "manage-books";
			}
			ModelMap map = new ModelMap();
			List<Long> bookIdslist = new ArrayList<Long>();
			List<Long> deleteList = new ArrayList<Long>();
			try {
				for(int i=0 ; i<bookIdsArr.length ; i++) {
					bookIdslist.add( Long.parseLong(bookIdsArr[i]) );
				}
				List<Book> books = bookDao.listBooksByStatus(bookIdslist,Constants.BOOK_AVAILABLE);
				for(Book b:books) {
					deleteList.add(b.getId());
				}
				int result=bookDao.deleteBks(deleteList);		
				request.setAttribute("checkbox", "("+result+")records deleted");
			} catch (NumberFormatException ex) {
				ex.printStackTrace();
				return "invalid number format";
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return "redirect:/manage-books.htm";
			}
		return "user-login";
	}
	
//	@RequestMapping(value="/borrow-books.htm", method = RequestMethod.POST)
	public String borrowBooks(HttpServletRequest request) {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute(Constants.LOGGED_IN_USER);
		if(user!=null && user.getStatus()==Constants.USER_ACTIVE && user.getRole().equalsIgnoreCase(Constants.ROLE_CUSTOMER)) {
			String[] bookIdsArr = request.getParameterValues("bookId");
			if(bookIdsArr==null || bookIdsArr.length==0) {
				request.setAttribute("checkbox", "Please select atleast one book");
				request.setAttribute("genres",genreDao.list());
				request.setAttribute("books", bookDao.list());
				request.setAttribute("book", new Book());
				return "manage-books";
			}
			ModelMap map = new ModelMap();
			List<Long> bookIdslist = new ArrayList<Long>();
			try {
				for(int i=0 ; i<bookIdsArr.length ; i++) {
					bookIdslist.add( Long.parseLong(bookIdsArr[i]) );
				}
				List<Book> books = bookDao.get(bookIdslist);
				Issue issue = new Issue();
				issue.setUser(user);
				issue = issueDao.addNew(issue);			
				for( int k=0 ; k<books.size() ; k++ ) {
					Book book = books.get(k);
					book.setStatus(Constants.BOOK_ISSUED);
					bookDao.update(book);
					
					IssuedBook ib = new IssuedBook();
					ib.setBook(book);
					ib.setIssue(issue);
					issuedBookDao.addNew( ib );
				}
			} catch (NumberFormatException ex) {
				ex.printStackTrace();
				return "invalid number format";
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return listBooksGet(request,map,new Book());
			}
		
		return "user-login";
	}
	
	@RequestMapping(value="/manage-return.htm", method = RequestMethod.GET)
	public String getActiveIssues(HttpServletRequest request) {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute(Constants.LOGGED_IN_USER);
		if(user!=null && user.getStatus()==Constants.USER_ACTIVE && user.getRole().equalsIgnoreCase(Constants.ROLE_CUSTOMER)) {	
			List<Issue> issue=issueDao.viewIssuesNotReturned(user);
			request.setAttribute("issues", issue);
			request.setAttribute("view", "return");
			return "manage-return";
	}
		return "user-login";
}
	
	
	@RequestMapping(value="/viewHistory.htm", method = RequestMethod.GET)
	public String userHistory(HttpServletRequest request) {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute(Constants.LOGGED_IN_USER);
		if(user!=null && user.getStatus()==Constants.USER_ACTIVE && user.getRole().equalsIgnoreCase(Constants.ROLE_CUSTOMER)) {
			List<Issue> issue=issueDao.viewAllIssues(user);
			request.setAttribute("issues", issue);
			request.setAttribute("view", "history");
			return "manage-return";
		}
		return "user-login";
	}
	
	public List<Book> notReturnedBooks(long id){
		Issue issue= issueDao.get(id);
		List<Book> books=issuedBookDao.viewBooksSpecificIssue(issue);
		List<Book> result=new ArrayList<>();
		for(Book bk:books) {
			if(bk.getStatus().equals(Constants.BOOK_ISSUED))
				result.add(bk);
		}
		return result;
	}
	
	
	
	@RequestMapping(value="/viewIssuedBooks.htm", method = RequestMethod.GET)
	public String viewNotReturnedIssueBks(@RequestParam("id") long id, HttpServletRequest  request) {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute(Constants.LOGGED_IN_USER);
		if(user!=null && user.getStatus()==Constants.USER_ACTIVE && user.getRole().equalsIgnoreCase(Constants.ROLE_CUSTOMER)) {
			Issue issue= issueDao.get((long)id);
			List<Book> result=notReturnedBooks(id);
			request.setAttribute("books", result);
			request.setAttribute("view", "return");
			session.setAttribute("issue",issue);
			return "view-books";
		}
		return "user-login";
	}
	
	@RequestMapping(value="/viewAllBooks.htm", method = RequestMethod.GET)
	public String viewAllBooksIssued(@RequestParam("id") int id, HttpServletRequest  request) {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute(Constants.LOGGED_IN_USER);
		if(user!=null && user.getStatus()==Constants.USER_ACTIVE && user.getRole().equalsIgnoreCase(Constants.ROLE_CUSTOMER)) {
			Issue issue= issueDao.get((long)id);
			List<Book> books=issuedBookDao.viewBooksSpecificIssue(issue);
			request.setAttribute("books", books);
			request.setAttribute("view", "history");
			request.setAttribute("id", id);
			return "view-books";
		}
		return "user-login";
	}

	@RequestMapping(value = "/return-issues.htm", method = RequestMethod.POST)
	public String returnAll(HttpServletRequest  request) throws BookException {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute(Constants.LOGGED_IN_USER);
		if(user!=null && user.getStatus()==Constants.USER_ACTIVE 
				&& user.getRole().equalsIgnoreCase(Constants.ROLE_CUSTOMER) ){
		
		String[] issueIdsArr = request.getParameterValues("issueId");
		List<Long> issueIdsList = new ArrayList<Long>();
		
		for(int i=0 ; i<issueIdsArr.length ; i++) {
				issueIdsList.add( Long.parseLong(issueIdsArr[i]) );
		}
		List<Issue> issues = issueDao.get(issueIdsList);
		for(Issue issue:issues) {
			List<IssuedBook> issuedBooks = issue.getIssuedBooks();
			for( int k=0 ; k<issuedBooks.size() ; k++ ) {
				IssuedBook ib = issuedBooks.get(k);
				ib.setReturned( Constants.BOOK_RETURNED );
				issuedBookDao.update( ib );
				
				Book book = ib.getBook();
				book.setStatus( Constants.BOOK_AVAILABLE );
				bookDao.update(book);
			}
			issue.setReturned( Constants.BOOK_RETURNED);
			issueDao.update(issue);
		}
		return getActiveIssues(request);
		}
		return "user-login";
}
		
	
	@RequestMapping(value="/return-books.htm", method = RequestMethod.POST)
	public String returnSelectedBooks(HttpServletRequest request) throws BookException {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute(Constants.LOGGED_IN_USER);
		if(user!=null && user.getStatus()==Constants.USER_ACTIVE && user.getRole().equalsIgnoreCase(Constants.ROLE_CUSTOMER)) {
			Issue issue=(Issue) session.getAttribute("issue");
			String[] bookIdsArr = request.getParameterValues("bookId");
			List<Long> bookIdsList = new ArrayList<Long>();
			
			for(int i=0 ; i<bookIdsArr.length ; i++) {
					bookIdsList.add( Long.parseLong(bookIdsArr[i]) );
				}
			if( issue != null ) {
				List<IssuedBook> issuedBks = issue.getIssuedBooks();
				for( int k=0 ; k<issuedBks.size() ; k++ ) {
					IssuedBook ib = issuedBks.get(k);
					if(bookIdsList.contains(ib.getBook().getId())) {
						ib.setReturned(Constants.BOOK_RETURNED);
						issuedBookDao.update(ib);
						Book book = ib.getBook();
						book.setStatus( Constants.BOOK_AVAILABLE);
						bookDao.update(book);
					}
				}			
			List<Book> res=notReturnedBooks(issue.getId());
			if(res.size()>0) {
				issue.setReturned( Constants.PENDING_RETURN);
				issueDao.update(issue);
				return viewNotReturnedIssueBks(issue.getId(), request);
			}else {
				issue.setReturned( Constants.BOOK_RETURNED);
				issueDao.update(issue);
				return getActiveIssues(request);
			}
			}
			return getActiveIssues(request);
	}
		return "user-login";
}
	
	@GetMapping("/book-report.pdf")
	public ModelAndView exportToPdf(HttpServletRequest request) {
		HttpSession session = request.getSession();
		 ModelAndView mv = new ModelAndView();
		 ModelMap model= new ModelMap();
		User user = (User) session.getAttribute(Constants.LOGGED_IN_USER);
		if(user!=null && user.getStatus()==Constants.USER_ACTIVE && user.getRole().equalsIgnoreCase(Constants.ROLE_ADMIN)) {
		
	   
	    mv.setView(new ExportPdfReport());
	    List<Book> list= bookDao.list();
	    if(list!=null) {
		    mv.addObject("list", list);
		    return mv; 
	    }else {
		    model.addAttribute("checkbox", "Please select atleast one book");
		    model.addAttribute("genres",genreDao.list());
		    model.addAttribute("books", bookDao.list());
		    model.addAttribute("book", new Book());
			return new ModelAndView("manage-books", model);
		    }
		}
	    mv.setViewName("user-login");
	    return mv;
	
	}		
}