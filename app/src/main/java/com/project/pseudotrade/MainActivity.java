package com.project.pseudotrade;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    Button mBtnSettingsPage;
    Button mBtnStocksPage;
    Button mBtnMainPage;
    ImageButton Investopedia;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnSettingsPage = findViewById(R.id.Settings_page);
        Investopedia = (ImageButton)findViewById(R.id.question_mark);

       // mBtnStocksPage = findViewById(R.id.Stock_page);

//        TabLayout tabLayout = findViewById(R.id.tabLayout);
//        TabLayout tabMain = findViewById(R.id.Main_page);
//        TabLayout tabStock = findViewById(R.id.Stocks_page);
//        TabLayout tabSetting = findViewById(R.id.Settings_page);
//        ViewPager viewPager = findViewById(R.id.viewPager);

        mBtnStocksPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent stocksIntent = new Intent(MainActivity.this, StocksActivity.class);
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

        Investopedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Ask Investopedia!", Toast.LENGTH_LONG).show();
                Uri uri = Uri.parse("https://www.investopedia.com/search?q=stocks");
                Intent intentz = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intentz);
            }
        });



    }


}