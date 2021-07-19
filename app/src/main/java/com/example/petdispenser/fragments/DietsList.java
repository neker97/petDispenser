package com.example.petdispenser.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.petdispenser.DatabasePetApplication;
import com.example.petdispenser.MainActivity;
import com.example.petdispenser.MainActivityToolbarListener;
import com.example.petdispenser.adapters.DispensersListAdapter;
import com.example.petdispenser.api.PetApiService;
import com.example.petdispenser.api.model.ApiGenericResponse;
import com.example.petdispenser.dao.DietaDAO;
import com.example.petdispenser.googleLogin;
import com.example.petdispenser.models.Diet;
import com.example.petdispenser.adapters.DietsListAdapter;
import com.example.petdispenser.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.example.petdispenser.models.Dispenser;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import org.json.JSONException;

public class DietsList extends Fragment {

    private ListView listview;
    private FloatingActionButton addDietaButton;
    private List<Diet> dietsList;
    private MainActivityToolbarListener itemDetailFragmentListener;
    private DietsListAdapter mAdapter;
    private TextView mTxtEmptyListDiets;
    private FirebaseAuth firebaseAuth;
    private  FirebaseAuth mAuth;
    private PetApiService petService = new PetApiService();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.diets_list_fragment, container, false);

        try {
            DatabasePetApplication dpa = new DatabasePetApplication(v.getContext());
            dpa.onCreate();

            FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
            mUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            String idToken = task.getResult().getToken();
                            // Send token to your backend via HTTPS
                            Log.i("token",idToken);
                        } else {
                            // Handle error -> task.getException();
                        }
                    }
                });
            addDietaButton =  (FloatingActionButton) v.findViewById(R.id.add_dieta);
            addDietaButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("isNewDieta",true);
                    Navigation.findNavController(v).navigate(R.id.action_diets_list_fragment_to_dietForm,bundle);
                }
            });

            listview = v.findViewById(R.id.listViewDiets);

            reloadList();

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {

                    Diet d = (Diet) parent.getItemAtPosition(position);
                    Bundle bundle = new Bundle();
                    bundle.putLong("id",d.getId());

                    //Toast.makeText(getContext(), "Diet info to implement", Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(view).navigate(R.id.diet_info_fragment,bundle);
                }
            });

            listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                    Diet d = (Diet) adapterView.getItemAtPosition(i);
                    showAlertDialog(v, d);
                    //refresh del fragment
                    return true;
                }
            });

        } catch (Exception e) {
            Log.e("TAG", "onCreateView", e);
        }
        return v;
    }

    public void showAlertDialog(View v, Diet d)
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());
        alertDialog.setTitle("Confirm delete");
        alertDialog.setMessage("Do you want delete the diet?");
        alertDialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Callback callback = new Callback<ApiGenericResponse<List<Diet>>>() {
                    @Override
                    public void onResponse(Call<ApiGenericResponse<List<Diet>>> call, Response<ApiGenericResponse<List<Diet>>> response) {

                        Log.d("TAG",response.code()+"");
                        if(response.isSuccessful()) {
                            ApiGenericResponse<List<Diet>> resource = response.body();
                            if (resource.success) {
                                reloadList();
                                Toast.makeText(v.getContext(),"diet "+d.getName()+" canceled",Toast.LENGTH_SHORT).show();
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
                petService.deleteDiet(Long.toString(d.getId()), callback);
            }
        });
        alertDialog.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(v.getContext(),"diet NOT canceled",Toast.LENGTH_SHORT).show();
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

        this.itemDetailFragmentListener.showBackButton(false);
        this.itemDetailFragmentListener.changeTitle(getString(R.string.diets));
        this.itemDetailFragmentListener.setToggleListner(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemDetailFragmentListener.goBack();
            }
        });

    }

    public void refreshFragment(View v)
    {
        Fragment currentFragment = getFragmentManager().findFragmentByTag("diets_list");
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.detach(currentFragment);
        fragmentTransaction.attach(currentFragment);
        fragmentTransaction.commit();

    }

    void reloadList() {
        Callback callback = new Callback<ApiGenericResponse<List<Diet>>>() {
            @Override
            public void onResponse(Call<ApiGenericResponse<List<Diet>>> call, Response<ApiGenericResponse<List<Diet>>> response) {

                Log.d("TAG",response.code()+"");
                if(response.isSuccessful()) {
                    ApiGenericResponse<List<Diet>> resource = response.body();
                    if (resource.success) {
                        dietsList = resource.result;
                        dietsList = dietsList.stream().filter((elem)-> elem.getName() != null).collect(Collectors.toList());
                        List<Diet> errDietList = dietsList.stream().filter((elem)-> elem.getName() == null).collect(Collectors.toList());

                        if(dietsList != null && !dietsList.isEmpty())
                        {
                            if(mAdapter != null) mAdapter.clear();
                            mAdapter = new DietsListAdapter(getContext(),R.layout.diet_list_adapter,dietsList);
                            listview.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
                        }
                        else
                        {
                            // mTxtEmptyListDispenser.setVisibility(View.VISIBLE);
                            listview.setVisibility(View.GONE);
                        }

                        for (Diet elem : errDietList) {
                            petService.deleteDiet(Long.toString(elem.id), null);
                        }
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

        this.petService.getAllDiets(callback);
    }

}
