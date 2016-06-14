package com.ideamos.web.questionit.Objects;

/**
 * Creado por Ideamosweb on 13/06/2016.
 */
public class Event {

    boolean success;
    String message;
    String error;

    //Getters
    public boolean isSuccess() {
        return success;
    }
    public String getMessage() {
        return message;
    }
    public String getError() {
        return error;
    }

    //Setters
    public void setSuccess(boolean success) {
        this.success = success;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public void setError(String error) {
        this.error = error;
    }
}
