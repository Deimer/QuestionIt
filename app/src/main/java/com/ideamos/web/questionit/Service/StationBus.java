package com.ideamos.web.questionit.Service;

import com.squareup.otto.Bus;

/**
 * Creado por Deimer, fecha: 27/07/2016.
 */
public class StationBus {

    private static Bus bus = new Bus();

    public static Bus getBus() {
        return bus;
    }
}
