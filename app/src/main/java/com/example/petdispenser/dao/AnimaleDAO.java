package com.example.petdispenser.dao;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Animale;
import com.example.petdispenser.DBConnection;
import com.example.petdispenser.DatabasePetApplication;
import com.example.petdispenser.models.Animal;
import com.example.petdispenser.models.Diet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class AnimaleDAO {
    public static final String TAG= "AnimaleDAO";
    private SQLiteDatabase db ;
    private DatabasePetApplication dbpet;
    private Context context;
    Connection con = null;
    DBConnection dbc = new DBConnection();
    private FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    private String[] mAllColumns = {
            dbpet.COLUMN_ANIMALE_ID, dbpet.COLUMN_ANIMALE_NAME, dbpet.COLUMN_ANIMALE_PATH_PICTURE,
            dbpet.COLUMN_ANIMALE_TIPOLOGIA, dbpet.COLUMN_ANIMALE_RAZZA,
            dbpet.COLUMN_ANIMALE_PESO, dbpet.COLUMN_ANIMALE_DDN};

    public AnimaleDAO(Context context){
        dbpet = new DatabasePetApplication(context);
        this.context = context;
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
    }

    public Animal getAnimalById (long id) throws JSONException, InterruptedException, ExecutionException {

        String url = "animals/" + id;
        Animal animal = null;

        JSONObject ja = dbc.connectSearch(context,url, null);
        Boolean success = ja.getBoolean("success");
        if (success) {
            JSONObject jsonObj = ja.getJSONObject("result");
            animal = new Animal( jsonObj.getString("nome_animale") ,  jsonObj.getString("path").replaceAll("image:","image%3A"), jsonObj.getString("tipologia"),jsonObj.getString("razza"), (float) jsonObj.getDouble("peso"),jsonObj.getString("ddn"));
            animal.setId(jsonObj.getInt("_id"));
        }
        return animal;
    }

}
