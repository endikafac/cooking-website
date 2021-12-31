package com.cookingwebsite.crud;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.context.WebApplicationContext;

public class TestWebApp extends CookingWebSiteApplicationTests {
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	private MockMvc mockMvc;
	
	@Before(value = "")
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
	}
	
	@Test
	public void testCommentController() throws Exception {
		this.mockMvc.perform(get("/comment/list")).andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=UTF-8")).andExpect(jsonPath("$[*].comment").isString())
				.andExpect(jsonPath("$[*].auCreationUser").isNumber()).andExpect(jsonPath("$[*].auCreationDate").isNumber())
				.andExpect(jsonPath("$[*].auActive").isNumber());
		
	}

	@Test
	public void testRecipeController() throws Exception {
		
		String param = "1";
		
		final ResultActions result = this.mockMvc.perform(get("/recipe/detail/{id}", param).accept(MimeTypeUtils.APPLICATION_JSON_VALUE));
		
		result.andExpect(status().isOk());
		
		// this.mockMvc.perform(get("/recipe/detail/1"))
		
		result.andExpect(content().contentType("application/json;charset=UTF-8"));
		result.andExpect(jsonPath("$.id").value("1"));
		
	}

//	.andExpect(jsonPath("$.comments").isArray())
//	.andExpect(jsonPath("$.keywords").isArray()).andExpect(jsonPath("$.description").isString())
//	.andExpect(jsonPath("$.name").isString()).andExpect(jsonPath("$.user").exists())
//	.andExpect(jsonPath("$.auCreationUser").isNumber()).andExpect(jsonPath("$.auCreationDate").isNumber())
//	.andExpect(jsonPath("$.auActive").isNumber())
	
//	final ResultActions result = mockMvc.perform(
//
//	        get("/api/languages")
//
//	            .accept(MimeTypeUtils.APPLICATION_JSON_VALUE));
//
//
//	    // Then
//
//	    final int expectedSize = LANGUAGES.size();
//
//	    final String[] expectedLanguageNames = LANGUAGES.stream().map(Language::getName)
//
//	        .collect(Collectors.toList()).toArray(new String[LANGUAGES.size()]);
//
//	    result.andExpect(status().isOk());
//
//	    result.andExpect(jsonPath("$.length()").value(expectedSize));
//
//	    result.andExpect(jsonPath("$[*].name", containsInAnyOrder(expectedLanguageNames)));
//
//	  }
	
}
