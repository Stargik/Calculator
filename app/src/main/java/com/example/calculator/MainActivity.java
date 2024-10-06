package com.example.calculator;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.calculator.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private NavController navController;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container_view);
        navController = navHostFragment.getNavController();

        binding.toolbar.setNavigationOnClickListener(view -> binding.drawerLayout.openDrawer(GravityCompat.START));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int currentFragment = navController.getCurrentDestination().getId();
        int itemId = item.getItemId();

        if (itemId == R.id.nav_info && currentFragment != R.id.infoFragment) {
            Log.i("MainActivity", "NAVIGATE TO INFO FRAGMENT!");
            navController.navigate(R.id.action_mainFragment_to_infoFragment);
        } else if (itemId == R.id.nav_home && currentFragment != R.id.mainFragment){
            Log.i("MainActivity", "NAVIGATE BACK TO HOME FRAGMENT!");
            navController.popBackStack();
        } else {
            Log.wtf("MainActivity", "???");
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}