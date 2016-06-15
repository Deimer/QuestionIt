package com.ideamos.web.questionit.Helpers;

import android.content.Context;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Creado por Ideamosweb on 13/06/2016.
 */
public class ParseTime {

    //Variable para alerta asincronica
    private Context contexto;

    public void parseTime(){}

    public ParseTime(Context contexto){
        this.contexto = contexto;
        parseTime();
    }

    public void setContext(Context contexto){
        this.contexto = contexto;
    }

    public String toCalendar(String dpost){
        String res = "";
        try {
            String pattern = "yyyy-MM-dd HH:mm:ss";
            Date date = new SimpleDateFormat(pattern,Locale.getDefault()).parse(dpost);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            res = intDay(calendar.get(Calendar.DAY_OF_WEEK)) + " "
                    + calendar.get(Calendar.DAY_OF_MONTH) + " de "
                    + intMonth(calendar.get(Calendar.MONTH)) + " de "
                    + calendar.get(Calendar.YEAR) + ", "
                    + calendar.get(Calendar.HOUR_OF_DAY) + ":"
                    + calendar.get(Calendar.MINUTE);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return res;
    }

    public String intMonth(int number){
        String month;
        switch (number) {
            case 0: month = "Enero";
                break;
            case 1: month = "Febrero";
                break;
            case 2: month = "Marzo";
                break;
            case 3: month = "Abril";
                break;
            case 4: month = "Mayo";
                break;
            case 5: month = "Junio";
                break;
            case 6: month = "Julio";
                break;
            case 7: month = "Agosto";
                break;
            case 8: month = "Septiembre";
                break;
            case 9: month = "Octubre";
                break;
            case 10:month = "Noviembre";
                break;
            case 11:month = "Diciembre";
                break;
            default:month = "Mes invalido";
                break;
        }
        return month;
    }

    public String intDay(int number){
        String day;
        switch (number) {
            case 1: day = "Domingo";
                break;
            case 2: day = "Lunes";
                break;
            case 3: day = "Martes";
                break;
            case 4: day = "Miercoles";
                break;
            case 5: day = "Jueves";
                break;
            case 6: day = "Viernes";
                break;
            case 7: day = "Sabado";
                break;
            default:day = "Dia invalido";
                break;
        }
        return day;
    }

}
