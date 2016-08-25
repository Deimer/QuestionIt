package com.ideamos.web.questionit.Objects;

/**
 * Creado por Deimer, fecha: 3/08/2016.
 */
public class ListReactions {

    private int user_id;
    private String user_fullname;
    private String user_username;
    private String user_avatar;
    private int reaction_id;

    public ListReactions() {}

    public ListReactions(int user_id, String user_fullname,
                         String user_username, String user_avatar,
                         int reaction_id) {
        this.user_id = user_id;
        this.user_fullname = user_fullname;
        this.user_username = user_username;
        this.user_avatar = user_avatar;
        this.reaction_id = reaction_id;
    }

//Getters
    public int getUser_id() {
        return user_id;
    }
    public String getUser_fullname() {
        return user_fullname;
    }
    public String getUser_username() {
        return user_username;
    }
    public String getUser_avatar() {
        return user_avatar;
    }
    public int getReaction_id() {
        return reaction_id;
    }
//

//Setters
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
    public void setUser_fullname(String user_fullname) {
        this.user_fullname = user_fullname;
    }
    public void setUser_username(String user_username) {
        this.user_username = user_username;
    }
    public void setUser_avatar(String user_avatar) {
        this.user_avatar = user_avatar;
    }
    public void setReaction_id(int reaction_id) {
        this.reaction_id = reaction_id;
    }
//

    @Override
    public String toString() {
        return "ListReactions{" +
            "user_id=" + user_id +
            ", user_fullname='" + user_fullname + '\'' +
            ", user_username='" + user_username + '\'' +
            ", user_avatar='" + user_avatar + '\'' +
            ", reaction_id=" + reaction_id +
        '}';
    }
}
