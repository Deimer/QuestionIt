package com.ideamos.web.questionit.Helpers;

import android.content.Context;
import com.facebook.login.LoginResult;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ideamos.web.questionit.Models.SocialUser;
import com.ideamos.web.questionit.Models.User;
import com.ideamos.web.questionit.R;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Creado por Deimer, fecha: 5/06/2016.
 */
public class DataOption {

    public ArrayList<String> scopesUserFacebook(){
        ArrayList<String> scopes = new ArrayList<>();
        scopes.add("public_profile");
        scopes.add("email");
        scopes.add("user_birthday");
        return scopes;
    }

    public JsonObject convertToJsonGson(JSONObject object){
        JsonParser jsonParser = new JsonParser();
        return (JsonObject)jsonParser.parse(object.toString());
    }

    public String formatUsername(String email){
        String[] parts = email.split("@");
        return parts[0];
    }

    public String replace(String value, String char1, String char2){
        return value.replace(char1, char2);
    }

    public User jsonToUser(JsonObject json){
        User user = new User();
        user.setUsername(formatUsername(json.get("email").getAsString()));
        user.setEmail(json.get("email").getAsString());
        user.setFirst_name(json.get("first_name").getAsString());
        user.setLast_name(json.get("last_name").getAsString());
        user.setBirth_date(replace(json.get("birthday").getAsString(), "/", "-"));
        return user;
    }

    public SocialUser jsonToSocialUser(JsonObject json, LoginResult loginResult, Context context){
        SocialUser social = new SocialUser();
        social.setFull_name(json.get("name").getAsString());
        social.setUsername(formatUsername(json.get("email").getAsString()));
        social.setAvatar(json
                .get("picture").getAsJsonObject()
                .get("data").getAsJsonObject()
                .get("url").getAsString());
        social.setProvider(context.getResources().getString(R.string.provider_social));
        social.setId_provider(json.get("id").getAsString());
        social.setSocial_token(loginResult.getAccessToken().getToken());
        return social;
    }

}
