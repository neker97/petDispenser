package com.example.petdispenser.api;

import android.util.ArrayMap;
import android.util.Log;

import com.example.petdispenser.api.model.AnimalResponse;
import com.example.petdispenser.api.model.ApiGenericResponse;
import com.example.petdispenser.api.model.InsertGenericResponse;
import com.example.petdispenser.models.Animal;
import com.example.petdispenser.models.Diet;
import com.example.petdispenser.models.Dispenser;
import com.example.petdispenser.models.Pasto;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class PetApiService {

    PetApiApi petApi;
    private FirebaseAuth mAuth;
    FirebaseUser firebaseUser;

    public PetApiService() {
        petApi = PetApiClient.getClient().create(PetApiApi.class);
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
    }

    //================================================================================
    // ANIMAL
    //================================================================================

    public void getAllAnimals(Callback callback) {
        getToken((String token)-> {
            Call<ApiGenericResponse<List<Animal>>> call = petApi.getAllAnimals(token);
            call.enqueue(callback);
        });
    }

    public void addAnimale(String name, String path, String tipologia, String razza, float peso, String ddn, Callback callback) {
        Map<String, Object> jsonParams = new ArrayMap<>();

        jsonParams.put("nome_animale", name);
        jsonParams.put("path", path);
        jsonParams.put("tipologia", tipologia);
        jsonParams.put("razza", razza);
        jsonParams.put("peso", peso);
        jsonParams.put("ddn", ddn);
        jsonParams.put("id_google_utente", firebaseUser.getUid());
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),(new JSONObject(jsonParams)).toString());
        getToken((String token)-> {
            Call<ApiGenericResponse<InsertGenericResponse>> call = petApi.addAnimale(body, token);
            call.enqueue(callback);
        });
    }

    public void deleteAnimale(String id, Callback callback) {
        getToken((String token)-> {
            Call<ApiGenericResponse<Object>> call = petApi.deleteAnimale(id, token);
            call.enqueue(callback);
        });
    }

    public void getAnimalById(String id, Callback callback) {
        getToken((String token)-> {
            Call<ApiGenericResponse<Animal>> call = petApi.getAnimalById(id, token);
            call.enqueue(callback);
        });
    }

    public void getAnimalByName(String name, Callback callback) {
        getToken((String token)-> {
            Call<ApiGenericResponse<Animal>> call = petApi.getAnimalByName(name, token);
            call.enqueue(callback);
        });
    }

    public void updateAnimal(Animal animal, Callback callback) {
        Map<String, Object> jsonParams = new ArrayMap<>();

        jsonParams.put("nome_animale", animal.getName());
        jsonParams.put("path", animal.getPathPicture());
        jsonParams.put("tipologia", animal.getTipologia());
        jsonParams.put("razza", animal.getRazza());
        jsonParams.put("peso", animal.getPeso());
        jsonParams.put("ddn", animal.getDdn());
        jsonParams.put("id_google_utente", firebaseUser.getUid());
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),(new JSONObject(jsonParams)).toString());

        getToken((String token)-> {
            Call<ApiGenericResponse<Object>> call = petApi.updateAnimal(Long.toString(animal.getId()), body, token);
            call.enqueue(callback);
        });
    }

    public void getDietOfAnimal(String id, Callback callback) {
        getToken((String token)-> {
            Call<ApiGenericResponse<List<Diet>>> call = petApi.getDietOfAnimal(id, token);
            call.enqueue(callback);
        });
    }

    //================================================================================
    // DISPENSER
    //================================================================================

    public void getAllDispenser(Callback callback) {
        getToken((String token)-> {
            Call<ApiGenericResponse<List<Dispenser>>> call = petApi.getAllDispenser(token);
            call.enqueue(callback);
        });
    }

    public void addDispenser(String name, String codBluethoot, Callback callback) {
        Map<String, Object> jsonParams = new ArrayMap<>();

        jsonParams.put("nome", name);
        jsonParams.put("codice_bluetooth", codBluethoot);
        jsonParams.put("id_google_utente", firebaseUser.getUid());
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),(new JSONObject(jsonParams)).toString());
        getToken((String token)-> {
            Call<ApiGenericResponse<InsertGenericResponse>> call = this.petApi.addDispenser(body, token);
            call.enqueue(callback);
        });

    }

    public void deleteDispenser(String id, Callback callback) {
        getToken((String token)-> {
            Call<ApiGenericResponse<Object>> call = this.petApi.deleteDispenser(id, token);
            call.enqueue(callback);
        });

    }

    public void getDispenserById(String id, Callback callback) {
        getToken((String token)-> {
            Call<ApiGenericResponse<Dispenser>> call = this.petApi.getDispenserById(id, token);
            call.enqueue(callback);
        });
    }

    public void getDispenserByName(String name, Callback callback) {
        getToken((String token)-> {
            Call<ApiGenericResponse<Dispenser>> call = this.petApi.getDispenserByName(name, token);
            call.enqueue(callback);
        });

    }

    public void getDietOfDispenser(String id, Callback callback) {
        getToken((String token)-> {
            Call<ApiGenericResponse<List<Diet>>> call = this.petApi.getDietOfDispenser(id, token);
            call.enqueue(callback);
        });

    }

    //================================================================================
    // MEAL
    //================================================================================
    public void getAllPasto(Callback callback) {
        getToken((String token)-> {
            Call<ApiGenericResponse<List<Pasto>>> call = this.petApi.getAllPasto(token);
            call.enqueue(callback);
        });
    }

    public void addPasto(String name, String note, String qcroccantini, String qumido, long idDieta, String data, String ora, Callback callback) {
        Map<String, Object> jsonParams = new ArrayMap<>();

        jsonParams.put("nome", name);
        jsonParams.put("quantita_croccantini", qcroccantini);
        jsonParams.put("quantita_umido", qumido);
        jsonParams.put("note", note);
        jsonParams.put("pasto_dieta_id", idDieta);
        jsonParams.put("data", data);
        jsonParams.put("ora", ora);
        jsonParams.put("id_google_utente", firebaseUser.getUid());
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),(new JSONObject(jsonParams)).toString());
        getToken((String token)-> {
            Call<ApiGenericResponse<InsertGenericResponse>> call = this.petApi.addPasto(body, token);
            call.enqueue(callback);
        });
    }

    public void deletePasto(String id, Callback callback) {
        getToken((String token)-> {
            Call<ApiGenericResponse<Object>> call = this.petApi.deletePasto(id, token);
            call.enqueue(callback);
        });
    }

    public void getPastoById(String id, Callback callback) {
        getToken((String token)-> {
            Call<ApiGenericResponse<Pasto>> call = this.petApi.getPastoById(id, token);
            call.enqueue(callback);
        });
    }

    public void getPastiOfDate(String name, Callback callback) {
        getToken((String token)-> {
            Call<ApiGenericResponse<List<Pasto>>> call = this.petApi.getPastiOfDate(name, token);
            call.enqueue(callback);
        });
    }

    public void updatePasto(Pasto pasto, Callback callback) {
        Map<String, Object> jsonParams = new ArrayMap<>();

        jsonParams.put("nome", pasto.getName());
        jsonParams.put("quantita_croccantini", pasto.getQcroccantini());
        jsonParams.put("quantita_umido", pasto.getQumido());
        jsonParams.put("note", pasto.getNote());
        jsonParams.put("pasto_dieta_id", pasto.getDietaId());
        jsonParams.put("data", pasto.getData());
        jsonParams.put("ora", pasto.getOra());
        jsonParams.put("id_google_utente", firebaseUser.getUid());
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),(new JSONObject(jsonParams)).toString());
        getToken((String token)-> {
            Call<ApiGenericResponse<Object>> call = this.petApi.updatePasto(Long.toString(pasto.getId()), body, token);
            call.enqueue(callback);
        });
    }

    //================================================================================
    // DIET
    //================================================================================

    public void getPastiOfDiet(String id, Callback callback) {
        getToken((String token)-> {
            Call<ApiGenericResponse<List<Pasto>>> call = this.petApi.getPastoOfDiet(id, token);
            call.enqueue(callback);
        });
    }

    public void getAllDiets(Callback callback) {
        getToken((String token)-> {
            Call<ApiGenericResponse<List<Diet>>> call = this.petApi.getAllDiets(token);
            call.enqueue(callback);
        });
    }

    public void addDiet(String name, String note, String attiva, long idAnimale, long idDispenser, Callback callback) {
        Map<String, Object> jsonParams = new ArrayMap<>();

        jsonParams.put("nome_dieta", name);
        jsonParams.put("note", note);
        jsonParams.put("dieta_attiva", attiva);
        jsonParams.put("dieta_animale_id", idAnimale);
        jsonParams.put("dieta_dispenser_id", idDispenser);
        jsonParams.put("id_google_utente", firebaseUser.getUid());
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),(new JSONObject(jsonParams)).toString());

        getToken((String token)-> {
            Call<ApiGenericResponse<InsertGenericResponse>> call = this.petApi.addDiet(body, token);
            call.enqueue(callback);
        });
    }

    public void deleteDiet(String id, Callback callback) {
        getToken((String token)-> {
            Call<ApiGenericResponse<Object>> call = this.petApi.deleteDiet(id, token);
            call.enqueue(callback);
        });

    }

    public void getDietById(String id, Callback callback) {
        getToken((String token)-> {
            Call<ApiGenericResponse<Diet>> call = this.petApi.getDietById(id, token);
            call.enqueue(callback);
        });

    }

    public void getLastDiet(Callback callback) {
        getToken((String token)-> {
            Call<ApiGenericResponse<Diet>> call = this.petApi.getLastDiet(token);
            call.enqueue(callback);
        });
    }

    public void updateDiet(Diet diet, Callback callback) {
        Map<String, Object> jsonParams = new ArrayMap<>();

        jsonParams.put("nome_dieta", diet.getName());
        jsonParams.put("note", diet.getNote());
        jsonParams.put("dieta_attiva", diet.getAttiva());
        jsonParams.put("dieta_animale_id", diet.getAnimalId());
        jsonParams.put("dieta_dispenser_id", diet.getDispenserId());
        jsonParams.put("id_google_utente", firebaseUser.getUid());
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),(new JSONObject(jsonParams)).toString());
        getToken((String token)-> {
            Call<ApiGenericResponse<Object>> call = this.petApi.updateDiet(Long.toString(diet.getId()), body, token);
            call.enqueue(callback);
        });
    }

    interface TokenCallback {
        void call(String token);
    }

    private void getToken(TokenCallback cb) {
        firebaseUser.getIdToken(true)
            .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                public void onComplete(@NonNull Task<GetTokenResult> task) {
                    if (task.isSuccessful()) {
                        String idToken = task.getResult().getToken();
                        cb.call(idToken);
                        Log.i("token",idToken);
                    } else {
                        // Handle error -> task.getException();
                    }
                }
            });
    }
}
