package com.example.notesalpha;

import com.example.notesalpha.controller.TasksViewController;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
class LoginTests {
	@Autowired
	private MockMvc mockMvc;

	@Test
	void checkRegPage() throws Exception {
		this.mockMvc.perform(get("/main"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Вход")));
	}

	@Test
	void loginTest() throws Exception {
		this.mockMvc.perform(post("http://localhost:8000/signin").param("login", "1").param("pass", "1"))
				.andDo(print())
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("http://localhost:8000/main"))
				.andExpect(cookie().value("idSession", "1"));
	}

	@Test
	void badLoginTest() throws Exception {
		this.mockMvc.perform(post("http://localhost:8000/signin").param("login", "22").param("pass", "1"))
				.andDo(print())
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("http://localhost:8000/main"))
				.andExpect(cookie().value("idSession", "1"));
	}

	@Test
	void badCredentials() throws Exception {
		this.mockMvc.perform(post("/signin").param("login", "Fedor"))
				.andDo(print())
				.andExpect(status().isForbidden());
	}
}
