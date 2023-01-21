package com.myapp.book.dao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import com.myapp.book.common.Constants;
import com.myapp.book.pojo.Issue;
import com.myapp.book.pojo.IssuedBook;
import com.myapp.book.pojo.User;


@Component
public class IssueDao extends Dao {

	public List<Issue> getAll(){
		Query<Issue> query = getSession().createQuery("FROM Issue");
		List<Issue> list = query.list();
		return list;
	}
	
	public Issue get(Long id) {
    	return getSession().get(Issue.class, id);
	}
	
	public List<Issue> getAllUnreturned() {
		Query<Issue> query = getSession().createQuery("FROM issue where returned=:status");
		query.setLong("status",Constants.BOOK_NOT_RETURNED);
		List<Issue> list = query.list();
		return list;
	}
	
	public Issue addNew(Issue issue) throws Exception {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, Constants.RETURN_DAYS);
		try {
			begin();
			issue.setIssueDate( new Date() );
			issue.setExpectedReturnDate((c.getTime()));
			issue.setReturned( Constants.BOOK_NOT_RETURNED );
			issue.setIssueStatus();
			getSession().save(issue);
			commit();
			close();
		} catch (HibernateException e) {
			rollback();
			throw new Exception("Exception while creating issue: " + e.getMessage());
		}
		return issue;
	}
	
	public Issue save(Issue issue) {
		issue.setIssueStatus();
		return (Issue) getSession().save(issue);
	}
	
	public void update(Issue issue){
        try {
            begin();
            issue.setIssueStatus();
            getSession().update(issue);
            commit();
        } catch (HibernateException e) {
            rollback();
        }
    }

	public List<Issue> viewIssuesNotReturned(User u) {
		// TODO Auto-generated method stub
		Criteria crit = getSession().createCriteria(Issue.class);
		crit.add(Restrictions.eq("user",u));
		Disjunction orConditions = Restrictions.disjunction();
		orConditions.add(Restrictions.in("returned", Constants.BOOK_NOT_RETURNED));
		orConditions.add(Restrictions.in("returned", Constants.PENDING_RETURN));
		crit.add(orConditions); 
		crit.addOrder(Order.desc("issueDate"));
		List<Issue> issues= crit.list();
		return issues;
	}
	
	public List<Issue> viewAllIssues(User u) {
		// TODO Auto-generated method stub
		Criteria crit = getSession().createCriteria(Issue.class);
		crit.add(Restrictions.eq("user",u));
		crit.addOrder(Order.desc("issueDate"));
		List<Issue> issues= crit.list();
		return issues;
	}
	
	public List<Issue> getIssueIssuedBooks(Issue issue) {
		// TODO Auto-generated method stub
		Criteria crit = getSession().createCriteria(IssuedBook.class);
		crit.add(Restrictions.eq("issue",issue));
		crit.add(Restrictions.eq("returned",Constants.BOOK_NOT_RETURNED));
		crit.addOrder(Order.desc("issueDate"));
		List<Issue> issues= crit.list();
		return issues;
	}
	
	public List<Issue> get(List<Long> ids) {
		List<Issue> issues = getSession().createQuery("SELECT i FROM Issue i WHERE i.id IN :ids").setParameter("ids", ids).getResultList();
		return issues;
	}
}
