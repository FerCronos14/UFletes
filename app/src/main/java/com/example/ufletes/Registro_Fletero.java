package com.example.ufletes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Registro_Fletero extends AppCompatActivity implements View.OnClickListener {

    private EditText mtxtNombre_Fletero;
    private EditText mtxtApePaterno_Fletero;
    private EditText mtxtApeMaterno_Fletero;
    private EditText mtxtTel_Fletero;
    private EditText mtxtEmail_Fletero;
    private EditText mtxtPassword_Register_Fletero;
    private EditText mtxtConfirmPassword_Register_Fletero;
    private Button mbtnRegistro_Fletero;
    private Button mbtnAgregar_Vehiculo;

    private String Nombre_Fletero_Register = "";
    private String ApePaterno_Fletero_Register= "";
    private String ApeMaterno_Fletero_Register= "";
    private String Tel_Fletero_Register;
    private String Email_Fletero_Register = "";
    private String Password_Fletero_Register = "";
    private String ConfirmPassword_Fletero_Register = "";

    private FirebaseAuth mAuthFletero;
    private DatabaseReference mDatabaseFletero;

    public void manejoEventosFletero (int opc) {
        switch (opc) {
            case R.id.btnRegistrar_Fletero:
                Nombre_Fletero_Register = mtxtNombre_Fletero.getText().toString();
                ApePaterno_Fletero_Register = mtxtApePaterno_Fletero.getText().toString();
                ApeMaterno_Fletero_Register = mtxtApeMaterno_Fletero.getText().toString();
                Tel_Fletero_Register = mtxtTel_Fletero.getText().toString();
                Email_Fletero_Register = mtxtEmail_Fletero.getText().toString();
                Password_Fletero_Register = mtxtPassword_Register_Fletero.getText().toString();
                ConfirmPassword_Fletero_Register = mtxtConfirmPassword_Register_Fletero.getText().toString();

                if(!Nombre_Fletero_Register.isEmpty() ||
                        !ApePaterno_Fletero_Register.isEmpty() ||
                        !ApeMaterno_Fletero_Register.isEmpty() ||
                        !Email_Fletero_Register.isEmpty() ||
                        !Password_Fletero_Register.isEmpty() ||
                        !ConfirmPassword_Fletero_Register.isEmpty()) {
                    if(Password_Fletero_Register.length() >= 6) {
                        if (!ConfirmPassword_Fletero_Register.equals(Password_Fletero_Register)) {
                            Toast.makeText(Registro_Fletero.this, "Contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                        }else {
                            RegistrarFletero();
                        }
                    }else {
                        Toast.makeText(Registro_Fletero.this, "Contraseña menor a 6", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Registro_Fletero.this, "Favor de llenar campos requeridos", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnAgregarAuto:
                //Intent intentRegistro_User = new Intent(this, Registro_User.class);
                //intent = new Intent(Registro_Main.this, Registro_User.class);
                startActivity(new Intent(Registro_Fletero.this, Registro_AgregarAuto.class));
                break;
            default:
                break;
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro__fletero);

        mtxtNombre_Fletero = findViewById(R.id.editTextNombre_Fletero);
        mtxtApePaterno_Fletero = findViewById(R.id.editTextApePaterno_Fletero);
        mtxtApeMaterno_Fletero = findViewById(R.id.editTextApeMaterno_Fletero);
        mtxtTel_Fletero = findViewById(R.id.editTextTelefono_Fletero);
        mtxtEmail_Fletero = findViewById(R.id.editTextEmail_Fletero);
        mtxtPassword_Register_Fletero = findViewById(R.id.editTextPassword_Register_Fletero);
        mtxtConfirmPassword_Register_Fletero = findViewById(R.id.editTextConfirmPass_Register_Fletero);

        mAuthFletero = FirebaseAuth.getInstance();
        mDatabaseFletero = FirebaseDatabase.getInstance().getReference();


        mbtnRegistro_Fletero.setOnClickListener(this);
        mbtnRegistro_Fletero = findViewById(R.id.btnRegistrar_Fletero);
        mbtnAgregar_Vehiculo.setOnClickListener(this);
        mbtnAgregar_Vehiculo = findViewById(R.id.btnAgregarAuto);
    }

    private void RegistrarFletero() {
        mAuthFletero.createUserWithEmailAndPassword(Email_Fletero_Register, Password_Fletero_Register).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("nombre", Nombre_Fletero_Register);
                    map.put("apellidom", ApeMaterno_Fletero_Register);
                    map.put("apellidop", ApePaterno_Fletero_Register);
                    map.put("telefono", Tel_Fletero_Register);
                    map.put("correo", Email_Fletero_Register);
                    map.put("password", Password_Fletero_Register);

                    String id = mAuthFletero.getCurrentUser().getUid();
                    mDatabaseFletero.child("fleteros").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if (task2.isSuccessful()){
                                Toast.makeText(Registro_Fletero.this, "Registro completo", Toast.LENGTH_SHORT).show();
                                //startActivity( new Intent(Registro_User.this, MainActivity.class));
                                //finish();
                            } else {
                                Toast.makeText(Registro_Fletero.this, "No se pudo guardar los datos", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(Registro_Fletero.this, "Usuario ya existe", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Registro_Fletero.this, "No se pudo guardar los datos", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        manejoEventosFletero(view.getId());
    }


}
