package com.example.notesalpha.controller;

import com.example.notesalpha.controller.accounts.UserProfile;
import com.example.notesalpha.controller.database.ClientsDAO;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.Cookie;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class TasksViewControllerTest {
    @Autowired
    TasksViewController tasksViewController;

    @Test
    void getClientLogin1() {
        var cookies = new Cookie[12];
        cookies[0] = new Cookie("idSession", "");
        var result = tasksViewController.getClientLogin(cookies);
        Assert.assertNull(result);
    }

    @Test
    void getClientLogin2() {
        var cookies = new Cookie[12];
        var result = tasksViewController.getClientLogin(cookies);
        Assert.assertNull(result);
    }
}