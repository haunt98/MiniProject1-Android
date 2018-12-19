package com.example.anon.miniproject1_cntn16;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText editText;
    ImageButton imageButton;
    RecyclerView recyclerView;
    MyDatabaseHelper myDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // set id
        toolbar = findViewById(R.id.toolbar_search);
        editText = findViewById(R.id.text_search);
        imageButton = findViewById(R.id.btn_search);
        recyclerView = findViewById(R.id.rv_search);

        setUpToolbar();

        setUpDatabase();

        setUpSearchButton();

        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );
    }

    private void setUpSearchButton() {
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // lay text nguoi dung nhap vao roi tim trong database
                String string = editText.getText().toString();
                // lay list short my place co duoc tu name
                ArrayList<ShortMyPlace> listShortMyPlace =
                        myDatabaseHelper.getListShortMyPlaceByName(string);
                // hien thi tren recylcer view
                passDataToRecyclerView(listShortMyPlace);
            }
        });
    }

    private void passDataToRecyclerView(ArrayList<ShortMyPlace> listShortMyPlace) {
        SearchAdapter searchAdapter = new SearchAdapter(listShortMyPlace);
        recyclerView.setAdapter(searchAdapter);
    }

    private void setUpDatabase() {
        myDatabaseHelper = MyDatabaseHelper.getDatabaseInstance(this);
    }

    private void setUpToolbar() {
        // use my toolbar
        setSupportActionBar(toolbar);

        // set drawer button for toolbar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_white);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    protected void onDestroy() {
        myDatabaseHelper.close();
        super.onDestroy();
    }
}
