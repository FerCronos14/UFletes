package com.example.ufletes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Registro_Main extends AppCompatActivity implements View.OnClickListener{

    private Button myRegistroFleteroMain;
    private Button myRegistroClienteMain;

    public void manejoEventosRegistroMain (int opc) {
        switch (opc) {
            case R.id.btnRegistroFletero_Main:
                //Intent intentRegistro_Fletero = new Intent(this, Registro_Fletero.class);
                //Intent intent = new Intent(Registro_Main.this, Registro_Fletero.class);
                startActivity(new Intent(Registro_Main.this, fragment_ConfirmarPedido_Fletero.class));
                break;
            case R.id.btnRegistoCliente_Main:
                //Intent intentRegistro_User = new Intent(this, Registro_User.class);
                //intent = new Intent(Registro_Main.this, Registro_User.class);
                startActivity(new Intent(Registro_Main.this, Registro_User.class));
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro__main);

        myRegistroClienteMain = findViewById(R.id.btnRegistroFletero_Main);
        myRegistroClienteMain.setOnClickListener(this);

        myRegistroFleteroMain =  findViewById(R.id.btnRegistoCliente_Main);
        myRegistroFleteroMain.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        manejoEventosRegistroMain(view.getId());
    }
}
