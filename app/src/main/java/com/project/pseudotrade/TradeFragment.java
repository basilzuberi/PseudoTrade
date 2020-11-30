package com.project.pseudotrade;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class TradeFragment extends Fragment {

    private static final String ALPHA_KEY = "PWTO0F2LFB70DMQ5";

    TextView tradeCashBalanceTextView;
    TextView stockTickerTradeTextView;
    Button buyButton;
    Button sellButton;
    EditText quantityEditText;

    String stockTicker;
    Double cashBalance;
    HashMap<String, Integer> holdings;
    Double curStockPrice;

    FirebaseDatabase mDatabase;
    DatabaseReference mDatabaseReference;
    FirebaseAuth mAuth;
    String userID;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stockTicker = getArguments().getString("ticker");
        cashBalance = getArguments().getDouble("cashBalance");
        holdings = (HashMap<String, Integer>) getArguments().getSerializable("holdings");

        mDatabase = FirebaseDatabase.getInstance(); // Get current user's balance and portfolio holdings
        mDatabaseReference = mDatabase.getReference("Users");
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        curStockPrice = null;
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trade, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GetPriceTask getPriceTask = new GetPriceTask();
        getPriceTask.execute(stockTicker);

        tradeCashBalanceTextView = view.findViewById(R.id.tradeCashBalanceTextView);
        tradeCashBalanceTextView.setText(String.format("Cash Balance: $%.2f", cashBalance));
        stockTickerTradeTextView = view.findViewById(R.id.stockTickerTradeTextView);
        stockTickerTradeTextView.setText(stockTicker);
        buyButton = view.findViewById(R.id.buyButton);
        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quantityEditText.getText() == null || quantityEditText.getText().toString().equals("") || quantityEditText.getText().toString().equals("0")) {
                    Toast errorToast = Toast.makeText(getActivity(), "Please enter a number of units to buy or sell...", Toast.LENGTH_SHORT);
                    errorToast.show();
                } else {
                    buyStock(stockTicker, Integer.parseInt(quantityEditText.getText().toString()));
                }
            }
        });
        sellButton = view.findViewById(R.id.sellButton);
        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quantityEditText.getText() == null || quantityEditText.getText().toString().equals("") || quantityEditText.getText().toString().equals("0")) {
                    Toast errorToast = Toast.makeText(getActivity(), "Please enter a number of units to buy or sell...", Toast.LENGTH_SHORT);
                    errorToast.show();
                } else {
                    sellStock(stockTicker, Integer.parseInt(quantityEditText.getText().toString()));
                }
            }
        });
        quantityEditText = view.findViewById(R.id.quantity_EditText);
    }

    private void buyStock(String stockTicker, Integer quantity) {
        if (curStockPrice == null) {
            Toast errorToast = Toast.makeText(getActivity(), String.format("The price of %s could not be fetched right now. Try again in a minute.", stockTicker), Toast.LENGTH_SHORT);
            errorToast.show();
            return;
        }

        Double totalCost = quantity * curStockPrice;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder
                .setMessage(String.format("Buy %d units of %s for $%.2f?", quantity, stockTicker, totalCost))
                .setTitle(String.format("Buy %s?", stockTicker))
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        buyStockHelper(stockTicker, quantity, totalCost);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                })
                .show();
    }

    private void buyStockHelper(String ticker, Integer quantity, Double totalCost) {
        if (totalCost > cashBalance) {
            Snackbar insufficientFundsSnackbar =
                    Snackbar.make(getActivity().getWindow().getDecorView().findViewById(android.R.id.content),
                            "Insufficient funds to make this purchase!",
                            BaseTransientBottomBar.LENGTH_LONG);
            insufficientFundsSnackbar.show();
            return;
        }

        cashBalance = cashBalance - totalCost;

        if (holdings.containsKey(ticker)) {
            holdings.put(ticker, holdings.get(ticker) + quantity);
        } else {
            holdings.put(ticker, quantity);
        }

        mDatabaseReference.child(userID).child("cashBalance").setValue(cashBalance);
        mDatabaseReference.child(userID).child("holdings").setValue(holdings);

        getActivity().setResult(1);
        getActivity().finish();
    }

    private void sellStock(String stockTicker, Integer quantity) {
        if (curStockPrice == null) {
            Toast errorToast = Toast.makeText(getActivity(), String.format("The price of %s could not be fetched right now. Try again in a minute.", stockTicker), Toast.LENGTH_SHORT);
            errorToast.show();
            return;
        }

        Double totalVal = quantity * curStockPrice;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder
                .setMessage(String.format("Sell %d units of %s for $%.2f?", quantity, stockTicker, totalVal))
                .setTitle(String.format("Buy %s?", stockTicker))
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        sellStockHelper(stockTicker, quantity, totalVal);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                })
                .show();
    }

    private void sellStockHelper(String stockTicker, Integer quantity, Double totalVal) {
        if (!holdings.containsKey(stockTicker)) {
            Snackbar dontOwnAnySnackbar =
                    Snackbar.make(getActivity().getWindow().getDecorView().findViewById(android.R.id.content),
                            String.format("You do not own any shares of %s.", stockTicker),
                            BaseTransientBottomBar.LENGTH_LONG);
            dontOwnAnySnackbar.show();
            return;
        } else if (holdings.containsKey(stockTicker) && quantity > holdings.get(stockTicker)) {
            Snackbar notEnoughSnackbar =
                    Snackbar.make(getActivity().getWindow().getDecorView().findViewById(android.R.id.content),
                            String.format("You don't have enough shares of %s.", stockTicker),
                            BaseTransientBottomBar.LENGTH_LONG);
            notEnoughSnackbar.show();
            return;
        } else {
            cashBalance = cashBalance + totalVal;
            if (quantity.equals(holdings.get(stockTicker))) {
                holdings.remove(stockTicker);
            } else {
                holdings.put(stockTicker, holdings.get(stockTicker) - quantity);
            }
        }

        mDatabaseReference.child(userID).child("cashBalance").setValue(cashBalance);
        mDatabaseReference.child(userID).child("holdings").setValue(holdings);

        getActivity().setResult(2);
        getActivity().finish();
    }

    private class GetPriceTask extends AsyncTask<String, Void, Double> {

        @Override
        protected Double doInBackground(String... strings) {
            String symbol = strings[0];
            Double price = null;
            try {
                price = getPrice(symbol);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return price;
        }

        private Double getPrice(String symbol) throws IOException, JSONException {
            URL alphaURL = new URL("https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=" + symbol + "&interval=1min&apikey=" + ALPHA_KEY);
            HttpURLConnection connection = (HttpURLConnection) alphaURL.openConnection();
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
                Double price = Double.valueOf(jsonResult.getJSONObject("Time Series (1min)").getJSONObject(latestTime).getString("4. close"));
                return price;
            }
        }

        @Override
        protected void onPostExecute(Double price) {
            super.onPostExecute(price);
            curStockPrice = price;
            if (curStockPrice == null) {
                Toast errorToast = Toast.makeText(getActivity(), String.format("The price of %s could not be fetched right now. Try again in a minute.", stockTicker), Toast.LENGTH_SHORT);
                errorToast.show();
            } else {
                stockTickerTradeTextView.setText(String.format("%s: $%.2f", stockTicker, curStockPrice));
            }
        }
    }
}