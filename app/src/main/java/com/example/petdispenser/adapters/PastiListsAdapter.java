package com.example.petdispenser.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.petdispenser.R;
import com.example.petdispenser.models.Pasto;

import java.util.List;

public class PastiListsAdapter extends ArrayAdapter<Pasto> {

    Context mCtx;
    int resource;
    List<Pasto> pastiList;
    public PastiListsAdapter(Context mCtx, int resource, List<Pasto> pastiList)
    {
        super(mCtx,resource,pastiList);

        this.mCtx=mCtx;
        this.resource=resource;
        this.pastiList=pastiList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.pasti_lists_adapter,null);
        TextView textViewPasto = view.findViewById(R.id.textViewListaPasti);

        Pasto pasto = pastiList.get(position);
        textViewPasto.setText(pasto.getName());

        return view;
    }

    @Override
    public boolean isEmpty()
    {
        return false;
    }
}
