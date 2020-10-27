package com.project.pseudotrade;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class StocksActivity extends AppCompatActivity {

    SearchView stockSearchBar;
    ListView stocksListView;
    ArrayList<Stock> stockList;
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
        // TEMPORARY DATA
        Stock stock1 = new Stock("Apple", 1, 500.00);
        stockList.add(stock1);
        Stock stock2 = new Stock("Google", 3, 250.00);
        stockList.add(stock2);
        Stock stock3 = new Stock("Microsoft", 2, 300.00);
        stockList.add(stock3);
        Stock stock4 = new Stock("Oracle", 5, 50.00);
        stockList.add(stock4);
        Stock stock5 = new Stock("Facebook", 6, 20.00);
        stockList.add(stock5);
    }

    private class StockListAdapter extends ArrayAdapter<Stock> {
        public StockListAdapter(Context context) {
            super(context, 0);
        }

        public int getCount() {
            return stockList.size();
        }

        public Stock getItem(int position) {
            return stockList.get(position);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = StocksActivity.this.getLayoutInflater();
            View result = inflater.inflate(R.layout.stock_row, null);
            TextView stockRowName = (TextView) result.findViewById(R.id.stock_name);
            stockRowName.setText(getItem(position).getName());
            TextView stockRowHoldingsValue = (TextView) result.findViewById(R.id.holdings_value);
            String stockValue = String.format("$%.02f", getItem(position).getHoldingsValue());
            stockRowHoldingsValue.setText(stockValue);
            TextView stockRowQuantity = (TextView) result.findViewById(R.id.stock_quantity);
            String stockUnits = String.format("%d units", getItem(position).quantity);
            stockRowQuantity.setText(stockUnits);
            TextView stockRowPrice = (TextView) result.findViewById(R.id.stock_price);
            String stockPrice = String.format("$%.02f", getItem(position).getCurrentPrice());
            stockRowPrice.setText(stockPrice);
            return result;
        }
    }

    private class Stock {
        private final String name;
        private final int quantity;
        private final double currentPrice;
        private final double holdingsValue;

        public Stock(String name, int quantity, double currentPrice) {
            this.name = name;
            this.quantity = quantity;
            this.currentPrice = currentPrice;
            this.holdingsValue = this.quantity * this.currentPrice;
        }

        public String getName() { return this.name; }

        public int getQuantity() { return this.quantity; }

        public double getCurrentPrice() { return this.currentPrice; }

        public double getHoldingsValue() { return this.holdingsValue; }
    }
}