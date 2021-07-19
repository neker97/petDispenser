package com.example.petdispenser.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.petdispenser.R;
import com.example.petdispenser.models.Diet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class DietsListAdapter extends ArrayAdapter<Diet> {
    Context mCtx;
    int resource;
    List<Diet> dietsList;
public DietsListAdapter(Context mCtx , int resource , List<Diet> diets)
{
    super(mCtx,resource,diets);
    this.mCtx = mCtx;
    this.resource = resource;
    this.dietsList = diets;

}

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);

        View view = inflater.inflate(resource,null);
        TextView textViewName = view.findViewById(R.id.nomeAnimale);
        ImageView status = view.findViewById(R.id.imageView);

        Diet diets = dietsList.get(position);
        textViewName.setText(diets.getName());
        if(diets.getAttiva().equals("1"))
            status.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.green_circle));
        else
            status.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.red_circle));

        return view;
    }
}
