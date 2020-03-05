package com.example.ufletes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mtxtLogin_Correo;
    private EditText mtxtLogin_Password;
    private Button btnLogin_IniciarSesion;
    private Button btnLogin_Registrate;
    private String Usuario_login = "";
    private String Password_login = "";
    private FirebaseAuth mAuth;

    public void manejoEventosMain (int opc) {
        switch (opc) {
            case R.id.btnInicioSesion:
                if (!Usuario_login.isEmpty() && !Password_login.isEmpty()) {
                    LogearUsuario();
                }
                else{
                    Toast.makeText(MainActivity.this, "Favor de llenar los campos", Toast.LENGTH_SHORT).show();
                }
                //Intent intent = new Intent(MainActivity.this, pantalla_busquedaFletero.class);
                //startActivity(intent);
                break;
            case R.id.btnRegistro:
                //Intent intentLoginRegistro = new Intent(this, Registro_Main.class);
                Intent intent  = new Intent(MainActivity.this, Registro_Main.class);

                startActivity(intent);
                break;
            default:
                break;

        }

    }

    private void LogearUsuario() {
        mAuth.signInWithEmailAndPassword(Usuario_login, Password_login).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(MainActivity.this, pantalla_busquedaFletero.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(MainActivity.this, "Bienvenido", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MainActivity.this, "No se pudo iniciar sesion, favro de comprobar datos.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        mtxtLogin_Correo = findViewById(R.id.editTextCorreo_Login);
        mtxtLogin_Password = findViewById(R.id.editTextPass_Login);

        btnLogin_IniciarSesion = findViewById(R.id.btnInicioSesion);
        btnLogin_Registrate = findViewById(R.id.btnRegistro);

        btnLogin_IniciarSesion.setOnClickListener(this);
        btnLogin_Registrate.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Usuario_login = mtxtLogin_Correo.getText().toString();
        Password_login =  mtxtLogin_Password.getText().toString();
        manejoEventosMain(view.getId());
    }

   /* @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(MainActivity.this, pantalla_busquedaFletero.class);
            startActivity(intent);
            finish();
        }
    } */
}


