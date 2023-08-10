package com.xm.cryptoservice.controller;


import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;



@SpringBootTest
@AutoConfigureMockMvc
public class CryptoserviceControllerTest {

	@Autowired
	private MockMvc mvc;
	
	@Autowired
	WebApplicationContext webApplicationContext;

	protected void setUp() {
		
	     mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

	}
	
	@Test
	public void loadAllCsvDataToDatabase() throws Exception {
		String uri = "/api/v1/crypto/load";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		Assert.assertEquals(200, status);
		Assert.assertNotNull(mvcResult.getResponse().getContentAsString());
	}

    
	@Test
	public void getAllCryptoSortByDescendingTest() throws Exception {
		String uri = "/api/v1/crypto/sort/desc";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		Assert.assertEquals(200, status);
		Assert.assertNotNull(mvcResult.getResponse().getContentAsString());
	}
	
	@Test
	public void getRequestedCryptoDataByNameTest() throws Exception {
		String uri = "/api/v1/crypto/minmax?cryptoName=DOGE";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		Assert.assertEquals(200, status);
		Assert.assertNotNull(mvcResult.getResponse().getContentAsString());
	}
	
	@Test
	public void getRequestedCryptoDataByDateTest() throws Exception {
		String uri = "/api/v1/crypto/normalized?cryptoDate=01-01-2022";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		Assert.assertEquals(200, status);
		Assert.assertNotNull(mvcResult.getResponse().getContentAsString());
	}
	
	
}
