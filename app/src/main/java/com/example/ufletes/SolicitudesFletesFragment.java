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

import com.bumptech.glide.Glide;
import com.example.ufletes.holders.articulosClienteHolder;
import com.example.ufletes.holders.pedidosHolder;
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
    private FirestoreRecyclerAdapter<Articulos_Lista, pedidosHolder> adapter;
    private FirestoreRecyclerOptions<Solicitudes_Lista> FirestoreRecyclerOptions;

    private Button filterButton;
    private Button mbtnAceptarPedido;


    static String idCliente_pedido;

    Query query;
    View view;
    Dialog listaArticulosCliente;
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
                       // if (isExpanded) {
                        mbtnAceptarPedido.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                                    LayoutInflater inflater = getLayoutInflater();
                                    View convertView = inflater.inflate(R.layout.fragment_confirmar_pedido, null);


                                    query = getInstance()
                                            .collection("Cliente")
                                            .document(idCliente_pedido)
                                            .collection("Articulos");
                                    RecyclerView recyclerView = convertView.findViewById(R.id.confirmacionArticulosClienteRVAct);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                    recyclerView.setHasFixedSize(true);


                                    FirestoreRecyclerOptions<Articulos_Lista> options = new FirestoreRecyclerOptions.Builder<Articulos_Lista>()
                                            .setQuery(query, Articulos_Lista.class)
                                            .build();
                                    adapter = new FirestoreRecyclerAdapter<Articulos_Lista, pedidosHolder>(options) {
                                        @NonNull
                                        @Override
                                        public pedidosHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                                            View view = LayoutInflater
                                                    .from(parent.getContext())
                                                    .inflate(R.layout.fragment_listaarticulos, parent, false);
                                            return new pedidosHolder(view);
                                        }

                                        @Override
                                        protected void onBindViewHolder(@NonNull pedidosHolder holder, int position, @NonNull Articulos_Lista model) {
                                            holder.textViewNombreArticuloListado.setText((model.getNombre_a()));
                                            holder.textViewDescripcionArticuloListado.setText((model.getDescri_a()));
                                            holder.textViewCantidadArticuloListado.setText((model.getCant_a()));
                                            Glide.with(getContext())
                                                    .load(model.getPathFoto_a())
                                                    .fitCenter()
                                                    .centerCrop()
                                                    .placeholder(R.drawable.ic_noimg)
                                                    .into(holder.imageViewArticulo);
                                        }
                                    };

                                    adapter.startListening();
                                    adapter.notifyDataSetChanged();
                                    recyclerView.setAdapter(adapter);
                                    alertDialog.setView(convertView);

                                    AlertDialog dialog = alertDialog.create();
                                    dialog.getWindow().setLayout(600, 400);

                                    dialog.show();
                                    adapter.notifyDataSetChanged();

                                    Button btnDetallePedidoFlete = (Button) convertView.findViewById(R.id.btnAceptarPedido_Confirmado_Dialog);
                                    btnDetallePedidoFlete.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                         public void onClick(View view) {
                                            Intent intent = new Intent(getContext(), MapsActivity_RastreoFletero.class);
                                            startActivity(intent);
                                        }
                                    });
                                    //listaArticulosCliente.show();
                                }
                            });
                        //}
                       mbtnAceptarPedido.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {


                               AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                               LayoutInflater inflater = getLayoutInflater();
                               View convertView = inflater.inflate(R.layout.fragment_confirmar_pedido, null);


                              query = getInstance()
                                       .collection("Cliente")
                                       .document(idCliente_pedido)
                                       .collection("Articulos");
                               RecyclerView recyclerView = convertView.findViewById(R.id.confirmacionArticulosClienteRVAct);
                               recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                               recyclerView.setHasFixedSize(true);


                               FirestoreRecyclerOptions<Articulos_Lista> options = new FirestoreRecyclerOptions.Builder<Articulos_Lista>()
                                        .setQuery(query, Articulos_Lista.class)
                                        .build();
                                adapter = new FirestoreRecyclerAdapter<Articulos_Lista, pedidosHolder>(options) {
                                    @NonNull
                                    @Override
                                    public pedidosHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                                        View view = LayoutInflater
                                                .from(parent.getContext())
                                                .inflate(R.layout.fragment_listaarticulos, parent, false);
                                        return new pedidosHolder(view);
                                    }

                                    @Override
                                    protected void onBindViewHolder(@NonNull pedidosHolder holder, int position, @NonNull Articulos_Lista model) {
                                        holder.textViewNombreArticuloListado.setText((model.getNombre_a()));
                                        holder.textViewDescripcionArticuloListado.setText((model.getDescri_a()));
                                        holder.textViewCantidadArticuloListado.setText((model.getCant_a()));
                                        Glide.with(getContext())
                                                .load(model.getPathFoto_a())
                                                .fitCenter()
                                                .centerCrop()
                                                .placeholder(R.drawable.ic_noimg)
                                                .into(holder.imageViewArticulo);
                                    }
                                };

                               //MyListaArticulosRecyclerViewAdapter adapter = new MyListaArticulosRecyclerViewAdapter(getContext(), options);
                               adapter.startListening();
                               adapter.notifyDataSetChanged();
                               recyclerView.setAdapter(adapter);
                               alertDialog.setView(convertView);

                               AlertDialog dialog = alertDialog.create();
                               dialog.getWindow().setLayout(600, 400);



                               dialog.show();
                               adapter.notifyDataSetChanged();

                           }
                       });
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
