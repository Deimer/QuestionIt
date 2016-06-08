package com.ideamos.web.questionit.Models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Creado por Deimer, fecha: 6/06/2016.
 * ----------------------------------------------------
 * Clase modelo del objeto Social:
 * Esta clase usa la libreria ORMLite para manejar la relacion
 * de la clase social acount del usuario con la tabla social_acounts en la base de datos.
 * Cualquier atributo o propiedad del objeto social, debe ser agregado
 * aqui para que este se agregue en la base de datos.
 */
@DatabaseTable(tableName = "social_users")
public class SocialUser {

    @DatabaseField(generatedId = true)
    private int code;

    @DatabaseField(canBeNull = true)
    private int user_id;
    @DatabaseField(canBeNull = true)
    private String full_name;
    @DatabaseField(canBeNull = true)
    private String username;
    @DatabaseField(canBeNull = true)
    private String avatar;
    @DatabaseField(canBeNull = false)
    private String provider;
    @DatabaseField(canBeNull = false)
    private String id_provider;
    @DatabaseField(canBeNull = false)
    private String social_token;

    public SocialUser() {}

    public SocialUser(int user_id, String full_name, String username,
                      String avatar, String provider, String id_provider,
                      String social_token) {
        this.user_id = user_id;
        this.full_name = full_name;
        this.username = username;
        this.avatar = avatar;
        this.provider = provider;
        this.id_provider = id_provider;
        this.social_token = social_token;
    }

//region Getters
    public int getCode() {
        return code;
    }
    public int getUser_id() {
        return user_id;
    }
    public String getFull_name() {
        return full_name;
    }
    public String getUsername() {
        return username;
    }
    public String getAvatar() {
        return avatar;
    }
    public String getProvider() {
        return provider;
    }
    public String getId_provider() {
        return id_provider;
    }
    public String getSocial_token() {
        return social_token;
    }
//endregion

//region Setters
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public void setProvider(String provider) {
        this.provider = provider;
    }
    public void setId_provider(String id_provider) {
        this.id_provider = id_provider;
    }
    public void setSocial_token(String social_token) {
        this.social_token = social_token;
    }
//endregion

    @Override
    public String toString() {
        return "SocialUser{" +
            "code=" + code +
            ", user_id=" + user_id +
            ", full_name='" + full_name + '\'' +
            ", username='" + username + '\'' +
            ", avatar='" + avatar + '\'' +
            ", provider='" + provider + '\'' +
            ", id_provider='" + id_provider + '\'' +
            ", social_token='" + social_token + '\'' +
        '}';
    }
}
