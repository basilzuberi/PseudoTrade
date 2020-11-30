package com.project.pseudotrade;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

public class TradeActivity extends AppCompatActivity {

    ListView resultsListView;
    TradeFragment tradeFragment;
    ResultListAdapter resultListAdapter;
    ArrayList<String> resultList;
    HashMap<String, String> resultHashMap;

    String searchString;
    Double cashBalance;
    HashMap<String, Integer> holdings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade);
        resultsListView = findViewById(R.id.results_list_view);

        Bundle tradeBundle = getIntent().getExtras();   // Get stock info from StocksActivity
        if (tradeBundle != null) {
            searchString = tradeBundle.getString("searchString");
            cashBalance = tradeBundle.getDouble("cashBalance");
            holdings = (HashMap<String, Integer>) tradeBundle.getSerializable("holdings");
        }

        resultList = new ArrayList<>();
        resultListAdapter = new ResultListAdapter(this);
        resultsListView.setAdapter(resultListAdapter);
        resultsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (tradeFragment != null) {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.remove(tradeFragment);
                    fragmentTransaction.commit();
                }
                String ticker = resultListAdapter.getItem(i);
                Bundle buySellBundle = new Bundle();
                buySellBundle.putString("ticker", ticker);
                buySellBundle.putDouble("cashBalance", cashBalance);
                buySellBundle.putSerializable("holdings", holdings);
                tradeFragment = new TradeFragment();
                tradeFragment.setArguments(buySellBundle);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.trade_frame, tradeFragment);
                fragmentTransaction.commit();
            }
        });
        resultHashMap = new HashMap<>();

        GetSearchResults getSearchResults = new GetSearchResults();
        getSearchResults.execute(searchString);
    }

    private class GetSearchResults extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            String searchString = strings[0];
            try {
                getResults(searchString);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        private void getResults(String searchString) throws IOException, JSONException {
            URL dumbStockURL = new URL("https://dumbstockapi.com/stock?name_search=" + searchString);
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
                JSONArray jsonResultArray = new JSONArray(result.toString());
                for (int i = 0; i < jsonResultArray.length(); i++) {
                    JSONObject resultObject = jsonResultArray.getJSONObject(i);
                    resultList.add(resultObject.getString("ticker"));
                    resultHashMap.put(resultObject.getString("ticker"), resultObject.getString("name"));
                }
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            resultListAdapter.notifyDataSetChanged();
        }
    }

    private class ResultListAdapter extends ArrayAdapter<String> {
        public ResultListAdapter(Context context) {
            super(context, 0);
        }

        public int getCount() {
            return resultList.size();
        }

        public String getItem(int position) {
            return resultList.get(position);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = TradeActivity.this.getLayoutInflater();
            View result = inflater.inflate(R.layout.result_row, null);
            TextView resultRowName = result.findViewById(R.id.result_name);
            String stockName = resultHashMap.get(getItem(position));
            resultRowName.setText(stockName);
            TextView resultRowTicker = result.findViewById(R.id.result_ticker);
            resultRowTicker.setText(String.format("Ticker: %s", getItem(position)));
            return result;
        }
    }
}