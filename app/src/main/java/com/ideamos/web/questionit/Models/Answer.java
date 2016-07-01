package com.ideamos.web.questionit.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Creado por Deimer, fecha: 29/06/2016.
 */
@DatabaseTable(tableName = "answers")
public class Answer {

    @Expose(serialize = false, deserialize = false)
    @DatabaseField(generatedId = true)
    private int code;

    @SerializedName("id")
    @DatabaseField(canBeNull = false)
    private int answer_id;
    @DatabaseField(canBeNull = false)
    private String description;
    @DatabaseField(canBeNull = false)
    private boolean active;
    @DatabaseField(canBeNull = false)
    private int post_id;

    public Answer() {}

    public Answer(int answer_id, String description, boolean active, int post_id) {
        this.answer_id = answer_id;
        this.description = description;
        this.active = active;
        this.post_id = post_id;
    }

//region Getters del modelo
    public int getCode() {
        return code;
    }
    public int getAnswer_id() {
        return answer_id;
    }
    public String getDescription() {
        return description;
    }
    public boolean isActive() {
        return active;
    }
    public int getPost_id() {
        return post_id;
    }
//endregion

//region Setters del modelo
    public void setAnswer_id(int answer_id) {
        this.answer_id = answer_id;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }
//endregion

    @Override
    public String toString() {
        return "Answer{" +
            "code=" + code +
            ", answer_id=" + answer_id +
            ", description='" + description + '\'' +
            ", active=" + active +
            ", post_id=" + post_id +
        '}';
    }

}
