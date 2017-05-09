package com.connoisseur.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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
import static org.junit.Assert.assertEquals;
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
	public void home() throws Exception {
		this.mvc.perform(get("/")).andExpect(status().isOk())
				.andExpect(content().string(containsString("Welcome to Connoisseur Account Service")));
	}

	@Test
	public void authAdmin() throws Exception {

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
	public void findAdminUser() throws Exception {

		this.mvc.perform(
				get("/user/1").header(HEADER_X_AUTH_TOKEN, ADMIN_AUTH_TOKEN))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("userName", equalTo("rayxiaonet@gmail.com")))
				.andExpect(jsonPath("userType", equalTo("A")));
	}

	@Test
	public void listAllusers() throws Exception {

		MvcResult result = this.mvc.perform(
				get("/user").header(HEADER_X_AUTH_TOKEN, ADMIN_AUTH_TOKEN))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(status().isOk())
				.andReturn();
		JsonElement root = new JsonParser().parse(result.getResponse().getContentAsString());
		JsonObject pageObject = root.getAsJsonObject().get("page").getAsJsonObject();
		JsonObject linksObject = root.getAsJsonObject().get("_links").getAsJsonObject();
		JsonArray users = root.getAsJsonObject().get("_embedded").getAsJsonObject().get("user").getAsJsonArray();
		assertEquals(22, pageObject.get("totalElements").getAsBigInteger().intValue());
		assertEquals(20, users.size());
		String nextLink = linksObject.get("next").getAsJsonObject().get("href").getAsString();
		System.out.println("next link:" + nextLink);

		//fetch next page
		MvcResult result2 = this.mvc.perform(
				get(nextLink).header(HEADER_X_AUTH_TOKEN, ADMIN_AUTH_TOKEN))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(status().isOk())
				.andReturn();
		JsonElement root2 = new JsonParser().parse(result2.getResponse().getContentAsString());
		JsonObject pageObject2 = root2.getAsJsonObject().get("page").getAsJsonObject();
		JsonArray users2 = root2.getAsJsonObject().get("_embedded").getAsJsonObject().get("user").getAsJsonArray();
		assertEquals(2, users2.size());


	}


}
