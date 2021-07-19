package com.example.petdispenser.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.example.petdispenser.adapters.DietsListAdapter;
import com.example.petdispenser.adapters.DispensersListAdapter;
import com.example.petdispenser.adapters.PastiListsAdapter;
import com.example.petdispenser.api.PetApiService;
import com.example.petdispenser.api.model.ApiGenericResponse;
import com.example.petdispenser.api.model.InsertGenericResponse;
import com.example.petdispenser.dao.AnimaleDAO;
import com.example.petdispenser.dao.DietaDAO;
import com.example.petdispenser.dao.PastoDAO;
import com.example.petdispenser.models.Animal;
import com.example.petdispenser.models.Diet;
import com.example.petdispenser.models.Dispenser;
import com.example.petdispenser.models.Pasto;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.GestureDetector.OnGestureListener;

import org.json.JSONException;

public class DietForm extends Fragment implements OnGestureListener {

    private MainActivityToolbarListener itemDetailFragmentListener;

    private EditText note;
    private TextInputLayout name;

    private List<Pasto> listaPasti;

    private ListView listView;

    private Button aggiungiPasto,annulla,conferma;

    private Switch attiva;

    private Spinner nomeAnimale,nomeDispenser;
    private DietaDAO dietaDAO;
    private PastoDAO pastoDAO;
    private AnimaleDAO animaleDAO;
    private PastiListsAdapter mAdapter;

    private HashMap<String,Long> animale_id = new HashMap<>();
    private HashMap<String,Long> dispenser_id = new HashMap<>();

    private ArrayList<Pasto> pastoAppoggio = new ArrayList<>();

    private Integer idThisDieta;

    private GestureDetector detector;
    private PetApiService petService = new PetApiService();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.diet_form_fragment, container, false);

        setHasOptionsMenu(true);

        listView = (ListView) v.findViewById(R.id.lista_pasti_inserimento_dieta);
        dietaDAO = new DietaDAO(v.getContext());
        pastoDAO = new PastoDAO(v.getContext());
        animaleDAO = new AnimaleDAO((v.getContext()));

        aggiungiPasto = v.findViewById(R.id.buttonAddSingoloPasto);
        nomeAnimale = v.findViewById(R.id.spinner_animale_aggiungi_pasto);
        nomeDispenser = v.findViewById(R.id.spinner_dispenser_aggiungi_pasto);
        annulla = v.findViewById(R.id.annulla_button_diet_form);
        conferma = v.findViewById(R.id.conferma_button_diet_form);
        attiva = v.findViewById(R.id.switchIsAttiva);
        listView = v.findViewById(R.id.lista_pasti_inserimento_dieta);
        name = v.findViewById(R.id.nome_dieta_Info_dieta);
        note = v.findViewById(R.id.note_info_dieta);




        detector=new GestureDetector(v.getContext(),this);

        String dietaPassata = getArguments().getString("nome");
        name.getEditText().setText(dietaPassata);

        TextView textView = new TextView(v.getContext());
        textView.setText("Meal list");
        textView.setTextSize(18);
        listView.addHeaderView(textView,null,false);

        if(getArguments().getBoolean("isNewDieta"))
        {
            Callback callback = new Callback<ApiGenericResponse<InsertGenericResponse>>() {
                @Override
                public void onResponse(Call<ApiGenericResponse<InsertGenericResponse>> call, Response<ApiGenericResponse<InsertGenericResponse>> response) {

                    Log.d("TAG",response.code()+"");
                    if(response.isSuccessful()) {
                        ApiGenericResponse<InsertGenericResponse> resource = response.body();
                        if (resource.success) {
                            listaPasti = new ArrayList<>();
                            idThisDieta = resource.result.insertId;
                            reloadListPasti(Integer.toString(resource.result.insertId));

                            PastiListsAdapter adapter = new PastiListsAdapter(getContext(),R.layout.pasti_lists_adapter,listaPasti);
                            listView.setAdapter(adapter);
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
            petService.addDiet("","","",0,0, callback);
        }


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Pasto p = (Pasto) adapterView.getItemAtPosition(i);
                Bundle bundle = new Bundle();
                bundle.putLong("id",p.getId());

                Navigation.findNavController(view).navigate(R.id.single_meal_info,bundle);
            }
        });



        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Pasto p = (Pasto) adapterView.getItemAtPosition(i);
                showAlertDialog(v,pastoDAO,p);
                //refresh del fragment
                return true;
            }
        });


        List<String> nomiAnimali = new ArrayList<>();
        List<String> nomiDispenser = new ArrayList<>();


        aggiungiPasto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("nome",name.getEditText().getText().toString());
                bundle.putLong("idDieta",idThisDieta);

                Navigation.findNavController(v).navigate(R.id.singleMealFormFragment,bundle);
            }
        });

        annulla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Callback callback = new Callback<ApiGenericResponse<List<Pasto>>>() {
                    @Override
                    public void onResponse(Call<ApiGenericResponse<List<Pasto>>> call, Response<ApiGenericResponse<List<Pasto>>> response) {

                        Log.d("TAG",response.code()+"");
                        if(response.isSuccessful()) {
                            ApiGenericResponse<List<Pasto>> resource = response.body();
                            if (resource.success) {
                                Navigation.findNavController(v).navigate(R.id.diets_list_fragment);
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
                petService.deleteDiet(Integer.toString(idThisDieta), callback);
            }
        });

        conferma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!name.getEditText().getText().toString().trim().isEmpty() && !nomiAnimali.isEmpty()) {
                    //dietaDAO.addDiet(name.getText().toString(), note.getText().toString(), attiva.isChecked() ? "1" : "0", animale_id.get(nomeAnimale.getSelectedItem()), dispenser_id.get(nomeDispenser.getSelectedItem()) == null ? 0 : dispenser_id.get(nomeDispenser.getSelectedItem()));
                    Diet d = new Diet(
                            name.getEditText().getText().toString(),
                            note.getText().toString(),
                            attiva.isChecked() ? "1" : "0");
                    d.setAnimalId(Math.toIntExact(animale_id.get(nomeAnimale.getSelectedItem())));
//                    d.setDispenser(dispenserDAO.getDispenserById(dispenser_id.get(nomeDispenser.getSelectedItem())));
                    //passo 0 solo perchè attualmente no ci sono dispenser
                    d.setDispenserId((long) 0);
                    d.setId(idThisDieta);

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
                else
                {
                    Toast.makeText(v.getContext(),"name not inserted or no one animal exist",Toast.LENGTH_SHORT).show();
                }

            }
        });

        //spinner per nomi animali

        Callback callbackAnimals = new Callback<ApiGenericResponse<List<Animal>>>() {
            @Override
            public void onResponse(Call<ApiGenericResponse<List<Animal>>> call, Response<ApiGenericResponse<List<Animal>>> response) {

                Log.d("TAG",response.code()+"");
                if(response.isSuccessful()) {
                    ApiGenericResponse<List<Animal>> resource = response.body();
                    if (resource.success) {

                        for (Animal an:resource.result) {
                            nomiAnimali.add(an.getName());
                            animale_id.put(an.getName(),an.getId());
                        }

                        aggiungiElementiInSpinner(nomiAnimali,nomeAnimale);

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

                        for(Dispenser di:resource.result){
                            nomiDispenser.add(di.getName());
                            dispenser_id.put(di.getName(),di.getId());
                        }

                        aggiungiElementiInSpinner(nomiDispenser,nomeDispenser);
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

        return v;
    }

    public void showAlertDialog(View v, PastoDAO dDao, Pasto d) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());
        alertDialog.setTitle("confirm delete");
        alertDialog.setMessage("do u want delete the meal?");
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

                                petService.getPastiOfDiet(String.valueOf(idThisDieta),callback);
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

    private void aggiungiElementiInSpinner(List<String> robe , Spinner spinner) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,robe);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
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
        this.itemDetailFragmentListener.changeTitle(getString(R.string.add_diet));
        this.itemDetailFragmentListener.setToggleListner(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Callback cb = new Callback<ApiGenericResponse<Diet>>() {
                    @Override
                    public void onResponse(Call<ApiGenericResponse<Diet>> call, Response<ApiGenericResponse<Diet>> response) {

                        Log.d("TAG",response.code()+"");
                        if(response.isSuccessful()) {
                            ApiGenericResponse<Diet> resource = response.body();
                            if (resource.success) {
                                showAlertDialog(v, resource.result);
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
                petService.getDietById(String.valueOf(idThisDieta), cb);
            }
        });

    }

    public void showAlertDialog(View v, Diet d)
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext(),android.R.style.Theme_Material_Light_Dialog_Alert);
        alertDialog.setTitle("Confirm back");
        alertDialog.setMessage("Do you want to go back? If you go back, all the changes will be lost.");
        alertDialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Callback cb = new Callback<ApiGenericResponse<Object>>() {
                    @Override
                    public void onResponse(Call<ApiGenericResponse<Object>> call, Response<ApiGenericResponse<Object>> response) {

                        Log.d("TAG",response.code()+"");
                        if(response.isSuccessful()) {
                            ApiGenericResponse<Object> resource = response.body();
                            if (resource.success) {
                                itemDetailFragmentListener.goBack();
                                Toast.makeText(v.getContext(),"dieta "+d.getName()+" canceled",Toast.LENGTH_SHORT).show();
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
                petService.deleteDiet(String.valueOf(d.getId()), cb);
            }
        });
        alertDialog.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(v.getContext(),"diet NOT canceled",Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float v, float v1) {
        if (e1.getX()<e2.getX()) {
            Callback cb = new Callback<ApiGenericResponse<Object>>() {
                @Override
                public void onResponse(Call<ApiGenericResponse<Object>> call, Response<ApiGenericResponse<Object>> response) {

                    Log.d("TAG",response.code()+"");
                    if(response.isSuccessful()) {
                        ApiGenericResponse<Object> resource = response.body();
                    }
                }

                @Override
                public void onFailure(Call<ApiGenericResponse<Object>> call, Throwable t) {
                    call.cancel();
                    Toast.makeText(getContext(), "Error with API call, please retry.", Toast.LENGTH_SHORT).show();
                    Log.e("GETALLANIMAL", "Error creating API wallet", t);
                }
            };
            petService.deleteDiet(String.valueOf(idThisDieta), cb);
        }
        return true;
    }

    void reloadListPasti(String id) {
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
                            // mTxtEmptyListDispenser.setVisibility(View.VISIBLE);
                            listView.setVisibility(View.GONE);
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

        petService.getPastiOfDiet(id, callback);
    }
}
