package com.project.pseudotrade;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    Button mBtnLogin;
    Button mBtnSignUp;
    private EditText mEmailAddress;
    private EditText mPassword;
    SharedPreferences mSharedPreference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mBtnLogin = findViewById(R.id.buttonLogin);
        mBtnSignUp = findViewById(R.id.buttonSignUp);
        mEmailAddress = findViewById(R.id.txtEmailAddress);
        mPassword = findViewById(R.id.txtPassword);

        mAuth = FirebaseAuth.getInstance(); //initialize Firebase

        mSharedPreference = getSharedPreferences("LoginActivityShared", Context.MODE_PRIVATE); //open SharedPreference for read
        mEmailAddress.setText(mSharedPreference.getString("LoginEmail",""));

        mBtnLogin.setOnClickListener(v -> signIn());

        mBtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startSignUpActivity = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(startSignUpActivity); //goto MainActivity
            }
        });

    }

    private void signIn() {
        String email = mEmailAddress.getText().toString();
        String password = mPassword.getText().toString();
        String TAG = "Login";
        if (email.equals("")|| password.equals("")){
            Toast.makeText(LoginActivity.this, "Authentication Failed.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        assert user != null;
                        signInSuccess(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "Invalid Email or Password.",
                                Toast.LENGTH_SHORT).show();
                    }

                });
    }

    private void signInSuccess(FirebaseUser user) {
        SharedPreferences.Editor editor = mSharedPreference.edit(); //edit shared Preferences
        editor.putString("LoginEmail", user.getEmail()); //we add the string from LoggedInUsers Email to SharedPreferences (LoginActivityShared.xml)
        editor.putString("UserName", user.getDisplayName()); //we add the string from LoggedInUsers DisplayName to SharedPreferences (LoginActivityShared.xml)
        editor.apply();

        Intent startMainActivity = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(startMainActivity); //goto MainActivity
        finish();
    }


    @Override
    protected void onStart() {
        super.onStart();

//        we check if user is logged in and continue otherwise make them sign-in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser!=null){
            signInSuccess(currentUser);
        }

    }
    
    
}
