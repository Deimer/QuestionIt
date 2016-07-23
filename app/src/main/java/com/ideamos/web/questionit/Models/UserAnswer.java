package com.ideamos.web.questionit.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Creado por Deimer, fecha: 30/06/2016.
 */
@DatabaseTable(tableName = "user_answers")
public class UserAnswer {

    @Expose(serialize = false, deserialize = false)
    @DatabaseField(generatedId = true)
    private int code;

    @SerializedName("id")
    @DatabaseField(canBeNull = false)
    private Integer user_answer_id;
    @DatabaseField(canBeNull = false)
    private Integer user_id;
    @SerializedName("fullname")
    @DatabaseField(canBeNull = false)
    private String user_fullname;
    @DatabaseField(canBeNull = true)
    private String avatar;
    @SerializedName("description")
    @DatabaseField(canBeNull = true)
    private String answer;
    @DatabaseField(canBeNull = true)
    private String created_at;
    @DatabaseField(canBeNull = false)
    private Boolean active;

    public UserAnswer() {}

    public UserAnswer(Integer user_answer_id, Integer user_id, String user_fullname,
                      String avatar, String answer, String created_at, Boolean active) {
        this.user_answer_id = user_answer_id;
        this.user_id = user_id;
        this.user_fullname = user_fullname;
        this.avatar = avatar;
        this.answer = answer;
        this.created_at = created_at;
        this.active = active;
    }

//region Getters del modelo
    public int getCode() {
        return code;
    }
    public Integer getUser_answer_id() {
        return user_answer_id;
    }
    public Integer getUser_id() {
        return user_id;
    }
    public String getUser_fullname() {
        return user_fullname;
    }
    public String getAvatar() {
        return avatar;
    }
    public String getAnswer() {
        return answer;
    }
    public String getCreated_at() {
        return created_at;
    }
    public Boolean getActive() {
        return active;
    }
//endregion

//region Setters del modelo
    public void setUser_answer_id(Integer user_answer_id) {
        this.user_answer_id = user_answer_id;
    }
    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }
    public void setUser_fullname(String user_fullname) {
        this.user_fullname = user_fullname;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public void setAnswer(String answer) {
        this.answer = answer;
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
        return "UserAnswer{" +
            "code=" + code +
            ", user_answer_id=" + user_answer_id +
            ", user_id=" + user_id +
            ", user_fullname='" + user_fullname + '\'' +
            ", avatar='" + avatar + '\'' +
            ", answer='" + answer + '\'' +
            ", created_at='" + created_at + '\'' +
            ", active=" + active +
        '}';
    }

}
