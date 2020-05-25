package com.example.ufletes.holders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ufletes.R;

public class solicitudesHolder extends RecyclerView.ViewHolder{

    public final TextView mtextViewNombreSolicitud;
    public final TextView mtextViewTelefonoSolicitud;
    //public final TextView mtextViewStatusSolicitud;
    public final TextView mtextViewDirOrigenSolicitud;
    public final TextView mtextViewDirDestinoSolicitud;
    public final TextView mtextViewFechaSolicitud;

    public solicitudesHolder(@NonNull View itemView) {
        super(itemView);

        mtextViewNombreSolicitud = itemView.findViewById(R.id.textViewNombreCliente_Solicitud);
        mtextViewTelefonoSolicitud = itemView.findViewById(R.id.textViewTelCliente_Solicitud);
        mtextViewDirOrigenSolicitud = itemView.findViewById(R.id.textViewDirOrigen_Solicitud);
        mtextViewDirDestinoSolicitud = itemView.findViewById(R.id.textViewDirDestino_Solicitud);
        mtextViewFechaSolicitud = itemView.findViewById(R.id.textViewFechaPedido);
    }
}
