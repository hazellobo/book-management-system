package com.myapp.book.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import com.myapp.book.exception.BookException;
import com.myapp.book.pojo.Book;
import com.myapp.book.pojo.Genre;

@Component
public class GenreDao extends Dao{
	public GenreDao() {

	}
	
	public List<Genre> list() {
		Query<Genre> query = getSession().createQuery("FROM Genre");
		List<Genre> list = query.list();
		return list;
	}

    public void save(Genre genre) throws BookException {
        try {
            begin();
            genre.setCreatedDate(new Date());
            getSession().save(genre);
            commit();
        } catch (HibernateException e) {
            rollback();
            throw new BookException("Could not add the genre", e);
        }
    }
    
    public Genre getGenre(long id) {
    	return getSession().get(Genre.class, id);
    }
    
    public void deleteGenre(Genre g) throws BookException {
        try {
            begin();
            getSession().delete(g);
            commit();
        } catch (HibernateException e) {
            rollback();
            throw new BookException("Could not delete the genre", e);
        }
    }
    
    public void updateGenre(Genre g) throws BookException {
        try {
            begin();
            g.setCreatedDate(new Date());
            getSession().update(g);
            commit();
        } catch (HibernateException e) {
            rollback();
            throw new BookException("Could not delete the genre", e);
        }
    }
    
    public Genre checkInitialGenre(String name) {
		Genre genre=null;
		try {
			begin();
			Query q = getSession().createQuery("from Genre where name = :name");
			q.setString("name", name);
			genre = (Genre) q.uniqueResult();
			close();
		} catch (HibernateException e) {
			rollback();
			System.out.println("Could not get genre " + name + e.getMessage());
		}
		return genre;
	}
    
    public int batchDelete(List<Long> ids) throws BookException {
    	int res=0;
    	try {
            begin();
    		List<Long> bookIds= getSession().createNativeQuery("SELECT b.id FROM Book b WHERE b.genre_id IN :ids").setParameter("ids", ids).getResultList();
    		List<Long> issuebkds= getSession().createNativeQuery("SELECT b.id FROM IssuedBook b WHERE b.book_id IN :ids").setParameter("ids", bookIds).getResultList();
    		List<Long> issueIds= getSession().createNativeQuery("SELECT b.issue_id FROM IssuedBook b WHERE b.book_id IN :ids").setParameter("ids", bookIds).getResultList();
    		res=getSession().createNativeQuery("DELETE FROM IssuedBook g WHERE g.id IN :ids").setParameter("ids", issuebkds).executeUpdate();
    		res=getSession().createNativeQuery("DELETE FROM book g WHERE g.id IN :ids").setParameter("ids", bookIds).executeUpdate();
    		res=getSession().createNativeQuery("DELETE FROM Issue g WHERE g.id IN :ids").setParameter("ids", issueIds).executeUpdate();
    		res=getSession().createNativeQuery("DELETE FROM Genre g WHERE g.id IN :ids").setParameter("ids", ids).executeUpdate();
    		commit();
        } catch (HibernateException e) {
            rollback();
            throw new BookException("Could not delete the genre", e);
        }
    	return res;
	}

}
