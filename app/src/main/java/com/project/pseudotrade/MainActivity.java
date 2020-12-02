package com.project.pseudotrade;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    ImageButton SettingsPage;
    ImageButton StocksPage;
    ImageButton LearningPage;
    ImageButton MainPage;
    ImageButton Help;
    FirebaseDatabase mDatabase;
    FirebaseAuth mAuth;
    DatabaseReference mDatabaseReference;
    SharedPreferences mSharedPreference;
    HashMap<String, Integer> holdings;
    String userID;
    TextView mainGreeting;
    TextView mainAccBal;
    ArrayList<String> stockSymbols;
    String url ="https://seekingalpha.com/market-news";
    WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SettingsPage = findViewById(R.id.settingsButton);
        StocksPage = findViewById(R.id.stockButton);
        LearningPage = findViewById(R.id.settingsButton);
        MainPage = findViewById(R.id.stockButton);
        //HelpPage = findViewById(R.id.stockButton);
        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance(); //database Refs
        mDatabaseReference = mDatabase.getReference("Users");

        mainGreeting = findViewById(R.id.main_greeting);
        mainAccBal = findViewById(R.id.main_acc_bal);

        holdings = new HashMap<>();
        stockSymbols = new ArrayList<>();

        Bundle userDataBundle = getIntent().getExtras();
        if (userDataBundle != null)
            userID = userDataBundle.getString("userID");

        mSharedPreference = getSharedPreferences("LoginActivityShared", Context.MODE_PRIVATE); //open SharedPreference for read

        updateBalance();

        //adding webView for stock news!
        mWebView =findViewById(R.id.wwStockNews);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {


            @Override
            public void onLoadResource(WebView view, String url) {
                //dont show us website header
                mWebView.loadUrl("javascript:(function() { " +
                        "var head = document.getElementsByTagName('header')[0];"
                        + "head.parentNode.removeChild(head);" +
                        "})()");
            }
        });

        mWebView.loadUrl(url);


        StocksPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle userDataBundle = new Bundle();
                userDataBundle.putString("userID", userID);
                Intent stocksIntent = new Intent(MainActivity.this, StocksActivity.class);
                stocksIntent.putExtras(userDataBundle);
                startActivity(stocksIntent);
            }
        });

        SettingsPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivityForResult(settingsIntent, 10);
            }
        });

//        LearningPage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent settingsIntent = new Intent(MainActivity.this,LearnAboutStocks.class);
//                startActivityForResult(settingsIntent, 10);
//            }
//        });
//
//       HelpPage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent settingsIntent = new Intent(MainActivity.this,LearnAboutStocks.class);
//                startActivityForResult(settingsIntent, 10);
//            }
//        });

        MainPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(MainActivity.this, MainActivity.class);
                startActivityForResult(settingsIntent, 10);
            }
        });


    }
    private void updateBalance() {
        DatabaseReference balanceRef = mDatabaseReference.child(userID).child("cashBalance");
        balanceRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Double cashBalance = snapshot.getValue(Double.class);
//                cashBalanceTextView.setText(String.format("%s $%.2f", R.string.cash_balance, cashBalance));
                FirebaseUser currentUser = mAuth.getCurrentUser();


                //mSharedPreference.getString("LoginEmail","")

//                mainGreeting.setText(String.format("Hello, \n%s",);
                mainGreeting.setText(String.format("Hello, \n%s", mSharedPreference.getString("LoginEmail","")));
                Log.i("MainActivity",String.valueOf(cashBalance));
                mainAccBal.setText(String.format("$%.2f",(cashBalance)));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 10){
            Log.i("BLADEEDBUUBLAH","Finished");
            Intent settingsIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(settingsIntent);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateBalance();
    }

    @Override
    public void onBackPressed() {

        //check if WebView can go back, if not use default functionality
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}