package com.example.petdispenser.fragments;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.example.petdispenser.MainActivity;
import com.example.petdispenser.MainActivityToolbarListener;
import com.example.petdispenser.adapters.DispensersListAdapter;
import com.example.petdispenser.R;
import com.example.petdispenser.api.PetApiService;
import com.example.petdispenser.api.model.ApiGenericResponse;
import com.example.petdispenser.models.Dispenser;
import com.example.petdispenser.models.Pasto;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class DispensersList extends Fragment {

    private Toolbar toolbarDispenser;
    private List<Dispenser> DispenserList;
    private ListView listview;
    private FloatingActionButton addDispenserButton;
    private MainActivityToolbarListener itemDetailFragmentListener;
    private TextView mTxtEmptyListDispenser;
    DispensersListAdapter adapter;
    private PetApiService petService = new PetApiService();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.dispensers_list_fragment, container, false);

        DispenserList = new ArrayList<>();
        listview = v.findViewById(R.id.listeViewDispenser);


        reloadList();

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Dispenser d = (Dispenser) parent.getItemAtPosition(position);
                Bundle bundle = new Bundle();
                bundle.putLong("id",d.getId());
                Navigation.findNavController(view).navigate(R.id.dispenser_info_fragment,bundle);
            }
        });

        addDispenserButton =  (FloatingActionButton) v.findViewById(R.id.add_dispenser);
        addDispenserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDispenserMethod(v);
            }
        });

        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                Dispenser p = (Dispenser) adapterView.getItemAtPosition(i);
                showAlertDialog(v,p);
                //refresh de fragment
                return true;
            }
        });

        return v;
    }



    private void addDispenserMethod(View v)
    {
        // TODO Giuseppe ricerca bluetooth
        // Toast.makeText(getContext(), "Giuseppe ricerca bluetooth", Toast.LENGTH_SHORT).show();
        Navigation.findNavController(v).navigate(R.id.devices_scan_list_fragment);

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

        this.itemDetailFragmentListener.showBackButton(false);
        this.itemDetailFragmentListener.changeTitle("Dispensers");

    }

    public void showAlertDialog(View v, Dispenser d) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());
        alertDialog.setTitle("Confirm delete");
        alertDialog.setMessage("Do you want delete the dispencer?");
        alertDialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Callback cb = new Callback<ApiGenericResponse<List<Dispenser>>>() {
                    @Override
                    public void onResponse(Call<ApiGenericResponse<List<Dispenser>>> call, Response<ApiGenericResponse<List<Dispenser>>> response) {

                        Log.d("TAG",response.code()+"");
                        if(response.isSuccessful()) {
                            ApiGenericResponse<List<Dispenser>> resource = response.body();
                            if (resource.success) {
                                reloadList();
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
                petService.deleteDispenser(String.valueOf(d.getId()), cb);
            }
        });
        alertDialog.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(v.getContext(), "dispencer NOT canceled", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog dialog = alertDialog.create();

        dialog.show();
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

        negativeButton.setX(-30);
    }

    void reloadList() {
        Callback callbackDispenser = new Callback<ApiGenericResponse<List<Dispenser>>>() {
            @Override
            public void onResponse(Call<ApiGenericResponse<List<Dispenser>>> call, Response<ApiGenericResponse<List<Dispenser>>> response) {

                Log.d("TAG",response.code()+"");
                if(response.isSuccessful()) {
                    ApiGenericResponse<List<Dispenser>> resource = response.body();
                    if (resource.success) {
                        DispenserList = resource.result;
                        if(DispenserList != null && !DispenserList.isEmpty())
                        {
                            if(adapter != null) adapter.clear();
                            adapter = new DispensersListAdapter(getActivity(), R.layout.dispensers_info_adapter, DispenserList);
                            listview.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                        else
                        {
                            // mTxtEmptyListDispenser.setVisibility(View.VISIBLE);
                            listview.setVisibility(View.GONE);
                        }
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
    }
}