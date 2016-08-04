package com.egen.restapp;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;
import org.junit.FixMethodOrder;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import models.User;
import spark.utils.IOUtils;



public class Tests {
	
	@Test
	public void newUserTest() throws IOException{
		
		String userJson = null;
		try {
			userJson = readJsonFromFile("src/test/resources/newUserTest.json");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TestResponse response = request("POST", "/addUser", userJson);
		assertEquals(200,response.status);
		assertEquals("1630215c-2608-44b9-aad4-9d56d8aafd4c",response.body);
		
	}
	
	@Test
	public void userAlreadyExist() throws IOException{
		String userJson = null;
		try {
			userJson = readJsonFromFile("src/test/resources/newUserTest.json");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TestResponse response = request("POST", "/addUser", userJson);
		assertEquals(409,response.status);
		//assertEquals("1630215c-2608-44b9-aad4-9d56d8aafd4c",response.body);
	}
	
	@Test
	public void userNotFoundForUpdate() throws IOException{
		String userJson = null;
		try {
			userJson = readJsonFromFile("src/test/resources/userNotFoundForUpdateTest.json");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TestResponse response = request("PUT", "/updateUser", userJson);
		assertEquals(404,response.status);
	}
	private String readJsonFromFile(String path) throws IOException{
		String json = FileUtils.readFileToString(new File(path));
		System.out.println(json);
		return json;
		
	}
	
	private TestResponse request(String method, String path, String json) throws IOException {
		
		URL url = new URL("http://localhost:4567" + path);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod(method);
		//connection.setRequestProperty("Content-Type", "application/json");
		
		connection.setDoOutput(true);
		OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
		out.write(json);
		out.close();
		connection.connect();
		int code = connection.getResponseCode();
		String body = "";
		if(code==200){
			body = IOUtils.toString(connection.getInputStream());
		}
		return new TestResponse(code,body);
			
	}
	
	

	private static class TestResponse {
	
		public final String body;
		public final int status;
	
		public TestResponse(int status, String body) {
			this.status = status;
		this.body = body;
	}

	}
}