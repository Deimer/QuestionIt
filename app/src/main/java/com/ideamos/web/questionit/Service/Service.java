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
    @POST("/api/register")
    void register(
            //User
            @Field("first_name") String first_name,
            @Field("last_name")String last_name,
            @Field("username")String username,
            @Field("email")String email,
            @Field("social")int social,
            @Field("avatar")String avatar,
            @Field("password")String password,
            @Field("birthdate")String birthdate,
            //Social
            @Field("uid_provider")String uid_provider,
            @Field("social_token")String social_token,
            Callback<JsonObject> cb
    );

}
