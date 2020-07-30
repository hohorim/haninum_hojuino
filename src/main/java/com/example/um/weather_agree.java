package com.example.um;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class weather_agree extends AppCompatActivity {
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    Button btn_weather;
    TextView textview_weather;

    Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_agree);

        btn_weather=(Button)findViewById(R.id.btn_weather);
        btn_weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                request();
            }
        });
        textview_weather=findViewById(R.id.textview_weather);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void request(){
        String title = "동의 요청";
        String message= "날씨에 따른 알람수신을 동의하시겠습니까?";
        String titleButtonYes = "예";
        String titleButtonNo = "아니오";
        AlertDialog dialog = (AlertDialog) makeRequestDialog(title, message, titleButtonYes, titleButtonNo);
        dialog.show();

        textview_weather.setText("대화상자 표시중...");
    }

    private AlertDialog makeRequestDialog(String title, String message, String titleButtonYes, String titleButtonNo){
        AlertDialog.Builder requestDialog = new AlertDialog.Builder(this);
        requestDialog.setTitle(title);
        requestDialog.setMessage(message);
        requestDialog.setPositiveButton(titleButtonYes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                textview_weather.setText("알람 수신을 동의하셨습니다.");

            }
        });

        requestDialog.setNegativeButton(titleButtonNo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                textview_weather.setText("알람 수신을 거부하셨습니다.");
            }
        });
        return requestDialog.create();


    }
}
