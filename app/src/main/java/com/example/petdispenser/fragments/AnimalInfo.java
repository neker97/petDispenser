package com.example.petdispenser.fragments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amplifyframework.AmplifyException;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.storage.s3.AWSS3StoragePlugin;
import com.bumptech.glide.Glide;
import com.example.petdispenser.MainActivity;
import com.example.petdispenser.MainActivityToolbarListener;
import com.example.petdispenser.R;
import com.example.petdispenser.api.PetApiService;
import com.example.petdispenser.api.model.ApiGenericResponse;
import com.example.petdispenser.dao.AnimaleDAO;
import com.example.petdispenser.models.Animal;
import com.example.petdispenser.models.Pasto;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.StringTokenizer;

import com.amazonaws.services.s3.AmazonS3Client;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@RuntimePermissions
public class AnimalInfo extends Fragment {
    

    private ImageView caricaFoto,fotoAnimale;

    private TextInputLayout editNomeAnimale,editTipoAnimale,editRazzaAnimale,editPesoAnimale,editDdnAnimale;

    private Button cancella,conferma;

    private MainActivityToolbarListener itemDetailFragmentListener;

    private static final int PICK_IMAGE = 100;

    private Animal animaleRicevuto;

    private AnimaleDAO animaleDao;

    private Uri imageUri;
    private Date data = null;
    private DatePickerDialog dpd;
    private Calendar c;
    private Uri uri;

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    private PetApiService petService = new PetApiService();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.animal_info_fragment, container, false);
        SimpleDateFormat formattedDate = new SimpleDateFormat("dd-MM-yyyy");


     //   configureAmplifyStorage();
        //trovo le edit text
        editNomeAnimale = v.findViewById(R.id.nome_animale_info_animale);
        editTipoAnimale = v.findViewById(R.id.tipo_animale_info_animale);
        editPesoAnimale = v.findViewById(R.id.peso_animale_info_animale);
        editRazzaAnimale = v.findViewById(R.id.razza_animale_info_animale);
        editDdnAnimale = v.findViewById(R.id.data_animale_info_animale);

        //trovo il pulsante per modificare l'immagine e l'immaginr
       // caricaFoto = v.findViewById(R.id.caria_foto_info_animale);
        fotoAnimale = v.findViewById(R.id.foto_animale_info_animale);

        //trovo pulsanti cancella e conferma
        cancella = v.findViewById(R.id.cancella_info_animale);
        conferma = v.findViewById(R.id.conferma_info_animale);


        editNomeAnimale.setEnabled(true);
        editTipoAnimale.setEnabled(true);
        editRazzaAnimale.setEnabled(true);
        editPesoAnimale.setEnabled(true);
        editDdnAnimale.setEnabled(true);

        animaleDao = new AnimaleDAO(getContext());

        Callback cb = new Callback<ApiGenericResponse<Animal>>() {
            @Override
            public void onResponse(Call<ApiGenericResponse<Animal>> call, Response<ApiGenericResponse<Animal>> response) {

                Log.d("TAG",response.code()+"");
                if(response.isSuccessful()) {
                    ApiGenericResponse<Animal> resource = response.body();
                    if (resource.success) {
                        animaleRicevuto = resource.result;
                        editNomeAnimale.getEditText().setText(animaleRicevuto.getName());
                        editTipoAnimale.getEditText().setText(animaleRicevuto.getTipologia());
                        editRazzaAnimale.getEditText().setText(animaleRicevuto.getRazza());
                        editPesoAnimale.getEditText().setText(animaleRicevuto.getPeso()+"");
                        editDdnAnimale.getEditText().setText(animaleRicevuto.getDdn());

                        setImage();
                    } else {
                        Toast.makeText(getContext(), resource.error, Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<ApiGenericResponse<Animal>> call, Throwable t) {
                call.cancel();
                Toast.makeText(getContext(), "Error with API call, please retry.", Toast.LENGTH_SHORT).show();
            }
        };

        petService.getAnimalById(Long.toString(getArguments().getLong("id")), cb);

        editDdnAnimale.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 c = Calendar.getInstance();

                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);
                dpd = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {

                        editDdnAnimale.getEditText().setText(mDay + "/" + (mMonth+1) +"/" + mYear);
                        data = new GregorianCalendar(mYear,mMonth+1,mDay).getTime();

                    }
                },year,month,day);
                dpd.show();
            }
        });

        //quando clicco su cancella , ricarico la activity da capo
        cancella.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment frg = null;
                frg = getFragmentManager().findFragmentById(R.id.animal_info_fragment);
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(frg);
                ft.attach(frg);
                ft.commit();
            }
        });

        conferma.setOnClickListener(new View.OnClickListener() { //update del database

            @Override
            public void onClick(View v) {
                Animal animal = new Animal(
                        editNomeAnimale.getEditText().getText().toString(),
                       animaleRicevuto.getPathPicture(),
                    editTipoAnimale.getEditText().getText().toString()
                        ,editRazzaAnimale.getEditText().getText().toString(),
                    Float.valueOf(editPesoAnimale.getEditText().getText().toString()),
                        editDdnAnimale.getEditText().getText().toString());
                animal.setId(animaleRicevuto.getId());
                Callback cb = new Callback<ApiGenericResponse<Object>>() {
                    @Override
                    public void onResponse(Call<ApiGenericResponse<Object>> call, Response<ApiGenericResponse<Object>> response) {

                        Log.d("TAG",response.code()+"");
                        if(response.isSuccessful()) {
                            ApiGenericResponse<Object> resource = response.body();
                            if (resource.success) {
                                Toast.makeText(getContext(),"animal updated", Toast.LENGTH_SHORT).show();
                                getActivity().onBackPressed();
                            } else {
                                //progress.setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiGenericResponse<Object>> call, Throwable t) {
                        call.cancel();
                        Toast.makeText(getContext(), "Error with API call, please retry.", Toast.LENGTH_SHORT).show();
                        Log.e("GETALLANIMAL", "Error creating API wallet", t);
                    }
                };

                petService.updateAnimal(animal, cb);
            }
        });

        //fare gli editNomeAnimale.setHint("mike"); col nome del animale per esempio con tutti gli altri attributi
        fotoAnimale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        return v;
    }



    private void openGallery()
    {
        Intent gallery = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(gallery,PICK_IMAGE);
    }


    @Override
    public void onActivityResult(int requestCode , int resultCode , Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE)
        {

            imageUri = data.getData();
            try {
                uploadFile(imageUri,getFileName(imageUri));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            imageUri.toString().replace(":","%3a");

            imageUri = Uri.parse(String.valueOf(imageUri));
            getContext().getContentResolver().takePersistableUriPermission(imageUri,Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);




            fotoAnimale.setImageURI(imageUri);


            uri = imageUri;
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            itemDetailFragmentListener = (MainActivityToolbarListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("$context must implement MainActivityToolbarListener");
        }
    }

    public void onResume(){
        super.onResume();

        this.itemDetailFragmentListener.showBackButton(true);
        this.itemDetailFragmentListener.changeTitle("Animal info");

    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void setImage()
    {
        if(animaleRicevuto.getPathPicture() == null || animaleRicevuto.getPathPicture().isEmpty() || animaleRicevuto.equals("") || animaleRicevuto.getPathPicture().equals("drawable://2131230919"))
        {
           // fotoAnimale.setImageResource(R.drawable.paw);
            uri = Uri.parse("android.resource://your.package.name/" + R.drawable.paw);
            Glide.with(getContext()).load("" +
                    "https://images7.alphacoders.com/101/1018088.jpg"
            ).into(fotoAnimale);
            //bellaluuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuu

        }
        else
        {
            Glide.with(getContext()).load(animaleRicevuto.getPathPicture()).into(fotoAnimale);

            fotoAnimale.setImageURI(Uri.parse(animaleRicevuto.getPathPicture()));
        }
    }

    private void uploadFile(Uri uri,String name) throws URISyntaxException, FileNotFoundException {
     //   String path = getRealPath2(uri);
        /*File exampleFile = new File(getActivity().getApplicationContext().getFilesDir(),name);

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(exampleFile));
            writer.append("Example file contents");
            writer.close();
        } catch (Exception exception) {
            Log.e("MyAmplifyApp", "Upload failed", exception);
        }*/

        InputStream inStream = getActivity().getContentResolver().openInputStream(uri);
        Amplify.Storage.uploadInputStream(name, inStream,
                result -> {
                    Log.i("MyAmplifyApp", "Successfully uploaded: " + result.getKey());
                    Amplify.Storage.getUrl(
                            name,
                            result2 -> {
                                Log.i("MyAmplifyApp", "Successfully generated: " + result2.getUrl());
                                animaleRicevuto.setPathPicture(result2.getUrl().toString());
                            },
                            error -> Log.e("MyAmplifyApp", "URL generation failure", error)

                    );
                }, error -> {
                    Log.e("MyAmplifyApp", "Upload failed", error);
                }

        );





    }

    private void getLinkFile()
    {

    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getActivity().getApplicationContext().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public String getPath(Uri uri)
    {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().getApplicationContext().getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index =             cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        return s;
    }

    private  String getRealPath(String path,String name)
    {
        StringBuilder fin = new StringBuilder();

        StringTokenizer st = new StringTokenizer(path,"//",true);
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if(token.startsWith("image:"))
            {
                token = name;
            }
            fin.append(token);
        }

        return fin.toString();
    }

    private String getRealPath2(Uri contentUri)
    {
        Context context = getActivity().getApplicationContext();
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(context, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }


    private void configureAmplifyStorage() {


        try {
            // Add these lines to add the AWSCognitoAuthPlugin and AWSS3StoragePlugin plugins
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.addPlugin(new AWSS3StoragePlugin());
            Amplify.configure(getActivity().getApplicationContext());

            Log.i("MyAmplifyApp", "Initialized Amplify");
        } catch (AmplifyException error) {
            Log.e("MyAmplifyApp", "Could not initialize Amplify", error);
        }


    }


}