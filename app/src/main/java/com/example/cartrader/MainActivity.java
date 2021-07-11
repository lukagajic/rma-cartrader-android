package com.example.cartrader;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.cartrader.state.AppState;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
                                                               SearchFragment.OnFragmentInteractionListener,
                                                               LoginFragment.OnFragmentInteractionListener,
                                                               RegisterFragment.OnFragmentInteractionListener,
                                                               AddCarFragment.OnFragmentInteractionListener {

    private NavigationView navigationView;
    private Menu navMenu;
    private MenuItem navLogin;
    private MenuItem navRegister;
    private MenuItem navLogout;

    private final static String SHARED_PREFERENCES_PREFIX = "CarTraderSharedPreferences";
    private final static String SHARED_PREFERENCES_TOKEN = "token";
    private final static String SHARED_PREFERENCES_EMAIL = "email";
    private final static String SHARED_PREFERENCES_USER_ID = "userId";

    @Override
    public void onSearchButtonClick(String json) {
        getSupportFragmentManager().beginTransaction()
                                   .replace(R.id.fragment_container, SearchResultFragment.newInstance(json))
                                   .addToBackStack(null)
                                   .commit();
    }

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadAppState();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        navigationView = findViewById(R.id.nav_view);

        navMenu = navigationView.getMenu();
        navLogin = navMenu.findItem(R.id.nav_login);
        navRegister = navMenu.findItem(R.id.nav_register);
        navLogout = navMenu.findItem(R.id.nav_logout);

        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        toggle.syncState();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CarsListFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_all_cars);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void replaceMenuItemsOnLogin() {
        navLogin.setVisible(false);
        navRegister.setVisible(false);
        navLogout.setVisible(true);
    }

    private void replaceMenuItemsOnLogout() {
        navLogout.setVisible(false);;
        navLogin.setVisible(true);
        navRegister.setVisible(true);
    }


    private void saveAppState() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_PREFIX, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (AppState.token != null) {
            editor.putString(SHARED_PREFERENCES_TOKEN, AppState.token);
        }

        if (AppState.email != null) {
            editor.putString(SHARED_PREFERENCES_EMAIL, AppState.email);
        }

        if (AppState.userId != 0) {
            editor.putInt(SHARED_PREFERENCES_USER_ID, AppState.userId);
        }

        editor.commit();
    }

    private void loadAppState() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_PREFIX, 0);
        AppState.token = sharedPreferences.getString(SHARED_PREFERENCES_TOKEN, null);
        AppState.email = sharedPreferences.getString(SHARED_PREFERENCES_EMAIL, null);
        AppState.userId = sharedPreferences.getInt(SHARED_PREFERENCES_USER_ID, 0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveAppState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_all_cars:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CarsListFragment()).addToBackStack(null).commit();
                break;
            case R.id.nav_search:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SearchFragment()).addToBackStack(null).commit();
                break;
            case R.id.nav_add_car:
                if (AppState.token == null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LoginFragment()).addToBackStack(null).commit();
                } else {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddCarFragment()).addToBackStack(null).commit();
                }
                break;
            case R.id.nav_favorites:
                if (AppState.token == null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LoginFragment()).addToBackStack(null).commit();
                } else {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FavoritesFragment()).addToBackStack(null).commit();
                }
                break;
            case R.id.nav_profile:
                if (AppState.token == null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LoginFragment()).addToBackStack(null).commit();
                } else {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new UserProfileFragment()).addToBackStack(null).commit();
                }
                break;
            case R.id.nav_my_cars:
                if (AppState.token == null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LoginFragment()).addToBackStack(null).commit();
                } else {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MyCarsFragment()).addToBackStack(null).commit();
                }
                break;
            case R.id.nav_author:
                Toast.makeText(this, R.string.author_info, Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_login:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LoginFragment()).addToBackStack(null).commit();
                break;
            case R.id.nav_register:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RegisterFragment()).addToBackStack(null).commit();
                break;
            case R.id.nav_logout:
                handleLogout();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void handleLogout() {
        AppState.token = null;
        AppState.userId = 0;
        AppState.email = null;
        Toast.makeText(this, "Odjavili ste se iz aplikacije!", Toast.LENGTH_LONG).show();
        invalidateOptionsMenu();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CarsListFragment()).addToBackStack(null).commit();
    }

    @Override
    public void onLoginSuccess() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CarsListFragment()).addToBackStack(null).commit();
        invalidateOptionsMenu();
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(AppState.token == null) {
            replaceMenuItemsOnLogout();
        } else {
            replaceMenuItemsOnLogin();
        }

        super.onPrepareOptionsMenu(menu);
        return true;
    }


    @Override
    public void onRegisterSuccess() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LoginFragment()).addToBackStack(null).commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onAddCarSuccess() {
        Toast.makeText(this, "Vozilo uspešno dodato!", Toast.LENGTH_SHORT).show();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CarsListFragment()).addToBackStack(null).commit();
    }
}