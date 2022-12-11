package com.example.notesalpha.service;

import com.example.notesalpha.model.Task;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {
    public List<Task> allTasks;

    public TaskService(){
        this.allTasks = new ArrayList<>();
    }

    public List<Task> listAllTasks(){
        return this.allTasks;
    }
}
