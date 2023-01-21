package com.myapp.book.pojo;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.myapp.book.common.Constants;

@Component
@Entity
public class Issue{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "issue_date")
	private Date issueDate;
	
	@Column(name = "notes")
	private String notes;
	
	@Column(name = "expected_return_date")
	private Date expectedReturnDate;
	
	@Column(name = "returned")
	private Integer returned;
	
	@JsonIgnore
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	
	@JsonIgnore
	@OneToMany(mappedBy = "issue", cascade = CascadeType.ALL)
	private List<IssuedBook> issuedBooks;
	
	private String issueStatus;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Date getExpectedReturnDate() {
		return expectedReturnDate;
	}

	public void setExpectedReturnDate(Date expectedReturnDate) {
		this.expectedReturnDate = expectedReturnDate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<IssuedBook> getIssuedBooks() {
		return issuedBooks;
	}

	public void setIssuedBooks(List<IssuedBook> issuedBooks) {
		this.issuedBooks = issuedBooks;
	}

	public Integer getReturned() {
		return returned;
	}

	public void setReturned(Integer returned) {
		this.returned = returned;
	}
	
	public void setIssueStatus() {
		if(this.returned==Constants.BOOK_NOT_RETURNED){
			this.issueStatus="NOT RETURNED";
		}else if(this.returned==Constants.BOOK_RETURNED){
			this.issueStatus="RETURNED";
		}else if(this.returned==Constants.PENDING_RETURN){
			this.issueStatus="PENDING RETURN";
		}
	}
	
	public String getIssueStatus() {
		return this.issueStatus;
	}
	
}
