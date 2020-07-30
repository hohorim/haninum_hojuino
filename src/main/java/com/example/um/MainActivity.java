package com.example.um;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText ID, PW;
    Button login, register;
//    private TextView mTextViewResult;

    String result2;
    private static String IP_ADDRESS = "192.168.0.140";
    private static String TAG = "phptest";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ID = findViewById(R.id.ID);
        PW = findViewById(R.id.PW);

        login = (Button)findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    String id = ID.getText().toString();
                    String password = PW.getText().toString();

                    InsertData task = new InsertData();
                    task.execute("http://" + IP_ADDRESS + "/login.php", id,password);

//                    Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_LONG).show();


//                    Intent intent = new Intent(MainActivity.this, MainView.class);
//                    startActivity(intent);

                }
            });
//            }
//        });

        register = (Button)findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, user_signupActivity.class);
                startActivity(intent);
            }
        });



    }
//
//    void login(){
//        try{
//            httpclient=new DefaultHttpClient();
//            httppost= new HttpPost("http://내 컴퓨터 아이피:8080/logcheck.php");
//            // 여기서는 가상 AVD로 구현하기 때문에 내 아이피 주소를 사용
//            // 실제 구동할 때는 공용 서버 주소를 사용
//            nameValuePairs = new ArrayList<NameValuePair>(2);
//            nameValuePairs.add(new BasicNameValuePair("id",inputID.getText().toString()));
//            nameValuePairs.add(new BasicNameValuePair("password",inputPW.getText().toString()));
//            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//            response=httpclient.execute(httppost);
//            ResponseHandler<String> responseHandler = new BasicResponseHandler();
//            final String response = httpclient.execute(httppost, responseHandler);
//            System.out.println("Response :" + response); //메시지 요청이 제대로 됬는지 확인용!
//            runOnUiThread(new Runnable() {
//                public void run() {
//                    dialog.dismiss();
//                }
//            });
//
//            if(response.equalsIgnoreCase("User Found")){
//                runOnUiThread(new Runnable() {
//                    public void run() {
//                        Toast.makeText(Login.this,"Login Success", Toast.LENGTH_SHORT).show();
//                        //로그인에 성공하면 토스트메시지 출력하고,
//                    }
//                });
//                startActivity(new Intent(Login.this,TabView.class));
//                //로그인 성공시 다음 화면으로 넘어감!
//                finish();
//            }else{
//                Toast.makeText(Login.this,"Login Fail", Toast.LENGTH_SHORT).show();
//            }
//        }catch(Exception e){
//            dialog.dismiss();
//            System.out.println("Exception : " + e.getMessage());
//        }
//    }
//}


    class InsertData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(MainActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
//            mTextViewResult.setText(result);
            result2 =result;

            if(result.equals("User Found"))
            {
                                    Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_LONG).show();


                    Intent intent = new Intent(MainActivity.this, MainView.class);
                    startActivity(intent);
            }
            else
            {
                Toast.makeText(getApplicationContext(), "아이디 또는 비밀번호가 다릅니다. ", Toast.LENGTH_LONG).show();
            }


            Log.d(TAG, "POST response  - " + result);





        }


        @Override
        protected String doInBackground(String... params) {

            String id = (String)params[1];
            String password = (String)params[2];

            String serverURL = (String)params[0];
            String postParameters = "id=" + id + "&password=" + password ;


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
