package com.experiment.todolist;

import org.litepal.crud.LitePalSupport;

public class Data extends LitePalSupport {
    private String title;
    private String content;
    private int year,month,day;
    private int priority;
    private String date;
    private int id;

    public Data(String title, String content,int year,int month,int day,int priority,String date)
    {
        this.title = title;
        this.content = content;
        this.month=month;
        this.day=day;
        this.year=year;
        this.priority=priority;
        this.date=date;
    }

    public int getPriority() {
        return priority;
    }

    public String getContent() {
        return content;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public int getId() {
        return id;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setId(int id) {
        this.id = id;
    }
}

