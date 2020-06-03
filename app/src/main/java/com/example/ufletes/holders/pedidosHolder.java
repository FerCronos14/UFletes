package com.example.ufletes.holders;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ufletes.R;

public class pedidosHolder extends RecyclerView.ViewHolder {

    public final TextView textViewNombreArticuloListado;
    public final TextView textViewDescripcionArticuloListado;
    public final TextView textViewCantidadArticuloListado;
    public final ImageView imageViewArticulo;

    public pedidosHolder(View mView) {
        super(mView);
        textViewNombreArticuloListado = mView.findViewById(R.id.textViewListado_Nombre);
        textViewDescripcionArticuloListado = mView.findViewById(R.id.textViewListado_Descripcion);
        textViewCantidadArticuloListado = mView.findViewById(R.id.textViewListado_Cantidad);
        imageViewArticulo = mView.findViewById(R.id.imageViewListado_Foto_Articulo);
    }

}