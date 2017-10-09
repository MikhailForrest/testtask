package com.bookdb.rest;

import java.util.List;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bookdb.domain.Book;
import com.bookdb.manager.BookManager;

@RestController
@Path("")
public class Myrest {

	@Autowired
	BookManager bmanager;

	@GET
	@Path("/books")
	@Produces("text/html; charset=UTF-8")
	public Response PrintAllBooks() {

		List<Book> bbooks = bmanager.getAllBookPlace();
		StringBuffer str1 = new StringBuffer("");
		for (Book bbb : bbooks) {
			str1.append(bbb.toStringShort());
		}
		String str = str1.toString();
		return Response.status(200).entity(str).build();
	}

	/*
	 * @GET
	 * 
	 * @Path("/booksSOAP/{id}")
	 * 
	 * @Produces("application/soap+xml") public Response
	 * PrintAllBooksSOAP(@PathParam("id") int id) { GetBookRequest gbr = new
	 * GetBookRequest(); gbr.setBookIdS(id); BookS bbbS =
	 * bmanager.getBookPlaceSOAP(gbr); /* BookS bbbS = new BookS();
	 * bbbS.setBookIdS(1); bbbS.setIsbnS(11); bbbS.setTitleS("12");
	 * bbbS.setPriceS(34.2); bbbS.setDescriptionS("jbnsv");
	 */

	/*
	 * Book bbb = new Book(bbbS.getIsbnS(), bbbS.getTitleS(), bbbS.getPriceS(),
	 * bbbS.getDescriptionS()); String str = bbb.toString(); return
	 * Response.status(200).entity(str).build(); }
	 */

	@GET
	@Path("/book/{id}")
	@Produces("text/html; charset=UTF-8")
	public Response PrintOneBook(@PathParam("id") int id) {

		Book bbb = bmanager.getBookPlace(id);
		String str = "";
		if (bbb != null) {
			str = bbb.toString();
		} else {
			str = "нет такого элемента";
		}
		return Response.status(200).entity(str).build();
	}

	@PUT
	@Path("/book/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public @ResponseBody Response UpdateBook(@PathParam("id") int id, @Valid @RequestBody Book request) {
		bmanager.updateBookPlace(id, request);
		return Response.status(200).entity("Update").build();
	}

	@POST
	@Path("/book")
	@Consumes(MediaType.APPLICATION_JSON)
	public @ResponseBody Response newBook(@Valid @RequestBody Book request) throws Exception {
		bmanager.addBookPlace(request);
		return Response.status(200).entity("Add").build();
	}

}
