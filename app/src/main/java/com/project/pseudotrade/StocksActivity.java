package com.project.pseudotrade;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class StocksActivity extends AppCompatActivity {

    ImageButton SettingsPage;
    ImageButton StocksPage;
    ImageButton LearningPage;
    ImageButton MainPage;

    SearchView stockSearchBar;
    ListView stocksListView;
    TextView cashBalanceTextView;
    ArrayList<Stock> stockList;
    StockListAdapter stockListAdapter;
    Button refreshButton;
    ProgressBar refreshProgressBar;

    FirebaseDatabase mDatabase;
    DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;

    String userID;
    HashMap<String, Integer> holdings;
    Double cashBalance;

    ArrayList<String> stockSymbols;
    ArrayList<String> stockNames;
    ArrayList<Double> stockPrices;

    private static final String ACTIVITY_NAME = "StocksActivity";
    private static final String ALPHA_KEY = "PWTO0F2LFB70DMQ5";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stocks);
        stockSearchBar = findViewById(R.id.stock_search_bar);

        SettingsPage = findViewById(R.id.settingsButton);
        StocksPage = findViewById(R.id.stockButton);
        LearningPage = findViewById(R.id.Learning);
        MainPage = findViewById(R.id.homeButton);

        mAuth = FirebaseAuth.getInstance();

        StocksPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle userDataBundle = new Bundle();
                userDataBundle.putString("userID", userID);
                Intent stocksIntent = new Intent(StocksActivity.this, StocksActivity.class);
                stocksIntent.putExtras(userDataBundle);
                startActivity(stocksIntent);
            }
        });

        SettingsPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(StocksActivity.this, SettingsActivity.class);
                startActivityForResult(settingsIntent, 10);
            }
        });

        LearningPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(StocksActivity.this, learning_page.class);
                startActivity(intent);


            }
        });


        MainPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(StocksActivity.this, MainActivity.class);
                startActivityForResult(settingsIntent, 10);
            }
        });


        stockSearchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                goTrade(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        cashBalanceTextView = findViewById(R.id.cashBalanceTextView);
        stocksListView = findViewById(R.id.stock_list);
        refreshButton = findViewById(R.id.refresh_button);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RefreshStocksList(false);
            }
        });
        refreshProgressBar = findViewById(R.id.refresh_progress_bar);
        refreshProgressBar.setVisibility(View.INVISIBLE);
        stockList = new ArrayList<>();
        stockListAdapter = new StockListAdapter(this);
        stocksListView.setAdapter(stockListAdapter);
        stocksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Stock stock = stockListAdapter.getItem(position);

                String stockTicker = stock.getTicker();
                String stockName = stock.getName();
                double stockPrice = stock.getCurrentPrice();

                Bundle stockInfoBundle = new Bundle();
                stockInfoBundle.putString("stockTicker", stockTicker);
                stockInfoBundle.putString("stockName", stockName);
                stockInfoBundle.putDouble("stockPrice", stockPrice);

                Intent getStockDetailsIntent = new Intent(StocksActivity.this, StockDetailActivity.class);
                getStockDetailsIntent.putExtras(stockInfoBundle);
                startActivity(getStockDetailsIntent);
            }
        });

        Bundle userDataBundle = getIntent().getExtras(); // Get the current user's UserID
        if (userDataBundle != null)
            userID = userDataBundle.getString("userID");
        else {
            userID = mAuth.getCurrentUser().getUid();
        }
        holdings = new HashMap<>();
        stockSymbols = new ArrayList<>();
        stockNames = new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance(); // Get current user's balance and portfolio holdings
        mDatabaseReference = mDatabase.getReference("Users");
        GetUserData(mDatabaseReference, false);
    }

    private void GetUserData(DatabaseReference mDatabaseReference, boolean justSold) {
        DatabaseReference balanceRef = mDatabaseReference.child(userID).child("cashBalance");
        balanceRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cashBalance = snapshot.getValue(Double.class);
                cashBalanceTextView.setText(String.format("%s $%.2f", getString(R.string.cash_balance), cashBalance));
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

                RefreshStocksList(justSold);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    void RefreshStocksList(Boolean justSold) {
        if (!stockSymbols.isEmpty()) {
            GetCurrentPrices getPrices = new GetCurrentPrices();
            getPrices.execute(stockSymbols);
        } else {
            if (!justSold) {
                Snackbar emptyPortfolioSnackbar =
                        Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                                getString(R.string.empty_portfolio_message),
                                BaseTransientBottomBar.LENGTH_LONG);
                emptyPortfolioSnackbar.show();
            }
        }
    }

    class GetCurrentPrices extends AsyncTask<ArrayList<String>, Integer, ArrayList<Double>> {

        @Override
        protected ArrayList<Double> doInBackground(ArrayList<String>... arrayLists) {
            ArrayList<Double> prices = new ArrayList<>();

            int progressIncrement = (int) (100.00 / arrayLists[0].size());
            ArrayList<Integer> progressIncrements = new ArrayList<>();
            for (int i = 1; i < arrayLists[0].size() + 1; i++) {
                progressIncrements.add(progressIncrement * i);
            }

            int counter = 0;
            for (String symbol: arrayLists[0]) {
                Double price = null;
                try {
                    price = getPrice(symbol);
                    publishProgress(progressIncrements.get(counter));
                    counter++;
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                prices.add(price);

                String name = "";
                try {
                    name = getName(symbol);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                stockNames.add(name);
            }

            return prices;
        }

        @Override
        protected void onPostExecute(ArrayList<Double> doubles) {
            super.onPostExecute(doubles);
            stockPrices = doubles;
            refreshProgressBar.setVisibility(View.INVISIBLE);
            populateStockList(stockSymbols, stockNames, stockPrices);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            refreshProgressBar.setVisibility(View.VISIBLE);
            refreshProgressBar.setProgress(values[0]);
        }

        private Double getPrice(String symbol) throws IOException, JSONException {
            URL alphaURL = new URL("https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=" + symbol + "&interval=1min&apikey=" + ALPHA_KEY);
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
                latestPrice = Math.round(latestPrice * 100.0) / 100.0;
                return latestPrice;
            }
        }

        private String getName(String symbol) throws IOException, JSONException {
            URL dumbStockURL = new URL("https://dumbstockapi.com/stock?ticker_search=" + symbol);
            HttpURLConnection connection = (HttpURLConnection)dumbStockURL.openConnection();
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
                JSONObject jsonResult = new JSONObject(new JSONArray(result.toString()).getString(0));
                String name = jsonResult.getString("name");
                return name;
            }
        }
    }

    private void populateStockList(ArrayList<String> symbols, ArrayList<String> names, ArrayList<Double> prices) {
        if (prices.contains(null)) {
            Toast errorToast = Toast.makeText(this, getString(R.string.cannot_refresh_portfolio_message), Toast.LENGTH_SHORT);
            errorToast.show();
        } else {
            stockList.clear();
            stockListAdapter.notifyDataSetChanged();

            for (int i = 0; i < symbols.size(); i++) {
                Stock stock = new Stock(symbols.get(i), names.get(i), holdings.get(symbols.get(i)), prices.get(i));
                stockList.add(stock);
            }
            stockListAdapter.notifyDataSetChanged();
        }
    }

    private void goTrade(String searchString) {
        Intent goTradeIntent = new Intent(StocksActivity.this, TradeActivity.class);
        Bundle goTradeBundle = new Bundle();
        goTradeBundle.putString("searchString", searchString);
        goTradeBundle.putDouble("cashBalance", cashBalance);
        goTradeBundle.putSerializable("holdings", holdings);
        goTradeIntent.putExtras(goTradeBundle);
        startActivityForResult(goTradeIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            Snackbar buySuccessSnackbar =
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                            getString(R.string.buy_successful_message),
                            BaseTransientBottomBar.LENGTH_LONG);
            buySuccessSnackbar.show();
            holdings.clear();
            stockList.clear();
            stockSymbols.clear();
            stockNames.clear();
            stockPrices.clear();
            stockListAdapter.notifyDataSetChanged();
            GetUserData(mDatabaseReference, true);
        }
        if (resultCode == 2) {
            Snackbar sellSuccessSnackbar =
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                            getString(R.string.sell_successful_message),
                            BaseTransientBottomBar.LENGTH_LONG);
            sellSuccessSnackbar.show();
            holdings.clear();
            stockList.clear();
            stockSymbols.clear();
            stockNames.clear();
            stockPrices.clear();
            stockListAdapter.notifyDataSetChanged();
            GetUserData(mDatabaseReference, true);
        }
        if (resultCode == 10){
            Log.i("LoggingOutFromLearning","Finished");
            Intent loginIntent = new Intent(StocksActivity.this, LoginActivity.class);
//            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finishAffinity();
//            System.exit(0);
            startActivity(loginIntent);
            finish();
        }
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
            stockRowName.setText(String.format("%s\n(%s)", getItem(position).getTicker(), getItem(position).getName()));
            TextView stockRowHoldingsValue = (TextView) result.findViewById(R.id.holdings_value);
            String stockValue = String.format("$%.02f", getItem(position).getHoldingsValue());
            stockRowHoldingsValue.setText(stockValue);
            TextView stockRowQuantity = (TextView) result.findViewById(R.id.stock_quantity);
            String stockUnits = String.format("%d %s", getItem(position).getQuantity(), getString(R.string.units));
            stockRowQuantity.setText(stockUnits);
            TextView stockRowPrice = (TextView) result.findViewById(R.id.stock_price);
            String stockPrice = String.format("$%.02f", getItem(position).getCurrentPrice());
            stockRowPrice.setText(stockPrice);
            return result;
        }
    }

    private class Stock {
        private final String ticker;
        private final String name;
        private final int quantity;
        private final double currentPrice;
        private final double holdingsValue;

        public Stock(String ticker, String name, int quantity, double currentPrice) {
            this.ticker = ticker;
            this.name = name;
            this.quantity = quantity;
            this.currentPrice = currentPrice;
            this.holdingsValue = this.quantity * this.currentPrice;
        }

        public String getTicker() { return this.ticker; }

        public String getName() { return this.name; }

        public int getQuantity() { return this.quantity; }

        public double getCurrentPrice() { return this.currentPrice; }

        public double getHoldingsValue() { return this.holdingsValue; }
    }


}