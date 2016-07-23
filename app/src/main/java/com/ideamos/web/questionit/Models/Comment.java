package com.ideamos.web.questionit.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Creado por Deimer, fecha: 22/07/2016.
 */
@DatabaseTable(tableName = "comments")
public class Comment {

    @Expose(serialize = false, deserialize = false)
    @DatabaseField(generatedId = true)
    private int code;

    @SerializedName("id")
    @DatabaseField(canBeNull = false)
    private Integer comment_id;
    @DatabaseField(canBeNull = false)
    private Integer user_id;
    @DatabaseField(canBeNull = false)
    private Integer post_id;
    @DatabaseField(canBeNull = false)
    private String username;
    @DatabaseField(canBeNull = true)
    private String avatar;
    @DatabaseField(canBeNull = true)
    private String description;
    @DatabaseField(canBeNull = true)
    private String created_at;
    @DatabaseField(canBeNull = false)
    private Boolean active;

    public Comment() {}

    public Comment(Integer comment_id, Integer user_id, Integer post_id, String username, String avatar, String description, String created_at, Boolean active) {
        this.comment_id = comment_id;
        this.user_id = user_id;
        this.post_id = post_id;
        this.username = username;
        this.avatar = avatar;
        this.description = description;
        this.created_at = created_at;
        this.active = active;
    }

//region Getters
    public int getCode() {
        return code;
    }
    public Integer getComment_id() {
        return comment_id;
    }
    public Integer getUser_id() {
        return user_id;
    }
    public Integer getPost_id() {
        return post_id;
    }
    public String getUsername() {
        return username;
    }
    public String getAvatar() {
        return avatar;
    }
    public String getDescription() {
        return description;
    }
    public String getCreated_at() {
        return created_at;
    }
    public Boolean getActive() {
        return active;
    }
//endregion

//region Setters
    public void setComment_id(Integer comment_id) {
        this.comment_id = comment_id;
    }
    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }
    public void setPost_id(Integer post_id) {
        this.post_id = post_id;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
    public void setActive(Boolean active) {
        this.active = active;
    }
//endregion

    @Override
    public String toString() {
        return "Comment{" +
            "code=" + code +
            ", comment_id=" + comment_id +
            ", user_id=" + user_id +
            ", post_id=" + post_id +
            ", username='" + username + '\'' +
            ", avatar='" + avatar + '\'' +
            ", description='" + description + '\'' +
            ", created_at='" + created_at + '\'' +
            ", active=" + active +
        '}';
    }

}
