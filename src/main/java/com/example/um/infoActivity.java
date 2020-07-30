package com.example.um;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class infoActivity extends AppCompatActivity {

    Button b1;

    TextView text_school;

    final static int ACT_SETTING = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);
        b1  = findViewById(R.id.edit_button);
        text_school = findViewById(R.id.textView11);

        b1.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {
                Intent intent = new Intent(infoActivity.this, info_editActivity.class);
                intent.putExtra("BeforeText", text_school.getText().toString());
                startActivityForResult(intent, ACT_SETTING);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACT_SETTING) {
            if (resultCode == RESULT_OK) {
                text_school.setText(data.getStringExtra("AfterText"));
            }

        }

    }
}
