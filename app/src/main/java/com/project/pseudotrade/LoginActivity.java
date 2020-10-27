package com.project.pseudotrade;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {
    Button mBtnLogin;
    Button mBtnSignUp;
    private EditText mEmailAddress;
    private EditText mPassword;
    SharedPreferences mSharedPreference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mBtnLogin = findViewById(R.id.buttonLogin);
        mBtnSignUp = findViewById(R.id.buttonSignUp);
        mEmailAddress = findViewById(R.id.txtEmailAddress);
        mPassword = findViewById(R.id.txtPassword);

        mSharedPreference = getSharedPreferences("LoginActivityShared", Context.MODE_PRIVATE);
        mEmailAddress.setText(mSharedPreference.getString("LoginEmail",""));

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = mSharedPreference.edit(); //edit shared preferance
                editor.putString("LoginEmail", mEmailAddress.getText().toString()); //we add the string from LoginText to SharedPreferance (email.xml)
                editor.apply();

                Intent loginActivityIntent = new Intent(LoginActivity.this, StocksActivity.class);
                startActivity(loginActivityIntent);
            }
        });

    }
}