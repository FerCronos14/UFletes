package com.example.ufletes.uifletero;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ufletes.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Pedidos_Fleteros_fragment extends Fragment {

    public Pedidos_Fleteros_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pedidos__fleteros, container, false);
        //return inflater.inflate(R.layout.SolicitudesFletesFragment, container, false);

    }
}
