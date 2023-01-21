package com.myapp.book.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.myapp.book.common.Constants;
import com.myapp.book.dao.UserDao;
import com.myapp.book.pojo.User;

@Controller
public class AdminController {
	
	@Autowired
	UserDao userDao;
	
	
	@RequestMapping(value = "/admin.htm", method = RequestMethod.GET)
	public String getHome(HttpServletRequest request) {
		HttpSession session = request.getSession();
		User u = (User) session.getAttribute(Constants.LOGGED_IN_USER);
		if (u == null) {
			return "user-login";
		}
		return "admin-dashboard";
	}
	
	@GetMapping("/manage-users.htm")
	public ModelAndView showUsersAdmin(HttpServletRequest request) {
		HttpSession session = request.getSession();
		User u = (User) session.getAttribute(Constants.LOGGED_IN_USER);
		ModelAndView mv = new ModelAndView();
		if (u == null) {
			mv.setViewName("user-login");
			return mv;
		}
		if (u.getRole().equals(Constants.ROLE_ADMIN)) {
			mv.addObject("admin","admin");
			List<User> userList = new ArrayList<>();
			userList = userDao.get();
			mv.addObject("userList", userList);
			mv.setViewName("manage-users");
			return mv;
		}
		return mv;
	}

	@PostMapping("/update-status.htm")
	public ModelAndView editUsersStatusAdmin(HttpServletRequest request) {
		HttpSession session = request.getSession();
		String email= request.getParameter("email");
		User u = (User) session.getAttribute(Constants.LOGGED_IN_USER);
		ModelAndView mv = new ModelAndView();
		try {
			if (u == null) {
				mv.setViewName("user-login");
				return mv;
			}
			if (u.getRole().equals(Constants.ROLE_ADMIN)) {
				userDao.updateUser(email);
				return showUsersAdmin(request);
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return mv;
	}
	
	
		
}
