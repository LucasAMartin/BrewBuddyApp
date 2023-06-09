package com.example.brewbuddycs380;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

/**
 * This activity is responsible for creating a new user account.
 */
public class CreateAccount extends AppCompatActivity {

    /**
     * Called when the activity is starting.
     *
     * @param savedInstanceState the saved instance state Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        // changes the status bar color so it looks prettier
        if (Build.VERSION.SDK_INT >= 21)
        {
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.statusbar_main));
        }

        // Find create account button
        Button createAccountButton = findViewById(R.id.createAccount);

        // Set onClickListener for create account button
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Set username, password, and confirm password EditText fields
                EditText usernameEditText = findViewById(R.id.username);
                EditText passwordEditText = findViewById(R.id.password);
                EditText confirmPasswordEditText = findViewById(R.id.confirmPassword);

                // Get text from username, password, and confirm password EditText fields
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String confirmPassword = confirmPasswordEditText.getText().toString();

                // Check if any fields are empty
                if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    // Display toast message asking user to fill in all fields
                    Toast.makeText(getApplicationContext(), "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    // Display toast message indicating passwords do not match
                    Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    boolean success = UserService.createAccount(username, password);
                    if (success) {
                        Toast.makeText(getApplicationContext(), "Account created successfully", Toast.LENGTH_SHORT).show();
                        UserService.setUsername(username);
                        usernameEditText.getText().clear();
                        passwordEditText.getText().clear();
                        confirmPasswordEditText.getText().clear();
                        startActivity(new Intent(CreateAccount.this, QuestionActivity.class));
                    } else {
                        Toast.makeText(getApplicationContext(), "Account creation failed", Toast.LENGTH_SHORT).show();
                    }
                } catch (AccountTakenException e) {
                    Toast.makeText(getApplicationContext(), "Account username taken", Toast.LENGTH_SHORT).show();
                } catch (UserServiceException e) {
                    Toast.makeText(getApplicationContext(), "Network error", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}
