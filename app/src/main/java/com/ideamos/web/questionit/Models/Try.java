package com.ideamos.web.questionit.Models;

/**
 * Creado por Ideamosweb on 20/06/2016.
 */
public class Try {

    private String description;
    private boolean active;
    private int answer_type;
    private int post_id;

    public Try(String description, boolean active, int answer_type, int post_id) {
        this.description = description;
        this.active = active;
        this.answer_type = answer_type;
        this.post_id = post_id;
    }

    //Getters
    public String getDescription() {
        return description;
    }
    public boolean isActive() {
        return active;
    }
    public int getAnswer_type() {
        return answer_type;
    }
    public int getPost_id() {
        return post_id;
    }

    //Setters
    public void setDescription(String description) {
        this.description = description;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
    public void setAnswer_type(int answer_type) {
        this.answer_type = answer_type;
    }
    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

}
