package com.myapp.book.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import com.myapp.book.common.Constants;
import com.myapp.book.pojo.User;

@Component
public class UserDao extends Dao {
	public UserDao() {
	}

	public User get(String email, String password) throws Exception {
		User user=null;
		try {
			begin();
			Query q = getSession().createQuery("from User where email = :email and password = :password");
			q.setString("email", email);
			q.setString("password", password);
			user = (User) q.uniqueResult();
			close();
		} catch (HibernateException e) {
			System.out.println(e.getMessage());
			rollback();
		}
		return user;
	}

	public User get(String email) {
		User user= null;
		try {
			begin();
			Query q = getSession().createQuery("from User where email = :email");
			q.setString("email", email);
			user = (User) q.uniqueResult();
			close();
			return user;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			rollback();
		}
		return user;

	}
	
	public List<User> get() {			
			Query<User> query = getSession().createQuery("FROM User where role=:customer");
			query.setString("customer",Constants.ROLE_CUSTOMER);
			List<User> list = query.list();
			return list;
	}

	public User registration(User u) throws Exception {
		try {
			begin();
			getSession().save(u);
			commit();
			close();
		} catch (HibernateException e) {
			rollback();
			throw new Exception("Exception while creating user: " + e.getMessage());
		}
		return u;
	}

	public boolean updateUser(String email) throws Exception {
		try {
			begin();
			Query q = getSession().createQuery("from User where email = :email");
			q.setMaxResults(1);
			q.setString("email", email);
			User user = (User) q.uniqueResult();
			if (user != null) {
				if(user.getStatus()==Constants.USER_ACTIVE)
					user.setStatus(Constants.USER_INACTIVE);
				else
					user.setStatus(Constants.USER_ACTIVE);
				getSession().update(user);
				commit();
				close();
				return true;
			} else {
				return false;
			}

		} catch (HibernateException e) {
			rollback();
			throw new Exception("Exception while creating user: " + e.getMessage());
		}

	}

	public User checkInitialUser(String email) {
		User user=null;
		try {
			begin();
			Query q = getSession().createQuery("from User where email = :email");
			q.setString("email", email);
			user = (User) q.uniqueResult();
			close();
		} catch (HibernateException e) {
			rollback();
			System.out.println("Could not get user " + email + e.getMessage());
		}
		return user;
	}

}
