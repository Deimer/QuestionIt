package com.ideamos.web.questionit.Service;

/**
 * Creado por Deimer, fecha: 27/07/2016.
 */
public class MessageBusComment {

    private boolean active;

    public MessageBusComment(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }
}
