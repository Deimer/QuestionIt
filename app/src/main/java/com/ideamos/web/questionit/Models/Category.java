package com.ideamos.web.questionit.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Creado por Ideamosweb on 14/06/2016.
 */
@DatabaseTable(tableName = "categories")
public class Category {

    @Expose(serialize = false, deserialize = false)
    @DatabaseField(generatedId = true)
    private int code;

    @SerializedName("id")
    @DatabaseField(canBeNull = false)
    private int category_id;
    @DatabaseField(canBeNull = false)
    private String name;
    @DatabaseField(canBeNull = false)
    private boolean active;

    public Category() {}

    public Category(int category_id, String name, boolean active) {
        this.category_id = category_id;
        this.name = name;
        this.active = active;
    }

//Getters
    public int getCode() {
        return code;
    }
    public int getCategory_id() {
        return category_id;
    }
    public String getName() {
        return name;
    }
    public boolean isActive() {
        return active;
    }

//Setters
    public void setCode(int code) {
        this.code = code;
    }
    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setActive(boolean active) {
        this.active = active;
    }

//Lector
    @Override
    public String toString() {
        return "Category{" +
            "code=" + code +
            ", category_id=" + category_id +
            ", name='" + name + '\'' +
            ", active=" + active +
        '}';
    }

}
