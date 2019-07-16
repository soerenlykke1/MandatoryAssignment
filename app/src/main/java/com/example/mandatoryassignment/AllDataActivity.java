package com.example.mandatoryassignment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.mandatoryassignment.LoginActivity1.USERNAME;


public class AllDataActivity extends AppCompatActivity{

private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_data);

        Toolbar toolbar = findViewById(R.id.AllDataToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("All Data");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



    }
    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        username = intent.getStringExtra(USERNAME);
        ReadTask task = new ReadTask();
        task.execute("https://berthabackendrestprovider.azurewebsites.net/api/data/" + username);
    }

    private class ReadTask extends ReadHttpTask {
        @Override
        protected void onPostExecute(CharSequence jsonString) {

            final List<DataObject> datalist = new ArrayList<>();
            try {
                JSONArray array = new JSONArray(jsonString.toString());
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    int deviceId = obj.getInt("deviceId");
                    double pm25 = obj.getDouble("pm25");
                    double pm10 = obj.getDouble("pm10");
                    int co2 = obj.getInt("co2");
                    int o3 = obj.getInt("o3");
                    double pressure = obj.getDouble("pressure");
                    double temperature = obj.getDouble("temperature");
                    double humidity = obj.getDouble("humidity");
                    long utc = obj.getLong("utc");
                    double latitude = obj.getDouble("latitude");
                    double longitude = obj.getDouble("longitude");
                    int noise = obj.getInt("noise");
                    String userId = obj.getString("userId");
                    DataObject dataObject = new DataObject(deviceId, pm25, pm10, co2, o3, pressure, temperature, humidity, utc, latitude, longitude, noise, userId);
                    datalist.add(dataObject);
                }




                ListView listView = findViewById(R.id.AllDataListView);
                ArrayAdapter<DataObject> adapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_list_item_1, datalist);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getBaseContext(), BepActivity.class);
                    DataObject dataObject = (DataObject) parent.getItemAtPosition(position);
                    intent.putExtra(BepActivity.DATA, dataObject);
                    startActivity(intent);
                }
            });
            } catch (JSONException ex) {
                cancel(true);
                TextView messageTextView = findViewById(R.id.AllDataMessage);
                messageTextView.setText(ex.getMessage());
                Log.e("DATALIST", ex.getMessage());


            }
        }
        @Override
        protected void onCancelled(CharSequence message) {
            TextView messageTextView = findViewById(R.id.AllDataMessage);
            messageTextView.setText(message);
            Log.e("DATALIST", message.toString());
        }
    }
    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;
    }
}
