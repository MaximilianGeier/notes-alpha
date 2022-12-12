package com.example.notesalpha.controller;

import com.example.notesalpha.controller.accounts.UserProfile;
import com.example.notesalpha.controller.database.ClientsDAO;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.Cookie;

@RunWith(SpringRunner.class)
@SpringBootTest
class TasksViewControllerTest {
    @InjectMocks
    TasksViewController tasksViewController;

    @Mock
    ClientsDAO clientsDAO;

    @Test
    void getClientLogin1() {
        Mockito.doReturn(new UserProfile("99", "99", "99")).when(clientsDAO).getUserBySessionId("1");
        var cookies = new Cookie[1];
        cookies[0] = new Cookie("idSession", "1");
        var result = tasksViewController.getClientLogin(cookies);
        Assert.assertEquals("99", result);

        Mockito.verify(clientsDAO, Mockito.times(1)).getUserBySessionId("1");
    }

    @Test
    void getClientLogin2() {
        var cookies = new Cookie[2];
        cookies[0] = new Cookie("test1", "1");
        cookies[1] = new Cookie("test2", "2");
        var result = tasksViewController.getClientLogin(cookies);
        Assert.assertNull(result);
    }
}