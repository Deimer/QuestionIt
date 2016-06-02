package com.ideamos.web.questionit.Helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import java.util.ArrayList;
import java.util.List;

/**
 * Creado por Ideamosweb on 31/05/2016.
 */
public class Validate {

    //Variable para alerta asincronica
    private Context contexto;

    public void Validaciones(){}

    public Validate(Context contexto){
        this.contexto = contexto;
        Validaciones();
    }

    public void setContext(Context contexto){
        this.contexto = contexto;
    }

    //Metodo para verificar la conectividad a internet
    public boolean connect() {
        ConnectivityManager connMgr = (ConnectivityManager) contexto.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public boolean validateString(ArrayList<String> lista){
        for (int i = 0; i < lista.size(); i++) {
            String cadena = lista.get(i);
            if(cadena.equalsIgnoreCase("")){
                return false;
            }
        }
        return true;
    }

    public boolean isEmailValid(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    public boolean isPasswordValid(String password) {
        return password.length() >= 6;
    }

    public String formatUsername(String email){
        String[] parts = email.split("@");
        return parts[0];
    }

    public String assignUserInvalid(String email, String usernameSocial){
        String username;
        if(usernameSocial == null){
            if(!email.equalsIgnoreCase("")){
                String[] parts = email.split("@");
                username = parts[0];
            }else{
                username = "";
            }
        }else{
            username = usernameSocial;
        }
        return username;
    }

    public boolean validateValues(List<String> array, String value_error){
        for (int i = 0; i < array.size(); i++) {
            if(array.get(i).equalsIgnoreCase(value_error) || array.get(i).equalsIgnoreCase("")){
                return false;
            }
        }
        return true;
    }

    public int toInt(boolean state){
        if(state){
            return 1;
        }else{
            return 0;
        }
    }

    public String validateStringNull(String path){
        String string = "no-data";
        if(path.equalsIgnoreCase("")){
            return string;
        } else {
            return path;
        }
    }

}
