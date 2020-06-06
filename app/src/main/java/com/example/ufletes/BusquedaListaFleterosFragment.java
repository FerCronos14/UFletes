package com.example.ufletes;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ufletes.holders.busqFleterosHolder;

import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.FragmentManager;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.ObservableSnapshotArray;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.firebase.firestore.FirebaseFirestore.getInstance;

public class BusquedaListaFleterosFragment extends Fragment {

    // TODO: Customize parameters
    private RecyclerView RVFleteros_Busqueda;
    private FirestoreRecyclerAdapter<Fleteros_Lista, busqFleterosHolder> Adapter_Fleteros_Busqueda;
    private FirestoreRecyclerOptions<Fleteros_Lista> FirestoreRecyclerOptions;
    private Button filterButton;
    private Button mbtnLlamarFletero;
    private EditText searchBox;
    private int expandedPosition = -1;
    private String idDocFletero_bus;
    private ProgressDialog mPDialog;
    Query query;
    View view;
    List<Fleteros_Lista> fleteros_listaList;

    Context context;
    private FirebaseFirestore mFireStore;

    public BusquedaListaFleterosFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFireStore = FirebaseFirestore.getInstance();
        mPDialog = new ProgressDialog(getContext(), R.style.CustomAlertDialog);
        //mSearchViewBusqFleteros = getActivity().findViewById(R.id.svBusqFletero);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_busqueda_lista_fleteros_list, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RVFleteros_Busqueda = view.findViewById(R.id.fleterosRVAct);
        RVFleteros_Busqueda.setHasFixedSize(true);
        LinearLayoutManager mlinearLayoutManager = new LinearLayoutManager(getActivity());
        RVFleteros_Busqueda.setLayoutManager(mlinearLayoutManager);

        final String[] order = getResources().getStringArray(R.array.order);
        filterButton = view.findViewById(R.id.filterButtonBusqFletero);

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(getActivity(), R.style.RoundShapeTheme);
                materialAlertDialogBuilder.setTitle("Seleccionar filtro.");
                materialAlertDialogBuilder.setItems(order, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        changeOrder(order[which]);
                    }
                }).show();
            }
        });

        searchBox = view.findViewById(R.id.searchBoxBusqFletero);
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Adapter_Fleteros_Busqueda.stopListening();
                String searchText = searchBox.getText().toString();
                query = getInstance()
                        .collection("Fletero")
                        .orderBy("nombre", Query.Direction.ASCENDING)
                        .startAt(searchText)
                        .endAt(searchText  + "\uf8ff")
                ;

            }
            @Override
            public void afterTextChanged(Editable editable) {
                FirestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Fleteros_Lista>()
                        .setQuery(query, Fleteros_Lista.class)
                        .build();
                attachRecyclerView();
                Adapter_Fleteros_Busqueda.startListening();
                Adapter_Fleteros_Busqueda.notifyDataSetChanged();
            }
        });
        getData();

    }

    private void getData() {
        query = getInstance()
                .collection("Fletero")
                .orderBy("correo");

        FirestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Fleteros_Lista>()
                .setQuery(query, Fleteros_Lista.class)
                .build();
        attachRecyclerView();
        Adapter_Fleteros_Busqueda.notifyDataSetChanged();
    }

    private void attachRecyclerView() {
        Adapter_Fleteros_Busqueda = new FirestoreRecyclerAdapter<Fleteros_Lista, busqFleterosHolder>(FirestoreRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull final busqFleterosHolder holder, final int position, @NonNull final Fleteros_Lista model) {
                Glide.with(getContext())
                        .load(model.getPathFoto_v())
                        .centerCrop()
                        .placeholder(R.drawable.ic_vehiculo_na)
                        .into(holder.imagenBusqFletero);

                holder.nombreBusqFletero.setText(String.format("%s %s", model.getNombre(), model.getApellidop()));
                holder.numeroBusqFletero.setText(model.getTelefono());
                final Uri phoneNumber = Uri.parse("tel:"+model.getTelefono());
                final String auxIDFLETERO = model.getIdDocFletero();

                final boolean isExpanded = position==expandedPosition;
                holder.mllExpandAreaBusquedaBusq.setVisibility(isExpanded?View.VISIBLE:View.GONE);
                holder.itemView.setActivated(isExpanded);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPDialog.setTitle("Obteniendo información");
                        mPDialog.setMessage("Espere un momento...");
                        mPDialog.setCancelable(false);
                        mPDialog.show();

                        idDocFletero_bus = auxIDFLETERO;
                        expandedPosition = isExpanded ? -1:position;
                        TransitionManager.beginDelayedTransition(RVFleteros_Busqueda);
                        notifyDataSetChanged();
                        // consulta para obtener datos extras...
                        mFireStore.collection("Fletero")
                                .document(idDocFletero_bus)
                                .collection("Vehiculos")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                holder.mtextViewMarcaV_Busq.setText(document.getData().get("marca_v").toString());
                                                holder.mtextViewTipoV_Busq.setText(document.getData().get("tipo_v").toString());
                                                holder.mtextViewMedida_Busq.setText(document.getData().get("medida_v").toString());
                                                holder.mtextViewVolv_Busq.setText(document.getData().get("vol_v").toString());
                                            }
                                        }
                                        mPDialog.dismiss();
                                    }
                                });

                        // accion de llamar a feltero
                        mbtnLlamarFletero = v.findViewById(R.id.btnLlamarFletero);
                        mbtnLlamarFletero.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(getContext(), "Llamando", Toast.LENGTH_SHORT).show();
                                Intent callIntent = new Intent(Intent.ACTION_DIAL,phoneNumber);
                                startActivity(callIntent);
                            }
                        });
                    }
                });

            }

            @NonNull
            @Override
            public busqFleterosHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.fragment_busqueda_lista_fleteros, parent, false);
                return new busqFleterosHolder(view);
            }
        };
        RVFleteros_Busqueda.setAdapter(Adapter_Fleteros_Busqueda);

    }

    private void changeOrder(String s) {
        Adapter_Fleteros_Busqueda.stopListening();
        if (s.equals("Ascendente")){
            query = getInstance()
                    .collection("Fletero")
                    .orderBy("nombre", Query.Direction.ASCENDING);
        }
        else if (s.equals("Descendente")){
            query = getInstance()
                    .collection("Fletero")
                    .orderBy("nombre", Query.Direction.DESCENDING);
        }

        else if(s.equals("Neutral")){
            query = getInstance().collection("Fletero").orderBy("correo");
        }

        final String[] order = getResources().getStringArray(R.array.order);

        filterButton = view.findViewById(R.id.filterButtonBusqFletero);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(getActivity(), R.style.RoundShapeTheme);
                materialAlertDialogBuilder.setTitle("Seleccionar filtro.");
                materialAlertDialogBuilder.setItems(order, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        changeOrder(order[which]);
                    }
                }).show();
            }
        });

        FirestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Fleteros_Lista>()
                .setQuery(query, Fleteros_Lista.class)
                .build();
        attachRecyclerView();
        Adapter_Fleteros_Busqueda.startListening();
        Adapter_Fleteros_Busqueda.notifyDataSetChanged();
    }


    @Override
    public void onStart() {
        super.onStart();
        Adapter_Fleteros_Busqueda.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        Adapter_Fleteros_Busqueda.stopListening();
    }

}
