package com.example.ufletes.uifletero;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ufletes.MainActivity;
import com.example.ufletes.Pantalla_Inicio_Fletero;
import com.example.ufletes.R;
import com.example.ufletes.Registro_AgregarAuto;

/**
 * A simple {@link Fragment} subclass.
 */
public class Inicio_fletero_fragment extends Fragment implements View.OnClickListener{

    private Button mbtnAgregarVehiculo_Fletero_Home;

    public Inicio_fletero_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_iinicio_fletero, container, false);
        mbtnAgregarVehiculo_Fletero_Home = root.findViewById(R.id.btnPreguntaSubirVehiculo_inicio);
        mbtnAgregarVehiculo_Fletero_Home.setOnClickListener(this);

        return root;
    }



    @Override
    public void onClick(View view) {
        startActivity(new Intent(getActivity(), Registro_AgregarAuto.class));

    }
}
