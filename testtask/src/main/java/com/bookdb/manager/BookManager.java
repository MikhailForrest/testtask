package com.bookdb.manager;

import java.util.List;

import com.bookdb.domain.Book;

public interface BookManager {

	List<Book> getAllBookPlace();

	// BookS getBookPlaceSOAP(GetBookRequest request);

	Book getBookPlace(int id);

	void addBookPlace(Book book);

	void updateBookPlace(int id, Book book);

	/* void deleteBookPlace(int id); */
}
