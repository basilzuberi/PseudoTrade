package com.project.pseudotrade;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    ImageButton mBtnSettingsPage;
    ImageButton mBtnStocksPage;
    Button mBtnMainPage;
    FirebaseDatabase mDatabase;
    DatabaseReference mDatabaseReference;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnSettingsPage = findViewById(R.id.settingsButton);
        mBtnStocksPage = findViewById(R.id.stockButton);

        mDatabase = FirebaseDatabase.getInstance(); //database Ref
        mDatabaseReference = mDatabase.getReference("Users");
        Bundle userDataBundle = getIntent().getExtras();
        if (userDataBundle != null)
            userID = userDataBundle.getString("userID");


//        TabLayout tabLayout = findViewById(R.id.tabLayout);
//        TabLayout tabMain = findViewById(R.id.Main_page);
//        TabLayout tabStock = findViewById(R.id.Stocks_page);
//        TabLayout tabSetting = findViewById(R.id.Settings_page);
//        ViewPager viewPag q   er = findViewById(R.id.viewPager);

        mBtnStocksPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle userDataBundle = new Bundle();
                userDataBundle.putString("userID", userID);
                Intent stocksIntent = new Intent(MainActivity.this, StocksActivity.class);
                stocksIntent.putExtras(userDataBundle);
                startActivity(stocksIntent);
            }
        });

        mBtnSettingsPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivityForResult(settingsIntent, 10);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 10){
            Log.i("BLADEEDBUUBLAH","Finished");
            finish();
        }
    }
}