package com.example.bibliotecaapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.bibliotecaapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        actualizarHeader();

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_inicio,
                R.id.nav_libros,
                R.id.nav_logout,
                R.id.nav_pedidos,
                R.id.busquedaAvanzadaFragment,
                R.id.nav_reportes
        )
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    private void actualizarHeader() {

        NavigationView navigationView = binding.navView;
        View headerView = navigationView.getHeaderView(0);

        TextView tvNombre = headerView.findViewById(R.id.tvNombreUsuario);
        TextView tvEmail = headerView.findViewById(R.id.tvEmailUsuario);

        SharedPreferences sp = getSharedPreferences("usuario", MODE_PRIVATE);

        String nombre = sp.getString("nombre", "Invitado");
        String email = sp.getString("email", "Sin email");

        tvNombre.setText(nombre);
        tvEmail.setText(email);
    }

    @Override
    protected void onResume() {
        super.onResume();
        actualizarHeader();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
