package com.example.petdispenser.models;

import com.example.petdispenser.R;
import com.google.gson.annotations.SerializedName;

public class Animal {

    @SerializedName("nome_animale")
    public String name;
    @SerializedName("path")
    public String pathPicture;
    public String ddn;
    public String tipologia;
    public String razza;
    public float peso;
    public String id_google_utente;
    @SerializedName("_id")
    long id;

    String pp;//pp sta per prossimo pasto

    public Animal(){}
    /*public Animal(int image, String name, String pp, String tipologia, String razza, float peso, String ddn) {
        this.image = image;
        this.name = name;
        this.pp = pp;
        this.ddn = ddn;
        this.tipologia = tipologia;
        this.peso= peso;
        this.razza = razza;
    }*/

    public Animal(String name, String pathPicture, String tipologia, String razza, float peso, String ddn) {
        this.name = name;
        if(pathPicture.equals(""))
            this.pathPicture = "drawable://" + R.drawable.paw;
        else
            this.pathPicture = pathPicture;

        this.ddn = ddn;
        this.tipologia = tipologia;
        this.peso= peso;
        this.razza = razza;
    }
    public String getPp() {
        return pp;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }



    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getPathPicture() { return pathPicture; }

    public void setPathPicture(String pathPicture) { this.pathPicture = pathPicture; }

    public String getDdn() { return ddn; }

    public void setDdn(String ddn) { this.ddn = ddn; }

    public String getTipologia() { return tipologia; }

    public void setTipologia(String tipologia) { this.tipologia = tipologia; }

    public String getRazza() { return razza; }

    public void setRazza(String razza) { this.razza = razza; }

    public float getPeso() { return peso; }

    public void setPeso(float peso) { this.peso = peso; }
}
