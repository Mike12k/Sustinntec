package com.example.sustinntec;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Litros extends AppCompatActivity  {

    private TextView text, vista;
    private TextView txtResull;
    private TextView txtResull2;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensores);

        //conexion entre el texto y los componentes del activity
        vista = (TextView) findViewById(R.id.txtResul);

        txtResull = (TextView) findViewById(R.id.txtCorre2);
        txtResull2 = (TextView) findViewById(R.id.txtVivie2);

        Bundle bundle = this.getIntent().getExtras();
        String usu = (bundle.getString("Corr"));
        String viv = (bundle.getString("Viv"));
        txtResull.setText(usu);
        txtResull2.setText(viv);

vista.setText("18.135" + " Litros");
//esperar 4 segundos para cerrar la actividad y lanzar otra
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Litros.this, Resultados.class);
                Bundle b = new Bundle();
                b.putString("Corr", txtResull.getText().toString());
                b.putString("Viv", txtResull2.getText().toString());
                //Añadimos la información al intent
                intent.putExtras(b);
                startActivity(intent);
            }
        }, 6000); // 3000 = 3seconds

    }

}