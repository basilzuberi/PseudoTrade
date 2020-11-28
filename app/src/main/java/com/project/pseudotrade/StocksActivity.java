package com.project.pseudotrade;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class StocksActivity extends AppCompatActivity {

    SearchView stockSearchBar;
    ListView stocksListView;
    ArrayList<Stock> stockList;
    StockListAdapter stockListAdapter;
    FirebaseDatabase mDatabase;
    DatabaseReference mDatabaseReference;

    String userID;
    HashMap<String, Integer> holdings;
    Double cashBalance;

    ArrayList<String> stockSymbols;
    ArrayList<Double> stockPrices;

    private static final String ACTIVITY_NAME = "StocksActivity";
    private static final String ALPHA_KEY = "PWTO0F2LFB70DMQ5";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stocks);
        stockSearchBar = findViewById(R.id.stock_search_bar);
        stocksListView = findViewById(R.id.stock_list);
        stockList = new ArrayList<>();
        stockListAdapter = new StockListAdapter(this);
        stocksListView.setAdapter(stockListAdapter);
        stocksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Stock stock = stockListAdapter.getItem(position);
                String tempMessage = String.format("A detailed page for %s will be shown eventually...", stock.getName());
                Toast toast = Toast.makeText(StocksActivity.this, tempMessage, Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        Bundle userDataBundle = getIntent().getExtras(); // Get the current user's UserID
        if (userDataBundle != null)
            userID = userDataBundle.getString("userID");
        holdings = new HashMap<>();
        stockSymbols = new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance(); // Get current user's balance and portfolio holdings
        mDatabaseReference = mDatabase.getReference("Users");
        GetUserData(mDatabaseReference);
    }

    private void GetUserData(DatabaseReference mDatabaseReference) {
        DatabaseReference balanceRef = mDatabaseReference.child(userID).child("cashBalance");
        balanceRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cashBalance = snapshot.getValue(Double.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        DatabaseReference holdingsRef = mDatabaseReference.child(userID).child("holdings");
        holdingsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {
                    String stockSymbol = ds.getKey();
                    Integer stockQuantity = Math.toIntExact((Long) ds.getValue());
                    Log.d(ACTIVITY_NAME, "Stock symbol is " + stockSymbol + " and quantity owned is " + stockQuantity);
                    holdings.put(stockSymbol, stockQuantity);
                    Log.d(ACTIVITY_NAME, stockSymbol + " was added to holdings with quantity " + stockQuantity);
                }

                for (String stockHeld: holdings.keySet()) {
                    if (!stockHeld.equals("PlaceholderStock"))
                        stockSymbols.add(stockHeld);
                }

                GetCurrentPrices getPrices = new GetCurrentPrices();
                getPrices.execute(stockSymbols);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    class GetCurrentPrices extends AsyncTask<ArrayList<String>, Void, ArrayList<Double>> {

        @Override
        protected ArrayList<Double> doInBackground(ArrayList<String>... arrayLists) {
            ArrayList<Double> prices = new ArrayList<>();

            for (String symbol: arrayLists[0]) {
                Double price = null;
                try {
                    price = getPrice(symbol);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                prices.add(price);
            }

            return prices;
        }

        @Override
        protected void onPostExecute(ArrayList<Double> doubles) {
            stockPrices = doubles;
            populateStockList(stockSymbols, stockPrices);
        }

        private Double getPrice(String symbol) throws IOException, JSONException {
            URL alphaURL = new URL("https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=" + symbol + "&interval=1min&apikey=" + ALPHA_KEY);
            // URL alphaURL = new URL("https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=IBM&interval=5min&apikey=demo");
            HttpURLConnection connection = (HttpURLConnection)alphaURL.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int response = connection.getResponseCode();
            if (response != 200)
                throw new RuntimeException("HttpResponseCode: " + response);
            else {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuffer result = new StringBuffer();
                while ((inputLine = in.readLine()) != null)
                    result.append(inputLine);
                connection.disconnect();
                in.close();
                JSONObject jsonResult = new JSONObject(result.toString());
                String latestTime = jsonResult.getJSONObject("Meta Data").getString("3. Last Refreshed");
                Double latestPrice = Double.valueOf(jsonResult.getJSONObject("Time Series (1min)").getJSONObject(latestTime).getString("4. close"));
                // Double latestPrice = Double.valueOf(jsonResult.getJSONObject("Time Series (5min)").getJSONObject(latestTime).getString("4. close"));
                latestPrice = Math.round(latestPrice * 100.0) / 100.0;
                return latestPrice;
            }
        }
    }

    private void populateStockList(ArrayList<String> symbols, ArrayList<Double> prices) {
        for (int i = 0; i < symbols.size(); i++) {
            Stock stock = new Stock(symbols.get(i), holdings.get(symbols.get(i)), prices.get(i));
            stockList.add(stock);
        }
        stockListAdapter.notifyDataSetChanged();
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
            String stockUnits = String.format("%d units", getItem(position).getQuantity());
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