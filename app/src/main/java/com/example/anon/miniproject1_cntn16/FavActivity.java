package com.example.anon.miniproject1_cntn16;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

public class FavActivity extends AppCompatActivity {
    Toolbar toolbar;
    MyDatabaseHelper myDatabaseHelper;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);

        // set id
        toolbar = findViewById(R.id.toolbar_fav);
        recyclerView = findViewById(R.id.rv_fav);

        setUpToolbar();

        setUpDatabase();

        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );

        // get a list favorite
        ArrayList<ShortMyPlace> listFavPlace = myDatabaseHelper.getListFavPlace();
        SearchAdapter searchAdapter = new SearchAdapter(listFavPlace);
        recyclerView.setAdapter(searchAdapter);
    }

    private void setUpDatabase() {
        myDatabaseHelper = MyDatabaseHelper.getDatabaseInstance(this);
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_white);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }
}
