package com.example.brewbuddycs380;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class LoginFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        // Find the login button from the main activity
        Button loginBtn = (Button) view.findViewById(R.id.login);

        // Set a click listener on the login button
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                // Set username and password EditText fields
                EditText usernameEditText = view.findViewById(R.id.username);
                EditText passwordEditText = view.findViewById(R.id.password);

                // Get text from username and password EditText fields
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // Check if any fields are empty
                if (username.isEmpty() || password.isEmpty()) {
                    // Display a toast message asking the user to fill in all fields
                    Toast.makeText(LoginFragment.this.getActivity(), "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean success;
                try {
                    Toast.makeText(LoginFragment.this.getActivity(), "Logging in", Toast.LENGTH_SHORT).show();
                    success = UserService.login(username, password);

                    if (success) {
                        UserService.setUsername(username);
                        usernameEditText.getText().clear();
                        passwordEditText.getText().clear();

                        if (UserService.getUserPreferences(username) == null)
                            LoginFragment.this.startActivity(new Intent(LoginFragment.this.getActivity(), QuestionActivity.class));
                        else
                            LoginFragment.this.startActivity(new Intent(LoginFragment.this.getActivity(), RecommendationScreen.class));
                    } else {
                        Toast.makeText(LoginFragment.this.getActivity(), "Login failed", Toast.LENGTH_SHORT).show();
                    }
                } catch (UserServiceException e) {
                    Toast.makeText(LoginFragment.this.getActivity(), "Network error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        TextView createAccountTxt = view.findViewById(R.id.createAccount);

        // Set a click listener on the text
        createAccountTxt.setOnClickListener(v -> {
            // Go to the create account activity
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.changeFragment(new CreateAccountFragment());
        });



        return view;
    }
}

