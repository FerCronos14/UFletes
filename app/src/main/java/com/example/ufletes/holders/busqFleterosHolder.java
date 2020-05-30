package com.example.ufletes.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ufletes.R;

public class busqFleterosHolder extends RecyclerView.ViewHolder {

    public ImageView imagenBusqFletero;
    public TextView nombreBusqFletero;
    public TextView numeroBusqFletero;
    public final LinearLayout mllExpandAreaBusquedaBusq;

    //textview expanded
    public final TextView mtextViewTipoV_Busq;
    public final TextView mtextViewMarcaV_Busq;
    public final TextView mtextViewVolv_Busq;
    public final TextView mtextViewMedida_Busq;

    public busqFleterosHolder(@NonNull View itemView) {
        super(itemView);
        imagenBusqFletero = itemView.findViewById(R.id.imageViewVehiculoFletero_busqueda);
        nombreBusqFletero = itemView.findViewById(R.id.txtNombreFletero_ListaBusqueda);
        numeroBusqFletero = itemView.findViewById(R.id.txtTelefono_ListaBusqueda);
        mllExpandAreaBusquedaBusq = itemView.findViewById(R.id.llExpandAreaBusquedaFleteros);
        mtextViewMarcaV_Busq = itemView.findViewById(R.id.textViewMarca_V_Busq);
        mtextViewTipoV_Busq = itemView.findViewById(R.id.textViewTipo_V_Busq);
        mtextViewVolv_Busq = itemView.findViewById(R.id.textViewVol_V_Busq);
        mtextViewMedida_Busq = itemView.findViewById(R.id.textViewMedidas_Busq);
    }
}
