package com.ideamos.web.questionit.Objects;

/**
 * Creado por Deimer, fecha: 5/08/2016.
 */
public class HashTags {

    private String name;
    private boolean active;

    public HashTags() {}

    public HashTags(String name, boolean active) {
        this.name = name;
        this.active = active;
    }

    //Getters
    public String getName() {
        return name;
    }
    public boolean isActive() {
        return active;
    }

    //Setters
    public void setName(String name) {
        this.name = name;
    }
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "HashTags{" +
            "name='" + name + '\'' +
            ", active=" + active +
        '}';
    }

}
