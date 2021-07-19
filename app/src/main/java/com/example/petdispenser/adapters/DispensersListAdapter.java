package com.example.petdispenser.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.petdispenser.R;
import com.example.petdispenser.models.Dispenser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class DispensersListAdapter extends ArrayAdapter<Dispenser>  {
    Context mCtx;
    int resource;
    List<Dispenser> dispensers_list;
    public DispensersListAdapter(Context mCtx , int resource , List<Dispenser> dispensers)
    {
        super(mCtx,resource,dispensers);
        this.mCtx = mCtx;
        this.resource = resource;
        this.dispensers_list = dispensers;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);

        View view = inflater.inflate(resource,null);
        TextView textViewName = view.findViewById(R.id.nomeDispenser);


        Dispenser dispenser = dispensers_list.get(position);
        textViewName.setText(dispenser.getName());
        //status.setImageDrawable(mCtx.getResources().getDrawable(dispenser.getImage()));
       // status.setImageResource(0);


        return view;
    }
}
