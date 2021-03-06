package com.ideamos.web.questionit.Helpers;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.github.johnpersano.supertoasts.SuperToast;

/**
 * Creado por Ideamosweb on 2/06/2016.
 */
public class ToastCustomer {

    //Variable para alerta asincronica
    private Context contexto;

    public void toasts(){}

    public ToastCustomer(Context contexto){
        this.contexto = contexto;
        toasts();
    }

    public void setContext(Context contexto){
        this.contexto = contexto;
    }

    public void toastBasic(String message){
        //Funcion que lanza una alerta en un toast con un tiempo definido
        Toast.makeText(contexto, message, Toast.LENGTH_LONG).show();
    }

    public void snackBarBasic(String message, View view){
        Snackbar.make(
                view, message, Snackbar.LENGTH_LONG
        ).show();
    }

    public void toastExit(String message){
        SuperToast superToast = new SuperToast(contexto);
        superToast.setDuration(SuperToast.Duration.LONG);
        superToast.setText(message);
        superToast.setIcon(SuperToast.Icon.Dark.INFO, SuperToast.IconPosition.LEFT);
        superToast.show();
    }

    public void toastWarning(String message){
        SuperToast superToast = new SuperToast(contexto);
        superToast.setDuration(SuperToast.Duration.LONG);
        superToast.setText(message);
        superToast.setIcon(SuperToast.Icon.Dark.EXIT, SuperToast.IconPosition.LEFT);
        superToast.show();
    }

}
