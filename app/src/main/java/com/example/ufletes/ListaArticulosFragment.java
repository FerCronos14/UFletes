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


import java.util.ArrayList;
import java.util.List;

public class ListaArticulosFragment extends Fragment {

    // TODO: Customize parameters
    private int mColumnCount = 1;
    RecyclerView recyclerView;
    MyListaArticulosRecyclerViewAdapter Adapter_Articulos;
    List<Articulos_Lista> articulos_listaList;
    private OnListFragmentInteractionListener mListener;

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
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            articulos_listaList = new ArrayList<>();
            articulos_listaList.add(new Articulos_Lista("Cama","https://dico.com.mx/media/2017/Dico_Centro/collage/DF/colchon_matrimonial-denver-2.jpg",2,"cama con base"));
            articulos_listaList.add(new Articulos_Lista("Estufa","https://lh3.googleusercontent.com/proxy/pbqL0QFvjI-GY6rRKFopp31CVsmz-3iCPjCe633nq5cWR0lRU8FmteRBJ8F9WtBFRKSPk6ur5FtwYh8F_2MUJGfARv2PsfJp9ajID1KC09sAF592R_taxi67gGbijiqnpZuy_VM0bzJ3d-RFeQqvhDfmJ2JOcj7mQl96aNcgSo95VkALBwx0HVkg0AaLO_Q",1,""));
            articulos_listaList.add(new Articulos_Lista("Lavadora","https://www.tiendamabe.com.co/medias/mabe-lavadora-19kg-blanca-LMA79114WBAB0-derecha.jpg-1200Wx1200H?context=bWFzdGVyfGltYWdlc3wxMTQ1NzV8aW1hZ2UvanBlZ3xpbWFnZXMvaGFhL2hhZi84ODc0OTkwODk1MTM0LmpwZ3wyMjM3YTA5NDVhZjQ5Njc5M2IzZDc3MTE3M2U4Y2VhN2ViZDY0YjhmZjU5MDU4ZmExNDEwNDU0OTJlZmE5MzNl",1,"lavadora y secadora"));
            articulos_listaList.add(new Articulos_Lista("TV","https://www.mobydecmuebles.com/wp-content/uploads/2018/10/centrotv_dublin_web1-600x600.jpg",2,"Delicado"));

            Adapter_Articulos = new MyListaArticulosRecyclerViewAdapter(articulos_listaList, mListener);
            recyclerView.setAdapter(Adapter_Articulos);
        }
        return view;
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
