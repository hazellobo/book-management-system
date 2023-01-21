package com.myapp.book.common;


public class Constants {
	public static final String LOGGED_IN_USER="loggedInUser";
	public static final String ROLE_ADMIN = "admin";
	public static final String ROLE_CUSTOMER = "customer";
	public static final Integer USER_ACTIVE=1;
	public static final Integer USER_INACTIVE=0;
	
	
	public static final Integer BOOK_AVAILABLE = 1;
	public static final Integer BOOK_ISSUED = 2;
	public static final Integer PENDING_RETURN = 3;
	public static final Integer BOOK_NOT_RETURNED = 0;
	public static final Integer BOOK_RETURNED = 1;
	public static final Integer RETURN_DAYS=5;
}
