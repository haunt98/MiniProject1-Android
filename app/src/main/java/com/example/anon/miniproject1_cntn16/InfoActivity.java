package com.example.anon.miniproject1_cntn16;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

public class InfoActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView info_name;
    ImageView info_image;
    TextView info_address;
    MyDatabaseHelper myDatabaseHelper;
    FloatingActionButton fap;
    Integer id;
    MyPlace myPlace;
    Button info_call;
    Button info_email;
    Button info_web;
    Button info_locate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        // set id
        toolbar = findViewById(R.id.toolbar_info);
        info_name = findViewById(R.id.info_name);
        info_image = findViewById(R.id.info_image);
        info_address = findViewById(R.id.info_address);
        fap = findViewById(R.id.fab);
        info_call = findViewById(R.id.info_call);
        info_email = findViewById(R.id.info_email);
        info_web = findViewById(R.id.info_web);
        info_locate = findViewById(R.id.info_locate);

        setUpToolbar();

        setUpDatabase();

        setUpFromIntent();

        setUpInfo();

        setUpFavBtn();

        setUpCallBtn();

        setUpEmailBtn();

        setUpWebBtn();

        setUpLocateBtn();
    }

    private void setUpLocateBtn() {
        info_locate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myPlace == null) {
                    return;
                }

                if (myPlace.getLat() == null || myPlace.getLng() == null) {
                    Toast.makeText(getApplicationContext(), "There is not gps coordinate",
                            Toast.LENGTH_SHORT)
                            .show();
                    return;
                }

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

    }

    private void setUpWebBtn() {
        info_web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myPlace == null) {
                    return;
                }

                if (myPlace.getWebsite() == null || myPlace.getWebsite().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "There is no website link",
                            Toast.LENGTH_SHORT)
                            .show();
                    return;
                }

                Uri website = Uri.parse(myPlace.getWebsite());
                Intent intent = new Intent(Intent.ACTION_VIEW, website);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "There is no browser app",
                            Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }

    private void setUpEmailBtn() {
        info_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myPlace == null) {
                    return;
                }

                if (myPlace.getEmail() == null || myPlace.getEmail().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "There is no email",
                            Toast.LENGTH_SHORT)
                            .show();
                    return;
                }

                // intent to send email
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("*/*");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{myPlace.getEmail()});
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "There is no email app",
                            Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }

    private void setUpCallBtn() {
        info_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myPlace == null) {
                    return;
                }

                if (myPlace.getPhone() == null || myPlace.getPhone().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "There is no phone number",
                            Toast.LENGTH_SHORT)
                            .show();
                    return;
                }

                // get correct phone number
                String correctPhone = PhoneNumberUtils.normalizeNumber(myPlace.getPhone());

                // open phone app and pass phone number
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel: " + correctPhone));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "There is no phone app",
                            Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }

    private void setUpInfo() {
        // neu du lieu khong co ai
        if (myPlace == null) {
            return;
        }

        // hien thi info
        info_name.setText(myPlace.getName());
        info_address.setText(myPlace.getAddress());
        info_image.setImageBitmap(myPlace.getImage());

        // hien thi fav
        updateFavBtn(myPlace.getFav());
    }

    private void setUpFavBtn() {
        fap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer new_fav_val = myDatabaseHelper.changFavByID(id);
                if (new_fav_val != null) {
                    updateFavBtn(new_fav_val);
                }
            }
        });

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

    private void setUpFromIntent() {
        Intent intent = getIntent();

        // nhan id
        id = intent.getIntExtra("ID", 0);

        // lay my place tu database
        myPlace = myDatabaseHelper.getMyPlaceByID(id);
    }

    private void updateFavBtn(Integer fav) {
        if (fav == 1) {
            fap.setImageDrawable(getResources().getDrawable(
                    R.drawable.ic_fav, getTheme()
            ));
        } else {
            fap.setImageDrawable(getResources().getDrawable(
                    R.drawable.ic_not_fav, getTheme()
            ));
        }
    }

    @Override
    protected void onDestroy() {
        myDatabaseHelper.close();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // dirty hack to return previous activity
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
