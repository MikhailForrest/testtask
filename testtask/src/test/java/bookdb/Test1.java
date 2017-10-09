package bookdb;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bookdb.domain.Book;
import com.bookdb.manager.BookManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class Test1 {
	@Autowired
	BookManager bmanager;

	@Test
	public void Tst() {
		Book book1 = new Book(1, "TITLE", 23, "Description");
		bmanager.addBookPlace(book1);
		Book book2 = bmanager.getBookPlace(1);
		assertEquals("TITLE", book2.getTitle());
	}
}
