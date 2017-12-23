package com.example.developer.lorimobile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


public class ProjectParticipant {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("project")
    @Expose
    private Project project;

    private User user;



    public ProjectParticipant(String id, Project project, User user) {
        this.id = id;
        this.project = project;
        this.user = user;
    }

    public ProjectParticipant(String id, Project project) {
        this.id = id;
        this.project = project;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
