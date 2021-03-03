package com.example.sustinntec;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Informacion extends AppCompatActivity {

    private Button btnUbu;
    private Button btnReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion);

        btnUbu = (Button) findViewById(R.id.btnUbi);
        btnReg = (Button) findViewById(R.id.btnRegg);

        btnUbu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Informacion.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        btnReg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Informacion.this, Login.class);
                startActivity(intent);
            }
        });

    }

}
