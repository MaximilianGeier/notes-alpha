package com.example.notesalpha;

import com.example.notesalpha.controller.TasksViewController;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.Cookie;

import static org.junit.matchers.JUnitMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
@TestPropertySource("/db-test.properties")
class ControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void badCredentials() throws Exception {
        this.mockMvc.perform(post("http://localhost:8000/signin")
                .param("login", "1").param("pass", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost:8000/main"))
                .andDo(print());
    }

    @Test
    void contentTest() throws Exception {
        String id = this.mockMvc.perform(post("http://localhost:8000/signin")
                .param("login", "1")
                .param("pass", "1"))
                .andReturn().getRequest().getSession().getId();
        this.mockMvc.perform(get("http://localhost:8000/main")
                        .cookie(new Cookie("idSession", id)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().string(containsString("Выход")));
    }
}
