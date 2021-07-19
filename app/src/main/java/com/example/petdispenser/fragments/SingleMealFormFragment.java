package com.example.petdispenser.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.petdispenser.CustomListDispencerInfoDispencerAdapter;
import com.example.petdispenser.MainActivityToolbarListener;
import com.example.petdispenser.R;
import com.example.petdispenser.api.PetApiService;
import com.example.petdispenser.api.model.ApiGenericResponse;
import com.example.petdispenser.api.model.InsertGenericResponse;
import com.example.petdispenser.dao.DietaDAO;
import com.example.petdispenser.dao.PastoDAO;
import com.example.petdispenser.models.Diet;
import com.example.petdispenser.models.Pasto;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import org.json.JSONException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class SingleMealFormFragment extends Fragment implements TimePickerDialog.OnTimeSetListener{
    private TextInputLayout nomePasto,dataPasto,oraPasto;
    private EditText quantitaCroccantini,quantitaUmido,note;

    private Button cancella,salva;

    Calendar c;
    DatePickerDialog dpd;
    Date data;

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    private int hour,minute;

    private MainActivityToolbarListener itemDetailFragmentListener;

    private PetApiService petService = new PetApiService();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.single_meal_form_fragment, container, false);

        //trovo gli oggetti nel activity
        nomePasto = v.findViewById(R.id.nome_pasto_singolo_pasto);
        quantitaCroccantini = v.findViewById(R.id.numero_quantita_croccantini_inserisci_pasto);
        quantitaUmido = v.findViewById(R.id.numero_quantita_umido_inserisci_pasto);
        note = v.findViewById(R.id.note_inserisci_pasto);
        dataPasto = v.findViewById(R.id.data_pasto_da_inserire);
        oraPasto = v.findViewById(R.id.ora_pasto_da_inserire);

        cancella = v.findViewById(R.id.cancella_inserisci_pasto);
        salva = v.findViewById(R.id.salva_inserisci_pasto);

        String dietaRiferimento = getArguments().getString("nome");


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
                Bundle bundle = new Bundle();
                bundle.putString("nome",dietaRiferimento);
                getFragmentManager().popBackStack();
            }
        });

        salva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("nome",dietaRiferimento);
                bundle.putBoolean("isNewDieta",false);
                Callback cb = new Callback<ApiGenericResponse<InsertGenericResponse>>() {
                    @Override
                    public void onResponse(Call<ApiGenericResponse<InsertGenericResponse>> call, Response<ApiGenericResponse<InsertGenericResponse>> response) {

                        Log.d("TAG",response.code()+"");
                        if(response.isSuccessful()) {
                            ApiGenericResponse<InsertGenericResponse> resource = response.body();
                            if (resource.success) {
                                Toast.makeText(getContext(),"meal saved",Toast.LENGTH_SHORT).show();
                                Navigation.findNavController(v).navigate(R.id.dietForm,bundle);
                            } else {
                                Toast.makeText(getContext(), resource.error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiGenericResponse<InsertGenericResponse>> call, Throwable t) {
                        call.cancel();
                        Toast.makeText(getContext(), "Error with API call, please retry.", Toast.LENGTH_SHORT).show();
                    }
                };
                petService.addPasto
                    (
                    nomePasto.getEditText().getText().toString(),note.getText().toString(),
                            quantitaCroccantini.getText().toString().isEmpty()?"0":quantitaCroccantini.getText().toString(),
                    quantitaUmido.getText().toString().isEmpty()?"0":quantitaUmido.getText().toString(),getArguments().getLong("idDieta"),
                    dataPasto.getEditText().getText().toString(),oraPasto.getEditText().getText().toString(), cb
                    );
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
        this.itemDetailFragmentListener.changeTitle(getString(R.string.add_pasto));
        this.itemDetailFragmentListener.setToggleListner(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog(v);
            }
        });

    }

    @Override
    public void onTimeSet(TimePicker timePicker,  int hourofDay, int minute) {

        oraPasto.getEditText().setText( String.format("%02d", hourofDay)+ ":"+ String.format("%02d", minute));
    }


    public void showAlertDialog(View v)
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());
        alertDialog.setTitle("Confirm back");
        alertDialog.setMessage("Do you want to go back? If you go back, all the changes will be lost.");
        alertDialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                itemDetailFragmentListener.goBack();
            }
        });
        alertDialog.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(v.getContext(),"meal NOT canceled",Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog dialog = alertDialog.create();

        //WindowManager.LayoutParams params = alertDialog.
        dialog.show();
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

        //positiveButton.setBackgroundColor(Color.parseColor("#0dd11a"));
        //negativeButton.setBackgroundColor(Color.parseColor("#ed1f0c"));


        negativeButton.setX(-30);

    }
}