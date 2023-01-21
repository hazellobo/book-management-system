package com.myapp.book.controller;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import com.myapp.book.common.Constants;
import com.myapp.book.dao.UserDao;
import com.myapp.book.pojo.User;



@Controller
public class UserController {
	
	@Autowired
	private UserDao userDao;
	
	@Autowired 
	private JavaMailSender mail;
	
	@GetMapping("/")
	public ModelAndView userLoginForm(HttpServletRequest request) {
		HttpSession session = request.getSession();
		ModelAndView mv= new ModelAndView();
		User u = (User) session.getAttribute(Constants.LOGGED_IN_USER);
		if (u == null) {
			mv.setViewName("user-login");
			return mv;
		} else {
			if (u.getRole().equals(Constants.ROLE_ADMIN)) {
				mv.setViewName("admin-dashboard");
			} else if (u.getRole().equals(Constants.ROLE_CUSTOMER)){
				mv.setViewName("customer");
			} 
			return mv;
		}
		}

	@RequestMapping(value = "/login.htm", method = RequestMethod.GET)
	public String showLoginForm(HttpServletRequest request) {
		HttpSession session = request.getSession();
		User u = (User) session.getAttribute(Constants.LOGGED_IN_USER);
		if (u == null) {
			return "user-login";
		}
		return "user-login";
	}

	@RequestMapping(value = "/login.htm", method = RequestMethod.POST)
	public ModelAndView handleLoginForm(HttpServletRequest request) {
		HttpSession session = request.getSession();
		String username = request.getParameter("email");
		String password = request.getParameter("password");
		Map<String,String> map=new HashMap<>();
		ModelAndView mv= new ModelAndView();
		try {
			User u = userDao.get(username, password);
			if (u != null && u.getStatus() == 1) {
				session.setAttribute(Constants.LOGGED_IN_USER, u);
				if (u.getRole().equals(Constants.ROLE_ADMIN)) {
					return new ModelAndView("admin-dashboard");
				}
				else if (u.getRole().equals(Constants.ROLE_CUSTOMER)) {
					return new ModelAndView("customer-dashboard");
				} 
			}else if (u != null && u.getStatus() == 0) {
				map.put("errorMessage", "Account not activated!");
				return new ModelAndView("user-login",map);
			} else {
				map.put("errorMessage", "Invalid credentials!");
				return new ModelAndView("user-login",map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}
	
	@RequestMapping(value = "/logout.htm", method = RequestMethod.GET)
	public String logOut(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.invalidate();
		return "user-login";
	}	
	
	@RequestMapping(value = "/customer.htm", method = RequestMethod.GET)
	public String getHome(HttpServletRequest request) {
		HttpSession session = request.getSession();
		User u = (User) session.getAttribute(Constants.LOGGED_IN_USER);
		if (u == null) {
			return "user-login";
		}
		return "customer-dashboard";
	}
	
	@GetMapping("/register.htm")
	public ModelAndView showUserRegisterForm(HttpServletRequest request,User user,ModelMap map) {
		ModelAndView mv= new ModelAndView();
		HttpSession session = request.getSession();
		User u = (User) session.getAttribute(Constants.LOGGED_IN_USER);
		if (u == null) {
			map.addAttribute("user", user);
			mv.setViewName("userRegistrationForm");
		} else {
			if (u.getRole().equals(Constants.ROLE_ADMIN)) {
				mv.setViewName("admin-dashboard");
			} else if (u.getRole().equals(Constants.ROLE_CUSTOMER)) {
				mv.setViewName("customer-dashboard");
			} 
		}
		return mv;
	}


	@PostMapping("/register.htm")
	public String handleRegistrationForm(@Valid @ModelAttribute("user") User user, BindingResult result, SessionStatus status,HttpServletRequest request, ModelMap map) {
		HttpSession session = request.getSession();
		if(result.hasErrors()) {
			return "userRegistrationForm";
		}
		if(userDao.checkInitialUser(user.getEmail())==null) {
			user.setStatus(Constants.USER_INACTIVE);
			user.setRole(Constants.ROLE_CUSTOMER);
			try {
				URL url;
				url = new URL(request.getRequestURL().toString());
				String scheme = url.getProtocol();
				String host = url.getHost();
				int port = url.getPort();
				
				User u = userDao.registration(user);
				Random rand = new Random();
				int randNum1 = rand.nextInt(5000000);
				int randNum2 = rand.nextInt(5000000);
				try {
					String str = scheme + "://" + host + ":" + port+ "/emailValidation.htm?email="
							+ user.getEmail() + "&key1=" + randNum1 + "&key2=" + randNum2;
					session.setAttribute("key1", randNum1);
					session.setAttribute("key2", randNum2);
					sendEmail(user.getEmail(), "Dear User, \n Click on the link for account activation : " + str,"Account Activation");
				} catch (Exception e) {
					System.out.println("Email could not be sent");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "user-created";
		}else {
			map.addAttribute("error",true);
			map.addAttribute("msg","Email already in use");
		}
		status.setComplete();
		return "userRegistrationForm";
	}
	
	public void sendEmail(String useremail, String message,String subject) {
	try {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setFrom("noreply@bms.com");
	    mailMessage.setTo(useremail);
	    mailMessage.setText(message);
	    mailMessage.setSubject(subject);	
	    // Sending the mail
	    mail.send(mailMessage);
	} catch (Exception e) {
		System.out.println(e.getMessage());
		System.out.println("Email cannot be sent");
	}
}

	@RequestMapping(value = "emailValidation.htm", method = RequestMethod.GET)
	public String validateEmail(HttpServletRequest request,ModelMap map) {
		HttpSession session = request.getSession();
		String email = request.getParameter("email");
		int k1 = Integer.parseInt(request.getParameter("key1"));
		int k2 = Integer.parseInt(request.getParameter("key2"));
		
		if ( session.getAttribute("key1")!=null && session.getAttribute("key2")!=null) {
			int key1=(Integer) session.getAttribute("key1");
			int key2=(Integer) session.getAttribute("key2");
			if(k1==key1 && k2==key2) {
				try {
					boolean updateStatus = userDao.updateUser(email);
					if (updateStatus) {
						return "user-login";
					} else {
						return "error";
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else {
				map.addAttribute("errorMessage", "Link expired , generate new link");
				map.addAttribute("resendLink", true);
				return "error";
			}
			
		} else {
			map.addAttribute("errorMessage", "Link expired , generate new link");
			map.addAttribute("resendLink", true);
			return "error";
		}
		return "user-login";

	}
	
	@RequestMapping(value = "/resendemail.htm", method = RequestMethod.POST)
	public String resendEmail(HttpServletRequest request) {
		HttpSession session = request.getSession();
		String useremail = request.getParameter("username");
		Random rand = new Random();
		int randNum1 = rand.nextInt(5000000);
		int randNum2 = rand.nextInt(5000000);
		try {
			URL url;
			url = new URL(request.getRequestURL().toString());
			String scheme = url.getProtocol();
			String host = url.getHost();
			int port = url.getPort();
			String str = scheme + "://" + host + ":" + port+ "/emailValidation.htm?email="
					+ useremail + "&key1=" + randNum1 + "&key2=" + randNum2;
			session.setAttribute("key1", randNum1);
			session.setAttribute("key2", randNum2);
			sendEmail(useremail, "Dear User, \n Click on the link for account activation : " + str,"Account Activation");
		} catch (Exception e) {
			System.out.println("Email cannot be sent");
		}
		request.setAttribute("msg", "Email has been sent to your inbox , please activate your account to login!");
		return "user-created";
	}
	
	@RequestMapping(value = "/forgotpassword.htm", method = RequestMethod.GET)
	public String getForgotPasswordForm(HttpServletRequest request) {
		return "forgot-password";
	}

	@RequestMapping(value = "/forgotpassword.htm", method = RequestMethod.POST)
	public ModelAndView handleForgotPasswordForm(HttpServletRequest request) {
		Map<String,String> map= new HashMap<>();
		String email = request.getParameter("email");
		if(email!=null) {
			User user = userDao.get(email);
			if(user!=null) {
				sendEmail(email, "Dear User, \n Your password : " + user.getPassword(),"Forgot Password");
				map.put("error", "false");
				map.put("msg","Password sent to your inbox");
				return new ModelAndView("forgot-password",map);
			}
		}
		map.put("error", "true");
		map.put("msg","Please enter valid email");
		return new ModelAndView("forgot-password",map);
}
}

	
	