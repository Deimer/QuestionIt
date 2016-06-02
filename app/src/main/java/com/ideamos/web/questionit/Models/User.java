package com.ideamos.web.questionit.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Creado por Ideamosweb on 31/05/2016.
 */
@DatabaseTable(tableName = "users")
public class User {

    @Expose(serialize = false, deserialize = false)
    @DatabaseField(generatedId = true)
    private int code;

    @DatabaseField(canBeNull = true)
    private String first_name;
    @DatabaseField(canBeNull = true)
    private String last_name;
    @DatabaseField(canBeNull = false)
    private String username;
    @DatabaseField(canBeNull = false)
    private String email;
    @DatabaseField(canBeNull = true)
    private String birth_date;
    @DatabaseField(canBeNull = false)
    private Boolean status;
    @DatabaseField(canBeNull = false)
    private Boolean social;
    @DatabaseField(canBeNull = true)
    private String avatar;
    @SerializedName("id")
    @DatabaseField(canBeNull = false)
    private Integer user_id;
    @DatabaseField(canBeNull = false)
    private String token;

    public User(){}

    public User(String first_name, String last_name, String username, String email,
                String birth_date, Boolean status, Boolean social,
                String avatar, Integer user_id, String token) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.username = username;
        this.email = email;
        this.birth_date = birth_date;
        this.status = status;
        this.social = social;
        this.avatar = avatar;
        this.user_id = user_id;
        this.token = token;
    }

    //region Getters
    public int getCode() {
        return code;
    }
    public String getFirst_name() {
        return first_name;
    }
    public String getLast_name() {
        return last_name;
    }
    public String getUsername() {
        return username;
    }
    public String getEmail() {
        return email;
    }
    public String getBirth_date() {
        return birth_date;
    }
    public Boolean getStatus() {
        return status;
    }
    public Boolean getSocial() {
        return social;
    }
    public String getAvatar() {
        return avatar;
    }
    public Integer getUser_id() {
        return user_id;
    }
    public String getToken() {
        return token;
    }
    //endregion

    //region Setters
    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }
    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }
    public void setStatus(Boolean status) {
        this.status = status;
    }
    public void setSocial(Boolean social) {
        this.social = social;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }
    public void setToken(String token) {
        this.token = token;
    }
    //endregion

    @Override
    public String toString() {
        return "User{" +
            "code=" + code +
            ", first_name='" + first_name + '\'' +
            ", last_name='" + last_name + '\'' +
            ", username='" + username + '\'' +
            ", email='" + email + '\'' +
            ", birth_date='" + birth_date + '\'' +
            ", status=" + status +
            ", social=" + social +
            ", avatar='" + avatar + '\'' +
            ", user_id=" + user_id +
            ", token='" + token + '\'' +
        '}';
    }

}
