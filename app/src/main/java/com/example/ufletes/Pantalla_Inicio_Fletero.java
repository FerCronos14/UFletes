package com.example.ufletes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.ufletes.uifletero.Inicio_fletero_fragment;
import com.example.ufletes.uifletero.Pedidos_Fleteros_fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Pantalla_Inicio_Fletero extends AppCompatActivity {

    BottomNavigationView mBottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla__inicio__fletero);

        getSupportActionBar().hide();

        mBottomNav = findViewById(R.id.btnNav_inicio_fletero);
        showSelectedFragment(new Inicio_fletero_fragment());

        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.menu_home_fletero)
                {
                    showSelectedFragment(new Inicio_fletero_fragment());
                }
                if (menuItem.getItemId() == R.id.menu_solcitudes_fletero){
                    showSelectedFragment(new SolicitudesFletesFragment());
                }
                if (menuItem.getItemId() == R.id.menu_orden_fletero) {
                    showSelectedFragment(new SoliTransitoFragment());
                }
                return true;
            }
        });



    }

    private void showSelectedFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frgInicioFletero, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }
}
