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
                    Toast.makeText(this, R.string.debe_aceptar_terminos, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnCreditos:
                final Dialog dialog_creditos= new Dialog(this, R.style.CustomAlertDialog);
                dialog_creditos.setContentView(R.layout.cuadro_dialogo_creditos);
                dialog_creditos.setTitle(R.string.creditos);
                dialog_creditos.setCancelable(true);
                //dialog_creditos.getWindow().setLayout(600, 400);
                Button cerrarDialog = dialog_creditos.findViewById(R.id.btnCerrar_Creditos_Dialog);
                cerrarDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog_creditos.dismiss();
                    }
                });
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
