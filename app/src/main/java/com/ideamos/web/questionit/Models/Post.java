package com.ideamos.web.questionit.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Creado por Ideamosweb on 9/06/2016.
 */
@DatabaseTable(tableName = "posts")
public class Post {

    @Expose(serialize = false, deserialize = false)
    @DatabaseField(generatedId = true)
    private int code;

    @SerializedName("id")
    @DatabaseField(canBeNull = false)
    private int post_id;
    @DatabaseField(canBeNull = false)
    private String question;
    @DatabaseField(canBeNull = false)
    private String username;
    @DatabaseField(canBeNull = false)
    private String full_name;
    @DatabaseField(canBeNull = false)
    private String avatar;
    @DatabaseField(canBeNull = false)
    private String created_at;
    @DatabaseField(canBeNull = false)
    private int category_id;
    @DatabaseField(canBeNull = false)
    private int answer_type;
    @DatabaseField(canBeNull = false)
    private boolean active;

    public Post() {}

    public Post(int post_id, String question, String username, String full_name, String avatar,
                String created_at, int category_id, int answer_type, boolean active) {
        this.post_id = post_id;
        this.question = question;
        this.username = username;
        this.full_name = full_name;
        this.avatar = avatar;
        this.created_at = created_at;
        this.category_id = category_id;
        this.answer_type = answer_type;
        this.active = active;
    }

//region Getters
    public int getCode() {
        return code;
    }
    public int getPost_id() {
        return post_id;
    }
    public String getQuestion() {
        return question;
    }
    public String getUsername() {
        return username;
    }
    public String getFull_name() {
        return full_name;
    }
    public String getAvatar() {
        return avatar;
    }
    public String getCreated_at() {
        return created_at;
    }
    public int getCategory_id() {
        return category_id;
    }
    public int getAnswer_type() {
        return answer_type;
    }
    public boolean isActive() {
        return active;
    }
//endregion

//region Setters
    public void setCode(int code) {
        this.code = code;
    }
    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }
    public void setQuestion(String question) {
        this.question = question;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }
    public void setAnswer_type(int answer_type) {
        this.answer_type = answer_type;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
//endregion

    @Override
    public String toString() {
        return "Post{" +
            "code=" + code +
            ", post_id=" + post_id +
            ", question='" + question + '\'' +
            ", username='" + username + '\'' +
            ", full_name='" + full_name + '\'' +
            ", avatar='" + avatar + '\'' +
            ", created_at='" + created_at + '\'' +
            ", category_id=" + category_id +
            ", answer_type=" + answer_type +
            ", active=" + active +
        '}';
    }
}
