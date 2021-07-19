package com.example.petdispenser;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabasePetApplication {
        private Context context;
        private static final String DATABASE_NAME = "petDispenser.db";
        private static final int DATABASE_VERSION = 3;
        public static final String IDGoogleUtente = "id_google_utente";

        //Animale
        public static final String TABLE_ANIMALE = "animale";
        public static final String COLUMN_ANIMALE_ID = "_id";
        public static final String COLUMN_ANIMALE_NAME = "nome_animale";
        public static final String COLUMN_ANIMALE_PATH_PICTURE = "path";
        public static final String COLUMN_ANIMALE_TIPOLOGIA = "tipologia";
        public static final String COLUMN_ANIMALE_RAZZA = "razza";
        public static final String COLUMN_ANIMALE_PESO = "peso";
        public static final String COLUMN_ANIMALE_DDN = "ddn";
        // Dieta
        public static final String TABLE_DIETA = "dieta";
        public static final String COLUMN_DIETA_ID = "_id";
        public static final String COLUMN_DIETA_NAME = "nome_dieta";
        public static final String COLUMN_DIETA_NOTE = "note";
        public static final String COLUMN_DIETA_ATTIVA = "dieta_attiva";
        public static final String COLUMN_DIETA_ANIMALE_ID = "dieta_animale_id";
        public static final String COLUMN_DIETA_DISPENSER_ID = "dieta_dispenser_id";

        //Pasto
        public static final String TABLE_PASTO = "pasto";
        public static final String COLUMN_PASTO_ID = "_id";
        public static final String COLUMN_PASTO_NAME = "nome";
        public static final String COLUMN_PASTO_QCROCCANTINI = "quantita_croccantini";
        public static final String COLUMN_PASTO_QUMIDO = "quantita_umido";
        public static final String COLUMN_PASTO_NOTE = "note";
        public static final String COLUMN_PASTO_DIETA_ID = "pasto_dieta_id";
        public static final String COLUMN_PASTO_DATA = "data";
        public static final String COLUMN_PASTO_ORA = "ora";


        //Dispenser
        public static final String TABLE_DISPENSER = "dispenser";
        public static final String COLUMN_DISPENSER_ID = "_id";
        public static final String COLUMN_DISPENSER_NAME = "nome";
        public static final String COLUMN_DISPENSER_CODBLUETOOTH = "codice_bluetooth";
        public DatabasePetApplication(Context c)
        {
            this.context = c;
        }


    public void onCreate() {
        DBConnection dbc = new DBConnection();
    }

}
