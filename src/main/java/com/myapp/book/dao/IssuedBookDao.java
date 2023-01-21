package com.myapp.book.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import com.myapp.book.common.Constants;
import com.myapp.book.exception.BookException;
import com.myapp.book.pojo.Book;
import com.myapp.book.pojo.Issue;
import com.myapp.book.pojo.IssuedBook;

@Component
public class IssuedBookDao extends Dao{

	public List<IssuedBook> getAll() {
		Query<IssuedBook> query = getSession().createQuery("FROM IssuedBook");
		List<IssuedBook> list = query.list();
		return list;
	}
	
	public IssuedBook get(Long id) {
		return getSession().get(IssuedBook.class, id);
	}
	
	public IssuedBook save(IssuedBook issuedBook) {
		return (IssuedBook) getSession().save(issuedBook);
	}
	
	public void update(IssuedBook issuedBook){
        try {
            begin();
            getSession().update(issuedBook);
            commit();
        } catch (HibernateException e) {
            rollback();
        }
    }
	
	public IssuedBook addNew(IssuedBook issuedBook) throws Exception {
		try {
			begin();
			issuedBook.setReturned( Constants.BOOK_NOT_RETURNED );
			getSession().save(issuedBook);
			commit();
			close();
		} catch (HibernateException e) {
			rollback();
			throw new Exception("Exception while creating issuebook: " + e.getMessage());
		}
		return issuedBook;
	}
	
	public List<Book> viewBooksSpecificIssue(Issue issue) {
		Criteria crit= getSession().createCriteria(IssuedBook.class)
		.setProjection(Projections.projectionList()
		.add(Projections.property("book"),"book"))
		.add(Restrictions.eq("issue", issue));
		 return crit.list();
	}

}
