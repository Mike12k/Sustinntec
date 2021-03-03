package com.example.sustinntec;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Administrador extends AppCompatActivity {
    private Button btnRes;
    private ImageButton btnTemp;
    private ImageButton  btnDet;
    private ImageButton  btnLit;
    private Button btnCerar;

    private final static String NOMBRE_DIRECTORIO = "ShowerLog";
    private final static String ETIQUETA_ERROR = "ERROR";

    private TextView txtResull;
    private TextView txtResull2;
    private TextView txtUsuario;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrador);


//Obtenemos las referencias a los controles
        txtResull = (TextView) findViewById(R.id.txtCorre);
        txtResull2 = (TextView) findViewById(R.id.txtVivie);
        txtUsuario = (TextView) findViewById(R.id.txtUser);

        btnRes = (Button) findViewById(R.id.btnRe);
        btnCerar = (Button) findViewById(R.id.btnCerrar);
        btnDet = (ImageButton) findViewById(R.id.btnVent);
        btnTemp = (ImageButton) findViewById(R.id.btnUsua);
        btnLit = (ImageButton) findViewById(R.id.btnVivien);

        //Bundle bundle = this.getIntent().getExtras();
       // String usu = (bundle.getString("Corr"));
      // String viv = (bundle.getString("Viv"));
      // txtResull.setText(usu);
        //txtResull2.setText(viv);



        btnTemp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Administrador.this, ListadoU2.class);
                startActivity(intent);
            }
        });

        btnDet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Administrador.this, ListadoVen.class);
                startActivity(intent);
            }
        });

        btnCerar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Administrador.this,Login.class);
                startActivity(intent);
            }
        });

        btnLit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Administrador.this, ListadoV2.class);
                startActivity(intent);
            }
        });


    }}
