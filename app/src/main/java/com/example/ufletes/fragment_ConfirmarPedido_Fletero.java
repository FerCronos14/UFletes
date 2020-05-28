package com.example.ufletes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.ufletes.holders.articulosClienteHolder;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

import static com.google.firebase.firestore.FirebaseFirestore.getInstance;

public class fragment_ConfirmarPedido_Fletero extends Fragment {

    Activity actividad;
    private RecyclerView RVArticulos;
    private FirestoreRecyclerAdapter<Articulos_Lista, articulosClienteHolder> Adapter_Articulos;
    private com.firebase.ui.firestore.FirestoreRecyclerOptions<Articulos_Lista> FirestoreRecyclerOptions;
    Query query;
    View view;

    public fragment_ConfirmarPedido_Fletero() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_listaarticulos_list, container, false);

        return view;
    }



    /*
        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

            return crearDialogo();
        }

    private Dialog crearDialogo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.fragmenr_confirmar_pedido_fletero, null);
        builder.setView(v);
        return builder.create();
    }

     */

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RVArticulos = view.findViewById(R.id.articulosClienteRVAct);
        RVArticulos.setHasFixedSize(true);
        LinearLayoutManager mlinearLM = new LinearLayoutManager(getActivity());
        RVArticulos.setLayoutManager(mlinearLM);
        getData();

    }

    private void getData() {

        query = getInstance()
                .collection("Cliente")
                .document(SolicitudesFletesFragment.idCliente_pedido)
                .collection("Articulos");

        FirestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Articulos_Lista>()
                .setQuery(query, Articulos_Lista.class)
                .build();
        attachRecyclerView();
        Adapter_Articulos.notifyDataSetChanged();
    }

    private void attachRecyclerView() {
        Adapter_Articulos = new FirestoreRecyclerAdapter<Articulos_Lista, articulosClienteHolder>(FirestoreRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull articulosClienteHolder holder, int position, @NonNull Articulos_Lista model) {
                holder.textViewNombreArticuloListado.setText((model.getNombre_a()));
                holder.textViewDescripcionArticuloListado.setText((model.getDescri_a()));
                holder.textViewCantidadArticuloListado.setText((model.getCant_a()));
                Glide.with(getContext())
                        .load(Registro_AgregarAuto.sPathFoto_Vehiculo)
                        .fitCenter()
                        .centerCrop()
                        .placeholder(R.drawable.ic_noimg)
                        .into(holder.imageViewArticulo)
                ;
            }

            @NonNull
            @Override
            public articulosClienteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_listaarticulos, parent, false);
                return new articulosClienteHolder(view);            }
        };
        RVArticulos.setAdapter(Adapter_Articulos);
    }

    @Override
    public void onStart() {
        super.onStart();
        Adapter_Articulos.startListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        Adapter_Articulos.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        Adapter_Articulos.stopListening();
    }
}
