package com.example.um;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class gpsActivity extends AppCompatActivity{

    public double x, y;
    private LocationManager lm;
    private LocationListener locationListener;

    public TextView gps;
    private TextView TextViewResult;

    private static String IP_ADDRESS = "192.168.0.37";
    private static String TAG = "phptest";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gps);

        Button buttonSetting = findViewById(R.id.button1);

        buttonSetting.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(intent,0);
            }
        });

        gps = (TextView) findViewById(R.id.gps_text);
        TextViewResult = (TextView)findViewById(R.id.textView_result);

        TextViewResult.setMovementMethod(new ScrollingMovementMethod());

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationListener = new MyLocationListener();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        lm.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0,
                0,
                locationListener);



    }

    public class MyLocationListener implements LocationListener
    {
        public void onLocationChanged(Location loc){
            if(loc != null) {
                Toast.makeText(getBaseContext(),
                        "Location changed : Lat : " + loc.getLatitude() +
                                "Lng :" + loc.getLongitude(),
                        Toast.LENGTH_SHORT).show();
                gps.setText("위도 :" + loc.getLatitude() + ", 경도 : " + loc.getLongitude());
                //abc = "위도 : "+loc.getLatitude();
                x = loc.getLatitude();
                y = loc.getLongitude();
                String GPS = String.valueOf(gps);

                InsertData task = new InsertData();
                task.execute("http://" + IP_ADDRESS + "/location.php", GPS);

                gps.setText("");

                //gps.setText("");

                Toast.makeText(getApplicationContext(), "완료", Toast.LENGTH_LONG).show();

            }

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch(requestCode) {
            case 0:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    public class InsertData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        // ProgressDialog progressDialog1;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(gpsActivity.this, "위치 받아오는 중",null, true);
            // progressDialog1 = ProgressDialog.show(gpsActivity.this, "wait", null, true, true);
            Log.d("tlqkf2", "이거되냐");
        }

        public void execute(String s) {
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();

            //  progressDialog1.dismiss();
            //  TextViewResult.setText(s);
            //Log.d(TAG, "POST response -" + s);



        }

        @Override
        protected String doInBackground(String... params) {

            String GPS = (String)params[1];

            String serverURL = (String)params[0];
            String postParameters = "gps=" + GPS;
            Log.d("qq", GPS);

            try{
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();
                Log.d("tlqkf", "이거되냐");
                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }
                Log.d("tlqkf3", "이거되냐");
                bufferedReader.close();

                return sb.toString();

            } /*catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } */catch (Exception e){
                Log.d(TAG, "InsertData: Error ", e);

                return new String("Error2: " + e.getMessage());
            }
            // return null;
        }
    }
}
