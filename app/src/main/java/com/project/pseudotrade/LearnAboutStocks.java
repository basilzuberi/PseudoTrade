package com.project.pseudotrade;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.project.pseudotrade.LinkListsAdapter;
import com.project.pseudotrade.LinkofLists;
import com.project.pseudotrade.R;

import java.util.ArrayList;


public class LearnAboutStocks extends AppCompatActivity {
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.learn_about_stocks);
        listView = findViewById(R.id.listView);


        ArrayList<LinkofLists> arrayList = new ArrayList<>();
        arrayList.add(new LinkofLists(R.drawable.investopedia,"Investopedia","A Beginner's Guide to Stock Investing"));
        arrayList.add(new LinkofLists(R.drawable.investopedia,"Investopedia","Learn How to Trade the Market in 5 Steps"));
        arrayList.add(new LinkofLists(R.drawable.wealthsimple,"WealthSimple","How to Buy Stocks"));
        arrayList.add(new LinkofLists(R.drawable.intelligent,"Intelligent","Top 10 Pieces of Investments Advice from Warren Buffet"));
        arrayList.add(new LinkofLists(R.drawable.youtube,"YouTube","Professional Stock Trading Course Lesson"));
        arrayList.add(new LinkofLists(R.drawable.youtube,"YouTube","The Stock Market"));
        arrayList.add(new LinkofLists(R.drawable.forbes,"Forbes","10 Things You Absolutely Need to Know About Stocks"));
        arrayList.add(new LinkofLists(R.drawable.usnews,"US News","How to Pick Stocks"));
        arrayList.add(new LinkofLists(R.drawable.udemy,"Udemy","Stock Trading Courses"));
        arrayList.add(new LinkofLists(R.drawable.wealthsimple,"WealthSimple","The Stock Market for Beginnerss"));
        arrayList.add(new LinkofLists(R.drawable.youtube,"YouTube","Warren Buffett: How To Invest for Beginners"));
        arrayList.add(new LinkofLists(R.drawable.youtube,"YouTube","Cryptocurrency Explained"));
        arrayList.add(new LinkofLists(R.drawable.youtube,"YouTube","How to Buy Cryptocurrency for Beginners"));
        arrayList.add(new LinkofLists(R.drawable.investopedia,"Investopdia","10 Day Trading Strategies for Beginners"));
        arrayList.add(new LinkofLists(R.drawable.cmc,"CMC Market","Top Tips From Successful Traders"));

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


        LinkListsAdapter linklistAdapter = new LinkListsAdapter(this,R.layout.list_row,arrayList, urls);
        listView.setAdapter(linklistAdapter);

    }

}
