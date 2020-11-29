package com.project.pseudotrade;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class StockDetailActivity extends AppCompatActivity {

    private static final String ACTIVITY_NAME = "StockDetailActivity";
    private static final String ALPHA_KEY = "PWTO0F2LFB70DMQ5";

    String stockTicker;
    String stockName;
    double currentPrice;
    HashMap<String, Double> hourData;

    TextView stockNameTextView;
    TextView stockTickerTextView;
    TextView stockOpenTextView;
    TextView stockCloseTextView;
    TextView stockHighTextView;
    TextView stockLowTextView;
    TextView stockVolumeTextView;
    ProgressBar getDataProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detail);
        stockNameTextView = findViewById(R.id.stockNameTextView);
        stockTickerTextView = findViewById(R.id.stockTickerTextView);
        stockOpenTextView = findViewById(R.id.stock_open);
        stockCloseTextView = findViewById(R.id.stock_close);
        stockHighTextView = findViewById(R.id.stock_high);
        stockLowTextView = findViewById(R.id.stock_low);
        stockVolumeTextView = findViewById(R.id.stock_volume);
        getDataProgressBar = findViewById(R.id.get_data_progress_bar);
        getDataProgressBar.setVisibility(View.INVISIBLE);

        Bundle stockDataBundle = getIntent().getExtras();   // Get stock info from StocksActivity
        if (stockDataBundle != null) {
            stockTicker = stockDataBundle.getString("stockTicker");
            stockName = stockDataBundle.getString("stockName");
            currentPrice = stockDataBundle.getDouble("stockPrice");
        }

        hourData = new HashMap<>();
        GetHourlyData getData = new GetHourlyData();
        getData.execute(stockTicker);

    }

    class GetHourlyData extends AsyncTask<String, Integer, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            String symbol = strings[0];
            try {
                getData(symbol);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        private void getData(String symbol) throws IOException, JSONException {
            URL alphaURL = new URL("https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=" + symbol + "&interval=60min&apikey=" + ALPHA_KEY);
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
                try {
                    Double open = Double.valueOf(jsonResult.getJSONObject("Time Series (60min)").getJSONObject(latestTime).getString("1. open"));
                    publishProgress(20);
                    Double high = Double.valueOf(jsonResult.getJSONObject("Time Series (60min)").getJSONObject(latestTime).getString("2. high"));
                    publishProgress(40);
                    Double low = Double.valueOf(jsonResult.getJSONObject("Time Series (60min)").getJSONObject(latestTime).getString("3. low"));
                    publishProgress(60);
                    Double close = Double.valueOf(jsonResult.getJSONObject("Time Series (60min)").getJSONObject(latestTime).getString("4. close"));
                    publishProgress(80);
                    Double volume = Double.valueOf(jsonResult.getJSONObject("Time Series (60min)").getJSONObject(latestTime).getString("5. volume"));
                    publishProgress(100);

                    hourData.put("open", open);
                    hourData.put("high", high);
                    hourData.put("low", low);
                    hourData.put("close", close);
                    hourData.put("volume", volume);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            getDataProgressBar.setVisibility(View.VISIBLE);
            getDataProgressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            getDataProgressBar.setVisibility(View.INVISIBLE);
            if (hourData.isEmpty()) {
                AlertDialog.Builder errorDialog = new AlertDialog.Builder(StockDetailActivity.this);
                LayoutInflater inflater = StockDetailActivity.this.getLayoutInflater();
                final View view = inflater.inflate(R.layout.error_dialog, null);
                errorDialog.setView(view)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                            }
                        });
                errorDialog.create().show();
            } else {
                stockNameTextView.setText(stockName);
                stockTickerTextView.setText(stockTicker);
                stockOpenTextView.setText(String.format("Open: $%.2f", hourData.get("open")));
                stockCloseTextView.setText(String.format("Close: $%.2f", hourData.get("close")));
                stockHighTextView.setText(String.format("High: $%.2f", hourData.get("high")));
                stockLowTextView.setText(String.format("Low: $%.2f", hourData.get("low")));
                stockVolumeTextView.setText(String.format("Volume traded: %d units", hourData.get("volume").intValue()));
            }
        }
    }
}
