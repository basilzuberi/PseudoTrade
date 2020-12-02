
package com.project.pseudotrade;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Set;


public class SettingsActivity extends AppCompatActivity {

    ImageButton SettingsPage;
    ImageButton StocksPage;
    ImageButton LearningPage;
    ImageButton MainPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SettingsPage = findViewById(R.id.settingsButton);
        StocksPage = findViewById(R.id.stockButton);
        LearningPage = findViewById(R.id.Learning);
        MainPage = findViewById(R.id.homeButton);

        StocksPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent stocksIntent = new Intent(SettingsActivity.this, StocksActivity.class);
                startActivity(stocksIntent);
            }
        });

        SettingsPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(SettingsActivity.this, SettingsActivity.class);
                startActivityForResult(settingsIntent, 10);
            }
        });

        LearningPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SettingsActivity.this, learning_page.class);
                startActivity(intent);


            }
        });


        MainPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivityForResult(settingsIntent, 10);
            }
        });

        Button signoutButton = findViewById(R.id.settings_signout_button);
        Button changeEmailButton = findViewById(R.id.settings_change_email);

        signoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
                setResult(10,intent);
                finish();
            }
        });

        changeEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


}