package com.example.developer.lorimobile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "Project")
public class Project implements Serializable {
    @SerializedName("id")
    @Expose
    @DatabaseField(id = true)
    private String id;
    @SerializedName("name")
    @Expose
    @DatabaseField
    private String name;

    public Project(String name) {
        this.name = name;
    }

    public Project(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Project() {
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

    @Override
    public String toString() {
        return name;
    }
}
