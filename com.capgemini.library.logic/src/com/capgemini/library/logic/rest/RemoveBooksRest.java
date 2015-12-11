package com.capgemini.library.logic.rest;

import java.net.HttpURLConnection;
import java.net.URL;

public class RemoveBooksRest {

	private static final String URL = "http://localhost:9721/workshop/services/books/";

	public void removeBook(String id) throws Exception {

		String url = URL + "book/" + id;

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestProperty("Content-Type", null);
		con.setRequestMethod("DELETE");

		con.setDoOutput(true);
		con.connect();

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'Delete' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		System.out.println("rem");

	}

}
