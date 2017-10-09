package com.bookdb.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bookdb.domain.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
	/*
	 * @Modifying
	 * 
	 * @Query("update Book b set b.isbn = ?1, b.title = ?2, b.price = ?3, b.description = ?4 where b.id = ?5"
	 * ) void updateBook(int isbn, String title, double price, String
	 * description, int id);
	 */

}
