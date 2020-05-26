package com.example.ufletes.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ufletes.R;

public class vehiculosFleterosHolder extends RecyclerView.ViewHolder{

    public final TextView mtextViewTipoV;
    public final TextView mtextViewMarcaV;
    public final TextView mtextViewVolv;
    public final ImageView imgFoto_Vehiculo;

    public vehiculosFleterosHolder(@NonNull View itemView) {
        super(itemView);
        mtextViewMarcaV = itemView.findViewById(R.id.textViewMarca_V);
        mtextViewTipoV = itemView.findViewById(R.id.textViewTipo_V);
        mtextViewVolv = itemView.findViewById(R.id.textViewVol_V);
        imgFoto_Vehiculo = itemView.findViewById(R.id.imageViewVehiculoFletero);

    }
}
