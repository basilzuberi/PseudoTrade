package com.project.pseudotrade;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;


public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText mEmailAddress;
    private EditText mPassword;
    private EditText mUserName;
    SharedPreferences mSharedPreference;
    FirebaseDatabase mDatabase;
    DatabaseReference mDatabaseReference;
    Button mBtnSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();

        mEmailAddress = findViewById(R.id.txtSignUpEmail);
        mPassword = findViewById(R.id.txtSignUpPassword);
        mUserName = findViewById(R.id.txtSignUpDisplayName);
        mBtnSignUp = findViewById(R.id.btnSignUpConfirm);

        mSharedPreference = getSharedPreferences("LoginActivityShared", Context.MODE_PRIVATE); //open SharedPreference for read
        mBtnSignUp.setOnClickListener(v -> {
            signUp();
            hideKeyboard(v);
        });


    }

    private void signUp() {

        //get all fields
        String email = mEmailAddress.getText().toString();
        String password = mPassword.getText().toString();
        String username = mUserName.getText().toString();
        View contextView = findViewById(R.id.btnSignUpConfirm);
        String TAG = "SignUp";

        if(email.equals("") || password.equals("") || username.equals("")){
            Toast.makeText(SignUpActivity.this, "Invalid Arguments.",
                    Toast.LENGTH_SHORT).show();

            return;
        }

        //call SignUp from Firebase
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            assert user != null;
                            signUpSuccess(user,username);


                        } else {
                            //EXCEPTIONS for Invalid SignUp
                            try{

                                throw Objects.requireNonNull(task.getException());

                            }catch (FirebaseAuthWeakPasswordException weakPassword){ //WEAK PASSWORD

//                                Toast.makeText(SignUpActivity.this, "Error Signing Up, Weak Password.", Toast.LENGTH_SHORT).show();
                                Snackbar.make(contextView, "Couldn't Sign you up: Choose a stronger password!", Snackbar.LENGTH_SHORT)
                                        .show();

                            }catch(FirebaseAuthInvalidCredentialsException malformedEmail){
                                Snackbar.make(contextView, "Couldn't Sign you up: Invalid Email Entry!", Snackbar.LENGTH_SHORT)
                                        .show();
                            }catch (FirebaseAuthUserCollisionException existEmail){
                                Snackbar.make(contextView, "Couldn't Sign you up: Email Already Exists!", Snackbar.LENGTH_SHORT)
                                        .show();
                            }
                            catch (Exception e) {
                                Log.d(TAG, "onComplete: " + e.getMessage());
                            }

                        }

                    }
                });
    }

    private void signUpSuccess(FirebaseUser user, String username) {
        String userID = user.getUid();
        String email = user.getEmail();

        SharedPreferences.Editor editor = mSharedPreference.edit(); //edit shared Preferences
        mDatabase = FirebaseDatabase.getInstance(); //database Ref
        mDatabaseReference = mDatabase.getReference("Users");
        SignUpUserHelper userHelper = new SignUpUserHelper(email,username); //create new user to put into database with email and username

        mDatabaseReference.child(userID).setValue(userHelper);

        editor.putString("LoginEmail", user.getEmail()); //we add the string from SignedUpUsers Email to SharedPreferences (LoginActivityShared.xml)
        editor.apply();

        Bundle userDataBundle = new Bundle(); // pass the Uid to MainActivity
        userDataBundle.putString("userID", userID);

        Intent startMainActivity = new Intent(SignUpActivity.this, MainActivity.class);
        startMainActivity.putExtras(userDataBundle);
        startActivity(startMainActivity); //goto MainActivity
        finish();
    }

    private void hideKeyboard(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);
    }
}