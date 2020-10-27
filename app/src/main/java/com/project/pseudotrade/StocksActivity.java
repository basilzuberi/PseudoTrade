package com.project.pseudotrade;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class StocksActivity extends AppCompatActivity {

    SearchView stockSearchBar;
    ListView stocksListView;
    ArrayList<String> stockList;
    StockListAdapter stockListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stocks);
        stockSearchBar = findViewById(R.id.stock_search_bar);
        stocksListView = findViewById(R.id.stock_list);
        stockList = new ArrayList<>();
        stockListAdapter = new StockListAdapter(this);
        stocksListView.setAdapter(stockListAdapter);
        populateStockList();
    }

    private void populateStockList() {
        stockList.add("Apple");
        stockList.add("Google");
        stockList.add("Microsoft");
        stockList.add("Oracle");
    }

    private class StockListAdapter extends ArrayAdapter<String> {
        public StockListAdapter(Context context) {
            super(context, 0);
        }

        public int getCount() {
            return stockList.size();
        }

        public String getItem(int position) {
            return stockList.get(position);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = StocksActivity.this.getLayoutInflater();
            View result = inflater.inflate(R.layout.stock_row, null);
            TextView stockRow = (TextView) result.findViewById(R.id.stock_row);
            stockRow.setText(getItem(position));
            return result;
        }
    }
}