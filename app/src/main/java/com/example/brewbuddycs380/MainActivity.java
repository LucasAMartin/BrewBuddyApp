package com.example.brewbuddycs380;

import java.sql.*;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.content.ContextCompat;

/**
 * The main activity of the BrewBuddy application.
 * Handles the login process and navigation to other activities.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    /**
     * Called when the activity is first created.
     * Initializes the UI components and sets up click listeners.
     *
     * @param savedInstanceState the saved instance state
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Changes the status bar color to enhance the visual appearance
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.statusbar_main));
        }

        // Sets the app view to the main activity (login page)
        setContentView(R.layout.activity_main);

        // Find the login button from the main activity
        Button loginBtn = (Button) findViewById(R.id.login);
        // Set a click listener on the login button
        loginBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Set username and passwordEditText fields
                EditText usernameEditText = (EditText) findViewById(R.id.username);
                EditText passwordEditText = (EditText) findViewById(R.id.password);

                // Get text from username and passwordEditText fields
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // Check if any fields are empty
                if (username.isEmpty() || password.isEmpty()) {
                    // Display a toast message asking the user to fill in all fields
                    Toast.makeText(getApplicationContext(), "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean success = false;
                try {
                    success = UserService.login(username, password);
                    if (success) {
                        UserService.setUsername(username);
                        usernameEditText.getText().clear();
                        passwordEditText.getText().clear();
                        if (UserService.getUserPreferences(username) == null)
                            startActivity(new Intent(MainActivity.this, QuestionActivity.class));
                        else
                            startActivity(new Intent(MainActivity.this, RecommendationScreen.class));
                    } else {
                        Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_SHORT).show();
                    }
                } catch (UserServiceException e) {
                    Toast.makeText(getApplicationContext(), "Network error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        TextView createAccountTxt = findViewById(R.id.createAccount);
        // Set a click listener on the text
        createAccountTxt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Go to the create account activity
                startActivity(new Intent(MainActivity.this, CreateAccount.class));
            }
        });
    }
}
