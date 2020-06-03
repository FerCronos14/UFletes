package com.example.ufletes;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.media.MediaMetadataCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MapsActivity_Pedido extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener,
        //GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMarkerDragListener
{

    private GoogleMap mMap;
    private Marker markerOrigen, markerDestino;
    private LocationListener locListener;
    private static List<Address> strDireccionOridgen;
    private static List<Address> strDireccionDestino;
    private static LatLng locOrigen, locDestino;
    private boolean existDirOrigen, existDirDestino;

    private Location lastLocation, currentLocation;
    FusedLocationProviderClient fusedLocation;
    private static final int REQUEST_CODE = 101;
    SupportMapFragment smf;

    private String addressOrigen, addressDestino;
    private double latitudOriginal, longitudOriginal;
    private String provider;
    private LocationManager locManager;

    private Button mbtnAceptarDirCliente;
    private EditText medDirrecion_Cliente;
    private EditText medDestino_Cliente;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps__pedido);

        mbtnAceptarDirCliente = findViewById(R.id.btnDefrDir_Cliente);
        medDirrecion_Cliente = findViewById(R.id.editTextDireccion_Pedidos);
        medDestino_Cliente = findViewById(R.id.editTextDestino_pedido);


        smf = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapPedido);
        fusedLocation = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //Metodo para obtener locacion actual
            fetchLastLocation();

            return;
        } else {
            ActivityCompat.requestPermissions(MapsActivity_Pedido.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},44);
        }

        mbtnAceptarDirCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MapsActivity_Pedido.this, Pantalla_pedidos.class);
                intent.putExtra("DireccionOrigenMaps", addressOrigen);
                Pantalla_pedidos.strPedidoDestino = addressDestino;
                Pantalla_pedidos.strPedidosOrigen = addressOrigen;
                //Toast.makeText(this, "Direccion de origen" + addressOrigen, Toast.LENGTH_LONG).show();

                //startActivity(intent);
                finish();
            }
        });

    }

    private void fetchLastLocation() {
        Task<Location> task = fusedLocation.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(final Location locationM2) {
                if (locationM2 != null) {
                    currentLocation = locationM2;
                    smf.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(final GoogleMap googleMap) {
                            LatLng latLngM2 = new LatLng(locationM2.getLatitude(), locationM2.getLongitude());
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngM2, 15));
                            markerOrigen = googleMap.addMarker(new MarkerOptions()
                                            .position(latLngM2)
                                            .draggable(true)
                                            .title("Ubicacion Actual")
                                    .snippet(addressOrigen)
                                    //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                            );

                            Geocoder geocdireccionOrigen = new Geocoder(getApplicationContext(), Locale.getDefault());
                            try {
                                strDireccionOridgen = geocdireccionOrigen.getFromLocation(locationM2.getLatitude(),locationM2.getLongitude(),1);
                                addressOrigen = strDireccionOridgen.get(0).getAddressLine(0);
                                //Toast.makeText(this, "Direccion pa: " + strDireccionOridgen.get(0).getAddressLine(0), Toast.LENGTH_LONG).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            //Toast.makeText(getApplicationContext(), "Longitud: " + currentLocation.getLongitude() + "Latitud: " + currentLocation.getLatitude(), Toast.LENGTH_LONG).show();


                            googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                                @Override
                                public void onMapLongClick(LatLng position) {
                                        markerDestino = googleMap.addMarker(new MarkerOptions()
                                                .position(position)
                                                .draggable(true)
                                                .title("Destino")
                                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                                        );
                                    locDestino = position;
                                    Geocoder geocdireccionDestino = new Geocoder(getApplicationContext(), Locale.getDefault());
                                    try {
                                        strDireccionDestino = geocdireccionDestino.getFromLocation(locDestino.latitude, locDestino.longitude, 1);
                                        addressDestino = strDireccionDestino.get(0).getAddressLine(0);
                                        //Toast.makeText(this, "Direccion pa: " + strDireccionOridgen.get(0).getAddressLine(0), Toast.LENGTH_LONG).show();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                            googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                                @Override
                                public void onMarkerDragStart(Marker marker) {
                                    marker.hideInfoWindow();
                                }

                                @Override
                                public void onMarkerDrag(Marker marker) {

                                }

                                @Override
                                public void onMarkerDragEnd(Marker marker) {
                                    if(marker.equals(markerOrigen)) {
                                        LatLng cooOrigen = marker.getPosition();
                                        Toast.makeText(getApplication(), "Direccion de origen" + cooOrigen, Toast.LENGTH_LONG).show();
                                        Geocoder geocdireccionOrigen = new Geocoder(getApplicationContext(), Locale.getDefault());
                                        try {
                                            strDireccionOridgen = geocdireccionOrigen.getFromLocation(cooOrigen.latitude, cooOrigen.longitude, 1);
                                            addressOrigen = strDireccionOridgen.get(0).getAddressLine(0);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        marker.setSnippet(addressOrigen);
                                        marker.showInfoWindow();
                                    }
                                    if(marker.equals(markerDestino)){
                                        LatLng cooDestino = marker.getPosition();
                                        Geocoder geocdireccionDestino = new Geocoder(getApplicationContext(), Locale.getDefault());
                                        try {
                                            strDireccionDestino = geocdireccionDestino.getFromLocation(cooDestino.latitude, cooDestino.longitude, 1);
                                            addressDestino = strDireccionDestino.get(0).getAddressLine(0);
                                            //Toast.makeText(this, "Direccion pa: " + strDireccionOridgen.get(0).getAddressLine(0), Toast.LENGTH_LONG).show();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        marker.setSnippet(addressDestino);
                                        marker.showInfoWindow();
                                    }
                                }
                            });

                        }
                    });




                }
            }
        });
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMarkerClickListener(this);

        // obtener coordenadas ubicacion actual
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locManager.getBestProvider(new Criteria(), false);

        mMap.setMyLocationEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        locListener = new LocationListener() {
            @Override
            public void onStatusChanged(String provider, int status,
                                        Bundle extras) {
            }
            @Override
            public void onProviderEnabled(String provider) {
            }
            @Override
            public void onProviderDisabled(String provider) {
            }
            @Override
            public void onLocationChanged(Location location) {

                lastLocation = location;
                latitudOriginal = location.getLatitude();
                longitudOriginal = location.getLongitude();


                locOrigen = new LatLng(location.getLatitude(), location.getLongitude());
            }
        };
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        if(marker.equals(markerOrigen)) {
            Toast.makeText(this, addressOrigen, Toast.LENGTH_LONG).show();
        }
        return false;
    }


    @Override
    public void onMarkerDragStart(Marker marker) {
        marker.hideInfoWindow();
    }

    @Override
    public void onMarkerDrag(Marker marker) {
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        if(marker.equals(markerOrigen)) {
            LatLng cooOrigen = marker.getPosition();
            Geocoder geocdireccionOrigen = new Geocoder(getApplicationContext(), Locale.getDefault());
            try {
                strDireccionOridgen = geocdireccionOrigen.getFromLocation(cooOrigen.latitude, cooOrigen.longitude, 1);
                addressOrigen = strDireccionOridgen.get(0).getAddressLine(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
            marker.setSnippet(addressOrigen);
            marker.showInfoWindow();
        }
        if(marker.equals(markerDestino)){
            LatLng cooDestino = marker.getPosition();
            Geocoder geocdireccionDestino = new Geocoder(getApplicationContext(), Locale.getDefault());
            try {
                strDireccionDestino = geocdireccionDestino.getFromLocation(cooDestino.latitude, cooDestino.longitude, 1);
                addressDestino = strDireccionDestino.get(0).getAddressLine(0);
                //Toast.makeText(this, "Direccion pa: " + strDireccionOridgen.get(0).getAddressLine(0), Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
            marker.setSnippet(addressDestino);
            marker.showInfoWindow();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLastLocation();
            }
        }
    }
}
