package com.connoisseur.services;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("scratch")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CnsUserApplicationTests {
	@Autowired
	private WebApplicationContext context;

	private MockMvc mvc;

	private static final String ADMIN_AUTH_TOKEN = "0f1a61c8-ccea-491e-a57c-cb76cc898a10";
	private static final String HEADER_X_AUTH_TOKEN = "X-Auth-Token";

	@Before
	public void setUp() {
		this.mvc = MockMvcBuilders.webAppContextSetup(this.context).build();
	}

	@Test
	public void t0Home() throws Exception {
		this.mvc.perform(get("/")).andExpect(status().isOk())
				.andExpect(content().string(containsString("Welcome to Connoisseur Account Service")));
	}

	@Test
	public void t0authAdmin() throws Exception {

		MvcResult result = this.mvc.perform(
				post("/user/loginsession").contentType(MediaType.APPLICATION_JSON).content("{\"email\":\"rayxiaonet@gmail.com\",\"password\":\"password1\"}"))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(jsonPath("userId", equalTo(1)))
				.andExpect(jsonPath("token", notNullValue()))
				.andExpect(status().isOk())
				.andReturn();
		JsonElement root = new JsonParser().parse(result.getResponse().getContentAsString());
		String returnedToken = root.getAsJsonObject().get("token").getAsString();
		System.out.println("The token is:" + returnedToken);
	}

	@Test
	public void t2findAdminUser() throws Exception {

		this.mvc.perform(
				get("/user/1").header(HEADER_X_AUTH_TOKEN, ADMIN_AUTH_TOKEN))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("userName", equalTo("rayxiaonet@gmail.com")))
				.andExpect(jsonPath("userType", equalTo("A")));
	}

	@Test
	public void t2allusers() throws Exception {

		this.mvc.perform(
				get("/user").header(HEADER_X_AUTH_TOKEN, ADMIN_AUTH_TOKEN))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(status().isOk());
	}

//
//	@Test
//	public void findByContaining() throws Exception {
//
//		this.mvc.perform(
//				get("/api/cities/search/findByNameContainingAndCountryContainingAllIgnoringCase?name=&country=UK"))
//				.andExpect(status().isOk())
//				.andExpect(jsonPath("_embedded.cities", hasSize(3)));
//	}

}
