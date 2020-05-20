package com.example.ufletes;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class BusquedaListaFleterosFragment extends Fragment {

    // TODO: Customize parameters
    private int mColumnCount = 1;
    RecyclerView RVFleteros_Busqueda;
    static MyBusquedaListaFleterosRecyclerViewAdapter Adapter_Fleteros_Busqueda;
    List<Fleteros_Lista> fleteros_listaList;
    private FirebaseFirestore mFirestore;
    FirebaseUser userFleter_Busqueda = FirebaseAuth.getInstance().getCurrentUser();

    private OnListFragmentInteractionListener mListener;
    private Query query;

    private String mConsulta;
    //private EditText metBusqFleteros;
    Context context;
    View view;

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
        // Set the adapter
        //metBusqFleteros = getActivity().findViewById(R.id.etBusqFletero);

        llamadoRecycler();
        return view;

    }

    private void llamadoRecycler() {
        if (view instanceof RecyclerView) {
            context = view.getContext();

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

            FirestoreRecyclerOptions<Fleteros_Lista> FirestoreRecyclerOptions =
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

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Adapter_Fleteros_Busqueda.startListening();
    }



    @Override
    public void onResume() {
        super.onResume();
        Adapter_Fleteros_Busqueda.startListening();
    }

    //Cuando la app esta pausa o minimizada para dejar de escuhar los cambios
    @Override
    public void onStop() {
        super.onStop();
        Adapter_Fleteros_Busqueda.stopListening();
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
        Toast.makeText(getContext(), "ATTACH", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        //Adapter_Fleteros_Busqueda.stopListening();
        Toast.makeText(getContext(), "LIASLASDSAL", Toast.LENGTH_LONG).show();
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
        void onListFragmentInteraction(Fleteros_Lista item);
    }
}
