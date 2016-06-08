package com.ideamos.web.questionit.Service;

import com.google.gson.JsonObject;
import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Creado por Ideamosweb on 26/05/2016.
 */
public interface Service {

    @FormUrlEncoded
    @POST("/api/login")
    void login(
            @Field("email") String email,
            @Field("password")String password,
            Callback<JsonObject> cb
    );

    @FormUrlEncoded
    @POST("/api/social/login")
    void socialLogin(
            @Field("provider") String provider,
            @Field("id_provider")String id_provider,
            @Field("email") String email,
            @Field("avatar")String avatar,
            Callback<JsonObject> cb
    );

    @FormUrlEncoded
    @POST("/api/register")
    void register(
            @Field("username")String username,
            @Field("email")String email,
            @Field("password")String password,
            @Field("password_confirmation")String password_confirmation,
            Callback<JsonObject> cb
    );

    @FormUrlEncoded
    @POST("/api/social/register")
    void socialRegister(
            //User
            @Field("username")String username,
            @Field("email")String email,
            @Field("password")String password,
            @Field("first_name")String first_name,
            @Field("last_name")String last_name,
            @Field("birth_date")String birth_date,
            //Social
            @Field("full_name")String full_name,
            @Field("avatar")String avatar,
            @Field("provider")String provider,
            @Field("id_provider")String id_provider,
            @Field("social_token")String social_token,
            Callback<JsonObject> cb
    );

    @FormUrlEncoded
    @POST("/api/user/update/{id}")
    void updateUser(
            @Field("id")Integer user_id,
            @Field("first_name")String first_name,
            @Field("last_name")String last_name,
            @Field("birth_date")String birth_date,
            Callback<JsonObject> cb
    );

}
