package com.example.g2int101experience;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.g2int101experience.databinding.ActivityPrubButtonNavigationBinding;

public class Home extends AppCompatActivity {

    private ActivityPrubButtonNavigationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPrubButtonNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.hide();
        }

        BottomNavigationView navView = findViewById(R.id.nav_view);

        // Configurar que fragmentos son de nivel superior (los botones del navigate).
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_map,
                R.id.navigation_home,
                R.id.navigation_perfil)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_prub_button_navigation);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        // Para Agregar más secciones
        // 1º Agregar un nuevo fragmento en res
        // 2º Agrego la nueva opción en button navigation menu
        // 3º Agregar el fragmento aqui en AppBarConfiguration

    }

    // Permite ir al fragmento anterior
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_prub_button_navigation);
        return navController.navigateUp() || super.onSupportNavigateUp();
    }
}

