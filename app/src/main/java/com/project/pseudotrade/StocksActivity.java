package com.project.pseudotrade;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class StocksActivity extends AppCompatActivity {

    SearchView stockSearchBar;
    ListView stocksListView;
    ArrayList<Stock> stockList;
    StockListAdapter stockListAdapter;

    String[] stockSymbols;
    ArrayList<Double> stockPrices;

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

        // TEMP DATA
        stockSymbols = new String[]{"AAPL", "GOOGL", "MSFT", "ORCL", "FB"};
        GetCurrentPrices getPrices = new GetCurrentPrices();
        getPrices.execute(stockSymbols);
    }

    class GetCurrentPrices extends AsyncTask<String[], Void, ArrayList<Double>> {

        @Override
        protected ArrayList<Double> doInBackground(String[]... strings) {
            ArrayList<Double> prices = new ArrayList<>();

            for (String symbol: strings[0]) {
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
            populateStockList(stockPrices);
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
    }

    private void populateStockList(ArrayList<Double> prices) {
        // TEMPORARY DATA
        Stock stock1 = new Stock("Apple", 1, prices.get(0));
        stockList.add(stock1);
        Stock stock2 = new Stock("Google", 3, prices.get(1));
        stockList.add(stock2);
        Stock stock3 = new Stock("Microsoft", 2, prices.get(2));
        stockList.add(stock3);
        Stock stock4 = new Stock("Oracle", 5, prices.get(3));
        stockList.add(stock4);
        Stock stock5 = new Stock("Facebook", 6, prices.get(4));
        stockList.add(stock5);
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