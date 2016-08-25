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
    private int user_id;
    @DatabaseField(canBeNull = false)
    private String question;
    @DatabaseField(canBeNull = false)
    private String username;
    @DatabaseField(canBeNull = false)
    private String full_name;
    @DatabaseField(canBeNull = true)
    private String avatar;
    @DatabaseField(canBeNull = false)
    private String created_at;
    @DatabaseField(canBeNull = false)
    private int category_id;
    @DatabaseField(canBeNull = false)
    private int answer_type;
    @DatabaseField(canBeNull = false, defaultValue = "false")
    private Boolean i_answered;
    @DatabaseField(canBeNull = false)
    private boolean active;
    @DatabaseField(canBeNull = true)
    private int votes;

    public Post() {}

    public Post(int post_id, int user_id, String question,
                String username, String full_name, String avatar,
                String created_at, int category_id, int answer_type,
                Boolean i_answered, boolean active, int votes) {
        this.post_id = post_id;
        this.user_id = user_id;
        this.question = question;
        this.username = username;
        this.full_name = full_name;
        this.avatar = avatar;
        this.created_at = created_at;
        this.category_id = category_id;
        this.answer_type = answer_type;
        this.i_answered = i_answered;
        this.active = active;
        this.votes = votes;
    }

//region Getters
    public int getCode() {
        return code;
    }
    public int getPost_id() {
        return post_id;
    }
    public int getUser_id() {
        return user_id;
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
    public Boolean isI_answered() {
        return i_answered;
    }
    public boolean isActive() {
        return active;
    }
    public int getVotes() {
        return votes;
    }
//endregion

//region Setters
    public void setCode(int code) {
        this.code = code;
    }
    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }
    public void setUser_id(int user_id) {
        this.user_id = user_id;
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
    public void setI_answered(Boolean i_answered) {
        this.i_answered = i_answered;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
    public void setVotes(int votes) {
        this.votes = votes;
    }
//endregion

    @Override
    public String toString() {
        return "Post{" +
            "code=" + code +
            ", post_id=" + post_id +
            ", user_id=" + user_id +
            ", question='" + question + '\'' +
            ", username='" + username + '\'' +
            ", full_name='" + full_name + '\'' +
            ", avatar='" + avatar + '\'' +
            ", created_at='" + created_at + '\'' +
            ", category_id=" + category_id +
            ", answer_type=" + answer_type +
            ", i_answered=" + i_answered +
            ", active=" + active +
            ", votes=" + votes +
        '}';
    }
}
