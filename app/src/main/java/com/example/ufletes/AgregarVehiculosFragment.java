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

import com.example.ufletes.ui.Vehiculos_Lista;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;

public class AgregarVehiculosFragment extends Fragment {

    // TODO: Customize parameters
    private int mColumnCount = 1;
    RecyclerView RVVehiculos;
    MyAgregarVehiculosRecyclerViewAdapter Adapter_Vehiculos;
    List<Vehiculos_Lista> articulos_listaList;
    private FirebaseFirestore mFirestore;
    FirebaseUser userFletero = FirebaseAuth.getInstance().getCurrentUser();


    private OnListFragmentInteractionListener mListener;


    public AgregarVehiculosFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_agregar_vehiculos_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mFirestore = FirebaseFirestore.getInstance();

            Query query = mFirestore.collection("Fletero")
                    .document(MainActivity.idDoc_Fletero)
                    .collection("Vehiculos")
                    ;
            RVVehiculos = (RecyclerView) view;
            FirestoreRecyclerOptions<Vehiculos_Lista> FirestoreRecyclerOptions =
                    new FirestoreRecyclerOptions.Builder<Vehiculos_Lista>()
                            .setQuery(query, Vehiculos_Lista.class)
                            .build();

            Adapter_Vehiculos = new MyAgregarVehiculosRecyclerViewAdapter(getActivity(), FirestoreRecyclerOptions);
            Adapter_Vehiculos.notifyDataSetChanged();
            //RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                RVVehiculos.setLayoutManager(new LinearLayoutManager(context));
            } else {
                RVVehiculos.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            RVVehiculos.setAdapter(Adapter_Vehiculos);
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Adapter_Vehiculos.startListening();
    }

    //Cuando la app esta pausa o minimizada para dejar de escuhar los cambios
    @Override
    public void onStop() {
        super.onStop();
        Adapter_Vehiculos.stopListening();
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Vehiculos_Lista item);
    }
}
