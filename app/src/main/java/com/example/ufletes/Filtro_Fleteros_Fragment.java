package com.example.ufletes;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.FragmentManager;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class Filtro_Fleteros_Fragment extends  DialogFragment {

    private Button mbtnFilroFletero;
    static String mBusqueda = "";

    private BusquedaListaFleterosFragment fragment;
    private FragmentManager fragmentManager;

    Context mContext;


    public Filtro_Fleteros_Fragment() {
        // Required empty public constructor
        mContext = getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_filtro__fleteros_, container, false);
        // Inflate the layout for this fragment


        mbtnFilroFletero = myView.findViewById(R.id.btnFiltro_Fletero);


        mbtnFilroFletero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //BusquedaListaFleterosFragment.Adapter_Fleteros_Busqueda.stopListening();
                BusquedaListaFleterosFragment.Adapter_Fleteros_Busqueda.notifyDataSetChanged();
                menuFiltroFleteros();
                //detenerFragment();
                //DialogFragment newFragment = Filtro_Fleteros_Fragment.newInstance();// call the static method
                //newFragment.show(getActivity().getSupportFragmentManager(), "Filtro_Fleteros_Fragment");

            }
        });

        // Crear fragmento de tu clase
        fragment = new BusquedaListaFleterosFragment();
        // Obtener el administrador de fragmentos a través de la actividad
        fragmentManager = getActivity().getSupportFragmentManager();

        //juegoFragment();
        return myView;
    }

    private void detenerFragment() {
        // Definir una transacción
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.detach(fragment);
        fragmentTransaction.commit();
    }

    private void queJaleElCondenado() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.attach(fragment);
        fragmentTransaction.commit();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //BusquedaListaFleterosFragment.Adapter_Fleteros_Busqueda.stopListening();
        final CharSequence[] opciones = {"A-Z", "Z-A", "Cancelar"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Seleccione filtro de busqueda")
                .setItems(opciones, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int i) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        if (opciones[i].equals("A-Z")) {
                            //Toast.makeText(getContext(), "de A a Z", Toast.LENGTH_SHORT).show();
                            //mBusqueda = "deaz";

                        }
                        if (opciones[i].equals("Z-A")) {
                            //Toast.makeText(getContext(), "al revez, campeon", Toast.LENGTH_SHORT).show();
                            //mBusqueda = "deza";

                        }
                        if (opciones[i].equals("Cancelar")){
                            //mBusqueda = "normal";
                            //dialog.dismiss();
                        }
                    }
                });
        return builder.create();
    }

    private void menuFiltroFleteros() {

        final CharSequence[] opciones = {"Acendente", "Descendente", "Cancelar"};

        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder((getContext()));
        alertOpciones.setTitle("Seleccione una opción");
        alertOpciones.setCancelable(false);
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Acendente")) {
                    Toast.makeText(getContext(), "Acendente", Toast.LENGTH_SHORT).show();
                    mBusqueda = "Acendente";
                    //queJaleElCondenado();
                    BusquedaListaFleterosFragment.Adapter_Fleteros_Busqueda.startListening();
                    BusquedaListaFleterosFragment.Adapter_Fleteros_Busqueda.notifyDataSetChanged();

                }
                if (opciones[i].equals("Descendente")) {
                    Toast.makeText(getContext(), "Descendente", Toast.LENGTH_SHORT).show();
                    mBusqueda = "Descendente";
                    BusquedaListaFleterosFragment.Adapter_Fleteros_Busqueda.startListening();

                }
                if (opciones[i].equals("Cancelar")){
                    mBusqueda = "normal";
                    dialogInterface.dismiss();
                    BusquedaListaFleterosFragment.Adapter_Fleteros_Busqueda.startListening();
                }


            }
        });
        alertOpciones.show();

    }

    private void showSelectedFragment(BusquedaListaFleterosFragment busquedaListaFleterosFragment) {
    }

    public static Filtro_Fleteros_Fragment newInstance() {
        Filtro_Fleteros_Fragment f = new Filtro_Fleteros_Fragment();
        return f;
    }


}
