package com.example.ufletes.uifletero;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Looper;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ufletes.MainActivity;
import com.example.ufletes.Pantalla_Inicio_Fletero;
import com.example.ufletes.R;
import com.example.ufletes.Registro_AgregarAuto;
import com.example.ufletes.holders.vehiculosFleterosHolder;
import com.example.ufletes.ui.Vehiculos_Lista;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.ObservableSnapshotArray;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.google.firebase.firestore.FirebaseFirestore.getInstance;

/**
 * A simple {@link Fragment} subclass.
 */
public class Inicio_fletero_fragment extends Fragment {

    private RecyclerView RVVehiculos;
    private FirestoreRecyclerAdapter<Vehiculos_Lista, vehiculosFleterosHolder> Adapter_Vehiculos;
    private com.firebase.ui.firestore.FirestoreRecyclerOptions<Vehiculos_Lista> FirestoreRecyclerOptions;
    Query query;
    View view;
    private Button mbtnAgregarVehiculo_Fletero_Home;

    private Button mbtnElimiarVehiculo;
    private Location currentLocation;
    private static List<Address> strDireccionRastreo;
    private String addressRastreo;
    FusedLocationProviderClient fusedLocation;
    LocationRequest mLocationRequest;
    LocationCallback mLocationCallback;
    private static final int REQUEST_CODE = 101;

    private int expandedPosition = -1;

    public Inicio_fletero_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_iinicio_fletero, container, false);
        mbtnAgregarVehiculo_Fletero_Home = root.findViewById(R.id.btnPreguntaSubirVehiculo_inicio);
        mbtnAgregarVehiculo_Fletero_Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Registro_AgregarAuto.class));
            }
        });
        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RVVehiculos = view.findViewById(R.id.vehiculosFleterosRVAct);
        RVVehiculos.setHasFixedSize(true);
        LinearLayoutManager mlinearLayoutManager = new LinearLayoutManager(getActivity());
        RVVehiculos.setLayoutManager(mlinearLayoutManager);
        getData();
    }

    private void getData() {
        query = getInstance()
                .collection("Fletero")
                .document(MainActivity.idDoc_Fletero)
                .collection("Vehiculos");

        FirestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Vehiculos_Lista>()
                .setQuery(query, Vehiculos_Lista.class)
                .build();
        attachRecyclerView();
        Adapter_Vehiculos.notifyDataSetChanged();
    }

    private void attachRecyclerView() {
        Adapter_Vehiculos = new FirestoreRecyclerAdapter<Vehiculos_Lista, vehiculosFleterosHolder>(FirestoreRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull vehiculosFleterosHolder holder, final int position, @NonNull Vehiculos_Lista model) {
                holder.mtextViewMarcaV.setText((model.getMarca_v()));
                holder.mtextViewTipoV.setText((model.getTipo_v()));
                holder.mtextViewVolv.setText((model.getVol_v()));
                holder.mtextViewMedida.setText(model.getMedida_v());

                ObservableSnapshotArray<Vehiculos_Lista> observableSnapshotArray = getSnapshots();
                final DocumentReference documentReference =
                        observableSnapshotArray.getSnapshot(position).getReference();

                Glide.with(getContext())
                        .load(model.getPathFoto_v())
                        .fitCenter()
                        .centerCrop()
                        .placeholder(R.drawable.ic_vehiculo_na)
                        .into(holder.imgFoto_Vehiculo)
                ;
                final boolean isExpanded = position==expandedPosition;
                holder.mllExpandAreaVehiculos_V.setVisibility(isExpanded?View.VISIBLE:View.GONE);
                holder.itemView.setActivated(isExpanded);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        expandedPosition = isExpanded ? -1:position;
                        TransitionManager.beginDelayedTransition(RVVehiculos);
                        notifyDataSetChanged();

                        // accion de llamar a feltero
                        mbtnElimiarVehiculo = v.findViewById(R.id.btnIEliminar_ListaArticulos);
                        mbtnElimiarVehiculo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                documentReference.delete();
                            }
                        });
                    }
                });
            }

            @NonNull
            @Override
            public vehiculosFleterosHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_agregar_vehiculos, parent, false);
                return new vehiculosFleterosHolder(view);
            }
        };
        RVVehiculos.setAdapter(Adapter_Vehiculos);
    }

    @Override
    public void onStart() {
        super.onStart();
        Adapter_Vehiculos.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        Adapter_Vehiculos.stopListening();
    }

}
