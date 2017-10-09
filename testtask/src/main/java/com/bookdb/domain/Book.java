package com.bookdb.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "book")
public class Book {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(name = "isbn", nullable = false)
	private int isbn;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "price", nullable = false)
	private double price;

	@Column(name = "description", nullable = false)
	private String description;

	public Book() {
		super();
	}

	public Book(int isbn, String title, double price, String description) {
		super();
		this.isbn = isbn;
		this.title = title;
		this.price = price;
		this.description = description;
	}

	public Book(Book book) {
		super();
		this.isbn = book.isbn;
		this.title = book.title;
		this.price = book.price;
		this.description = book.description;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIsbn() {
		return isbn;
	}

	public void setIsbn(int isbn) {
		this.isbn = isbn;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "{isbn=" + isbn + ", title=" + title + ", price=" + price + ", description=" + description + "}";
	}

	public String toStringShort() {
		return "{isbn=" + isbn + ", title=" + title + ", price=" + price + "}";
	}

}
