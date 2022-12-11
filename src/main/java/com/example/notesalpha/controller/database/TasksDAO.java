package com.example.notesalpha.controller.database;

import com.example.notesalpha.controller.accounts.UserProfile;
import com.example.notesalpha.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class TasksDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TasksDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveTask(Task task, String clientLogin) {
        try {
            jdbcTemplate.update(String.format("INSERT tasks(client_login, task_text, is_bought) " +
                    "VALUES ('%s', '%s', '%s');", clientLogin, task.getText(), task.getBought() ? 1 : 0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteTask(int id) {
        jdbcTemplate.update(String.format("DELETE FROM tasks WHERE id = %d", id));
    }

    public void changeStatus(int id, Boolean status) {
        jdbcTemplate.update(String.format("UPDATE tasks SET is_bought = " + status
                + " WHERE id = " + id + ";"));
    }

    public Boolean getStatus(int id) {
        try {
            return jdbcTemplate.query("SELECT * FROM tasks WHERE id = " + id + " LIMIT 1;", (result) -> {
                result.next();
                return result.getBoolean("is_bought");});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Task> getTasks(String login) {
        List<Task> allTasks = new ArrayList<>();
        try {
            return jdbcTemplate.query("SELECT * FROM tasks WHERE client_login = '" + login + "';",
                    (result, rowNum) ->
                new Task(result.getInt("id"), result.getString("task_text"), result.getBoolean("is_bought")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
