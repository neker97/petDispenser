package com.example.petdispenser.bluetooth;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Timer;

/**
 * Gestisce i dati relativi alla sincronizzazione, come in quali contenitori inserire i pasti, e i timer dei contenitori
 * I contenenitori sono numerati da 0 a 2
 */
public class Data {
    private Timestamp lastSync;
    private ArrayList<Timer> listatimer;
    private void Data(){
        lastSync = new Timestamp(System.currentTimeMillis());
        listatimer = new ArrayList<>();
    }

    /**
     * Ritorna il timestamp dell'ultima sincronizzazione
     * @return Timestamp
     */
    public Timestamp getLastSync (){ return lastSync; }

    /**
     * Aggiorna i dati della sincronizzazione
     * @param time
     */
    public void setLastSync (Timestamp time){ lastSync= time;}





}
