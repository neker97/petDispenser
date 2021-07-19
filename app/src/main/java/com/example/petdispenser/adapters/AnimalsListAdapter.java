package com.example.petdispenser.adapters;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.petdispenser.R;
import com.example.petdispenser.models.Animal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import java.util.List;


public class AnimalsListAdapter extends ArrayAdapter<Animal> {

    Context mCtx;
    int resource;
    List<Animal> animali_list;

public AnimalsListAdapter(Context mCtx, int resource , List<Animal> animali_list)
{
    super(mCtx,resource,animali_list);
    this.mCtx = mCtx;
    this.resource = resource;
    this.animali_list = animali_list;
}


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);

        View view = inflater.inflate(resource,null);
        TextView nomeAnimale= view.findViewById(R.id.nomeAnimale);
        TextView pp = view.findViewById(R.id.pp);

        ImageView fotoAnimale = view.findViewById(R.id.foto_animale);
        fotoAnimale.setMaxWidth(100);
        fotoAnimale.setMaxHeight(100);

        Animal av = animali_list.get(position);

        nomeAnimale.setText(av.getName());
        pp.setText(av.getPp());
        Log.i("i","ddd");
        if(av.getPathPicture().isEmpty() || av.getPathPicture() == null || av.getPathPicture().equals("") || av.getPathPicture().equals("drawable://2131230919"))
            fotoAnimale.setImageResource(R.drawable.paw);
        else
        {

            fotoAnimale.setImageURI(
                    Uri.parse(av.getPathPicture()
                    ));
        }

       // fotoAnimale.setImageURI(Uri.parse(av.getPathPicture()));

        return view;
    }
}
