package com.example.petdispenser.api;

import com.example.petdispenser.api.model.AnimalResponse;
import com.example.petdispenser.api.model.ApiGenericResponse;
import com.example.petdispenser.api.model.InsertGenericResponse;
import com.example.petdispenser.models.Animal;
import com.example.petdispenser.models.Diet;
import com.example.petdispenser.models.Dispenser;
import com.example.petdispenser.models.Pasto;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.DELETE;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PetApiApi {

    String ANIMAL = "animals";
    String DISPENSER = "dispenser";
    String MEAL = "meals";
    String DIET = "diets";

    //================================================================================
    // ANIMAL
    //================================================================================

    @GET(ANIMAL)
    Call<ApiGenericResponse<List<Animal>>> getAllAnimals(@Header("Authorization") String token);

    @POST(ANIMAL)
    Call<ApiGenericResponse<InsertGenericResponse>> addAnimale(@Body RequestBody body, @Header("Authorization") String token);

    @DELETE(ANIMAL + "/{id}")
    Call<ApiGenericResponse<Object>> deleteAnimale(@Path("id") String id, @Header("Authorization") String token);

    @GET(ANIMAL + "/{id}")
    Call<ApiGenericResponse<Animal>> getAnimalById(@Path("id") String id, @Header("Authorization") String token);

    @GET(ANIMAL)
    Call<ApiGenericResponse<Animal>> getAnimalByName(@Query("name") String name, @Header("Authorization") String token);

    @PUT(ANIMAL + "/{id}")
    Call<ApiGenericResponse<Object>> updateAnimal(@Path("id") String id, @Body RequestBody body, @Header("Authorization") String token);

    @GET(ANIMAL + "/{id}/" + DIET)
    Call<ApiGenericResponse<List<Diet>>> getDietOfAnimal(@Path("id") String id, @Header("Authorization") String token);

    //================================================================================
    // DISPENSER
    //================================================================================

    @GET(DISPENSER)
    Call<ApiGenericResponse<List<Dispenser>>> getAllDispenser(@Header("Authorization") String token);

    @POST(DISPENSER)
    Call<ApiGenericResponse<InsertGenericResponse>> addDispenser(@Body RequestBody body, @Header("Authorization") String token);

    @DELETE(DISPENSER + "/{id}")
    Call<ApiGenericResponse<Object>> deleteDispenser(@Path("id") String id, @Header("Authorization") String token);

    @GET(DISPENSER + "/{id}")
    Call<ApiGenericResponse<Dispenser>> getDispenserById(@Path("id") String id, @Header("Authorization") String token);

    @GET(DISPENSER)
    Call<ApiGenericResponse<Dispenser>> getDispenserByName(@Query("name") String name, @Header("Authorization") String token);

    @GET(DISPENSER + "/{id}/diet")
    Call<ApiGenericResponse<List<Diet>>> getDietOfDispenser(@Path("id") String id, @Header("Authorization") String token);

    //================================================================================
    // MEAL
    //================================================================================

    @GET(MEAL)
    Call<ApiGenericResponse<List<Pasto>>> getAllPasto(@Header("Authorization") String token);

    @POST(MEAL)
    Call<ApiGenericResponse<InsertGenericResponse>> addPasto(@Body RequestBody body, @Header("Authorization") String token);

    @DELETE(MEAL + "/{id}")
    Call<ApiGenericResponse<Object>> deletePasto(@Path("id") String id, @Header("Authorization") String token);

    @GET(MEAL + "/{id}")
    Call<ApiGenericResponse<Pasto>> getPastoById(@Path("id") String id, @Header("Authorization") String token);

    @GET(MEAL)
    Call<ApiGenericResponse<List<Pasto>>> getPastiOfDate(@Query("date") String date, @Header("Authorization") String token);

    @PUT(MEAL + "/{id}")
    Call<ApiGenericResponse<Object>> updatePasto(@Path("id") String id, @Body RequestBody body, @Header("Authorization") String token);

    //================================================================================
    // DIET
    //================================================================================

    @GET(DIET + "/{id}/meal")
    Call<ApiGenericResponse<List<Pasto>>> getPastoOfDiet(@Path("id") String id, @Header("Authorization") String token);

    @GET(DIET)
    Call<ApiGenericResponse<List<Diet>>> getAllDiets(@Header("Authorization") String token);

    @POST(DIET)
    Call<ApiGenericResponse<InsertGenericResponse>> addDiet(@Body RequestBody body, @Header("Authorization") String token);

    @DELETE(DIET + "/{id}")
    Call<ApiGenericResponse<Object>> deleteDiet(@Path("id") String id, @Header("Authorization") String token);

    @GET(DIET + "/{id}")
    Call<ApiGenericResponse<Diet>> getDietById(@Path("id") String id, @Header("Authorization") String token);

    @GET(DIET + "/last")
    Call<ApiGenericResponse<Diet>> getLastDiet(@Header("Authorization") String token);

    @PUT(DIET + "/{id}")
    Call<ApiGenericResponse<Object>> updateDiet(@Path("id") String id, @Body RequestBody body, @Header("Authorization") String token);

}
