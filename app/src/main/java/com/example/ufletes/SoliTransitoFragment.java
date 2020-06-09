package com.example.ufletes;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import static com.google.firebase.firestore.FirebaseFirestore.getInstance;

public class SoliTransitoFragment extends Fragment {

    private RecyclerView RVPedido;
    private FirestoreRecyclerAdapter<Solicitudes_Lista, ordenesPedidosHolder> Adapter_Pedido;
    private com.firebase.ui.firestore.FirestoreRecyclerOptions<Solicitudes_Lista> FirestoreRecyclerOptions;
    Query query;
    View view;

    private int expandedPosition = -1;
    private FirebaseFirestore mFireStore;
    Button mbtnEliminarSolicitud;
    Button mbtnLlamarSolicitud;
    Button mbtnMensajeSolicitud;
    String telFletero;
    String idFletero;
    String telCliente;

    public SoliTransitoFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_soli_transito, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RVPedido = view.findViewById(R.id.PedidosRVAct);
        mFireStore = FirebaseFirestore.getInstance();
        LinearLayoutManager mlinearLayoutManager = new LinearLayoutManager(getActivity());
        RVPedido.setLayoutManager(mlinearLayoutManager);
        getData();
    }

    private void getData() {
        if (!MainActivity.idDoc_Cliente.isEmpty()) {
            query = getInstance()
                    .collection("Pedidos")
                    .whereEqualTo("idCliente_s", MainActivity.idDoc_Cliente)
                    .whereEqualTo("statusSolicitud_s", "Ocupado")
            ;

        }
        if (!MainActivity.idDoc_Fletero.isEmpty()){
            query = getInstance()
                    .collection("Pedidos")
                    .whereEqualTo("idFletero_s", MainActivity.idDoc_Fletero)
                    .whereEqualTo("statusSolicitud_s", "Ocupado")
            ;
        }

        FirestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Solicitudes_Lista>()
                .setQuery(query, Solicitudes_Lista.class)
                .setLifecycleOwner(this)
                .build();
        attachRecyclerView();
        Adapter_Pedido.notifyDataSetChanged();
    }

    private void attachRecyclerView() {
        Adapter_Pedido = new FirestoreRecyclerAdapter<Solicitudes_Lista, ordenesPedidosHolder>(FirestoreRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull ordenesPedidosHolder holder, final int position, @NonNull Solicitudes_Lista model) {
                holder.mtextViewNombrePedido.setText(String.format("%s %s %s", model.getNombre_s(), model.getApellidop_s(), model.getApellidom_s()));
                holder.mtextViewNombreFleteroPedido.setText(String.format("%s %s %s", model.getNombre_f_s(), model.getApellidop_f_s(), model.getApellidom_f_s()));
                holder.mtextViewTelefonoPedido.setText((model.getTelefono_s()));
                holder.mtextViewDirOrigenPedido.setText((model.getDirOrigen_s()));
                holder.mtextViewDirDestinoPedido.setText(model.getDirDestino_s());
                holder.mtextViewFechaPedido.setText(model.getFecha_s());

                telCliente = model.getTelefono_s();
                idFletero = model.getIdFletero_s();
                ObservableSnapshotArray<Solicitudes_Lista> observableSnapshotArray = getSnapshots();
                final DocumentReference documentReference_Solicitudes =
                        observableSnapshotArray.getSnapshot(position).getReference();

                final boolean isExpanded = position==expandedPosition;
                holder.mllExpandAreaPedido.setVisibility(isExpanded?View.VISIBLE:View.GONE);
                holder.itemView.setActivated(isExpanded);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        expandedPosition = isExpanded ? -1:position;
                        TransitionManager.beginDelayedTransition(RVPedido);
                        notifyDataSetChanged();

                        mFireStore.collection("Fletero")
                                .whereEqualTo("idDocFletero", idFletero)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                telFletero = document.getData().get("telefono").toString();
                                            }
                                        }
                                    }
                                });

                        mbtnEliminarSolicitud = view.findViewById(R.id.btnFinalizar_Pedido);
                        mbtnEliminarSolicitud.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                documentReference_Solicitudes.delete();
                            }
                        });
                        mbtnLlamarSolicitud = view.findViewById(R.id.btnLlamarCliente_Pedido);
                        mbtnLlamarSolicitud.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (!MainActivity.idDoc_Cliente.isEmpty()) { // Cliente
                                    Toast.makeText(getContext(), R.string.Llamando, Toast.LENGTH_SHORT).show();
                                    Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+telFletero));
                                    startActivity(callIntent);
                                }
                                if (!MainActivity.idDoc_Fletero.isEmpty()){ // Fletero
                                    Toast.makeText(getContext(), R.string.Llamando, Toast.LENGTH_SHORT).show();
                                    Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+telCliente));
                                    startActivity(callIntent);
                                }
                            }
                        });
                        mbtnMensajeSolicitud = view.findViewById(R.id.btnMsjCliente_Pedido);
                        mbtnMensajeSolicitud.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (!MainActivity.idDoc_Cliente.isEmpty()) { // Cliente
                                    Intent msjIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + telFletero));
                                    //msjIntent.putExtra("sms_body", "Hola");
                                    try {
                                        startActivity(msjIntent);
                                    } catch (ActivityNotFoundException e){

                                    }
                                }
                                if (!MainActivity.idDoc_Fletero.isEmpty()){ // Fletero
                                    Intent msjIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + telCliente));
                                    //msjIntent.putExtra("sms_body", "Hola");
                                    try {
                                        startActivity(msjIntent);
                                    } catch (ActivityNotFoundException e){

                                    }
                                }
                            }
                        });
                    }
                });
            }

            @NonNull
            @Override
            public ordenesPedidosHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.fragment_ordenes_pedidos, parent, false);
                return new ordenesPedidosHolder(view);
            }

        };
        Adapter_Pedido.startListening();
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
