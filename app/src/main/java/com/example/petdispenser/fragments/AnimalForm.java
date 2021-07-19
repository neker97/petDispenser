package com.example.petdispenser.fragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.petdispenser.MainActivityToolbarListener;
import com.example.petdispenser.R;
import com.example.petdispenser.adapters.PastiListsAdapter;
import com.example.petdispenser.api.PetApiService;
import com.example.petdispenser.api.model.ApiGenericResponse;
import com.example.petdispenser.dao.AnimaleDAO;
import com.example.petdispenser.models.Animal;
import com.example.petdispenser.models.Pasto;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;

public class AnimalForm extends Fragment {

    private MainActivityToolbarListener itemDetailFragmentListener;

    private TextInputLayout nomeAnimale, tipoAnimale, razzaAnimale, pesoAnimale, ddnAnimale;
    private Button conferma,cancella;

    private AnimaleDAO animaleDAO;
    private Calendar c;
    private DatePickerDialog dpd;

    private PetApiService petService = new PetApiService();

    DatabaseReference reff;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.animal_form_fragment, container, false);

        this.animaleDAO = new AnimaleDAO(v.getContext());

        reff = FirebaseDatabase.getInstance().getReference().child("Animal");
        nomeAnimale = v.findViewById(R.id.Inserisci_nome_animale);
        tipoAnimale = v.findViewById(R.id.tipo_animale_da_inserire);
        razzaAnimale = v.findViewById(R.id.razza_animale_da_inserire);
        pesoAnimale = v.findViewById(R.id.peso_da_inserire);
        ddnAnimale = v.findViewById(R.id.data_animale_da_inserire);

        this.conferma =  v.findViewById(R.id.conferma_animale_da_inserire);
        cancella = v.findViewById(R.id.annulla_animale_da_inserire);




        ddnAnimale.getEditText().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                c = Calendar.getInstance();
                int day = 0;
                int month = 0;
                int year = 0;
                if(!ddnAnimale.getEditText().getText().toString().isEmpty()) {
                    String[] date = ddnAnimale.getEditText().getText().toString().split("/");
                    day = Integer.parseInt(date[0]);
                    month = Integer.parseInt(date[1])-1;
                    year = Integer.parseInt(date[2]);
                } else {
                    day = c.get(Calendar.DAY_OF_MONTH);
                    month = c.get(Calendar.MONTH);
                    year = c.get(Calendar.YEAR);
                }

                dpd = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {
                        ddnAnimale.getEditText().setText(mDay + "/" + (mMonth+1) +"/" + mYear);
                    }
                },year,month,day);
                dpd.show();
            }
        });

        cancella.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });


        this.conferma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable nome = nomeAnimale.getEditText().getText();
                Editable tipo = tipoAnimale.getEditText().getText();
                Editable razza = razzaAnimale.getEditText().getText();
                Editable peso = pesoAnimale.getEditText().getText();
                Editable ddn = ddnAnimale.getEditText().getText();


                //se i campi non sono vuoti
                if (!TextUtils.isEmpty(nome) && !TextUtils.isEmpty(tipo) &&
                        !TextUtils.isEmpty(razza) && !TextUtils.isEmpty(peso) &&
                        !TextUtils.isEmpty(ddn)){
                    // aggiungi l'animale al db
                    Callback cb = new Callback<ApiGenericResponse<List<Pasto>>>() {
                        @Override
                        public void onResponse(Call<ApiGenericResponse<List<Pasto>>> call, Response<ApiGenericResponse<List<Pasto>>> response) {

                            Log.d("TAG",response.code()+"");
                            if(response.isSuccessful()) {
                                ApiGenericResponse<List<Pasto>> resource = response.body();
                                if (resource.success) {
                                    Toast.makeText(getContext(),"animal inserted", Toast.LENGTH_LONG).show();
                                    getActivity().onBackPressed();
                                } else {
                                    //progress.setVisibility(View.GONE);
                                }
                            }

                        }

                        @Override
                        public void onFailure(Call<ApiGenericResponse<List<Pasto>>> call, Throwable t) {
                            call.cancel();
                            Toast.makeText(getContext(), "Error with API call, please retry.", Toast.LENGTH_SHORT).show();
                            Log.e("GETALLANIMAL", "Error creating API wallet", t);
                        }
                    };

                    petService.addAnimale(nome.toString(), "", tipo.toString(), razza.toString(), Float.valueOf(peso.toString()),  ddn.toString(), cb);
                }
            }
        });

        return v;
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

    @Override
    public void onResume() {
        super.onResume();

        this.itemDetailFragmentListener.showBackButton(true);
        this.itemDetailFragmentListener.changeTitle("Animal insertion");
    }
}