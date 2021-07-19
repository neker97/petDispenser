package com.example.petdispenser.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.petdispenser.R;
import com.example.petdispenser.adapters.DietsListAdapter;
import com.example.petdispenser.api.PetApiService;
import com.example.petdispenser.api.model.ApiGenericResponse;
import com.example.petdispenser.dao.DietaDAO;
import com.example.petdispenser.dao.PastoDAO;
import com.example.petdispenser.models.Diet;
import com.example.petdispenser.models.Pasto;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

public class SingleMealInfo extends Fragment {

    private EditText quantitaCroccantini,quantitaUmido,note;
    private TextInputLayout nomePasto,dataPasto,oraPasto;
    private Button cancella,salva;


    private Calendar c;
    private DatePickerDialog dpd;
    private Date data;
    private long idPasto;
    private int hour,minute;
    private PetApiService petService = new PetApiService();
    private Pasto currentPasto;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.single_meal_info, container, false);
        idPasto = getArguments().getLong("id");
        nomePasto = v.findViewById(R.id.nome_info_pasto);
        quantitaCroccantini = v.findViewById(R.id.numero_quantita_croccantini_info_pasto);
        quantitaUmido = v.findViewById(R.id.numero_quantita_umido_info_pasto);
        note = v.findViewById(R.id.note_info_pasto);
        dataPasto = v.findViewById(R.id.data_info_pasto);
        oraPasto = v.findViewById(R.id.ora_info_pasto);

        cancella = v.findViewById(R.id.cancella_info_pasto);
        salva = v.findViewById(R.id.salva_info_pasto);

        Callback cbPasto = new Callback<ApiGenericResponse<Pasto>>() {
            @Override
            public void onResponse(Call<ApiGenericResponse<Pasto>> call, Response<ApiGenericResponse<Pasto>> response) {

                Log.d("TAG",response.code()+"");
                if(response.isSuccessful()) {
                    ApiGenericResponse<Pasto> resource = response.body();
                    if (resource.success) {
                        currentPasto = resource.result;

                        //inserisco i dati che ho
                        nomePasto.getEditText().setText(currentPasto.getName());
                        quantitaCroccantini.setText(currentPasto.getQcroccantini());
                        quantitaUmido.setText(currentPasto.getQumido());
                        note.setText(currentPasto.getNote());
                        dataPasto.getEditText().setText(currentPasto.getData());
                        oraPasto.getEditText().setText(currentPasto.getOra());
                    } else {
                        Toast.makeText(getContext(), resource.error, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiGenericResponse<Pasto>> call, Throwable t) {
                call.cancel();
                Toast.makeText(getContext(), "Error with API call, please retry.", Toast.LENGTH_SHORT).show();
            }
        };
        petService.getPastoById(String.valueOf(idPasto), cbPasto);

        oraPasto.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        v.getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                hour = i;
                                minute = i1;


                                Calendar calendar = Calendar.getInstance();

                                calendar.set(Calendar.HOUR_OF_DAY, i);
                                calendar.set(Calendar.MINUTE,i1);
                                oraPasto.getEditText().setText(String.format("%02d",calendar.get(Calendar.HOUR_OF_DAY)) +":"+ String.format("%02d",calendar.get(Calendar.MINUTE)));
                            }
                        },24,0,true
                );
                timePickerDialog.updateTime(hour,minute);
                timePickerDialog.show();
            }
        });

        cancella.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        salva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("isNewDieta",false);
                bundle.putLong("id",getArguments().getLong("idDieta"));
                Pasto pasto = new Pasto(nomePasto.getEditText().getText().toString(),note.getText().toString(),
                        quantitaCroccantini.getText().toString().isEmpty()?"0":quantitaCroccantini.getText().toString(),
                        quantitaUmido.getText().toString().isEmpty()?"0":quantitaUmido.getText().toString(),
                        dataPasto.getEditText().getText().toString(), oraPasto.getEditText().getText().toString());
                pasto.setDietaId(getArguments().getLong("idDieta"));
                pasto.setId(idPasto);

                Callback cb = new Callback<ApiGenericResponse<Object>>() {
                    @Override
                    public void onResponse(Call<ApiGenericResponse<Object>> call, Response<ApiGenericResponse<Object>> response) {

                        Log.d("TAG",response.code()+"");
                        if(response.isSuccessful()) {
                            ApiGenericResponse<Object> resource = response.body();
                            if (resource.success) {
                                Navigation.findNavController(v).navigate(R.id.diet_info_fragment,bundle);
                            } else {
                                Toast.makeText(getContext(), resource.error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiGenericResponse<Object>> call, Throwable t) {
                        call.cancel();
                        Toast.makeText(getContext(), "Error with API call, please retry.", Toast.LENGTH_SHORT).show();
                    }
                };

                petService.updatePasto(pasto, cb);
            }
        });

        dataPasto.getEditText().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                c = Calendar.getInstance();

                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);
                dpd = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {
                        dataPasto.getEditText().setText(mDay + "/" + (mMonth+1) +"/" + mYear);
                        data = new GregorianCalendar(mYear,mMonth+1,mDay).getTime();
                    }
                },year,month,day);
                dpd.show();
            }
        });

        return v;
    }



























}
