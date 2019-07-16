package com.example.mandatoryassignment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import static com.example.mandatoryassignment.LoginActivity1.USERNAME;


public class MainActivity extends AppCompatActivity {
    public static final int MY_PERMISSION_REQUEST_CODE = 1234;
    private String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.MainToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        username = intent.getStringExtra(USERNAME);
        TextView welcomeView = findViewById(R.id.MainHelloTextView);
        welcomeView.setText("Hello, " + username);



    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d("MINE", "Must ask for permission");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSION_REQUEST_CODE);
        } else {
            Log.d("MINE", "Permission already given");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("MINE", "permission given");
                } else {
                    Log.d("MINE", "Permission not given");
                }
        }
    }

    public void LoadCurrentData(View view) {
        Intent intentData = new Intent(this, CurrentDataActivity.class);
        intentData.putExtra(USERNAME, username);
        startActivity(intentData);
    }

    public void LoadAllData(View view) {
        Intent intentData = new Intent(this, AllDataActivity.class);
        intentData.putExtra(USERNAME, username);
        startActivity(intentData);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;
    }
}
