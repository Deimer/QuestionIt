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

    public Date stringToDate(String dtPost){
        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            date = format.parse(dtPost);
        } catch (ParseException e) {
            System.out.println("Parse time error: " + e.getMessage());
            e.printStackTrace();
        }
        return date;
    }

    public String dayOfWeek(String datePost){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss a", Locale.getDefault());
        String currentDate = sdf.format(datePost);
        SimpleDateFormat sdf_ = new SimpleDateFormat("EEEE", Locale.getDefault());
        Date date = new Date();
        String dayName = sdf_.format(date);
        return dayName + " " + currentDate;
    }

}
