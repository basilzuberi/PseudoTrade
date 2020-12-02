//package com.example.listview_project;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.os.Bundle;
//import android.widget.ListView;
//
//import com.project.pseudotrade.LinkListsAdapter;
//import com.project.pseudotrade.LinkofLists;
//import com.project.pseudotrade.R;
//
//import java.util.ArrayList;
//
//
//public class LearnAboutStocks extends AppCompatActivity {
//    ListView listView;
//    private Object LinkListsAdapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        listView = findViewById(R.id.listView);
//
//
//        ArrayList<LinkofLists> arrayList = new ArrayList<>();
////        arrayList.add(new LinkofLists(R.drawable.image2,"Investopedia","Learn about stocks"));
////        arrayList.add(new LinkofLists(R.drawable.image2,"Investopedia","Learn about bonds"));
////        arrayList.add(new LinkofLists(R.drawable.image3,"WealthSimple","What are options"));
////        arrayList.add(new LinkofLists(R.drawable.image4,"Intelligent","What you need to do"));
////        arrayList.add(new LinkofLists(R.drawable.image1,"YouTube","Learn about stocks"));
////        arrayList.add(new LinkofLists(R.drawable.image2,"YouTube","Learn about bonds"));
////        arrayList.add(new LinkofLists(R.drawable.image3,"Forbes","What are options"));
////        arrayList.add(new LinkofLists(R.drawable.image4,"US News","What you need to do"));
////        arrayList.add(new LinkofLists(R.drawable.image1,"Udemy","Learn about stocks"));
////        arrayList.add(new LinkofLists(R.drawable.image2,"WealthSimple","Learn about bonds"));
////        arrayList.add(new LinkofLists(R.drawable.image3,"YouTube","What are options"));
////        arrayList.add(new LinkofLists(R.drawable.image4,"YouTube","What you need to do"));
////        arrayList.add(new LinkofLists(R.drawable.image1,"YouTube","Learn about stocks"));
////        arrayList.add(new LinkofLists(R.drawable.image2,"Investopdia","Learn about bonds"));
////        arrayList.add(new LinkofLists(R.drawable.image3,"CMC Market","Top Tips From"));
//
//        String[] urls ={"https://www.investopedia.com/articles/basics/06/invest1000.asp",
//                "https://www.investopedia.com/learn-how-to-trade-the-market-in-5-steps-4692230",
//                "https://www.wealthsimple.com/en-ca/learn/how-to-buy-stocks",
//                "https://www.simplysafedividends.com/intelligent-income/posts/37-top-10-pieces-of-investment-advice-from-warren-buffett",
//                "https://www.youtube.com/watch?v=slBxM4J3BEA",
//                "https://www.youtube.com/watch?v=ZCFkWDdmXG8",
//                "https://www.forbes.com/sites/steveschaefer/2016/01/05/10-things-you-absolutely-need-to-know-about-stocks/",
//                "https://money.usnews.com/investing/investing-101/slideshows/how-to-pick-stocks-things-all-beginner-investors-should-know",
//                "https://www.udemy.com/topic/stock-trading/",
//                "https://www.wealthsimple.com/en-ca/class/stock-market-beginners",
//                "https://www.youtube.com/watch?v=yRr0_gJ-3mI",
//                "https://www.youtube.com/watch?v=8NgVGnX4KOw",
//                "https://www.youtube.com/watch?v=sEtj34VMClU",
//                "https://www.investopedia.com/articles/trading/06/daytradingretail.asp",
//                "https://www.cmcmarkets.com/en-ca/trading-guides/top-trading-tips-from-successful-traders"
//};
//
//
//        LinkListsAdapter linklistAdapter = new LinkListsAdapter(this,R.layout.list_row,arrayList, urls);
//        listView.setAdapter(linklistAdapter);
//
//    }
//
//}
