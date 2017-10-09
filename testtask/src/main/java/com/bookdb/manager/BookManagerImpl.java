package com.bookdb.manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bookdb.domain.Book;
import com.bookdb.repositories.BookRepository;

@Component
public class BookManagerImpl implements BookManager {

	@Autowired
	BookRepository bookRepository;

	/*
	 * @Autowired private WBookService wBookService;
	 */

	public List<Book> getAllBookPlace() {
		return bookRepository.findAll();
	}

	/*
	 * public BookS getBookPlaceSOAP(GetBookRequest request) { GetBookResponse
	 * gbt; gbt = wBookService.getBook(request); /*gbt = new GetBookResponse();
	 * BookS bbbS = new BookS(); bbbS.setBookIdS(1); bbbS.setIsbnS(11);
	 * bbbS.setTitleS("12"); bbbS.setPriceS(34.2);
	 * bbbS.setDescriptionS("jbnsv"); gbt.setBookS(bbbS);
	 */
	/*
	 * return gbt.getBookS(); }
	 */

	public Book getBookPlace(int id) {
		return bookRepository.findOne(id);
	}

	@Transactional
	public void addBookPlace(Book book) {
		bookRepository.save(book);

	}

	@Transactional
	public void updateBookPlace(int id, Book book) {
		Book up_book = bookRepository.findOne(id);
		up_book.setIsbn(book.getIsbn());
		up_book.setTitle(book.getTitle());
		up_book.setPrice(book.getPrice());
		up_book.setDescription(book.getDescription());
		bookRepository.save(up_book);
	}

}
