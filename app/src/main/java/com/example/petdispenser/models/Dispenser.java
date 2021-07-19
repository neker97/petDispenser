package com.example.petdispenser.models;

import com.google.gson.annotations.SerializedName;

public class Dispenser {

    @SerializedName("nome")
    String name;
    @SerializedName("codice_bluetooth")
    String codBluetooth;
    String id_google_utente;
    @SerializedName("_id")
    long id;

    public Dispenser(){}

    public Dispenser(String name, String codBluetooth) {
        this.name = name;
        this.codBluetooth = codBluetooth;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getCodBluetooth() {
        return codBluetooth;
    }

    public void setCodBluetooth(String codBluetooth) {
        this.codBluetooth = codBluetooth;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) { this.id = id; }
}
