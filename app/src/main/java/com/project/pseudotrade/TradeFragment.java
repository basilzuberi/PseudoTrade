package com.project.pseudotrade;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;

public class TradeFragment extends Fragment {

    TextView stockTickerTradeTextView;
    Button buyButton;
    Button sellButton;
    EditText quantityEditText;

    String stockTicker;
    Double cashBalance;
    HashMap<String, Integer> holdings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stockTicker = getArguments().getString("ticker");
        cashBalance = getArguments().getDouble("cashBalance");
        holdings = (HashMap<String, Integer>) getArguments().getSerializable("holdings");
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

        stockTickerTradeTextView = view.findViewById(R.id.stockTickerTradeTextView);
        stockTickerTradeTextView.setText(stockTicker);
        buyButton = view.findViewById(R.id.buyButton);
        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        sellButton = view.findViewById(R.id.sellButton);
        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        quantityEditText = view.findViewById(R.id.quantity_EditText);
    }
}