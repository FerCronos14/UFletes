package com.example.ufletes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class pantalla_AgregarArticulos extends AppCompatActivity implements ListaArticulosFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla__agregar_articulos);

    }

    @Override
    public void onListFragmentInteraction(Articulos_Lista item) {

    }
}
