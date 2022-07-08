package com.andyra.tugas7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mdrawer;
    private Toolbar mtoolbar;
    private NavigationView mnavigation;
    public SharedPreferences msharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        mdrawer = findViewById(R.id.layout_drawer);
        mtoolbar = findViewById(R.id.menutoolbar);
        mnavigation = findViewById(R.id.navigation);

        setSupportActionBar(mtoolbar);
        mnavigation.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(
                this, mdrawer, mtoolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mdrawer.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        tampil_awal();
    }

    @Override
    public void onBackPressed() {
        if(mdrawer.isDrawerOpen(GravityCompat.START)){
            mdrawer.closeDrawer(GravityCompat.START);
        }
        else {
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Bundle mbundle = new Bundle();
        Edit_Data editData = new Edit_Data();
        int pilih_menu = item.getItemId();

        if(pilih_menu == R.id.nav_list){
            getSupportFragmentManager().beginTransaction().replace(R.id.frame,
                    new Tampil_Data()).commit();
        }
        else if(pilih_menu == R.id.nav_add){
            getSupportFragmentManager().beginTransaction().replace(R.id.frame,
                    new Tambah_Data()).commit();
        }
        else if(pilih_menu == R.id.nav_edit){
            mbundle.putString("opsi", "edit");
            editData.setArguments(mbundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.frame,
                    editData).commit();
        }
        else if(pilih_menu == R.id.nav_del){
            mbundle.putString("opsi", "delete");
            editData.setArguments(mbundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.frame,
                    editData).commit();
        }
        else if (pilih_menu == R.id.nav_out){
            msharedpreferences = getSharedPreferences
                    (MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor meditor = msharedpreferences.edit();
            meditor.clear();
            meditor.apply();
            moveTaskToBack(true);
            Dashboard.this.finish();
            Intent out = new Intent(Dashboard.this, MainActivity.class);
            startActivity(out);
        }

        else if(pilih_menu == R.id.nav_exit){
            moveTaskToBack(true);
        }
        mdrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void tampil_awal() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame,
                new Tampil_Data()).commit();
    }
}