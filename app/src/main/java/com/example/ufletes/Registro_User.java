package com.example.ufletes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Registro_User extends AppCompatActivity implements View.OnClickListener{
    private EditText mtxtNombre_User;
    private EditText mtxtApePaterno_User;
    private EditText mtxtApeMaterno_User;
    private EditText mtxtTel_User;
    public EditText mtxtEmail_User;
    private EditText mtxtPassword_Register_User;
    private EditText mtxtConfirmPassword_Register_User;
    private Button mbtnRegistro_User;
    private Spinner comboUsuarios;

    private String Nombre_User_Register = "";
    private String ApePaterno_User_Register= "";
    private String ApeMaterno_User_Register= "";
    private String Tel_User_Register;
    public String Email_User_Register = "";
    private String Password_User_Register = "";
    private String ConfirmPassword_User_Register = "";
    static String TipoUsuario = "";
    private String idDocumento = "";
    private ProgressDialog mPDialog;


    private FirebaseAuth mAuth;
    //private DatabaseReference mDatabase;
    private FirebaseFirestore mFireStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro__user);

        getSupportActionBar().hide();

        mtxtNombre_User = findViewById(R.id.editTextNombre_User);
        mtxtApePaterno_User = findViewById(R.id.editTextApePaterno_User);
        mtxtApeMaterno_User = findViewById(R.id.editTextApeMaterno_User);
        mtxtTel_User = findViewById(R.id.editTextTel_User);
        mtxtEmail_User = findViewById(R.id.editTextEmail_User);
        mtxtPassword_Register_User = findViewById(R.id.editTextPassword_Register_User);
        mbtnRegistro_User = findViewById(R.id.btnRegistrar_User);
        mtxtConfirmPassword_Register_User = findViewById(R.id.editTextConfirmPass_Register_User);

        comboUsuarios = findViewById(R.id.spinnerTipoUsuario);
        mPDialog = new ProgressDialog(this, R.style.CustomAlertDialog);

        mAuth = FirebaseAuth.getInstance();
        //mDatabase = FirebaseDatabase.getInstance().getReference();
        mFireStore = FirebaseFirestore.getInstance();


        mbtnRegistro_User.setOnClickListener(this);


        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,
                R.array.comboTipoUsuario, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        comboUsuarios.setAdapter(adapter);
        comboUsuarios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TipoUsuario = adapterView.getItemAtPosition(i).toString();
                if (TipoUsuario.contains("tipo")) { }
                else {
                    Toast.makeText(adapterView.getContext(), "Ha seleccionado: " + adapterView.getItemAtPosition(i).toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected (AdapterView<?> adapterView) {

            }
        });

    }

    private void RegistroUser() {
        if (TipoUsuario.contains("tipo")) {
            Toast.makeText(this, "Favor de seleccionar un tipo de usuario", Toast.LENGTH_SHORT).show();
        }
        else {
            mAuth.createUserWithEmailAndPassword(Email_User_Register, Password_User_Register).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        mPDialog.setTitle("Iniciando sesion");
                        mPDialog.setMessage("Espere un momento...");
                        mPDialog.setCancelable(false);
                        mPDialog.show();

                        Map<String, Object> map = new HashMap<>();
                        map.put("nombre", Nombre_User_Register);
                        map.put("apellidom", ApeMaterno_User_Register);
                        map.put("apellidop", ApePaterno_User_Register);
                        map.put("telefono", Tel_User_Register);
                        map.put("correo", Email_User_Register);
                        map.put("password", Password_User_Register);
                        if (TipoUsuario.contains("Fletero")) {
                            map.put("idDocFletero", "");
                            map.put("pathFoto_v", "");
                        }
                        if (TipoUsuario.contains("Cliente")) {
                            map.put("idDocCliente", "");
                        }

                        String id = mAuth.getCurrentUser().getUid();
                        mFireStore.collection(TipoUsuario)
                                .add(map)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                //String postId = " ";
                                mPDialog.dismiss();

                                Toast.makeText(Registro_User.this, "Registro completo", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Registro_User.this, MainActivity.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                mPDialog.dismiss();
                                Toast.makeText(Registro_User.this, "No se pudo completar el registro", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(Registro_User.this, "Usuario ya existe", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Registro_User.this, "No se pudo guardar los datos", Toast.LENGTH_SHORT).show();
                        }
                        mPDialog.dismiss();
                        //Toast.makeText(Registro_User.this, "No se pudo registrar el usuario", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        Nombre_User_Register = mtxtNombre_User.getText().toString();
        ApePaterno_User_Register = mtxtApePaterno_User.getText().toString();
        ApeMaterno_User_Register = mtxtApeMaterno_User.getText().toString();
        Tel_User_Register = mtxtTel_User.getText().toString();
        Email_User_Register = mtxtEmail_User.getText().toString();
        Password_User_Register = mtxtPassword_Register_User.getText().toString();
        ConfirmPassword_User_Register = mtxtConfirmPassword_Register_User.getText().toString();

        if(!Nombre_User_Register.isEmpty() ||
                !ApePaterno_User_Register.isEmpty() ||
                !ApeMaterno_User_Register.isEmpty() ||
                !Email_User_Register.isEmpty() ||
                !Password_User_Register.isEmpty() ||
                !ConfirmPassword_User_Register.isEmpty()) {
            if(Password_User_Register.length() >= 6) {
                if (!ConfirmPassword_User_Register.equals(Password_User_Register)) {
                    Toast.makeText(Registro_User.this, "Contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                }else {
                    RegistroUser();
                }
            }else {
                Toast.makeText(Registro_User.this, "Contraseña menor a 6", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(Registro_User.this, "Favor de llenar campos requeridos", Toast.LENGTH_SHORT).show();
        }

    }
}
