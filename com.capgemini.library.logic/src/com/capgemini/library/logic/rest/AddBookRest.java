package com.capgemini.library.logic.rest;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import com.capgemini.library.logic.model.BookVO;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AddBookRest {

	private static final String URL = "http://localhost:9721/workshop/services/books/book";
	private Collection<BookVO> books = new ArrayList<>();
	private final ObjectMapper objectMapper = new ObjectMapper();

	public void addBook(BookVO book) throws Exception {

		String bookJSON = new ObjectMapper().writeValueAsString(book);

		String url = URL;

		HttpClient client = HttpClientBuilder.create().build();
		HttpPost postRequest = new HttpPost(url);
		postRequest.setHeader("Content-Type", "application/json");
		postRequest.setEntity(new StringEntity(bookJSON));
		HttpResponse response2 = client.execute(postRequest);
		if (response2.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Failed: HTTP error code: " + response2.getStatusLine().getStatusCode());
		}

	}

}
