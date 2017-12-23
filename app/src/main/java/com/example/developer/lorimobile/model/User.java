package com.example.developer.lorimobile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "User")
public class User implements Serializable {

    @SerializedName("id")
    @Expose
    @DatabaseField(id = true)
    private String id;

    @SerializedName("login")
    @Expose
    private String login;

    public User() {
    }

    public User(String id) {
        this.id = id;
    }

    public User(String id, String login) {
        this.id = id;
        this.login = login;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
