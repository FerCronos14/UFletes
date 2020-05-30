package com.example.ufletes.holders;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ufletes.R;

public class articulosClienteHolder  extends RecyclerView.ViewHolder {

    public final TextView textViewNombreArticuloListado;
    public final TextView textViewDescripcionArticuloListado;
    public final TextView textViewCantidadArticuloListado;
    public final ImageView imageViewArticulo;


    public articulosClienteHolder(@NonNull View itemView) {
        super(itemView);
        textViewNombreArticuloListado = itemView.findViewById(R.id.textViewListado_Nombre);
        textViewDescripcionArticuloListado = itemView.findViewById(R.id.textViewListado_Descripcion);
        textViewCantidadArticuloListado = itemView.findViewById(R.id.textViewListado_Cantidad);
        imageViewArticulo = itemView.findViewById(R.id.imageViewListado_Foto_Articulo);
    }
}
