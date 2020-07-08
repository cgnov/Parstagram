package com.example.parstagram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";

    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnSignup;
    private Button btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (ParseUser.getCurrentUser() != null) {
            goMainActivity();
        }

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignup = findViewById(R.id.btnSignup);
        btnCancel = findViewById(R.id.btnCancel);
    }

    public void logInUser(View view) {
        if(btnSignup.getVisibility()==View.VISIBLE) {
            // User wants to log in. Display relevant buttons and hide signup button
            btnSignup.setVisibility(View.GONE);
            etUsername.setVisibility(View.VISIBLE);
            etPassword.setVisibility(View.VISIBLE);
            btnCancel.setVisibility(View.VISIBLE);
        } else {
            // Get login credentials
            final String username = etUsername.getText().toString();
            final String password = etPassword.getText().toString();

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
        if(btnLogin.getVisibility()==View.VISIBLE) {
            // User wants to create new account. Display relevant buttons and hide login
            btnLogin.setVisibility(View.GONE);
            etUsername.setVisibility(View.VISIBLE);
            etPassword.setVisibility(View.VISIBLE);
            btnCancel.setVisibility(View.VISIBLE);
        } else {
            // Get info for new account
            final String username = etUsername.getText().toString();
            final String password = etPassword.getText().toString();

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

    public void cancelLoginSignup(View view) {
        btnSignup.setVisibility(View.VISIBLE);
        btnLogin.setVisibility(View.VISIBLE);
        etUsername.setVisibility(View.GONE);
        etPassword.setVisibility(View.GONE);
        btnCancel.setVisibility(View.GONE);
    }

    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        this.startActivity(i);
        finish();
    }
}