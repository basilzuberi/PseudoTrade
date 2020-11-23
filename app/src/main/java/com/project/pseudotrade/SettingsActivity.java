
package com.project.pseudotrade;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class SettingsActivity extends AppCompatActivity {

    private Button resetData;
    private Button changeEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    //add alert dialogue - amber
        resetData = (Button) findViewById(R.id.settings_reset_data);



    }
}