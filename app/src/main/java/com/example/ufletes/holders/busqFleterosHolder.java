package com.example.ufletes.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ufletes.R;

public class busqFleterosHolder extends RecyclerView.ViewHolder {

    public ImageView imagenBusqFletero;
    public TextView nombreBusqFletero;
    public TextView numeroBusqFletero;

    public busqFleterosHolder(@NonNull View itemView) {
        super(itemView);
        imagenBusqFletero = itemView.findViewById(R.id.imageViewVehiculoFletero_busqueda);
        nombreBusqFletero = itemView.findViewById(R.id.txtNombreFletero_ListaBusqueda);
        numeroBusqFletero = itemView.findViewById(R.id.txtTelefono_ListaBusqueda);
    }
}
