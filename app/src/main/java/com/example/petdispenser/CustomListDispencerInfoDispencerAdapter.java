package com.example.petdispenser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.petdispenser.models.Diet;
import com.example.petdispenser.models.DispencerInfoDispencer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class CustomListDispencerInfoDispencerAdapter extends ArrayAdapter<Diet> {

    Context mCtx;
    int resource;
    List<Diet> DispencersListInfoDispencerList;
    public CustomListDispencerInfoDispencerAdapter(Context mCtx , int resource , List<Diet> DispencersListInfoDispencerList)
    {
        super(mCtx,resource,DispencersListInfoDispencerList);
        this.mCtx = mCtx;
        this.resource = resource;
        this.DispencersListInfoDispencerList = DispencersListInfoDispencerList;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);

        View view = inflater.inflate(resource,null);
        TextView textViewName = view.findViewById(R.id.nomeDispencerInfoDispencerLista);
        ImageView status = view.findViewById(R.id.connessoDispencerInfoDispencerLista);

        Diet dispencersInfoDispencer = DispencersListInfoDispencerList.get(position);
        textViewName.setText(dispencersInfoDispencer.getName());
       // status.setImageDrawable(mCtx.getResources().getDrawable(dispencersInfoDispencer.getImage()));


        return view;
    }
}
