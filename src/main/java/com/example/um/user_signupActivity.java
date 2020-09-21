package com.example.um;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.um.gpsActivity.abc;


public class user_signupActivity extends AppCompatActivity {

    private static String IP_ADDRESS = "192.168.0.22";
    private static String TAG = "phptest";

    private EditText mEditTextID;
    private EditText mEditTextPW, mEditTextphone;
    private TextView mTextViewResult;

    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_signup);

        mEditTextID = (EditText)findViewById(R.id.ID);
        mEditTextPW = (EditText)findViewById(R.id.PW);
        mTextViewResult = (TextView)findViewById(R.id.textView_main_result);
        mEditTextphone = (EditText)findViewById(R.id.phoneN);


        mTextViewResult.setMovementMethod(new ScrollingMovementMethod());


        Button buttonInsert = (Button)findViewById(R.id.sign_up);
        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ID = mEditTextID.getText().toString();
                String PW = mEditTextPW.getText().toString();
                String phoneN = mEditTextphone.getText().toString();

                InsertData task = new InsertData();
                task.execute("http://" + IP_ADDRESS + "/insert.php",ID,PW,phoneN);

                ID = mEditTextID.getText().toString();
                mEditTextID.setText("");
                mEditTextPW.setText("");
                mEditTextphone.setText("");

                Toast.makeText(getApplicationContext(), "id : "+ID +" 님의 회원가입이 완료 되었습니다.", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(user_signupActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });

    }



    class InsertData extends AsyncTask<String, Void, String>{
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(user_signupActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            mTextViewResult.setText(result);
            Log.d(TAG, "POST response  - " + result);
        }


        @Override
        protected String doInBackground(String... params) {

            String ID = (String)params[1];
            String PW = (String)params[2];
            String phoneN = (String)params[3];

            String serverURL = (String)params[0];
            String postParameters = "ID=" + ID + "&PW=" + PW +"&phoneN="+phoneN;
            //String postParameters = "ID=" + ID + "&PW=" + PW +"&phoneN="+phoneN + "&a="+abc ;
            Log.d("qq",ID+PW+phoneN);

            try {

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


                bufferedReader.close();


                return sb.toString();


            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }
    }


}