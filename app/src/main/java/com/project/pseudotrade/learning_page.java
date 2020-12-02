package com.project.pseudotrade;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.project.pseudotrade.R;

import java.util.ArrayList;

public class learning_page extends AppCompatActivity {
    ListView listView;
    ImageButton SettingsPage;
    ImageButton StocksPage;
    ImageButton LearningPage;
    ImageButton MainPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning_page);
        listView = findViewById(R.id.listView);

        SettingsPage = findViewById(R.id.settingsButton);
        StocksPage = findViewById(R.id.stockButton);
        LearningPage = findViewById(R.id.Learning);
        MainPage = findViewById(R.id.homeButton);

        StocksPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent stocksIntent = new Intent(learning_page.this, StocksActivity.class);
                startActivity(stocksIntent);
            }
        });

        SettingsPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(learning_page.this, SettingsActivity.class);
                startActivityForResult(settingsIntent, 10);
            }
        });

        LearningPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(learning_page.this, learning_page.class);
                startActivity(intent);


            }
        });


        MainPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(learning_page.this, MainActivity.class);
                startActivityForResult(settingsIntent, 10);
            }
        });

        ArrayList<Webs> arrayList = new ArrayList<>();
        arrayList.add(new Webs(R.drawable.investopedia,"Investopedia","A Beginner's Guide to Stock Investing"));
        arrayList.add(new Webs(R.drawable.investopedia,"Investopedia","Learn How to Trade the Market in 5 Steps"));
        arrayList.add(new Webs(R.drawable.wealthsimple,"WealthSimple","How to Buy Stocks"));
        arrayList.add(new Webs(R.drawable.intelligent,"Intelligent","Top 10 Pieces of Investments Advice from Warren Buffet"));
        arrayList.add(new Webs(R.drawable.youtube,"YouTube","Professional Stock Trading Course Lesson"));
        arrayList.add(new Webs(R.drawable.youtube,"YouTube","The Stock Market"));
        arrayList.add(new Webs(R.drawable.forbes,"Forbes","10 Things You Absolutely Need to Know About Stocks"));
        arrayList.add(new Webs(R.drawable.usnews,"US News","How to Pick Stocks"));
        arrayList.add(new Webs(R.drawable.udemy,"Udemy","Stock Trading Courses"));
        arrayList.add(new Webs(R.drawable.wealthsimple,"WealthSimple","The Stock Market for Beginners"));
        arrayList.add(new Webs(R.drawable.youtube,"YouTube","Warren Buffett: How To Invest for Beginners"));
        arrayList.add(new Webs(R.drawable.youtube,"YouTube","Cryptocurrency Explained"));
        arrayList.add(new Webs(R.drawable.youtube,"YouTube","How to Buy Cryptocurrency for Beginners"));
        arrayList.add(new Webs(R.drawable.investopedia,"Investopdia","10 Day Trading Strategies for Beginners"));
        arrayList.add(new Webs(R.drawable.cmc,"CMC Market","Top Tips From Successful Traders"));

        String[] urls ={"https://www.investopedia.com/articles/basics/06/invest1000.asp",
                "https://www.investopedia.com/learn-how-to-trade-the-market-in-5-steps-4692230",
                "https://www.wealthsimple.com/en-ca/learn/how-to-buy-stocks",
                "https://www.simplysafedividends.com/intelligent-income/posts/37-top-10-pieces-of-investment-advice-from-warren-buffett",
                "https://www.youtube.com/watch?v=slBxM4J3BEA",
                "https://www.youtube.com/watch?v=ZCFkWDdmXG8",
                "https://www.forbes.com/sites/steveschaefer/2016/01/05/10-things-you-absolutely-need-to-know-about-stocks/",
                "https://money.usnews.com/investing/investing-101/slideshows/how-to-pick-stocks-things-all-beginner-investors-should-know",
                "https://www.udemy.com/topic/stock-trading/",
                "https://www.wealthsimple.com/en-ca/class/stock-market-beginners",
                "https://www.youtube.com/watch?v=yRr0_gJ-3mI",
                "https://www.youtube.com/watch?v=8NgVGnX4KOw",
                "https://www.youtube.com/watch?v=sEtj34VMClU",
                "https://www.investopedia.com/articles/trading/06/daytradingretail.asp",
                "https://www.cmcmarkets.com/en-ca/trading-guides/top-trading-tips-from-successful-traders"
        };


        webAdapter linkadapt = new webAdapter(this,R.layout.activity_web_adapter,arrayList, urls);
        listView.setAdapter(linkadapt);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 10){
            Log.i("LoggingOutFromLearning","Finished");
            Intent loginIntent = new Intent(learning_page.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }
    }
}
