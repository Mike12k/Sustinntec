package com.example.sustinntec;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Tipo  extends AppCompatActivity {
    private ImageButton btnAutom;
    private ImageButton btnManual;
    private Button btnCerrar;
    private TextView txtCorreo;
    private TextView txtVivie;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipo);

        btnAutom= (ImageButton) findViewById(R.id.btnAut);
        btnManual = (ImageButton) findViewById(R.id.btnMan);
        txtCorreo = (TextView) findViewById(R.id.txtCorre);
        txtVivie = (TextView) findViewById(R.id.txtVivie);
        btnCerrar = (Button) findViewById(R.id.btnCerrar);

        //Recuperamos la informacion pasada en el intent
        Bundle bundle = this.getIntent().getExtras();
        String corre = (bundle.getString("Corr"));
        String vivi = (bundle.getString("Viv"));
        txtVivie.setText(vivi);
        txtCorreo.setText(corre);

        btnAutom.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Tipo.this, Configuracion.class);
                Bundle b = new Bundle();
                b.putString("Corr", txtCorreo.getText().toString());
                b.putString("Viv", txtVivie.getText().toString());
                //Anadimos la informacion al intent
                intent.putExtras(b);
                startActivity(intent);
            }
        });
        btnCerrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DatabaseReference dbRef =
                        FirebaseDatabase.getInstance().getReference()
                                .child("Sesión");

                Map<String, String> vivienda = new HashMap<>();
                vivienda.put("Tipo", "false");
                vivienda.put("User", "");

                dbRef.child("Estado").setValue(vivienda);

                Intent intent = new Intent(Tipo.this, ListadoU.class);
                Bundle b = new Bundle();
                b.putString("Corr", txtCorreo.getText().toString());
                b.putString("Viv", txtVivie.getText().toString());
                //Anadimos la informacion al intent
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        btnManual.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Tipo.this, ConfiguracionMa.class);
                Bundle b = new Bundle();
                b.putString("Corr", txtCorreo.getText().toString());
                b.putString("Viv", txtVivie.getText().toString());
                //Anadimos la informacion al intent
                intent.putExtras(b);
                startActivity(intent);
            }
        });

    }

    }
