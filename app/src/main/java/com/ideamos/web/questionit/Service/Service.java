package com.ideamos.web.questionit.Service;

import com.google.gson.JsonObject;
import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

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

    @GET("/api/logout")
    void logout(
            @Query("token") String token,
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
            @Query("token") String token,
            @Path("id") Integer user_id,
            @Field("first_name")String first_name,
            @Field("last_name")String last_name,
            @Field("birth_date")String birth_date,
            Callback<JsonObject> cb
    );

//region Posts

    @GET("/api/post/get")
    void getPosts(
            @Query("token") String token,
            Callback<JsonObject> cb
    );

    @GET("/api/post/category/get")
    void getCategories(
            @Query("token") String token,
            Callback<JsonObject> cb
    );

    @GET("/api/post/answer/type/get")
    void getAnswerTypes(
            @Query("token") String token,
            Callback<JsonObject> cb
    );

    @FormUrlEncoded
    @POST("/api/post/create")
    void createPost(
            @Query("token") String token,
            @Field("question")String question,
            @Field("user_id")int user_id,
            @Field("category_id")int category_id,
            @Field("answer_type")int answer_type,
            @Field("answers")String anwers,
            @Field("active")boolean active,
            Callback<JsonObject> cb
    );

//endregion

//region Answers

    @GET("/api/post/answer/get/{post_id}")
    void getUserAnswers(
            @Query("token") String token,
            @Path("post_id") Integer post_id,
            Callback<JsonObject> cb
    );

    @GET("/api/post/answer/options/get/{post_id}")
    void getAnswerOptions(
            @Query("token") String token,
            @Path("post_id") Integer post_id,
            Callback<JsonObject> cb
    );

//endregion

}
