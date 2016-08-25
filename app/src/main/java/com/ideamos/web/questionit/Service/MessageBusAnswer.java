package com.ideamos.web.questionit.Service;

/**
 * Creado por Deimer, fecha: 27/07/2016.
 */
public class MessageBusAnswer {

    private int type_post;

    public MessageBusAnswer(int type_post) {
        this.type_post = type_post;
    }

    public int getType_post() {
        return type_post;
    }
}
