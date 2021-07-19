package com.example.petdispenser.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.petdispenser.CustomListDispencerInfoDispencerAdapter;
import com.example.petdispenser.MainActivityToolbarListener;
import com.example.petdispenser.R;
import com.example.petdispenser.api.PetApiService;
import com.example.petdispenser.api.model.ApiGenericResponse;
import com.example.petdispenser.dao.DietaDAO;
import com.example.petdispenser.models.Diet;
import com.example.petdispenser.models.DispencerInfoDispencer;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class DispenserInfo extends Fragment {

    private Toolbar toolbar;
    private List<DispencerInfoDispencer> dispencers;
    private ListView listviewInfoDispencer;
    private ImageView caricaFoto , fotoDispenser;
    private TextView  nomeDispenser ;
    private Button apriDispenser;
    private static final int PICK_IMAGE = 100;
    private List<Diet> dieteAttiveSuDispenser;

    private Uri imageUri;
    private MainActivityToolbarListener itemDetailFragmentListener;

    private long dispenserID;

    private DietaDAO dietaDAO;

    private PetApiService petService = new PetApiService();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dispenser_info_fragment, container, false);
        dietaDAO = new DietaDAO(getContext());

        //robe di cose per la lista dei dispenser
        dispencers = new ArrayList<>();
        //dispencers.add(new DispencerInfoDispencer(R.drawable.green_circle,"dispencer cucina"));
        //dispencers.add(new DispencerInfoDispencer(R.drawable.green_circle,"dispencer camino"));
        dispenserID = getArguments().getLong("id");

        listviewInfoDispencer = v.findViewById(R.id.lista_dispencer_info_dispencer);

        //nomeDispenser.setText(petService.getDispenserById(dispenserID, cbGetDispenser).getName());
        nomeDispenser.setText("Dispenser");
        Callback callback = new Callback<ApiGenericResponse<List<Diet>>>() {
            @Override
            public void onResponse(Call<ApiGenericResponse<List<Diet>>> call, Response<ApiGenericResponse<List<Diet>>> response) {

                Log.d("TAG",response.code()+"");
                if(response.isSuccessful()) {
                    ApiGenericResponse<List<Diet>> resource = response.body();
                    if (resource.success) {
                        dieteAttiveSuDispenser = resource.result;
                        CustomListDispencerInfoDispencerAdapter cldida = new  CustomListDispencerInfoDispencerAdapter(getActivity(),R.layout.dispencers_list_info_dispencer,dieteAttiveSuDispenser);
                        listviewInfoDispencer.setAdapter(cldida);
                    } else {
                        Toast.makeText(getContext(), resource.error, Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<ApiGenericResponse<List<Diet>>> call, Throwable t) {
                call.cancel();
                Toast.makeText(getContext(), "Error with API call, please retry.", Toast.LENGTH_SHORT).show();
            }
        };

        petService.getDietOfDispenser(String.valueOf(dispenserID),callback);


        nomeDispenser = v.findViewById(R.id.nome_dispencer_info_dispencer);

        apriDispenser = v.findViewById(R.id.open_dispenser);

        //apro manualmente il dispenser
        apriDispenser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"dispenser opened",Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }

    private void openGallery()
    {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery,PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode , int resultCode , Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE)
        {
            imageUri = data.getData();
            fotoDispenser.setImageURI(imageUri);
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

    @Override
    public void onResume() {
        super.onResume();

        this.itemDetailFragmentListener.showBackButton(true);
        this.itemDetailFragmentListener.changeTitle("Animal insertion");
    }
}