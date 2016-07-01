package com.ideamos.web.questionit.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Creado por Deimer, fecha: 22/06/2016.
 */
@DatabaseTable(tableName = "answer_types")
public class AnswerType {

    @Expose(serialize = false, deserialize = false)
    @DatabaseField(generatedId = true)
    private int code;

    @SerializedName("id")
    @DatabaseField(canBeNull = false)
    private int answer_type_id;
    @DatabaseField(canBeNull = false)
    private String name;
    @DatabaseField(canBeNull = false)
    private String concept;
    @DatabaseField(canBeNull = false)
    private boolean active;

    public AnswerType(){}

    public AnswerType(int answer_type_id, String name, String concept, boolean active) {
        this.answer_type_id = answer_type_id;
        this.name = name;
        this.concept = concept;
        this.active = active;
    }

//region Getters
    public int getCode() {
        return code;
    }
    public int getAnswer_type_id() {
        return answer_type_id;
    }
    public String getName() {
        return name;
    }
    public String getConcept() {
        return concept;
    }
    public boolean isActive() {
        return active;
    }
//endregion

//region Setters
    public void setAnswer_type_id(int answer_type_id) {
        this.answer_type_id = answer_type_id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setConcept(String concept) {
        this.concept = concept;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
//endregion

    @Override
    public String toString() {
        return "AnswerType{" +
                "code=" + code +
                ", answer_type_id=" + answer_type_id +
                ", name='" + name + '\'' +
                ", concept='" + concept + '\'' +
                ", active=" + active +
                '}';
    }

}
