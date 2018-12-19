package com.example.anon.miniproject1_cntn16;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private GoogleMap map;
    MyDatabaseHelper myDatabaseHelper;
    MyPlace myPlaceFromContent;
    private static final Float ZOOM_ASPECT = 12f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set id
        drawerLayout = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolbar_main);
        navigationView = findViewById(R.id.nav);

        setUpToolbar();

        setUpNavClick();

        setUpDatabase();

        setUpFromIntent();

        setUpMap();
    }

    private void setUpFromIntent() {
        // lay intent tu activity info gui qua
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            // lay my place tu csdl
            Integer id = intent.getIntExtra("id", 0);
            myPlaceFromContent = myDatabaseHelper.getMyPlaceByID(id);
        } else {
            myPlaceFromContent = null;
        }
    }

    private void setUpDatabase() {
        myDatabaseHelper = MyDatabaseHelper.getDatabaseInstance(this);
    }

    private void setUpMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    map = googleMap;

                    if (myPlaceFromContent != null) {
                        LatLng latLng = new LatLng(
                                myPlaceFromContent.getLat(),
                                myPlaceFromContent.getLng()
                        );
                        map.addMarker(new MarkerOptions().position(latLng)
                                .title(myPlaceFromContent.getName()));
                        // move camera with zoom aspect
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_ASPECT));
                    } else {
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                new LatLng(10.762913, 106.679983),
                                ZOOM_ASPECT
                        ));
                    }

                    // set zoom
                    map.setMinZoomPreference(6.0f);
                    map.setMaxZoomPreference(18.0f);
                    map.getUiSettings().setZoomControlsEnabled(true);
                }
            });
        }

    }

    private void setUpToolbar() {
        // use my toolbar
        setSupportActionBar(toolbar);

        // set drawer button for toolbar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white);
            actionBar.setTitle("Search places");
        }
    }

    private void setUpNavClick() {
        navigationView.setNavigationItemSelectedListener(new NavigationView
                .OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);

                // close drawer when item is tapped
                drawerLayout.closeDrawers();

                switch (menuItem.getItemId()) {
                    case R.id.nav_item_exit:
                        finish();
                        System.exit(0);
                        break;
                    case R.id.nav_item_fav:
                        Intent intent = new Intent(getApplicationContext(), FavActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }

                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // add search icon to bar
        getMenuInflater().inflate(R.menu.bar_item, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.search_icon:
                // goi search activity
                Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
