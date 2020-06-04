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
import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mtxtLogin_Correo;
    private EditText mtxtLogin_Password;
    private Button btnLogin_IniciarSesion;
    private Button btnLogin_Registrate;
    String Usuario_login = "";
    private String Password_login = "";
    static String correoUsuario = "";
    //=============================================== Datos Clientes ============================
    static String idDoc_Cliente = "";
    static String nombreCliente = "";
    static String apellidoCliente = "";
    static String telefonoCliente = "";
    //=============================================== Datos Fleteros ============================
    public static String idDoc_Fletero = "";
    static String correoFletero = "";

    private FirebaseAuth mAuth;
    //private DatabaseReference mDatabase;
    private FirebaseFirestore mFireStore;

    private ProgressDialog mPDialog;

    public void manejoEventosMain (int opc) {
        switch (opc) {
            case R.id.btnInicioSesion:
                if (!Usuario_login.isEmpty() && !Password_login.isEmpty()) {
                    ObtenerDatosCliente();
                }
                else{
                    Toast.makeText(MainActivity.this, "Favor de llenar los campos", Toast.LENGTH_SHORT).show();
                }
                //Intent intent = new Intent(MainActivity.this, pantalla_busquedaFletero.class);
                //startActivity(intent);
                break;
            case R.id.btnRegistro:
                //Intent intentLoginRegistro = new Intent(this, Registro_Main.class);
                startActivity(new Intent(MainActivity.this, TermsCondActivity.class));

                //Intent intent  = new Intent(MainActivity.this, Registro_User.class);
                //startActivity(intent);
                break;
            default:
                break;
        }
    }
    private void ObtenerDatosFletero (){
        mFireStore.collection("Fletero")
                .whereEqualTo("correo", Usuario_login)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                idDoc_Fletero = document.getId();
                                correoFletero = document.getData().get("correo").toString();
                                String auxIdDoc = document.getData().get("idDocFletero").toString();
                                if (auxIdDoc.isEmpty()) {
                                    Map<String, Object> mapUpDoc = new HashMap<>();
                                    mapUpDoc.put("idDocFletero", idDoc_Fletero);
                                    mFireStore.collection("Fletero")
                                            .document(MainActivity.idDoc_Fletero)
                                            .update(mapUpDoc)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                }
                                            });
                                }
                            }
                        }
                    }
                });
    }
    private void ObtenerDatosCliente (){
        mFireStore.collection("Cliente")
                .whereEqualTo("correo", Usuario_login)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                correoUsuario = document.getData().get("correo").toString();
                                idDoc_Cliente = document.getId();
                                nombreCliente = document.getData().get("nombre").toString();
                                apellidoCliente = document.getData().get("apellidop").toString();
                                telefonoCliente = document.getData().get("telefono").toString();
                                String auxIdDoc = document.getData().get("idDocCliente").toString();
                                if (auxIdDoc.isEmpty()) {
                                    Map<String, Object> mapUpDoc = new HashMap<>();
                                    mapUpDoc.put("idDocCliente", idDoc_Cliente);
                                    mFireStore.collection("Cliente")
                                            .document(idDoc_Cliente)
                                            .update(mapUpDoc)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                }
                                            });
                                }
                            }
                            ObtenerDatosFletero();
                            LogearUsuario();
                        }
                    }
                });
    }

    private void LogearUsuario()
    {
        mPDialog.setTitle("Iniciando sesion");
        mPDialog.setMessage("Espere un momento...");
        mPDialog.setCancelable(false);
        mPDialog.show();
        //ObtenerDatosFletero();
        //ObtenerDatosCliente();
        mAuth.signInWithEmailAndPassword(Usuario_login, Password_login).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (!user.isEmailVerified() && !correoUsuario.equals("fer2314@gmail.com") && !correoUsuario.equals("aldo@gmail.com") && !correoUsuario.equals("fletero@gmail.com")) {
                        Toast.makeText(MainActivity.this, "Correo electrónico no verificado", Toast.LENGTH_SHORT).show();
                        mPDialog.dismiss();
                    } else {
                        if (correoUsuario.equals(Usuario_login)) {
                            //Toast.makeText(MainActivity.this, "Bienvenido " + nombreUsuario + " " + apellidoUsuario, Toast.LENGTH_LONG).show();
                            //Toast.makeText(MainActivity.this, "Bienvenido " + correoUsuario, Toast.LENGTH_LONG).show();
                            //startActivity(new Intent(MainActivity.this,Pantalla_pedidos.class));
                            startActivity(new Intent(MainActivity.this, pantalla_busquedaFletero.class));
                        } else if (!correoUsuario.equals(Usuario_login)) {
                            //Toast.makeText(MainActivity.this, "Bienvenido " + Usuario_login, Toast.LENGTH_LONG).show();
                            startActivity(new Intent(MainActivity.this, Pantalla_Inicio_Fletero.class));
                        }
                        mPDialog.dismiss();
                        //finish();
                    }
                }else {
                    mPDialog.dismiss();
                    Toast.makeText(MainActivity.this, "No se pudo iniciar sesion, favor de comprobar datos.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();

        mtxtLogin_Correo = findViewById(R.id.editTextCorreo_Login);
        mtxtLogin_Password = findViewById(R.id.editTextPass_Login);

        btnLogin_IniciarSesion = findViewById(R.id.btnInicioSesion);
        btnLogin_Registrate = findViewById(R.id.btnRegistro);

        btnLogin_IniciarSesion.setOnClickListener(this);
        btnLogin_Registrate.setOnClickListener(this);

        mFireStore = FirebaseFirestore.getInstance();

        mPDialog = new ProgressDialog(this, R.style.CustomAlertDialog);

        compruebaPermisoSD();

    }

    private void compruebaPermisoSD() {
        // Check if we have write permission
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            //Log.e(TAG, "Permission not granted WRITE_EXTERNAL_STORAGE.");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        225);
            }
        }
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            //Log.e(TAG, "Permission not granted CAMERA.");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.CAMERA)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.CAMERA},
                        226);
            }
        }
    }

    @Override
    public void onClick(View view) {
        Usuario_login = mtxtLogin_Correo.getText().toString();
        Password_login =  mtxtLogin_Password.getText().toString();
        //ObtenerDatosFletero();
        //ObtenerDatosCliente();
        //Toast.makeText(MainActivity.this, "Bienvenido " + Usuario_login + "\n" + correoUsuario, Toast.LENGTH_LONG).show();
        manejoEventosMain(view.getId());
    }
}


