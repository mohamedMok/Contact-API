package com.ebiz.ressources;

import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.*;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import groovyx.net.http.ContentType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.ebiz.Application;
import com.ebiz.dao.myBatissDAO.UserDao;
import com.jayway.restassured.*;

import org.hamcrest.Matchers.*;

import com.jayway.restassured.matcher.RestAssuredMatchers.*;


public class UserControllerTest {

	@Autowired
	private UserDao userDao;
/*
	@Test
	public void canCreateUser(){
		String fluxJson = "{\"username\":\"UserTest\",\"password\":\"passwordTest\", \"age\":30,\"phone\":\"1234567890\", \"mail\":\"user@test.com\"}";
		//Response res = post("/contact/users", fluxJson);
		//assertEquals(201, res.getStatusCode());
		given().contentType(ContentType.JSON).body(fluxJson).when()
				.post("/contact/users/");

	}

	@Test
	public void canCreateAdress(){
		String fluxJson =  "{\"rue\":\"rueTest\",\"city\":\"cityTest\",\"country\":\"countryTest\"}";
		Response res = given().contentType(ContentType.JSON).body(fluxJson).when()
				.post("/contact/users/adresses");
		System.out.println("ADRESS  " + res.getStatusCode());
		assertEquals(201, res.getStatusCode());
	}

	@Test
	public void canGetAllUsers(){
	 	Response res = get("/contact/users/");
		assertEquals(200, res.getStatusCode());
	}

	@Test
	public void canGetAdressesByUser(){
		Response res = get("/contact/users/AUtQSoZvpgAePibQglSm/adresses");
		assertEquals(200, res.getStatusCode());
	}

	@Test
	public void canDeleteUser(){
		Response res = delete("/contact/users/id");
		assertEquals(204, res.getStatusCode());
	}

	@Test
	public void canDeleteAdress(){
		Response res = delete("/contact/users/id/adresses/idAdress");
	}


	@Test
	public void canGetUser(){
		Response res = get("/contact/users/AUtQSoZvpgAePibQglSm/");
		assertEquals(200, res.getStatusCode());
		String json = res.asString();
		JsonPath jp = new JsonPath(json);
		assertEquals("Test", jp.get("username"));
		//assertEquals(1,1)
	}*/
}