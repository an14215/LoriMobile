package com.example.developer.lorimobile.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "Task")
public class Task implements Serializable {

    @DatabaseField(id = true)
    private String id;

    @DatabaseField
    private String name;

    @DatabaseField(foreign = true, canBeNull = false, foreignAutoRefresh = true)
    private Project project;

    public Task() {
    }

    public Task(String id) {
        this.id = id;
    }

    public Task(String id, String name, Project project) {
        this.id = id;
        this.name = name;
        this.project = project;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
