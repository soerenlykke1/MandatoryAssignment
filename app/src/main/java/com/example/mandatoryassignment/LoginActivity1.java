package com.example.mandatoryassignment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;



public class LoginActivity1 extends AppCompatActivity {
    public static final String PREF_FILE_NAME = "loginPref";
    public static final String USERNAME = "USERNAME";
    public static final String PASSWORD = "PASSWORD";
    private SharedPreferences preferences;
    private EditText usernameField;
    private EditText passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        preferences = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);
        usernameField = findViewById(R.id.LoginUserNameEditText);
        passwordField = findViewById(R.id.LoginPasswordEditText);
        String username = preferences.getString(USERNAME, null);
        String password = preferences.getString(PASSWORD, null);
        if (username != null && password != null) {
            usernameField.setText(username);
            passwordField.setText(password);
        }
    }

    public void loginButtonClicked(View view) {
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();
        boolean ok = LoginChecker.INSTANCE.Check(username, password);
        if (ok) {
            CheckBox checkBox = findViewById(R.id.LoginPasswordCheckbox);
            SharedPreferences.Editor editor = preferences.edit();
            if (checkBox.isChecked()) {
                editor.putString(USERNAME, username);
                editor.putString(PASSWORD, password);
            } else {
                editor.remove(USERNAME);
                editor.remove(PASSWORD);
            }
            editor.apply();
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(USERNAME, username);
            startActivity(intent);
        } else {
            usernameField.setError("Wrong username or password");
            TextView messageView = findViewById(R.id.LoginMessageTextView);
            messageView.setText("Username + password not valid");
        }
    }
}