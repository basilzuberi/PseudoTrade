
package com.project.pseudotrade;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Set;


public class SettingsActivity extends AppCompatActivity {

    EditText password;
    EditText email;
    String TAG = "SettingsActivity";
    SharedPreferences mSharedPreference;

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

//        SettingsPage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent settingsIntent = new Intent(SettingsActivity.this, SettingsActivity.class);
//                startActivityForResult(settingsIntent, 10);
//            }
//        });

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
        Button resetButton = findViewById(R.id.settings_reset_data);
        Button infoButton = findViewById(R.id.settings_about_button);

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
                updateEmail();
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetData();
            }
        });


        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInfo();
            }
        });

    }

    private void showInfo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
        builder
                .setMessage("Created by: Amber D'Silva,\nKieara Miranda,\nPatrick Mandarino,\nSri Vinukonda,\nBasil Zuberi\nVerison: 1.0.0")

                .setTitle("ABOUT US")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })

                .show();
    }

    private void updateEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        Log.i("SettingsActivity",user.getEmail());
        password = findViewById(R.id.sett_password);
        email = findViewById(R.id.sett_email);
        String pswd = password.getText().toString();
        String eml = email.getText().toString();

        mSharedPreference = getSharedPreferences("LoginActivityShared", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreference.edit();
        if (user != null) {
            Log.i("pswd != null", String.valueOf(pswd!=""));
            if (!pswd.equals("") && !eml.equals("") && pswd != null && eml != null && !(user.getEmail().equals("")) && user.getEmail() != null) {
                Log.i("SettingsActivityInsideUpdateEmail",user.getEmail()+pswd);
                AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), pswd);
                user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    user.updateEmail(eml).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "Email updated");
                                                   Toast.makeText(SettingsActivity.this, "Your email has been changed to: " + eml,Toast.LENGTH_SHORT).show();

                                                password.setText("");
                                                email.setText("");

                                                editor.putString("LoginEmail",eml);
                                                editor.apply();
                                            } else {
                                                Log.d(TAG, "Error Email not updated");
                                            }
                                        }
                                    });
//                                    HashMap<String, Object> newEmail = new HashMap<>();
//                                    newEmail.put("email", eml);
//                                    FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("email").updateChildren(newEmail);

                                } else {
                                    Log.d(TAG, "Error auth failed");
                                }
                            }
                        });
            } else {

            }
        }
    }
    private void resetData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        HashMap<String, Object> placeHolderStock = new HashMap<>();
        placeHolderStock.put("PlaceholderStock", -1);

        // Wasn't working before because getReference("Users") wasn't there
        FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("holdings").setValue(null);
        FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("cashBalance").setValue(1000.00);
        FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("holdings").updateChildren(placeHolderStock);
        Toast.makeText(SettingsActivity.this, "Your stocks and account have been reset",Toast.LENGTH_SHORT).show();

    }
}