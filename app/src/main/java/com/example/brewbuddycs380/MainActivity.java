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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * The main activity of the Brew Buddy application.
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


        // Change the status bar color to enhance the visual appearance
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.statusbar_main));
        }

        // Sets the app view to the main activity (login page)
        setContentView(R.layout.activity_main);
        changeFragment(new LoginFragment());

    }

    public void changeFragment(Fragment newFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, newFragment);
        fragmentTransaction.commit();
    }
}
