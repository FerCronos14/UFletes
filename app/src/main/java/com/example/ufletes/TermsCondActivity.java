package com.example.ufletes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class TermsCondActivity extends AppCompatActivity implements View.OnClickListener{

    private Button mbtnSiguiente, mbtnCreditos;
    private CheckBox mCBTC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_cond);
        getSupportActionBar().hide();

        mbtnSiguiente = findViewById(R.id.btnAcptarTC);
        mbtnSiguiente.setOnClickListener(this);

        mbtnCreditos = findViewById(R.id.btnCreditos);
        mbtnCreditos.setOnClickListener(this);

        mCBTC = findViewById(R.id.checkBoxAcTC);
    }

    public void manejoEventosTC (int opc) {
        switch (opc) {
            case R.id.btnAcptarTC:
                if (mCBTC.isChecked()) {
                    startActivity(new Intent(TermsCondActivity.this, Registro_User.class));
                } else {
                    Toast.makeText(this, "Debe acepter los Términos y Condiciones para continuar.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnCreditos:
                final Dialog dialog_creditos= new Dialog(this);
                //dialog_creditos.getWindow().setBackgroundDrawable(new ColorDrawable(Color.));
                dialog_creditos.setContentView(R.layout.cuadro_dialogo_creditos);
                dialog_creditos.setTitle("Creditos");
                dialog_creditos.setCancelable(true);
                //dialog_creditos.getWindow().setLayout(600, 400);
                Button cerrarDialog = dialog_creditos.findViewById(R.id.btnCerrar_Creditos_Dialog);
                cerrarDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog_creditos.dismiss();
                    }
                });
                //startActivity(new Intent(MainActivity.this, TermsCondActivity.class));
                dialog_creditos.show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View view) {
        manejoEventosTC(view.getId());
    }
}
