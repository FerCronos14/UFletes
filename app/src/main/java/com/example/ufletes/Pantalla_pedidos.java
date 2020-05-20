package com.example.ufletes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Pantalla_pedidos extends AppCompatActivity implements View.OnClickListener {


    private Button mbtnAgregar_Articulos;
    private Button mbtnSolicitar_Flete;
    private Button mbtnAbrir_Mapa;
    private TextView txtOrigen, txtDestino;
    static String strPedidosOrigen, strPedidoDestino;
    //private String toallitahumeda = (String) getIntent().getStringExtra("miLista");
    private ProgressDialog mPDialog;
    private FirebaseFirestore mFireStore;

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

        mPDialog = new ProgressDialog(this);
        mFireStore = FirebaseFirestore.getInstance();

        txtOrigen = findViewById(R.id.editTextDireccion_Pedidos);
        txtDestino = findViewById(R.id.editTextDestino_pedido);

        Bundle parametros = this.getIntent().getExtras();
        if(parametros !=null){
             String datos = parametros.getString("DireccionOrigenMaps");
                txtOrigen.setText(datos);
                Toast.makeText(this, "Direccion de origen OnCreate" + datos, Toast.LENGTH_LONG).show();
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
            Toast.makeText(this, "Favor de seleccionar las direcciones abriendo el mapa", Toast.LENGTH_SHORT).show();
        }
        else if (txtOrigen.getText().toString().isEmpty()){
            Toast.makeText(this, "Favor de seleccionar la direccion de origen abriendo el mapa", Toast.LENGTH_SHORT).show();
        }
        else if (txtDestino.getText().toString().isEmpty()) {
            Toast.makeText(this, "Favor de seleccionar la direccion de destino abriendo el mapa", Toast.LENGTH_SHORT).show();
        }
        if (!txtOrigen.getText().toString().isEmpty() && !txtDestino.getText().toString().isEmpty()) {
            Map<String, Object> mapSolicFlete = new HashMap<>();
            mapSolicFlete.put("nombre_s", MainActivity.nombreCliente);
            mapSolicFlete.put("apellidop_s", MainActivity.apellidoCliente);
            mapSolicFlete.put("telefono_s", MainActivity.telefonoCliente);
            mapSolicFlete.put("idCliente_s", MainActivity.idDoc_Cliente);
            mapSolicFlete.put("dirOrigen_s", strPedidosOrigen);
            mapSolicFlete.put("dirDestino_s", strPedidoDestino);
            mapSolicFlete.put("statusSolicitud_s", "Disponible");

            mPDialog.setTitle("Subiendo...");
            mPDialog.setMessage("Solicitando flete...");
            mPDialog.setCancelable(false);
            mPDialog.show();

            mFireStore.collection("Pedidos")
                    .add(mapSolicFlete)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    mPDialog.dismiss();
                    Toast.makeText(Pantalla_pedidos.this, "Solicitud guardada", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Pantalla_pedidos.this, "No se pudo guardar la solicitud", Toast.LENGTH_SHORT).show();
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
