package com.example.mandatoryassignment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

public class CurrentDataActivity extends AppCompatActivity {

    public static final String LOG_TAG = "REST_JSON";
    public static final String LOG_TAG2 = "locations";
    public static final String GetUrl = "https://berthawristbandrestprovider.azurewebsites.net/api/wristbanddata";
    private LocationManager locationManager;
    private LocationListener locationListener;
    private TextView latitudeView, longitudeView;
    private static final int minimumTime = 5000;
    private static final int minimumDistance = 0;
    private String username;
    private double latitude, longitude;
    private GestureDetector gestureDetector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_data);

        Toolbar toolbar = findViewById(R.id.CurrentDataToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Send Data");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        username = intent.getStringExtra(LoginActivity1.USERNAME);

        final ReadJSONFeedTask task = new ReadJSONFeedTask();
        task.execute(GetUrl);

        final DataObjectReadTask postTask = new DataObjectReadTask();
        postTask.execute(GetUrl);



        latitudeView = findViewById(R.id.CurrentDataActivityLatitudeTextView);
        longitudeView = findViewById(R.id.CurrentDataActivityLongitudeTextView);
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.w(LOG_TAG2, "Missing location permission");

            return;
        }
        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        showLocation(lastKnownLocation);
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                showLocation(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minimumTime, minimumDistance, locationListener);
        } catch (SecurityException ex) {
            Log.e(LOG_TAG2, ex.toString());
        }

        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return DoIt(e1, e2);
            }

            private boolean DoIt(MotionEvent e1, MotionEvent e2) {
                boolean leftMovement = e1.getX() < e2.getX();
                if (leftMovement) {
                    finish();
                }
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return DoIt(e1, e2);
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    



    private void showLocation(Location location) {
        if (location == null) {
            latitudeView.setText("No location found!");
            longitudeView.setText("No location found!");
            return;
        }
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        latitudeView.setText("Latitude: " + latitude);
        longitudeView.setText("Longitude: " + longitude);
    }




    private class ReadJSONFeedTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                return readJSonFeed(urls[0]);
            } catch (IOException ex) {
                Log.e(LOG_TAG, ex.toString());
                cancel(true);
                return ex.toString();
            }
        }


        @Override
        protected void onPostExecute(String jsonString) {

            try {
                JSONObject jsonObject = new JSONObject(jsonString);

                TextView pm25 = findViewById(R.id.CurrentDataActivityPm25TextView);
                pm25.setText("Pm25 = " + jsonObject.getDouble("pm25"));

                TextView pm10 = findViewById(R.id.CurrentDataActivityPm10TextView);
                pm10.setText("Pm10 = " + jsonObject.getDouble("pm10"));

                TextView co2 = findViewById(R.id.CurrentDataActivityCo2TextView);
                co2.setText("Co2 = " + jsonObject.getInt("co2"));

                TextView o3 = findViewById(R.id.CurrentDataActivityO3TextView);
                o3.setText("O3 = " + jsonObject.getInt("o3"));

                TextView pressure = findViewById(R.id.CurrentDataActivityPressureTextView);
                pressure.setText("Pressure = " + jsonObject.getDouble("pressure"));

                TextView temperature = findViewById(R.id.CurrentDataActivityTemperatureTextView);
                temperature.setText("Temperature = " + jsonObject.getDouble("temperature"));

                TextView humidity = findViewById(R.id.CurrentDataActivityHumidityTextView);
                humidity.setText("Humidity: " + jsonObject.getDouble("humidity"));

            } catch (JSONException ex) {
                cancel(true);
                TextView messageTextView = findViewById(R.id.AllDataMessage);
                messageTextView.setText(ex.getMessage());
                Log.e("CurrentDataJsonProblem", ex.getMessage());
            }
        }


        @Override
        protected void onCancelled(String message) {
            TextView messageTextView = findViewById(R.id.AllDataMessage);
            messageTextView.setText(message);
            Log.e("CurrentDataCancelled", message.toString());
        }
    }

    private String readJSonFeed(String urlString) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        final InputStream content = openHttpConnection(urlString);
        final BufferedReader reader = new BufferedReader(new InputStreamReader(content));
        while (true) {
            final String line = reader.readLine();
            if (line == null)
                break;
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }




    private InputStream openHttpConnection(final String urlString) throws IOException {
        final URL url = new URL(urlString);
        final URLConnection conn = url.openConnection();
        if (!(conn instanceof HttpURLConnection))
            throw new IOException("Not an HTTP connection");
        final HttpURLConnection httpConn = (HttpURLConnection) conn;
        httpConn.setAllowUserInteraction(false);
        // No user interaction like dialog boxes, etc.
        httpConn.setInstanceFollowRedirects(true);
        // follow redirects, response code 3xx
        httpConn.setRequestMethod("GET");
        httpConn.connect();
        final int response = httpConn.getResponseCode();
        if (response == HttpURLConnection.HTTP_OK) {
            return httpConn.getInputStream();
        } else {
            throw new IOException("HTTP response not OK");
        }
    }

    private class DataObjectReadTask extends ReadHttpTask{
        @Override
        protected void onPostExecute(CharSequence jsonString) {
            Gson gson = new GsonBuilder().create();
            TextView messageView = findViewById(R.id.CurrentDataActivityMessageTextView);
            final DataObject dataObject = gson.fromJson(jsonString.toString(), DataObject.class);
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("deviceId", dataObject.getDeviceId());
                jsonObject.put("pm25", dataObject.getPm25());
                jsonObject.put("pm10", dataObject.getPm10());
                jsonObject.put("co2", dataObject.getCo2());
                jsonObject.put("o3", dataObject.getO3());
                jsonObject.put("pressure", dataObject.getPressure());
                jsonObject.put("temperature", dataObject.getTemperature());
                jsonObject.put("humidity", dataObject.getHumidity());
                jsonObject.put("utc", new Date().getTime());
                jsonObject.put("latitude", latitude);
                jsonObject.put("longitude", longitude);
                jsonObject.put("noise", 10);
                jsonObject.put("userId", username);


                String jsonFile = jsonObject.toString();
                PostDataObjectTask postTask = new PostDataObjectTask();
                postTask.execute("https://berthabackendrestprovider.azurewebsites.net/api/data/", jsonFile);
            }
            catch (JSONException ex) {
                cancel(true);
                TextView messageTextView = findViewById(R.id.AllDataMessage);
                messageTextView.setText(ex.getMessage());
                Log.e("PostData", ex.getMessage());
            }
        }

        @Override
        protected void onCancelled(CharSequence message) {
            super.onCancelled();
            TextView view = findViewById(R.id.CurrentDataActivityMessageTextView);
            view.setText(message);
        }
    }

    private class PostDataObjectTask extends AsyncTask<String, Void, CharSequence> {
        @Override
        protected CharSequence doInBackground(String... params) {
            String urlString = params[0];
            String jsonFile = params[1];
            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream());
                outputStreamWriter.write(jsonFile);
                outputStreamWriter.flush();
                outputStreamWriter.close();
                int responsecode = connection.getResponseCode();
                if (responsecode != 200) {
                    String responseMessage = connection.getResponseMessage();
                    throw new IOException("HTTP Response code: " + responsecode + " " + responseMessage);
                }
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
                String line = reader.readLine();
                return line;
            } catch (MalformedURLException ex) {
                cancel(true);
                String message = ex.getMessage() + " " + urlString;
                Log.e("PostObject", message);
                return message;

            } catch (IOException ex) {
                cancel(true);
                Log.e("PostObject", ex.getMessage());
                return ex.getMessage();
            }
        }
        @Override
        protected void onPostExecute(CharSequence charSequence) {
            super.onPostExecute(charSequence);
            TextView messageView = findViewById(R.id.CurrentDataActivityMessageTextView);
            messageView.setText(charSequence);
            Log.e("PostObject", charSequence.toString());
        }

        @Override
        protected void onCancelled(CharSequence charSequence) {
            super.onCancelled(charSequence);
            TextView messageView = findViewById(R.id.CurrentDataActivityMessageTextView);
            messageView.setText(charSequence);
            Log.e("PostObject", charSequence.toString());
            finish();
        }

    }

    public void Update(View view) {
        finish();
        startActivity(getIntent());
    }


    public void back(View view) {
        finish();
    }

    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;
    }
}
