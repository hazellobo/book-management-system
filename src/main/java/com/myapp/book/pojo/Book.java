package com.myapp.book.pojo;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.myapp.book.common.Constants;


@Component
@Entity
//@Table(name="book")
public class Book {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;
	
	@NotBlank(message="Book name is mandatory")
	private String title;
	
	@NotBlank(message="Author is mandatory")
	private String author;
	
	@NotBlank(message="ISBN is mandatory")
	private String isbn;
	
	private Integer status;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "lastmodified_date")
	private Date lastModifiedDate;
	
	@ManyToOne
	@JoinColumn(name = "genre_id")
	private Genre genre;

	@Lob
	private String imagePath;
	
	@Transient
	private MultipartFile imageFile;
	
	@Lob
	@Column(name = "photo")
	private byte[] bytePhoto;
	
	private String bookStatus;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Genre getGenre() {
		return genre;
	}
	public void setGenre(Genre genre) {
		this.genre = genre;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public MultipartFile getImageFile() {
		return imageFile;
	}
	public void setImageFile(MultipartFile imageFile) {
		this.imageFile = imageFile;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	
	public void setBookStatus() {
		if(this.status==Constants.BOOK_AVAILABLE) {
			this.bookStatus="AVAILABLE";
		}else if(this.status==Constants.BOOK_ISSUED) {
			this.bookStatus="ISSUED";
		}
	}
	
	public String getBookStatus() {
		return this.bookStatus;
	}

	public byte[] getBytePhoto() {
		return bytePhoto;
	}

	public void setBytePhoto(byte[] bytePhoto) {
		this.bytePhoto = bytePhoto;
	}

	public void setBookStatus(String bookStatus) {
		this.bookStatus = bookStatus;
	}

	
	
		
}
