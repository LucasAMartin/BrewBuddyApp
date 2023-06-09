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

public class CreateAccountFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_account, container, false);

        // Find create account button
        Button createAccountButton = view.findViewById(R.id.createAccount);

        // Set onClickListener for create account button
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                // Set username, password, and confirm password EditText fields
                EditText usernameEditText = view.findViewById(R.id.username);
                EditText passwordEditText = view.findViewById(R.id.password);
                EditText confirmPasswordEditText = view.findViewById(R.id.confirmPassword);

                // Get text from username, password, and confirm password EditText fields
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String confirmPassword = confirmPasswordEditText.getText().toString();

                // Check if any fields are empty
                if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    // Display toast message asking user to fill in all fields
                    Toast.makeText((getActivity().getApplicationContext()), "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    // Display toast message indicating passwords do not match
                    Toast.makeText((getActivity().getApplicationContext()), "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    boolean success = UserService.createAccount(username, password);
                    if (success) {
                        Toast.makeText((getActivity().getApplicationContext()), "Account created successfully", Toast.LENGTH_SHORT).show();
                        UserService.setUsername(username);
                        usernameEditText.getText().clear();
                        passwordEditText.getText().clear();
                        confirmPasswordEditText.getText().clear();
                        startActivity(new Intent(getActivity(), QuestionActivity.class));
                    } else {
                        Toast.makeText((getActivity().getApplicationContext()), "Account creation failed", Toast.LENGTH_SHORT).show();
                    }
                } catch (AccountTakenException e) {
                    Toast.makeText((getActivity().getApplicationContext()), "Account username taken", Toast.LENGTH_SHORT).show();
                } catch (UserServiceException e) {
                    Toast.makeText((getActivity().getApplicationContext()), "Network error", Toast.LENGTH_SHORT).show();

                }
            }
        });
        return view;
    }
}

