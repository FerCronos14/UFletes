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

import java.util.List;

public class SolicitudesFletesFragment extends Fragment {

    // TODO: Customize parameters
    private int mColumnCount = 1;
    RecyclerView RVSOLICITUDES;
    MySolicitudesFletesRecyclerViewAdapter Adapter_Solicitudes;
    List<Solicitudes_Lista> solicitudes_listaList;
    private OnListFragmentInteractionListener mListener;
    private FirebaseFirestore mFirestore;


    public SolicitudesFletesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_solicitudes_fletes_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();

            mFirestore = FirebaseFirestore.getInstance();

            Query query = mFirestore.collection("Pedidos").orderBy("nombre_s", Query.Direction.ASCENDING);
            RVSOLICITUDES = (RecyclerView) view;
            FirestoreRecyclerOptions<Solicitudes_Lista> FirestoreRecyclerOptions =
                    new FirestoreRecyclerOptions.Builder<Solicitudes_Lista>()
                            .setQuery(query, Solicitudes_Lista.class).build();

            Adapter_Solicitudes = new MySolicitudesFletesRecyclerViewAdapter(getActivity(), FirestoreRecyclerOptions);
            Adapter_Solicitudes.notifyDataSetChanged();

            //RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                RVSOLICITUDES.setLayoutManager(new LinearLayoutManager(context));
            } else {
                RVSOLICITUDES.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            RVSOLICITUDES.setAdapter(Adapter_Solicitudes);
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Adapter_Solicitudes.startListening();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }

         */
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Solicitudes_Lista item);
    }


}
