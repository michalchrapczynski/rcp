package com.capgemini.library.logic.rest;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.capgemini.library.logic.model.BookVO;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

public class FindBooksRest {

	private static final String URL = "http://localhost:9721/workshop/services/books/";
	private Collection<BookVO> books = new ArrayList<>();
	private final ObjectMapper objectMapper = new ObjectMapper();

	public List<BookVO> sendGet(String prefix) throws Exception {

		ObjectMapper mapper = new ObjectMapper();
		String url = URL + "books-by-title?titlePrefix=" + prefix;

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("GET");

		con.setRequestProperty("User-Agent", null);

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		InputStream inputStream = con.getInputStream();
		CollectionType constructCollectionType = mapper.getTypeFactory().constructCollectionType(List.class,
				BookVO.class);
		books = extractBook(inputStream, constructCollectionType);

		System.out.println("Books size: " + books.size());
		return (List<BookVO>) books;

	}

	private List<BookVO> extractBook(InputStream inputStream, CollectionType constructCollectionType)
			throws IOException, JsonParseException, JsonMappingException {
		Object value = objectMapper.readValue(inputStream, constructCollectionType);
		return value instanceof List ? (List<BookVO>) value:new ArrayList<BookVO>();
	}
}
