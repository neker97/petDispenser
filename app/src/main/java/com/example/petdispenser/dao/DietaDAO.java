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
import com.example.petdispenser.bluetooth.Data;
import com.example.petdispenser.models.Animal;
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
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class DietaDAO {
    public static final String TAG = "DietaDAO";
    private SQLiteDatabase db ;
    private DatabasePetApplication dbpet;
    private Context context;
    Connection con = null;
    DBConnection dbc = new DBConnection();
    private FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    private String[] mAllColumns = {
            dbpet.COLUMN_DIETA_ID, dbpet.COLUMN_DIETA_NAME, dbpet.COLUMN_DIETA_NOTE, dbpet.COLUMN_DIETA_ATTIVA,dbpet.COLUMN_DIETA_ANIMALE_ID,dbpet.COLUMN_DIETA_DISPENSER_ID};
    public DietaDAO(Context context){
        dbpet = new DatabasePetApplication(context);
        this.context = context;
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

    }



    public Diet getDietById(long id) throws JSONException, ExecutionException, InterruptedException {

        String url = "diets/" + id;

        Diet d = null;
        JSONObject ja = dbc.connectSearch(context,url, null);
        Boolean success = ja.getBoolean("success");
        if (success) {
            JSONObject jsonObj = ja.getJSONObject("result");
            d = new Diet(jsonObj.getString("nome_dieta"), jsonObj.getString("note"), jsonObj.getString("dieta_attiva"));
            d.setId(jsonObj.getInt("_id"));
            d.setAnimalId(jsonObj.getInt("dieta_animale_id"));
            d.setDispenserId(jsonObj.getLong("dieta_dispenser_id"));
        }
        return d;
        }

}
