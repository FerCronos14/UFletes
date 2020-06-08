package com.example.ufletes;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
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
import java.util.List;
import java.util.Locale;

public class MapsActivity_RastreoFletero extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker markerRastreo, markerClienteOrigen;
    private Location currentLocation;
    private static List<Address> strDireccionRastreo;
    private static List<Address> strDireccionCliente;
    static Double cooLatitudCliente, cooLongitudCliente;
    private String addressRastreo;

    FusedLocationProviderClient fusedLocation;
    private static final int REQUEST_CODE = 101;
    SupportMapFragment smf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps__rastreo_fletero);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        smf = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapRastreo);
        fusedLocation = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //Metodo para obtener locacion actual
            fetchLastLocation();

            return;
        } else {
            ActivityCompat.requestPermissions(MapsActivity_RastreoFletero.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},44);
        }
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

                            googleMap.getUiSettings().setZoomControlsEnabled(true);
                            markerRastreo = googleMap.addMarker(new MarkerOptions()
                                            .position(latLngM2)
                                            .title("Ubicacion actual del fletero")
                                            .snippet(addressRastreo)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                            );

                            Geocoder geocdireccionDestino = new Geocoder(getApplicationContext(), Locale.getDefault());
                            try {
                                strDireccionCliente = geocdireccionDestino.getFromLocationName(SolicitudesFletesFragment.strcooOrigen_cliente, 1);
                                cooLatitudCliente = strDireccionCliente.get(0).getLatitude();
                                cooLongitudCliente = strDireccionCliente.get(0).getLongitude();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            LatLng cooCliente = new LatLng(cooLatitudCliente, cooLongitudCliente);

                            markerClienteOrigen = googleMap.addMarker(new MarkerOptions()
                                            .position(cooCliente)
                                            .title("Ubicacion del cliente")
                                            //.snippet(addressRastreo)
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                            );

                            Geocoder geocdireccionOrigen = new Geocoder(getApplicationContext(), Locale.getDefault());
                            try {
                                strDireccionRastreo = geocdireccionOrigen.getFromLocation(locationM2.getLatitude(),locationM2.getLongitude(),1);
                                addressRastreo = strDireccionRastreo.get(0).getAddressLine(0);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cooCliente, 15));
                       }
                    });

                }
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera

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
