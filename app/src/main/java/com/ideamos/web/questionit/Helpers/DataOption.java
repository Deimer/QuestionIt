package com.ideamos.web.questionit.Helpers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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

}
