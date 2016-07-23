package com.ideamos.web.questionit.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Creado por Deimer, fecha: 14/07/2016.
 */
@DatabaseTable(tableName = "reactions")
public class Reaction {

    @Expose(serialize = false, deserialize = false)
    @DatabaseField(generatedId = true)
    private int code;

    @SerializedName("id")
    @DatabaseField(canBeNull = true)
    private int user_reaction_id;
    @DatabaseField(canBeNull = false)
    private int user_id;
    @DatabaseField(canBeNull = false)
    private int post_id;
    @DatabaseField(canBeNull = false)
    private int reaction_id;
    @DatabaseField(canBeNull = false)
    private boolean active;
    @DatabaseField(canBeNull = true)
    private String created_at;
    @DatabaseField(canBeNull = true)
    private String updated_at;

    public Reaction() {}

    public Reaction(int user_reaction_id, int user_id, int post_id, int reaction_id,
                    boolean active, String created_at, String updated_at) {
        this.user_reaction_id = user_reaction_id;
        this.user_id = user_id;
        this.post_id = post_id;
        this.reaction_id = reaction_id;
        this.active = active;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

//region Getters
    public int getCode() {
        return code;
    }
    public int getUser_reaction_id() {
        return user_reaction_id;
    }
    public int getUser_id() {
        return user_id;
    }
    public int getPost_id() {
        return post_id;
    }
    public int getReaction_id() {
        return reaction_id;
    }
    public boolean isActive() {
        return active;
    }
    public String getCreated_at() {
        return created_at;
    }
    public String getUpdated_at() {
        return updated_at;
    }
//endregion

//region Setters
    public void setCode(int code) {
        this.code = code;
    }
    public void setUser_reaction_id(int user_reaction_id) {
        this.user_reaction_id = user_reaction_id;
    }
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }
    public void setReaction_id(int reaction_id) {
        this.reaction_id = reaction_id;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
//endregion

    @Override
    public String toString() {
        return "Reaction{" +
            "code=" + code +
            ", user_reaction_id=" + user_reaction_id +
            ", user_id=" + user_id +
            ", post_id=" + post_id +
            ", reaction_id=" + reaction_id +
            ", active=" + active +
            ", created_at='" + created_at + '\'' +
            ", updated_at='" + updated_at + '\'' +
        '}';
    }

}
