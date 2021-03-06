package com.example.developer.lorimobile.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

@DatabaseTable(tableName = "TimeEntry")
public class TimeEntry implements Serializable {
    @DatabaseField(id = true)
    private String id;

    @DatabaseField(foreign = true, canBeNull = false, foreignAutoRefresh = true)
    private User user;

    @DatabaseField
    private String date;

    @DatabaseField(foreign = true, canBeNull = false, foreignAutoRefresh = true)
    private Task task;

    @DatabaseField
    private int timeInMinutes;

    @DatabaseField
    private double timeInHours;

    @DatabaseField
    private String description;

    public TimeEntry(User user, String date, Task task, int minutes, int hours, String description) {
        this.id = id;
        this.user = user;
        this.date = date;
        this.task = task;
        this.timeInMinutes = minutes+hours*60;
        this.timeInHours = new BigDecimal(hours+ minutes/60.0).setScale(2, RoundingMode.UP).doubleValue();
        this.description = description;
    }

    public TimeEntry(String id, User user, String date, Task task, int minutes, int hours, String description) {
        this.id = id;
        this.user = user;
        this.date = date;
        this.task = task;
        this.timeInMinutes = minutes+hours*60;
        this.timeInHours = new BigDecimal(hours+ minutes/60.0).setScale(2, RoundingMode.UP).doubleValue();
        this.description = description;
    }

    public TimeEntry() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public int getTimeInMinutes() {
        return timeInMinutes;
    }

    public void setTimeInMinutes(int timeInMinutes) {
        this.timeInMinutes = timeInMinutes;
    }

    public double getTimeInHours() {
        return timeInHours;
    }

    public void setTimeInHours(double timeInHours) {
        this.timeInHours = timeInHours;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
