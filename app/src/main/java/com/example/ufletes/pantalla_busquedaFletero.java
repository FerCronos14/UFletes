package com.example.ufletes;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.ufletes.dummy.DummyContent;
import com.example.ufletes.ui.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class pantalla_busquedaFletero extends AppCompatActivity {

    BottomNavigationView mBottomNav;
    FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_busqueda_fletero);

        getSupportActionBar().hide();

        mBottomNav = findViewById(R.id.nav_view_inicio_cliente);
        showSelectedFragment(new HomeFragment());

        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navigation_home_Cliente)
                {
                    showSelectedFragment(new HomeFragment());
                }
                if (item.getItemId() == R.id.navigation_BusqFletero){
                    showSelectedFragment(new BusquedaListaFleterosFragment());
                }
                return false;
            }
        });

    }
    private void showSelectedFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.containerInicioCliente, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }
}
