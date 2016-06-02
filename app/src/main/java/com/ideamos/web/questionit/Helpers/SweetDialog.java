package com.ideamos.web.questionit.Helpers;

import android.content.Context;
import com.ideamos.web.questionit.R;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Creado por Ideamosweb on 31/05/2016.
 */
public class SweetDialog {

    //Variable para alerta asincronica
    SweetAlertDialog sweetDialog;
    private Context contexto;

    public void dialogs(){}

    public SweetDialog(Context contexto){
        this.contexto = contexto;
        dialogs();
    }

    public void setContext(Context contexto){
        this.contexto = contexto;
    }

    public void dialogBasic(String message){
        new SweetAlertDialog(contexto)
                .setTitleText(message)
                .show();
    }

    public void dialogSuccess(String title, String message){
        new SweetAlertDialog(contexto, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(title)
                .setContentText(message)
                .show();
    }

    public void dialogWarning(String title, String message){
        new SweetAlertDialog(contexto, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(title)
                .setContentText(message)
                .show();
    }

    public void dialogError(String title, String message){
        new SweetAlertDialog(contexto, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(title)
                .setContentText(message)
                .show();
    }

    //Funcion que lanza una alerta asincronica para los procesos que requieren tiempo
    public void dialogProgress(String title){
        sweetDialog = new SweetAlertDialog(contexto, SweetAlertDialog.PROGRESS_TYPE);
        sweetDialog.getProgressHelper().setBarColor(R.color.colorPrimary);
        sweetDialog.setTitleText(title);
        sweetDialog.setCancelable(false);
        sweetDialog.show();
    }

    //Funcion que permite cancelar una alerta asincronica
    public void cancelarProgress(){
        sweetDialog.cancel();
    }

}
