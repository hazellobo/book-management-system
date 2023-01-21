package com.myapp.book.dao;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import com.myapp.book.common.Constants;
import com.myapp.book.exception.BookException;
import com.myapp.book.pojo.Book;
import com.myapp.book.pojo.Genre;
import com.myapp.book.pojo.User;

@Component
public class BookDao extends Dao {
    public boolean save(Book book) throws BookException {
    	boolean status=false;
        try {
            begin();
            book.setStatus(Constants.BOOK_AVAILABLE);
            book.setCreatedDate(new Date());
            book.setBookStatus();
            getSession().save(book);
            commit();
            status=true;;
        } catch (HibernateException e) {
            rollback();
            throw new BookException("Could not delete book", e);
        }
        return status;
    }
    
    public void update(Book book) throws BookException {
        try {
            begin();
            book.setLastModifiedDate(new Date());
            book.setBookStatus();
            getSession().update(book);
            commit();
        } catch (HibernateException e) {
            rollback();
            throw new BookException("Could not delete book", e);
        }
    }
    
    public int deleteBks(List<Long> ids) throws BookException {
        int res=0;
    	try {
        	begin();
        	List<Long> issuebkds= getSession().createNativeQuery("SELECT b.id FROM IssuedBook b WHERE b.book_id IN :ids").setParameter("ids", ids).getResultList();
    		List<Long> issueIds= getSession().createNativeQuery("SELECT b.issue_id FROM IssuedBook b WHERE b.book_id IN :ids").setParameter("ids", ids).getResultList();
    		res=getSession().createNativeQuery("DELETE FROM IssuedBook g WHERE g.id IN :ids").setParameter("ids", issuebkds).executeUpdate();
    		res=getSession().createNativeQuery("DELETE FROM Issue g WHERE g.id IN :ids").setParameter("ids", issueIds).executeUpdate();
    		res=getSession().createNativeQuery("DELETE FROM book g WHERE g.id IN :ids").setParameter("ids", ids).executeUpdate();
        	commit();
        } catch (HibernateException e) {
            rollback();
            throw new BookException("Could not delete book", e);
        }
		return res;
    }
    
    public List<Book> list() {
		Query<Book> query = getSession().createQuery("FROM Book");
		List<Book> list = query.list();
		return list;
	}
    
    public List<Book> searchBooks(String search,User u) {
		Criteria criteria = getSession().createCriteria(Book.class);
        Conjunction obj = Restrictions.conjunction();
        if(u.getRole().equalsIgnoreCase(Constants.ROLE_CUSTOMER))
        	obj.add(Restrictions.eq("status", Constants.BOOK_AVAILABLE));
        if(search!=null)
        	obj.add(Restrictions.like("title",search,MatchMode.ANYWHERE));
        criteria.add(obj);
        List<Book> res=criteria.list();
		return res;
	}
    
    public List<Book> listBookByStatus(int status) {
		Query<Book> query = getSession().createQuery("FROM Book where status=:id");
		query.setInteger("id",status);
		List<Book> list = query.list();
		return list;
	}
    
    @SuppressWarnings("deprecation")
	public List<Book> listBooksByStatus(List<Long> ids, int status) {
		Query<Book> query = getSession().createQuery("FROM Book b where b.status=:status and b.id IN :ids ");
		query.setInteger("status",status);
		query.setParameter("ids",ids);
		List<Book> list = query.list();
		return list;
	}
    
    public List<Book> get(List<Long> ids) {
		List<Book> books = getSession().createQuery("SELECT b FROM Book b WHERE b.id IN :ids").setParameter("ids", ids).getResultList();
		return books;
	}
    
    public Book getBook(long id) {
    	return getSession().get(Book.class, id);
    }

 }
 
    
