package com.example.ufletes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Pantalla_pedidos extends AppCompatActivity implements View.OnClickListener {


    private Button mbtnAgregar_Articulos;
    private Button mbtnSolicitar_Flete;
    private Button mbtnAbrir_Mapa;
    private TextView txtOrigen, txtDestino;
    static String strPedidosOrigen, strPedidoDestino, fechaPedido;
    //private String toallitahumeda = (String) getIntent().getStringExtra("miLista");
    private ProgressDialog mPDialog;
    private FirebaseFirestore mFireStore;
    private static List<Address> strDireccionDestino_pedido;

    private void manejoEventosPantalla_pedidos (int opc) {
        switch (opc) {
            case R.id.btnAgregar_Articulos_User:
                startActivity(new Intent(Pantalla_pedidos.this, pantalla_AgregarArticulos.class));
                break;
            case R.id.btnAbrirMapa:
                Intent intent = new Intent(getApplicationContext(), MapsActivity_Pedido.class);
                startActivity(intent);
                break;
            case R.id.btnPedirFlete_Pedidos:
                fechaPedido = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                solicitarFleter();
                break;
            default:
                break;

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_pedidos);
        mbtnAgregar_Articulos = findViewById(R.id.btnAgregar_Articulos_User);
        mbtnAgregar_Articulos.setOnClickListener(this);

        mbtnAbrir_Mapa = findViewById(R.id.btnAbrirMapa);
        mbtnAbrir_Mapa.setOnClickListener(this);

        mbtnSolicitar_Flete = findViewById(R.id.btnPedirFlete_Pedidos);
        mbtnSolicitar_Flete.setOnClickListener(this);

        mPDialog = new ProgressDialog(this, R.style.CustomAlertDialog);
        mFireStore = FirebaseFirestore.getInstance();

        txtOrigen = findViewById(R.id.editTextDireccion_Pedidos);
        txtDestino = findViewById(R.id.editTextDestino_pedido);

        Bundle parametros = this.getIntent().getExtras();
        if(parametros !=null){
             String datos = parametros.getString("DireccionOrigenMaps");
                txtOrigen.setText(datos);
        }
        compruebaPermisoGPS();

        getSupportActionBar().hide();
    }

    private void compruebaPermisoGPS() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        1);
            }
        }
    }

    private void solicitarFleter() {
        if (txtOrigen.getText().toString().isEmpty() && txtDestino.getText().toString().isEmpty()) {
            Toast.makeText(this, R.string.favor_seleccionar_direcciones, Toast.LENGTH_SHORT).show();
        }
        else if (txtOrigen.getText().toString().isEmpty()){
            Toast.makeText(this, R.string.favor_seleccionar_origen, Toast.LENGTH_SHORT).show();
        }
        else if (txtDestino.getText().toString().isEmpty()) {
            Toast.makeText(this, R.string.favor_seleccionar_destino, Toast.LENGTH_SHORT).show();
        }
        if (!txtOrigen.getText().toString().isEmpty() && !txtDestino.getText().toString().isEmpty()) {
            Map<String, Object> mapSolicFlete = new HashMap<>();
            mapSolicFlete.put("nombre_s", MainActivity.nombreCliente);
            mapSolicFlete.put("apellidop_s", MainActivity.apellidoCliente);
            mapSolicFlete.put("apellidom_s", MainActivity.apellidomatCliente);
            mapSolicFlete.put("telefono_s", MainActivity.telefonoCliente);
            mapSolicFlete.put("idCliente_s", MainActivity.idDoc_Cliente);
            mapSolicFlete.put("idFletero_s", "");
            mapSolicFlete.put("nombre_f_s", "");
            mapSolicFlete.put("apellidop_f_s", "");
            mapSolicFlete.put("apellidom_f_s", "");
            mapSolicFlete.put("dirOrigen_s", strPedidosOrigen); //Cambiar a coordenadas
            mapSolicFlete.put("dirDestino_s", strPedidoDestino);
            mapSolicFlete.put("statusSolicitud_s", "Disponible");
            mapSolicFlete.put("fecha_s", fechaPedido);

            mPDialog.setTitle(R.string.dialog_subiendo);
            mPDialog.setMessage(Pantalla_pedidos.this.getResources().getString(R.string.dialog_espere_solicitando));
            mPDialog.setCancelable(false);
            mPDialog.show();

            mFireStore.collection("Pedidos")
                    .add(mapSolicFlete)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    mPDialog.dismiss();
                    Toast.makeText(Pantalla_pedidos.this, R.string.solicitud_enviada, Toast.LENGTH_SHORT).show();
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Pantalla_pedidos.this, R.string.no_guardo_solicitud, Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (strPedidosOrigen != null){
            txtOrigen.setText(strPedidosOrigen);
        }

        if (strPedidoDestino != null) {
            txtDestino.setText(strPedidoDestino);

        }
    }

    @Override
    public void onClick(View view) {
        manejoEventosPantalla_pedidos(view.getId());
    }
}
