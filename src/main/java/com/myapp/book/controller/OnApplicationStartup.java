package com.myapp.book.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.myapp.book.dao.UserDao;
import com.myapp.book.pojo.User;

@Component
public class OnApplicationStartup {
	 @Autowired
	 UserDao userDao;

	 @EventListener
	 public void appReady(ApplicationReadyEvent event) {
	    User existUser = userDao.checkInitialUser("admin@admin.com");
		if (existUser == null) {
			User u = new User();
			u.setFname("Admin");
			u.setLname("Admin");
			u.setEmail("admin@admin.com");
			u.setPassword("123@Hazel");
			u.setStatus(1);
			u.setRole("admin");
			
			User u1 = new User();
			u1.setFname("Hazel");
			u1.setLname("Lobo");
			u1.setEmail("hazel@gmail.com");
			u1.setPassword("123@Hazel");
			u1.setStatus(1);
			u1.setRole("customer");
			
			User u2 = new User();
			u2.setFname("Akshay");
			u2.setLname("Barne");
			u2.setEmail("akshay@gmail.com");
			u2.setPassword("123@Hazel");
			u2.setStatus(1);
			u2.setRole("customer");
			
		try {
				userDao.registration(u);
				userDao.registration(u1);
				userDao.registration(u2);
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
}
	    
	    
}
