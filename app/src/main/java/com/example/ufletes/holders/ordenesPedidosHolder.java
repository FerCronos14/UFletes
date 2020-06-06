package com.example.ufletes.holders;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ufletes.R;

public class ordenesPedidosHolder  extends RecyclerView.ViewHolder{

    public final TextView mtextViewNombrePedido;
    public final TextView mtextViewTelefonoPedido;
    //public final TextView mtextViewStatusPedido;
    public final TextView mtextViewDirOrigenPedido;
    public final TextView mtextViewDirDestinoPedido;
    public final TextView mtextViewFechaPedido;
    public final LinearLayout mllExpandAreaPedido;

    public ordenesPedidosHolder(@NonNull View itemView) {
        super(itemView);

        mtextViewNombrePedido = itemView.findViewById(R.id.textViewNombreCliente_Pedido);
        mtextViewTelefonoPedido = itemView.findViewById(R.id.textViewTelCliente_Pedido);
        mtextViewDirOrigenPedido = itemView.findViewById(R.id.textViewDirOrigen_Pedido);
        mtextViewDirDestinoPedido = itemView.findViewById(R.id.textViewDirDestino_Pedido);
        mtextViewFechaPedido = itemView.findViewById(R.id.textViewFechaPedido_Pedido);
        mllExpandAreaPedido = (LinearLayout) itemView.findViewById(R.id.llExpandAreaPedido);
    }
}
