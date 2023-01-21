package com.myapp.book.controller;

import java.util.ArrayList;
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
import org.springframework.web.bind.support.SessionStatus;
import com.myapp.book.common.Constants;
import com.myapp.book.dao.GenreDao;
import com.myapp.book.exception.BookException;
import com.myapp.book.pojo.Genre;
import com.myapp.book.pojo.User;

@Controller
public class GenreController {
	
	@Autowired
	GenreDao genreDao;
	
	@GetMapping("/manage-genre.htm")
	public String manageGenre(ModelMap model, Genre genre,HttpServletRequest request) {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute(Constants.LOGGED_IN_USER);
		if(user!=null && user.getStatus()==Constants.USER_ACTIVE && user.getRole().equalsIgnoreCase(Constants.ROLE_ADMIN)) {
			model.addAttribute("genre", genre);
			model.addAttribute("genreList",genreDao.list());
			return "manage-genre";
		}
		return "user-login";	
	}

	@PostMapping("/addgenre.htm")
	public String addGenrePost(@Valid @ModelAttribute("genre") Genre genre, BindingResult result,
			SessionStatus status, HttpServletRequest request,ModelMap model) {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute(Constants.LOGGED_IN_USER);
		Genre update=(Genre) session.getAttribute("update");
		if(user!=null && user.getStatus()==Constants.USER_ACTIVE && user.getRole().equalsIgnoreCase(Constants.ROLE_ADMIN)) {
		try {
			if(genre!=null) {
				Genre gen=genreDao.checkInitialGenre(genre.getName());
				if(gen!=null)
					result.addError(new FieldError("genre", "name", "Genre already exists"));
			}
			if(result.hasErrors()){
				return manageGenre(model, genre, request);
			}
			if(update!=null) {
				genre.setId(update.getId());
				genreDao.updateGenre(genre);
				model.addAttribute("msg","Genre "+genre.getName()+" updated successfully");
				session.setAttribute("update",null);
			}else {
				genreDao.save(genre);
				model.addAttribute("msg","Genre "+genre.getName()+" added successfully");
			}			
		} catch (BookException e) {
			System.out.println("Genre cannot be Added: " + e.getMessage());
		}
		status.setComplete();
		return manageGenre(model, genre, request);
	}
		return "user-login";
	}
		
	@RequestMapping(value="/genre-operation.htm", method = RequestMethod.POST)
	public String bookOperations(HttpServletRequest request) {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute(Constants.LOGGED_IN_USER);
		if(user!=null && Constants.USER_ACTIVE==user.getStatus()
				&& user.getRole().equalsIgnoreCase(Constants.ROLE_ADMIN)) {
		String deleteop = request.getParameter("delete");
		String updateop = request.getParameter("update");
			if(deleteop!=null) {
				return deleteGenre(request);
			}else if(updateop!=null) {
				return update(request);
			}
		}
		return "user-login";
	}
	
	public String update(HttpServletRequest request) {
		String[] genreIds = request.getParameterValues("genreId");
		if(genreIds==null || genreIds.length>1) {
			request.setAttribute("genreList",genreDao.list());
			request.setAttribute("checkbox", "Please select only one genre at a time");
			request.setAttribute("genre", new Genre());
			return "manage-genre";			
		}
		Genre gen=genreDao.getGenre(Long.parseLong(genreIds[0]));
		request.getSession().setAttribute("update",gen);
		request.setAttribute("genreList",genreDao.list());
		request.setAttribute("genre",gen);
		return "manage-genre";		
	}
	
	public String deleteGenre(HttpServletRequest request) {
		String[] genreIds = request.getParameterValues("genreId");
		if(genreIds==null || genreIds.length<1) {
			request.setAttribute("checkbox", "Please select atleast one genre");
			request.setAttribute("genre", new Genre());
			request.setAttribute("genreList",genreDao.list());
			return "manage-genre";			
		}
		List<Long> genreIdslist = new ArrayList<Long>();
		try {
			for(int i=0 ; i<genreIds.length ; i++) {
				genreIdslist.add( Long.parseLong(genreIds[i]) );
			}
			int result=genreDao.batchDelete(genreIdslist);		
			request.setAttribute("checkbox",result+" record(s) deleted");
		} catch (BookException e) {
			e.printStackTrace();
		}
		request.setAttribute("genreList",genreDao.list());
		request.setAttribute("genre",new Genre());
		return "manage-genre";
	}
}
