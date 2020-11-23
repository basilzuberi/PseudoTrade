
package com.project.pseudotrade;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    private Button resetData;
    private Button changeEmail;
    private Button signOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        resetData = findViewById(R.id.settings_reset_data);
        changeEmail = findViewById(R.id.settings_change_email);
        signOut = findViewById(R.id.settings_signout_button);

        resetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(SettingsActivity.this);

                builder.setTitle("Are you sure?");
                builder.setMessage("Selecting 'Confirm' will reset your data");

                builder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // add reset data code here
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        changeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(SettingsActivity.this);

                builder.setTitle("Are you sure you want to change your account detail?");
                builder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // add change email code here
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(SettingsActivity.this);

                builder.setTitle("Are you sure you want to sign out?");
                builder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // add sign out info
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });




    }
}

