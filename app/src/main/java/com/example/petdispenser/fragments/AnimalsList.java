package com.example.petdispenser.fragments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.petdispenser.DBConnection;
import com.example.petdispenser.MainActivity;
import com.example.petdispenser.MainActivityToolbarListener;
import com.example.petdispenser.R;
import com.example.petdispenser.adapters.AnimalsListAdapter;
import com.example.petdispenser.api.PetApiService;
import com.example.petdispenser.api.model.AnimalResponse;
import com.example.petdispenser.api.model.ApiGenericResponse;
import com.example.petdispenser.dao.AnimaleDAO;
import com.example.petdispenser.dao.PastoDAO;
import com.example.petdispenser.models.Animal;
import com.example.petdispenser.models.Pasto;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;


public class AnimalsList extends Fragment {
    ListView listView;
    List<Animal> av;
    private FloatingActionButton addAnimalButton;
    private MainActivityToolbarListener itemDetailFragmentListener;

    private AnimalsListAdapter mAdapter;
    private List<Animal> mListAnimals;
    private AnimaleDAO animaleDAO;
    private TextView mTxtEmptyListAnimals;
    private PetApiService petService;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.animals_list_fragment, container, false);
        listView = v.findViewById(R.id.lista_dei_veri_animali);
        addAnimalButton =  (FloatingActionButton) v.findViewById(R.id.add_animal);

        this.petService = new PetApiService();

        animaleDAO = new AnimaleDAO(getContext());

        this.reloadList();

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                Animal p = (Animal) adapterView.getItemAtPosition(i);
                try {
                    showAlertDialog(v,animaleDAO,p);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                //refresh del fragment
                return true;
            }
        });
        av = new ArrayList<>();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Animal a = (Animal) parent.getItemAtPosition(position);
                Bundle bundle = new Bundle();
                bundle.putLong("id",a.getId());
                Navigation.findNavController(view).navigate(R.id.animal_info_fragment,bundle);
            }
        });

        addAnimalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();

                Navigation.findNavController(v).navigate(R.id.animal_form_fragment,bundle);
                ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        });

        return v;
    }

    public void showAlertDialog(View v, AnimaleDAO dDao, Animal d) throws java.sql.SQLException
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());
        alertDialog.setTitle("Confirm delete");
        alertDialog.setMessage("Do you want delete the animal?");
        alertDialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Callback callback = new Callback<ApiGenericResponse<List<Animal>>>() {
                    @Override
                    public void onResponse(Call<ApiGenericResponse<List<Animal>>> call, Response<ApiGenericResponse<List<Animal>>> response) {

                        Log.d("TAG",response.code()+"");
                        if(response.isSuccessful()) {
                            ApiGenericResponse<List<Animal>> resource = response.body();
                            if (resource.success) {
                                reloadList();
                                Toast.makeText(v.getContext(),"animal "+d.getName()+" canceled",Toast.LENGTH_SHORT).show();
                            } else {
                                //progress.setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiGenericResponse<List<Animal>>> call, Throwable t) {
                        call.cancel();
                        Toast.makeText(getContext(), "Error with API call, please retry.", Toast.LENGTH_SHORT).show();
                        Log.e("GETALLANIMAL", "Error creating API wallet", t);
                    }
                };
                petService.deleteAnimale(Long.toString(d.getId()), callback);
            }
        });
        alertDialog.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(v.getContext(),"animal NOT canceled",Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog dialog = alertDialog.create();

        dialog.show();
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

        negativeButton.setX(-30);
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

        // Set title bar
        //((MainActivity) getActivity()).setActionBarTitle("Animals");
        this.itemDetailFragmentListener.showBackButton(false);
        this.itemDetailFragmentListener.changeTitle("Animals");
    }

    public void reloadList() {

        Callback callback = new Callback<ApiGenericResponse<List<Animal>>>() {
            @Override
            public void onResponse(Call<ApiGenericResponse<List<Animal>>> call, Response<ApiGenericResponse<List<Animal>>> response) {

                Log.d("TAG",response.code()+"");
                if(response.isSuccessful()) {
                    ApiGenericResponse<List<Animal>> resource = response.body();
                    if (resource.success) {
                        mListAnimals = resource.result;
                        if(mListAnimals != null && !mListAnimals.isEmpty())
                        {
                            if(mAdapter != null) mAdapter.clear();
                            mAdapter = new AnimalsListAdapter(getContext(),R.layout.animal_info_adapter,mListAnimals);
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
            public void onFailure(Call<ApiGenericResponse<List<Animal>>> call, Throwable t) {
                call.cancel();
                Toast.makeText(getContext(), "Error with API call, please retry.", Toast.LENGTH_SHORT).show();
                Log.e("GETALLANIMAL", "Error creating API wallet", t);
            }
        };

        this.petService.getAllAnimals(callback);
    }
}