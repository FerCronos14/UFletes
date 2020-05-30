package com.example.ufletes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ufletes.holders.solicitudesHolder;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import static com.google.firebase.firestore.FirebaseFirestore.getInstance;

public class SolicitudesFletesFragment extends Fragment {

    // TODO: Customize parameters
    private int mColumnCount = 1;
    private RecyclerView RVSOLICITUDES;
    private FirestoreRecyclerAdapter<Solicitudes_Lista, solicitudesHolder> Adapter_Solicitudes;
    private FirestoreRecyclerOptions<Solicitudes_Lista> FirestoreRecyclerOptions;

    private Button filterButton;
    private EditText searchBox;
    private Button mbtnAceptarPedido;


    static String idCliente_pedido;

    Query query;
    View view;
    private FirebaseFirestore mFirestore;
    private int expandedPosition = -1;



    public SolicitudesFletesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_solicitudes_fletes_list, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RVSOLICITUDES = view.findViewById(R.id.SolicitudesRVAct);
        RVSOLICITUDES.setHasFixedSize(true);
        LinearLayoutManager mlinearLayoutManager = new LinearLayoutManager(getActivity());
        RVSOLICITUDES.setLayoutManager(mlinearLayoutManager);

        final String[] order = {"Recientes", "Antiguos", "Neutral"};

        filterButton = view.findViewById(R.id.filterButtonSolicitudes);
        searchBox = view.findViewById(R.id.searchBoxSolicitudes);


        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(getActivity());
                materialAlertDialogBuilder.setTitle("Seleccionar filtro.");
                materialAlertDialogBuilder.setItems(order, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        changeOrder(order[which]);
                    }
                }).show();
            }
        });
        getData();
    }

    private void getData() {
        query = getInstance()
                .collection("Pedidos");

        FirestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Solicitudes_Lista>()
                .setQuery(query, Solicitudes_Lista.class)
                .build();
        attachRecyclerView();
        Adapter_Solicitudes.notifyDataSetChanged();
    }

    private void attachRecyclerView() {
        Adapter_Solicitudes = new FirestoreRecyclerAdapter<Solicitudes_Lista,solicitudesHolder>(FirestoreRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull solicitudesHolder holder, final int position, @NonNull Solicitudes_Lista model) {

                holder.mtextViewNombreSolicitud.setText(String.format("%s %s", model.getNombre_s(), model.getApellidop_s()));
                holder.mtextViewTelefonoSolicitud.setText((model.getTelefono_s()));
                holder.mtextViewDirOrigenSolicitud.setText((model.getDirOrigen_s()));
                holder.mtextViewDirDestinoSolicitud.setText(model.getDirDestino_s());
                holder.mtextViewFechaSolicitud.setText(model.getFecha_s());
                final String auxidCliente_pedido = model.getIdCliente_s();

                final boolean isExpanded = position==expandedPosition;
                holder.mllExpandArea.setVisibility(isExpanded?View.VISIBLE:View.GONE);
                holder.itemView.setActivated(isExpanded);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        idCliente_pedido = auxidCliente_pedido;
                        expandedPosition = isExpanded ? -1:position;
                        TransitionManager.beginDelayedTransition(RVSOLICITUDES);
                        notifyDataSetChanged();
                        mbtnAceptarPedido = v.findViewById(R.id.btnInfoPedido_Solicitud);

                      //  final Dialog listaArticulosCliente = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar);
                      //  listaArticulosCliente.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(100,0,0,0)));
                      //  listaArticulosCliente.setContentView(R.layout.fragment_confirmar_pedido);
                      //  listaArticulosCliente.onAttachedToWindow();
                      // listaArticulosCliente.setCanceledOnTouchOutside(true);
                      //  listaArticulosCliente.setCancelable(true);

                       // if (isExpanded) {
                            mbtnAceptarPedido.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    fragment_ConfirmarPedido_Fletero dialog = new fragment_ConfirmarPedido_Fletero();
                                    dialog.show(getActivity().getSupportFragmentManager(), "DialogoConfirmacion");


                                    // Button btnConfirmarPedidoFlete = (Button) listaArticulosCliente.findViewById(R.id.btnAceptarPedido_Confirmado_Dialog);
                                   // btnConfirmarPedidoFlete.setOnClickListener(new View.OnClickListener() {
                                     //   @Override
                                       // public void onClick(View view) {
                                         //   Intent intent = new Intent(getContext(), MapsActivity_RastreoFletero.class);
                                           // startActivity(intent);
                                        //}
                                    //});

                                    //listaArticulosCliente.show();
                                }
                            });
                        //}
                    }
                });

            }

            @NonNull
            @Override
            public solicitudesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.fragment_solicitudes_fletes, parent, false);

                return new solicitudesHolder(view);
            }
        };
        RVSOLICITUDES.setAdapter(Adapter_Solicitudes);
    }

    private void changeOrder(String s) {
        Adapter_Solicitudes.stopListening();
        if (s.equals("Recientes")){
            query = getInstance()
                    .collection("Pedidos")
                    .orderBy("fecha_s", Query.Direction.DESCENDING);
        }
        else if (s.equals("Antiguos")){
            query = getInstance()
                    .collection("Pedidos")
                    .orderBy("fecha_s", Query.Direction.ASCENDING);
        }

        else if(s.equals("Neutral")){
            query = getInstance().collection("Pedidos");
        }

        final String[] order = {"Recientes", "Antiguos", "Neutral"};

        filterButton = view.findViewById(R.id.filterButtonSolicitudes);
        searchBox = view.findViewById(R.id.searchBoxSolicitudes);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(getActivity());
                materialAlertDialogBuilder.setTitle("Seleccionar filtro.");
                materialAlertDialogBuilder.setItems(order, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        changeOrder(order[which]);
                    }
                }).show();


            }
        });

        FirestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Solicitudes_Lista>()
                .setQuery(query, Solicitudes_Lista.class)
                .build();
        attachRecyclerView();
        Adapter_Solicitudes.startListening();
        Adapter_Solicitudes.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        Adapter_Solicitudes.startListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        Adapter_Solicitudes.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        Adapter_Solicitudes.stopListening();
    }

}
