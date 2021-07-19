package com.example.petdispenser.models;

import com.google.gson.annotations.SerializedName;

public class Diet {

    @SerializedName("nome_dieta")
    public String name;
    public String note;
    @SerializedName("dieta_attiva")
    public String attiva; //0 (false) and 1 (true).
    @SerializedName("_id")
    public long id;
    @SerializedName("dieta_animale_id")
    public long idAnimale;
    @SerializedName("dieta_dispenser_id")
    public long idDispenser;
    public String id_google_utente;

    public Diet(){}

    public Diet(String name, String note, String attiva) {
        this.name = name;
        this.note = note;
        this.attiva = attiva;
    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String isAttiva() {
        return attiva;
    }

    public void setAttiva(String attiva) {
        this.attiva = attiva;
    }

    public String getAttiva() { return attiva; }

    public Long getDispenserId() { return idDispenser; }

    public void setDispenserId(Long dispenserId) { this.idDispenser = dispenserId; }

    public void setAnimalId(int dieta_animale_id) { this.idAnimale = dieta_animale_id; }

    public long getAnimalId() { return this.idAnimale; }

}
