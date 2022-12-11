package com.example.notesalpha.controller;

import com.example.notesalpha.controller.accounts.UserProfile;
import com.example.notesalpha.controller.database.ClientsDAO;
import com.example.notesalpha.controller.database.TasksDAO;
import com.example.notesalpha.model.Task;
import com.example.notesalpha.service.TaskService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;

@Controller
public class TasksViewController {
    @Autowired
    private ClientsDAO clientsDAO;
    @Autowired
    private TasksDAO tasksDAO;

    @GetMapping("/")
    public String getDefault() {
        return "view/signin";
    }

    @GetMapping("/signup")
    public String signup() {
        return "view/signup";
    }

    @GetMapping("/signin")
    public String signin() {
        return "view/signin";
    }

    @GetMapping("/main")
    public String main(HttpServletRequest request, HttpServletResponse response) {
        if(getClientLogin(request.getCookies()) == null){
            return  "view/signup";
        };
        return "view/list";
    }

    @ResponseBody
    @GetMapping(value = "/api/tasks", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String getTasks(HttpServletRequest request, HttpServletResponse response){
        JSONObject obj = new JSONObject();
        String login = getClientLogin(request.getCookies());
        obj.put("result", tasksDAO.getTasks(login));
        System.out.println(obj.toString());
        return obj.toString();
    }

    @ResponseBody
    @PutMapping(value = "/api/tasks/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void changeStatus(@PathVariable(value="id") int id, HttpServletRequest request, HttpServletResponse response){
        Boolean currentStatus = tasksDAO.getStatus(id);
        tasksDAO.changeStatus(id, !currentStatus);
        if(!currentStatus){
            response.addIntHeader("status", 1);
        }else{
            response.addIntHeader("status", 0);
        }
    }

    @ResponseBody
    @DeleteMapping(value = "/api/tasks/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void deleteTask(@PathVariable(value="id") int id, HttpServletRequest request, HttpServletResponse response){
        tasksDAO.deleteTask(id);
    }

    @ResponseBody
    @PostMapping(value = "/api/tasks", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void addTask(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String text = request.getParameter("text");
        tasksDAO.saveTask(new Task(text), getClientLogin(request.getCookies()));
        response.sendRedirect("/main");
    }

    @PostMapping("/signup")
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String pass = request.getParameter("pass");
        String email = request.getParameter("email");

        if(login.length() == 0 || pass.length() == 0 || email.length() == 0){
            response.sendRedirect("/");
            return;
        }

        try {
            if(clientsDAO.getProfile(login) == null){
                UserProfile user = new UserProfile(login, pass, email);
                clientsDAO.saveProfile(user);
                clientsDAO.addSession(request.getSession().getId(), user);

                //set coockie
                Cookie cookie = new Cookie("idSession", request.getSession().getId());
                cookie.setMaxAge(-1);
                response.addCookie(cookie);

                response.sendRedirect("/main");
            }
            else{
                response.sendRedirect("/signup");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/api/logout")
    public void doLogOut(HttpServletRequest request,
                             HttpServletResponse response) throws ServletException, IOException {
        String baseURL = getBaseURL(request);
        Cookie cookie = new Cookie("idSession", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        response.sendRedirect(baseURL + "signin");
    }

    @PostMapping("/signin")
    public void doPostSignIn(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String pass = request.getParameter("pass");
        String baseURL = getBaseURL(request);

        if (login == null || pass == null) {
            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        UserProfile currentUser;
        try {
            currentUser = clientsDAO.getProfile(login);
        }catch (NullPointerException e){
            response.sendRedirect(baseURL + "signin");
            return;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (currentUser == null || !currentUser.getPass().equals(pass)) {
            response.sendRedirect(baseURL + "signin");
        }
        else{
            Cookie cookie = new Cookie("idSession", request.getSession().getId());
            cookie.setMaxAge(-1);
            response.addCookie(cookie);
            clientsDAO.addSession(request.getSession().getId() ,currentUser);
            response.sendRedirect(baseURL + "main");
        }
    }

    public String getBaseURL(HttpServletRequest request) throws MalformedURLException {
        URL requestURL = new URL(request.getRequestURL().toString());
        return "http://" + requestURL.getHost() + ":" + requestURL.getPort() +
                request.getContextPath() + "/";
    }

    public String getClientLogin(Cookie[] cookies){
        String cookieName = "idSession";
        Cookie cookie = null;
        if(cookies !=null) {
            for(Cookie c: cookies) {
                if(cookieName.equals(c.getName())) {
                    cookie = c;
                    break;
                }
            }
        }
        try{
            return clientsDAO.getUserBySessionId(cookie.getValue()).getLogin();
        }catch (Exception e){
            return null;
        }
    }
}

