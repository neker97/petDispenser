package com.example.petdispenser.fragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.petdispenser.MainActivityToolbarListener;
import com.example.petdispenser.R;
import com.example.petdispenser.adapters.DispensersListAdapter;
import com.example.petdispenser.adapters.PastiListsAdapter;
import com.example.petdispenser.api.PetApiService;
import com.example.petdispenser.api.model.ApiGenericResponse;
import com.example.petdispenser.dao.AnimaleDAO;
import com.example.petdispenser.dao.DietaDAO;
import com.example.petdispenser.dao.PastoDAO;
import com.example.petdispenser.models.Animal;
import com.example.petdispenser.models.Diet;
import com.example.petdispenser.models.Dispenser;
import com.example.petdispenser.models.Pasto;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;

public class DietInfo extends Fragment {

    private Spinner spinnerAnimali;
    private ArrayAdapter<String> adAnimali;
    private ArrayList<String> nomiAnimali;

    private Spinner spinnerDistributori;
    private ArrayAdapter<String> adDistributori;
    private ArrayList<String> nomi_distributori;

    private Switch DietaSwitch;

    private EditText noteDieta;
    private TextInputLayout nomeDieta;

    private MainActivityToolbarListener itemDetailFragmentListener;

    private ListView listView;
    private Button addSingleMeal,cancel,confirm;
    private List<Pasto> listaPasti;


    private AnimaleDAO animaleDAO;
    private DietaDAO dietaDAO;
    private PastoDAO pastoDAO;

    private Long idDietaSelezionata;
    private PastiListsAdapter mAdapter;
    private PetApiService petService = new PetApiService();
    private Diet currentDiet;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.diet_info_fragment, container, false);

        animaleDAO = new AnimaleDAO(v.getContext());
        dietaDAO = new DietaDAO(v.getContext());
        pastoDAO = new PastoDAO(v.getContext());




        spinnerAnimali = v.findViewById(R.id.spinner_animale__info_dieta);
        spinnerDistributori = v.findViewById(R.id.spinner_dispenser_info_dieta);
        nomeDieta = v.findViewById(R.id.nome_dieta_Info_dieta_vera);
        noteDieta = v.findViewById(R.id.note_info_dieta_vera);
        listView = v.findViewById(R.id.lista_pasti_info_dieta);
        addSingleMeal = v.findViewById(R.id.buttonAddSingoloPastoInfoDieta);
        DietaSwitch = v.findViewById(R.id.switchIsAttivaInfoDieta);
        cancel = v.findViewById(R.id.annulla_button_info_dieta);
        confirm = v.findViewById(R.id.conferma_button_info_dieta);

        idDietaSelezionata = getArguments().getLong("id");

        //riempimento campi
        Callback cbDiet = new Callback<ApiGenericResponse<Diet>>() {
            @Override
            public void onResponse(Call<ApiGenericResponse<Diet>> call, Response<ApiGenericResponse<Diet>> response) {

                Log.d("TAG",response.code()+"");
                if(response.isSuccessful()) {
                    ApiGenericResponse<Diet> resource = response.body();
                    if (resource.success) {
                        currentDiet = resource.result;
                        nomeDieta.getEditText().setText(currentDiet.getName());
                        noteDieta.setText(currentDiet.getNote());
                        DietaSwitch.setChecked(currentDiet.getAttiva().equals("1"));
                    } else {
                        Toast.makeText(getContext(), resource.error, Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<ApiGenericResponse<Diet>> call, Throwable t) {
                call.cancel();
                Toast.makeText(getContext(), "Error with API call, please retry.", Toast.LENGTH_SHORT).show();
            }
        };
        petService.getDietById(String.valueOf(idDietaSelezionata), cbDiet);

        listaPasti = new ArrayList<>();
        TextView textView = new TextView(v.getContext());
        textView.setText("Meal list");
        textView.setTextSize(18);
        listView.addHeaderView(textView,null,false);


        Callback cb = new Callback<ApiGenericResponse<List<Pasto>>>() {
            @Override
            public void onResponse(Call<ApiGenericResponse<List<Pasto>>> call, Response<ApiGenericResponse<List<Pasto>>> response) {

                Log.d("TAG",response.code()+"");
                if(response.isSuccessful()) {
                    ApiGenericResponse<List<Pasto>> resource = response.body();
                    if (resource.success) {
                        List<Pasto> pastoTemp = resource.result;
                        for(Pasto p : pastoTemp) {
                            try {
                                CancellaPastoScaduto(p);
                            } catch (SQLException | UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        Toast.makeText(getContext(), resource.error, Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<ApiGenericResponse<List<Pasto>>> call, Throwable t) {
                call.cancel();
                Toast.makeText(getContext(), "Error with API call, please retry.", Toast.LENGTH_SHORT).show();
            }
        };
        petService.getAllPasto(cb);

        Callback callback = new Callback<ApiGenericResponse<List<Pasto>>>() {
            @Override
            public void onResponse(Call<ApiGenericResponse<List<Pasto>>> call, Response<ApiGenericResponse<List<Pasto>>> response) {

                Log.d("TAG",response.code()+"");
                if(response.isSuccessful()) {
                    ApiGenericResponse<List<Pasto>> resource = response.body();
                    if (resource.success) {
                        listaPasti = resource.result;
                        if(listaPasti != null && !listaPasti.isEmpty())
                        {
                            if(mAdapter != null) mAdapter.clear();
                            mAdapter = new PastiListsAdapter(getContext(),R.layout.pasti_lists_adapter,listaPasti);
                            listView.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
                        }
                        else
                        {
                            //  mTxtEmptyListAnimals.setVisibility(View.VISIBLE);
                            listView.setVisibility(View.GONE);
                        }
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

        petService.getPastiOfDiet(String.valueOf(idDietaSelezionata),callback);

        if(listaPasti.isEmpty()) {

            listView.setEmptyView(new View(v.getContext()));
        }

        if(listaPasti != null && !listaPasti.isEmpty()){
           // listaPasti.add(new Pasto("pastoProva","noteProva","111","111","12/9/2020","11:22"));
            mAdapter = new PastiListsAdapter(getContext(),R.layout.pasti_lists_adapter,listaPasti);
            listView.setAdapter(mAdapter);
        }
        else {
            listView.setVisibility(View.GONE);
        }


        addSingleMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //passare ad aggiungi singolo pasto
                //ma dove recupero l'id di questa dieta?
                Bundle bundle = new Bundle();
                bundle.putString("nome",nomeDieta.getEditText().getText().toString());
                bundle.putLong("idDieta",getArguments().getLong("id"));
                Navigation.findNavController(view).navigate(R.id.singleMealFormFragment,bundle);

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Pasto p = (Pasto) adapterView.getItemAtPosition(i);
                Bundle bundle = new Bundle();
                bundle.putLong("id",p.getId());
                bundle.putString("nome",nomeDieta.getEditText().getText().toString());
                bundle.putLong("idDieta",getArguments().getLong("id"));
                Navigation.findNavController(view).navigate(R.id.single_meal_info,bundle);
            }
        });


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Diet d = new Diet(nomeDieta.getText().toString(),noteDieta.getText().toString(),DietaSwitch.getText().toString());
                Diet d = currentDiet;

                d.setName(nomeDieta.getEditText().getText().toString());
                d.setNote(noteDieta.getText().toString());
                d.setAttiva(DietaSwitch.isChecked()? "1":"0");
                d.setAnimalId(Integer.parseInt(spinnerAnimali.getSelectedItem().toString()));

                if(spinnerDistributori.getSelectedItem() != null) {
                    d.setDispenserId(Long.parseLong(spinnerDistributori.getSelectedItem().toString()));
                }
                else
                    {
                    Dispenser dis = new Dispenser();
                    dis.setId(0);
                    d.setDispenserId((long) 0);
                }

                Callback cb = new Callback<ApiGenericResponse<Object>>() {
                    @Override
                    public void onResponse(Call<ApiGenericResponse<Object>> call, Response<ApiGenericResponse<Object>> response) {

                        Log.d("TAG",response.code()+"");
                        if(response.isSuccessful()) {
                            ApiGenericResponse<Object> resource = response.body();
                            if (resource.success) {
                                Navigation.findNavController(v).navigate(R.id.diets_list_fragment);
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
                petService.updateDiet(d, cb);
            }

        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        nomiAnimali = new ArrayList<>();
        nomi_distributori = new ArrayList<>();

        Callback callbackAnimals = new Callback<ApiGenericResponse<List<Animal>>>() {
            @Override
            public void onResponse(Call<ApiGenericResponse<List<Animal>>> call, Response<ApiGenericResponse<List<Animal>>> response) {

                Log.d("TAG",response.code()+"");
                if(response.isSuccessful()) {
                    ApiGenericResponse<List<Animal>> resource = response.body();
                    if (resource.success) {

                        for (Animal an:resource.result) {
                            nomiAnimali.add(an.getName());
                        }

                        adAnimali = new ArrayAdapter<String>(v.getContext(), android.R.layout.simple_spinner_dropdown_item, nomiAnimali);
                        adAnimali.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerAnimali.setAdapter(adAnimali);
                    } else {
                        Toast.makeText(getContext(), resource.error, Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<ApiGenericResponse<List<Animal>>> call, Throwable t) {
                call.cancel();
                Toast.makeText(getContext(), "Error with API call, please retry.", Toast.LENGTH_SHORT).show();
            }
        };

        this.petService.getAllAnimals(callbackAnimals);

        Callback callbackDispenser = new Callback<ApiGenericResponse<List<Dispenser>>>() {
            @Override
            public void onResponse(Call<ApiGenericResponse<List<Dispenser>>> call, Response<ApiGenericResponse<List<Dispenser>>> response) {

                Log.d("TAG",response.code()+"");
                if(response.isSuccessful()) {
                    ApiGenericResponse<List<Dispenser>> resource = response.body();
                    if (resource.success) {

                        for(Dispenser dis:resource.result){
                            nomi_distributori.add(dis.getName());
                        }

                        adDistributori = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, nomi_distributori);
                        adDistributori.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerDistributori.setAdapter(adDistributori);
                    } else {
                        Toast.makeText(getContext(), resource.error, Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<ApiGenericResponse<List<Dispenser>>> call, Throwable t) {
                call.cancel();
                Toast.makeText(getContext(), "Error with API call, please retry.", Toast.LENGTH_SHORT).show();
            }
        };

        this.petService.getAllDispenser(callbackDispenser);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                Pasto p = (Pasto) adapterView.getItemAtPosition(i);
                showAlertDialog(v,pastoDAO,p);
                //refresh del fragment
                return true;
            }
        });

        PastiListsAdapter adapter = new PastiListsAdapter(getContext(),R.layout.pasti_lists_adapter,listaPasti);
        listView.setAdapter(adapter);

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
        this.itemDetailFragmentListener.changeTitle("Diet info");
    }

    public void showAlertDialog(View v, PastoDAO dDao, Pasto d)
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());
        alertDialog.setTitle("Confirm delete");
        alertDialog.setMessage("Do you want delete the meal?");
        alertDialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Callback cbDelete = new Callback<ApiGenericResponse<List<Pasto>>>() {
                    @Override
                    public void onResponse(Call<ApiGenericResponse<List<Pasto>>> call, Response<ApiGenericResponse<List<Pasto>>> response) {

                        Log.d("TAG",response.code()+"");
                        if(response.isSuccessful()) {
                            ApiGenericResponse<List<Pasto>> resource = response.body();
                            if (resource.success) {
                                Callback callback = new Callback<ApiGenericResponse<List<Pasto>>>() {
                                    @Override
                                    public void onResponse(Call<ApiGenericResponse<List<Pasto>>> call, Response<ApiGenericResponse<List<Pasto>>> response) {

                                        Log.d("TAG",response.code()+"");
                                        if(response.isSuccessful()) {
                                            ApiGenericResponse<List<Pasto>> resource = response.body();
                                            if (resource.success) {
                                                listaPasti = resource.result;
                                                if(listaPasti != null && !listaPasti.isEmpty())
                                                {
                                                    if(mAdapter != null) mAdapter.clear();
                                                    mAdapter = new PastiListsAdapter(getContext(),R.layout.pasti_lists_adapter,listaPasti);
                                                    listView.setAdapter(mAdapter);
                                                    mAdapter.notifyDataSetChanged();
                                                    Toast.makeText(v.getContext(),"meal "+d.getName()+" canceled",Toast.LENGTH_SHORT).show();
                                                }
                                                else
                                                {
                                                    //  mTxtEmptyListAnimals.setVisibility(View.VISIBLE);
                                                    listView.setVisibility(View.GONE);
                                                }
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

                                petService.getPastiOfDiet(String.valueOf(idDietaSelezionata),callback);
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
                petService.deletePasto(String.valueOf(d.getId()), cbDelete);
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

    public void CancellaPastoScaduto(Pasto p) throws SQLException, UnsupportedEncodingException {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int hour = Calendar.getInstance().get(Calendar.HOUR);
        int minute = Calendar.getInstance().get(Calendar.MINUTE);

        String data = p.getData();
        String ora = p.getOra();

        if(!data.isEmpty() && !ora.isEmpty()) {
            StringTokenizer st = new StringTokenizer(data, "/");
            int dayP = Integer.parseInt(st.nextToken());
            int monthP = Integer.parseInt(st.nextToken());
            int yearP = Integer.parseInt(st.nextToken());

            StringTokenizer st2 = new StringTokenizer(ora,":");
            int hourP = Integer.parseInt(st2.nextToken());
            int minuteP = Integer.parseInt(st2.nextToken());



            if (year >= yearP && month >= monthP && day >= dayP && hour>= hourP && minute >= minuteP)
            {
                petService.deletePasto(String.valueOf(p.getId()), null);
            }
        }
    }
}
