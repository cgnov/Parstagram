package com.example.parstagram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.parstagram.databinding.ActivityLoginBinding;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";
    private ActivityLoginBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        if (ParseUser.getCurrentUser() != null) {
            goMainActivity();
        }
    }

    public void logInUser(View view) {
        if(mBinding.btnSignup.getVisibility()==View.VISIBLE) {
            // User wants to log in. Display relevant buttons and hide signup button
            showCredentials(true);
            mBinding.btnSignup.setVisibility(View.GONE);
        } else {
            // Get login credentials
            final String username = mBinding.etUsername.getText().toString();
            final String password = mBinding.etPassword.getText().toString();

            logInUser(username, password);
        }
    }

    private void logInUser(String username, String password) {
        Log.i(TAG, "Attempting to login user " + username);
        if(username.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Make sure to fill in both username and password", Toast.LENGTH_LONG).show();
            return;
        }

        // Check if login credentials are valid
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e != null) {
                    Log.e(TAG, "Issue with login", e);
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    // Login was valid, proceed to main activity
                    goMainActivity();
                }
            }
        });
    }

    public void signUpUser(View view) {
        if(mBinding.btnLogin.getVisibility()==View.VISIBLE) {
            // User wants to create new account. Display relevant buttons and hide login
            showCredentials(true);
            mBinding.btnLogin.setVisibility(View.GONE);
        } else {
            // Get info for new account
            final String username = mBinding.etUsername.getText().toString();
            final String password = mBinding.etPassword.getText().toString();

            if(username.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Make sure to fill in both username and password", Toast.LENGTH_LONG).show();
                return;
            }

            // Make ParseUser object
            ParseUser user = new ParseUser();
            user.setUsername(username);
            user.setPassword(password);
            user.signUpInBackground(new SignUpCallback() {
                public void done(ParseException e) {
                    if (e != null) {
                        Log.e(TAG, "Issue with signing up", e);
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        logInUser(username, password);
                    }
                }
            });
        }
    }

    // Hide username/password and show login/signup buttons
    public void cancelLoginSignup(View view) {
        showCredentials(false);
        mBinding.btnLogin.setVisibility(View.VISIBLE);
        mBinding.btnSignup.setVisibility(View.VISIBLE);
    }

    private void showCredentials(boolean show) {
        int visibility = show ? View.VISIBLE : View.GONE;
        mBinding.etUsername.setVisibility(visibility);
        mBinding.etPassword.setVisibility(visibility);
        mBinding.btnCancel.setVisibility(visibility);
    }

    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        this.startActivity(i);
        finish();
    }
}