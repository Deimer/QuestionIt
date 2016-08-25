package com.ideamos.web.questionit.Helpers;

import android.content.Context;
import com.facebook.login.LoginResult;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ideamos.web.questionit.Models.SocialUser;
import com.ideamos.web.questionit.Models.User;
import com.ideamos.web.questionit.Models.UserAnswer;
import com.ideamos.web.questionit.R;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

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

    public List<String> titleTabs(){
        ArrayList<String> scopes = new ArrayList<>();
        scopes.add("Respuestas");
        scopes.add("Comentarios");
        return scopes;
    }

    public List<String> titleTabsSheet(int total_reactions){
        ArrayList<String> scopes = new ArrayList<>();
        scopes.add("Todas " + total_reactions);
        scopes.add("Me Gusta");
        scopes.add("No Me Gusta");
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

    public UserAnswer jsonToUserAnswer(JsonObject json){
        UserAnswer answers = new UserAnswer();
        answers.setUser_answer_id(json.get("id").getAsInt());
        answers.setUser_id(json.get("user_id").getAsInt());
        answers.setUser_fullname(json.get("fullname").getAsString());
        answers.setAvatar(validateJsonItem(json.get("avatar").getAsString()));
        answers.setAnswer(json.get("description").getAsString());
        answers.setCreated_at(json.get("created_at").getAsString());
        answers.setActive(json.get("active").getAsBoolean());
        return answers;
    }

    public String validateJsonItem(String data){
        String avatar;
        if(data != null){
            avatar = data;
        } else {
            avatar = "no data";
        }
        return avatar;
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
