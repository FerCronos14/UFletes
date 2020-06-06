package com.example.ufletes;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ufletes.holders.ordenesPedidosHolder;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.ObservableSnapshotArray;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;

import static com.google.firebase.firestore.FirebaseFirestore.getInstance;


public class SoliTransitoFragment extends Fragment {

    private RecyclerView RVPedido;
    private FirestoreRecyclerAdapter<Solicitudes_Lista, ordenesPedidosHolder> Adapter_Pedido;
    private com.firebase.ui.firestore.FirestoreRecyclerOptions<Solicitudes_Lista> FirestoreRecyclerOptions;
    Query query;
    View view;

    public SoliTransitoFragment() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_soli_transito, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RVPedido = view.findViewById(R.id.PedidosRVAct);
        RVPedido.setHasFixedSize(true);
        LinearLayoutManager mlinearLayoutManager = new LinearLayoutManager(getActivity());
        RVPedido.setLayoutManager(mlinearLayoutManager);
        getData();
    }

    private void getData() {
        query = getInstance()
                .collection("Pedidos")
        .whereEqualTo("idCliente_s", MainActivity.idDoc_Cliente)
        ;
        Toast.makeText(getContext(), "get data", Toast.LENGTH_SHORT).show();

        FirestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Solicitudes_Lista>()
                .setQuery(query, Solicitudes_Lista.class)
                .build();
        attachRecyclerView();
        Adapter_Pedido.notifyDataSetChanged();
    }

    private void attachRecyclerView() {
        Adapter_Pedido = new FirestoreRecyclerAdapter<Solicitudes_Lista, ordenesPedidosHolder>(FirestoreRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull ordenesPedidosHolder holder, int position, @NonNull Solicitudes_Lista model) {
                Toast.makeText(getContext(), "OnblindHolder", Toast.LENGTH_SHORT).show();
                holder.mtextViewNombrePedido.setText(String.format("%s %s", model.getNombre_s(), model.getApellidop_s()));
                holder.mtextViewTelefonoPedido.setText((model.getTelefono_s()));
                holder.mtextViewDirOrigenPedido.setText((model.getDirOrigen_s()));
                holder.mtextViewDirDestinoPedido.setText(model.getDirDestino_s());
                holder.mtextViewFechaPedido.setText(model.getFecha_s());


            }

            @NonNull
            @Override
            public ordenesPedidosHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.fragment_ordenes_pedidos, parent, false);
                return new ordenesPedidosHolder(view);
            }
        };
        RVPedido.setAdapter(Adapter_Pedido);
    }
    @Override
    public void onResume() {
        super.onResume();
        Adapter_Pedido.startListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        Adapter_Pedido.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        Adapter_Pedido.stopListening();
    }
}
