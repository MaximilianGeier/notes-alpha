package com.example.notesalpha.model;

public class Task {
    private int id;
    private String text;
    private Boolean isBought;

    public Task(String text){
        this.text = text;
        this.isBought = false;
    }

    public Task(int id, String text){
        this.text = text;
        this.isBought = false;
        this.id = id;
    }

    public Task(int id, String text, Boolean isBought){
        this.text = text;
        this.isBought = isBought;
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public Boolean getBought() {
        return isBought;
    }

    public int getId() {return id; }
}
