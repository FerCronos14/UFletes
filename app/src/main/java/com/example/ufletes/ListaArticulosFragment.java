package com.example.ufletes;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class ListaArticulosFragment extends Fragment {

    // TODO: Customize parameters
    private int mColumnCount = 1;
    RecyclerView RVARTICULOS;
    MyListaArticulosRecyclerViewAdapter Adapter_Articulos;
    List<Articulos_Lista> articulos_listaList;
    private OnListFragmentInteractionListener mListener;
    private FirebaseFirestore mFirestore;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public ListaArticulosFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listaarticulos_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mFirestore = FirebaseFirestore.getInstance();

            Query query = mFirestore.collection("Cliente")
                    .document(MainActivity.idDoc_Cliente)
                    .collection("Articulos");
            RVARTICULOS = (RecyclerView) view;
            FirestoreRecyclerOptions<Articulos_Lista> FirestoreRecyclerOptions =
                    new FirestoreRecyclerOptions.Builder<Articulos_Lista>()
                            .setQuery(query, Articulos_Lista.class).build();

            Adapter_Articulos = new MyListaArticulosRecyclerViewAdapter(getActivity(), FirestoreRecyclerOptions);
            Adapter_Articulos.notifyDataSetChanged();
            if (mColumnCount <= 1) {
                RVARTICULOS.setLayoutManager(new LinearLayoutManager(context));
            } else {
                RVARTICULOS.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            RVARTICULOS.setAdapter(Adapter_Articulos);
        }

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        Adapter_Articulos.startListening();
    }

    //Cuando la app esta pausa o minimizada para dejar de escuhar los cambios
    @Override
    public void onStop() {
        super.onStop();
        Adapter_Articulos.stopListening();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
         mListener = null;
    }
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Articulos_Lista item);
    }

}
