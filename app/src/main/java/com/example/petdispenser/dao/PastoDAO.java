package com.example.petdispenser.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.example.petdispenser.DBConnection;
import com.example.petdispenser.DatabasePetApplication;
import com.example.petdispenser.models.Diet;
import com.example.petdispenser.models.Dispenser;
import com.example.petdispenser.models.Pasto;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class PastoDAO {
    public static final String TAG = "PastoDAO";
    Connection con = null;
    DBConnection dbc = new DBConnection();
    private SQLiteDatabase db ;
    private DatabasePetApplication dbpet;
    private Context context;
    private FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    private String[] mAllColumns = {
            dbpet.COLUMN_PASTO_ID, dbpet.COLUMN_PASTO_NAME, dbpet.COLUMN_PASTO_NOTE, dbpet.COLUMN_PASTO_QUMIDO,
            dbpet.COLUMN_PASTO_QCROCCANTINI,dbpet.COLUMN_PASTO_DIETA_ID,dbpet.COLUMN_PASTO_DATA, dbpet.COLUMN_PASTO_ORA};
    public PastoDAO(Context context) {
        this.context = context;
        dbpet = new DatabasePetApplication(context);
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
    }

    public List<Pasto> getPastiOfDate(String date) throws JSONException, ExecutionException, InterruptedException {
        ArrayList<Pasto> pastoList = new ArrayList<Pasto>();
        DietaDAO dietaDAO = new DietaDAO(context);
        String url = "meals?date=" + date;

        JSONObject ja = dbc.connectSearch(context,url, null);
        Boolean success = ja.getBoolean("success");
        if (success) {
            JSONArray mealsArray = ja.getJSONArray("result");
            for(int i =0 ; i < mealsArray.length() ;i++) {
                JSONObject jsonObj = mealsArray.getJSONObject(i);
                Pasto d = new Pasto(jsonObj.getString("nome"),jsonObj.getString("note"),String.valueOf( jsonObj.getInt("quantita_croccantini")),String.valueOf(jsonObj.getInt("quantita_umido")),jsonObj.getString("data"),jsonObj.getString("ora"));
                d.setId(jsonObj.getInt("_id"));
                d.setDietaId(jsonObj.getLong("pasto_dieta_id"));
                pastoList.add(d);
            }
        }



        return pastoList;
    }

}
