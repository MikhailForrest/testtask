package com.bookdb.jms;

import java.io.IOException;

import javax.jms.JMSException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.bookdb.domain.Book;
import com.bookdb.manager.BookManager;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class Receiver {
	@Autowired
	BookManager bmanager;

	@JmsListener(containerFactory = "jmsListenerContainerFactory", destination = "myqueue")

	public void processMessage(String message) throws JMSException, JsonParseException, JsonMappingException, IOException {
		Book bbb = new ObjectMapper().readValue(message, Book.class);
		bmanager.addBookPlace(bbb);
		System.out.println("Ok");
	}
}
