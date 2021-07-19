package com.example.petdispenser.models;

import com.google.gson.annotations.SerializedName;

public class Pasto {
    @SerializedName("_id")
    public long id;
    @SerializedName("nome")
    public String name;
    public String note;
    @SerializedName("quantita_croccantini")
    public String qcroccantini;
    @SerializedName("quantita_umido")
    public String qumido;
    public String data;
    public String ora;
    public String id_google_utente;
    public long pasto_dieta_id;

    public Pasto(){}
    public Pasto(String name, String note, String qcroccantini, String qumido, String data, String ora){
        this.name = name ;
        this.note = note;
        this.qcroccantini = qcroccantini;
        this.qumido = qumido;
        this.data = data;
        this.ora = ora;
    }

    public String getOra() { return ora; }

    public void setOra(String ora) { this.ora = ora; }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
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

    public String getQcroccantini() {
        return qcroccantini;
    }

    public void setQcroccantini(String qcroccantini) {
        this.qcroccantini = qcroccantini;
    }

    public String getQumido() {
        return qumido;
    }

    public void setQumido(String qumido) {
        this.qumido = qumido;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Long getDietaId() { return pasto_dieta_id; }

    public void setDietaId(Long pasto_dieta_id) { this.pasto_dieta_id = pasto_dieta_id; }
}
