package com.example.um;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class gpsActivity extends AppCompatActivity {

    private LocationManager lm;
    private LocationListener locationListener;
    TextView recieveText, messageText, messageText2;
    EditText editTextAddress, editTextPort ;
    // Button connectBtn, clearBtn;
    Button gps_btn;

    Socket socket = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gps);

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener();

        gps_btn = (Button) findViewById(R.id.gps_btn);
        gps_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(intent, 0);
            }
        });
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        lm.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0,
                0,
                locationListener);
        //앱 기본 스타일 설정
        getSupportActionBar().setElevation(0);

        //connectBtn = (Button) findViewById(R.id.buttonConnect);
        // clearBtn = (Button) findViewById(R.id.buttonClear);
        editTextAddress = (EditText) findViewById(R.id.addressText);
        editTextPort = (EditText) findViewById(R.id.portText);
        recieveText = (TextView) findViewById(R.id.textViewReciev);
        messageText = (TextView) findViewById(R.id.messageText);
        messageText2 = (TextView) findViewById(R.id.messageText2);
        editTextAddress.setVisibility(View.INVISIBLE);
        editTextPort.setVisibility(View.INVISIBLE);
        recieveText.setVisibility(View.INVISIBLE);
/*
        //connect 버튼 클릭
        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyClientTask myClientTask = new MyClientTask(editTextAddress.getText().toString(), Integer.parseInt(editTextPort.getText().toString()), messageText.getText().toString(), messageText2.getText().toString());
                myClientTask.execute();
                //messageText.setText("");
            }
        });

        //clear 버튼 클릭
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recieveText.setText("");
                messageText.setText("");
            }
        });*/
    }
    private class MyLocationListener implements LocationListener
    {


        public void onLocationChanged(Location loc){
            if(loc != null) {
                Toast.makeText(getBaseContext(),"x: " + loc.getLatitude() + "y :" + loc.getLongitude(), Toast.LENGTH_SHORT).show();
                MyClientTask myClientTask = new MyClientTask(editTextAddress.getText().toString(), Integer.parseInt(editTextPort.getText().toString()), messageText.getText().toString(), messageText2.getText().toString());
                myClientTask.execute();
                messageText.setText("" + loc.getLatitude());
                messageText2.setText("" + loc.getLongitude());


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


    //
    public class MyClientTask extends AsyncTask<Void, Void, Void> {
        String dstAddress;
        int dstPort;
        String response = "";
        String myMessage = "";
        String myMessage2 = "";

        //constructor
        MyClientTask(String addr, int port, String message, String message2){
            dstAddress = addr;
            dstPort = port;
            myMessage = message;
            myMessage2 = message2;
        }



        @Override
        protected Void doInBackground(Void... para) {

            Socket socket = null;
            myMessage = myMessage.toString();
            myMessage2 = myMessage2.toString();
            try {
                socket = new Socket(dstAddress, dstPort);
                //송신
                OutputStream out = socket.getOutputStream();
                out.write(myMessage.getBytes());
                out.write(myMessage2.getBytes());

                //수신
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
                byte[] buffer = new byte[1024];
                int bytesRead;
                InputStream inputStream = socket.getInputStream();
                /*
                 * notice:
                 * inputStream.read() will block if no data return
                 */
                while ((bytesRead = inputStream.read(buffer)) != -1){
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                    response += byteArrayOutputStream.toString("UTF-8");
                }
                response = "서버의 응답: " + response;

            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "UnknownHostException: " + e.toString();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "IOException: " + e.toString();
            }finally{
                if(socket != null){
                    try {
                        socket.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            recieveText.setText(response);
            super.onPostExecute(result);
        }
    }

}