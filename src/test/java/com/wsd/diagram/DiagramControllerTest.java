package com.wsd.diagram;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.junit.Assert.assertEquals;

import com.wsd.controller.DiagramController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.file.Files;
import java.nio.file.Paths;

@RunWith(SpringRunner.class)
@WebMvcTest(DiagramController.class)
public class DiagramControllerTest {
	private static final String smallTestFilePath = "src/test/resources/small-test.json";
	private static final String largeTestFilePath = "src/test/resources/large-test.json";
	private String smallTestJsonPayload;
	private String largeTestJsonPayload;

	@Autowired
	private MockMvc mvc;

	@Test
	public void givenSmallTest_whenGetDiagram_thenStatus200() throws Exception {
		smallTestJsonPayload = new String(Files.readAllBytes(Paths.get(smallTestFilePath)));
		mvc.perform(get("/diagram")
				.contentType(MediaType.APPLICATION_JSON)
				.content(smallTestJsonPayload))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.IMAGE_PNG_VALUE));
	}

	@Test
	public void givenLargeTest_whenGetDiagram_thenStatus200() throws Exception {
		largeTestJsonPayload = new String(Files.readAllBytes(Paths.get(largeTestFilePath)));
		mvc.perform(get("/diagram")
				.contentType(MediaType.APPLICATION_JSON)
				.content(largeTestJsonPayload))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.IMAGE_PNG_VALUE));
	}

	@Test
	public void givenEmptyTest_whenGetDiagram_thenStatus400() throws Exception {
		MvcResult result = mvc.perform(get("/diagram")
				.contentType(MediaType.APPLICATION_JSON)
				.content("Bad Request")).andReturn();
		assertEquals(result.getResponse().getStatus(), 400);
	}
}
