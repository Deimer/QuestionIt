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
    @POST("/api/register")
    void register(
            //User
            @Field("username")String username,
            @Field("email")String email,
            @Field("password")String password,
            @Field("password_confirmation")String password_confirmation,
            Callback<JsonObject> cb
    );

}
