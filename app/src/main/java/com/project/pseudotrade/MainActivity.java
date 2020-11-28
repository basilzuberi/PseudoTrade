package com.project.pseudotrade;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    Button mBtnSettingsPage;
    Button mBtnStocksPage;
    Button mBtnMainPage;
    FirebaseDatabase mDatabase;
    DatabaseReference mDatabaseReference;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnSettingsPage = findViewById(R.id.Settings_page);
        mBtnStocksPage = findViewById(R.id.Stock_page);

        mDatabase = FirebaseDatabase.getInstance(); //database Ref
        mDatabaseReference = mDatabase.getReference("Users");
        Bundle userDataBundle = getIntent().getExtras();
        if (userDataBundle != null)
            userID = userDataBundle.getString("userID");


//        TabLayout tabLayout = findViewById(R.id.tabLayout);
//        TabLayout tabMain = findViewById(R.id.Main_page);
//        TabLayout tabStock = findViewById(R.id.Stocks_page);
//        TabLayout tabSetting = findViewById(R.id.Settings_page);
//        ViewPager viewPager = findViewById(R.id.viewPager);

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
                startActivity(settingsIntent);
            }
        });



    }


}