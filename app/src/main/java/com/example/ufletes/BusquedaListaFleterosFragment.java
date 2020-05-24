package com.example.ufletes;

import android.content.Context;
import android.content.DialogInterface;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import static com.google.firebase.firestore.FirebaseFirestore.getInstance;

public class BusquedaListaFleterosFragment extends Fragment {

    // TODO: Customize parameters
    private RecyclerView recyclerView, RVFleteros_Busqueda;
    private FirestoreRecyclerAdapter<Fleteros_Lista, busqFleterosHolder> Adapter_Fleteros_Busqueda;
    private FirestoreRecyclerOptions<Fleteros_Lista> FirestoreRecyclerOptions;
    private Button filterButton;
    private EditText searchBox;
    Query query;
    View view;
    List<Fleteros_Lista> fleteros_listaList;


    private String mConsulta;
    //private EditText metBusqFleteros;
    Context context;

    public BusquedaListaFleterosFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        searchBox = view.findViewById(R.id.searchBoxBusqFletero);

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
            protected void onBindViewHolder(@NonNull busqFleterosHolder holder, int position, @NonNull Fleteros_Lista model) {
                Glide.with(getContext())
                        .load(model.getPathFoto_v())
                        .centerCrop()
                        .placeholder(R.drawable.ic_vehiculo_na)
                        .into(holder.imagenBusqFletero);

                holder.nombreBusqFletero.setText(String.format("%s %s", model.getNombre(), model.getApellidop()));
                holder.numeroBusqFletero.setText(model.getTelefono());
            }

            @NonNull
            @Override
            public busqFleterosHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_busqueda_lista_fleteros, parent, false);
                return new busqFleterosHolder(view);
            }
        };

         /*   context = view.getContext();

            mFirestore = FirebaseFirestore.getInstance();
            CollectionReference colFletero =  mFirestore.collection("Fletero");
            //query = colFletero.orderBy("nombre", Query.Direction.ASCENDING);

            //query = mFirestore.collection("Fletero").orderBy("nombre").startAt(searchName).endAt(searchName + "\uf8ff")

            if (Filtro_Fleteros_Fragment.mBusqueda.contains("Acendente")) {
                query = mFirestore.collection("Fletero").orderBy("nombre", Query.Direction.ASCENDING);
            }
            if (Filtro_Fleteros_Fragment.mBusqueda.contains("Descendente")) {
                query = mFirestore.collection("Fletero").orderBy("nombre", Query.Direction.DESCENDING);
            }
            if (Filtro_Fleteros_Fragment.mBusqueda.isEmpty() || Filtro_Fleteros_Fragment.mBusqueda.contains("normal")) {
                query = mFirestore.collection("Fletero").orderBy("correo");
            }
//https://stackoverflow.com/questions/49995926/how-to-search-firebase-text-by-lower-case-also
//https://stackoverflow.com/questions/58458640/is-it-possible-to-filter-a-firestore-recyclerview-no-third-party-app
            RVFleteros_Busqueda = (RecyclerView) view;

            FirestoreRecyclerOptions =
                    new FirestoreRecyclerOptions.Builder<Fleteros_Lista>()
                            .setQuery(query, Fleteros_Lista.class).build();

            Adapter_Fleteros_Busqueda = new MyBusquedaListaFleterosRecyclerViewAdapter(getActivity(), FirestoreRecyclerOptions);
            Adapter_Fleteros_Busqueda.notifyDataSetChanged();
            if (mColumnCount <= 1) {
                RVFleteros_Busqueda.setLayoutManager(new LinearLayoutManager(context));
            } else {
                RVFleteros_Busqueda.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            RVFleteros_Busqueda.setAdapter(Adapter_Fleteros_Busqueda);
//------------------------------------------------------------------------------------------------------------------------------------------


          */
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
        searchBox = view.findViewById(R.id.searchBox);
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

        FirestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Fleteros_Lista>()
                .setQuery(query, Fleteros_Lista.class)
                .build();
        attachRecyclerView();
        Adapter_Fleteros_Busqueda.startListening();
        Adapter_Fleteros_Busqueda.notifyDataSetChanged();
    }

    /*
    public void filtroInicial() {
        mFirestore = FirebaseFirestore.getInstance();
        query = mFirestore.collection("Fletero").orderBy("correo");
        recyclerattach();
        Adapter_Fleteros_Busqueda.notifyDataSetChanged();
    }


    public void filtroAscendente () {
        mFirestore = FirebaseFirestore.getInstance();
        query = mFirestore.collection("Fletero").orderBy("nombre", Query.Direction.ASCENDING);
        recyclerattach();
        Adapter_Fleteros_Busqueda.notifyDataSetChanged();
    }

    public void filtroDescendente() {
        mFirestore = FirebaseFirestore.getInstance();
        query = mFirestore.collection("Fletero").orderBy("nombre", Query.Direction.DESCENDING);
        recyclerattach();
        Adapter_Fleteros_Busqueda.notifyDataSetChanged();
    }

    public void recyclerattach() {
        context = view.getContext();

        RVFleteros_Busqueda = (RecyclerView) view;
        FirestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Fleteros_Lista>()
                        .setQuery(query, Fleteros_Lista.class).build();

        Adapter_Fleteros_Busqueda = new MyBusquedaListaFleterosRecyclerViewAdapter(getActivity(), FirestoreRecyclerOptions);
        Adapter_Fleteros_Busqueda.notifyDataSetChanged();

        if (mColumnCount <= 1) {
            RVFleteros_Busqueda.setLayoutManager(new LinearLayoutManager(context));
        } else {
            RVFleteros_Busqueda.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }

        RVFleteros_Busqueda.setAdapter(Adapter_Fleteros_Busqueda);
    }

     */

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

    /*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }

        Toast.makeText(getContext(), "ATTACH", Toast.LENGTH_LONG).show();
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        Toast.makeText(getContext(), "LIASLASDSAL", Toast.LENGTH_LONG).show();
    }




    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Fleteros_Lista item);
    }

    //Una buena opcion para pasar los datos al adapter es justamente ponerlos como parametro de este metodo y luego pasarlos al adapter
    public void updateListaFleteros(){
        //Aca deberias actualizar los datos del adapter... en el codigo no veo dodne le cargas los datos, pero aqui deberias actualizarlos y luego hacer el notifyDataSetChanged() que le indicara que tiene que refrescar el recycler y actualizar lo que esta mostrando.
        RVFleteros_Busqueda.setAdapter(new MyBusquedaListaFleterosRecyclerViewAdapter(getActivity(), FirestoreRecyclerOptions));
    }
     */
}
