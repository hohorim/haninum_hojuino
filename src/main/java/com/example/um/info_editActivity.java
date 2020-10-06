package com.example.um;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class info_editActivity extends AppCompatActivity {

    EditText editText;

    Button btn1;

    Button btn2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_edit);

        editText = findViewById(R.id.ev11);
        btn1 = findViewById(R.id.button_ok);
        btn2 = findViewById(R.id.button_cancel);

        Intent intent = getIntent();
        String text = intent.getStringExtra("BeforeText");
        if (text != null) {
            editText.setText(text);
        }

        btn1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("AfterText", editText.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                setResult(RESULT_CANCELED);
                finish();
            }
        });

    }
}
