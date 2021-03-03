package com.example.sustinntec;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Proximidad extends AppCompatActivity {
    private SensorManager mgr, sensorManager;
    private Sensor temp, sensor;
    private TextView text;
    SensorEventListener sensorEventListener;
    private StringBuilder msg = new StringBuilder(2048);
    private TextView  vista;

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

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        if(sensor==null){
            Toast.makeText(getApplicationContext(), "No se cuenta con el sensor, lo sentimos", Toast.LENGTH_SHORT).show();
            finish();}
        //esperar 4 segundos para cerrar la actividad y lanzar otra
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Proximidad.this, Resultados.class);
                Bundle b = new Bundle();
                b.putString("Corr", txtResull.getText().toString());
                b.putString("Viv", txtResull2.getText().toString());
                //Añadimos la información al intent
                intent.putExtras(b);
                startActivity(intent);
            }
        }, 6000); // 3000 = 3seconds

        sensorEventListener=new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if (sensorEvent.values[0] < sensor.getMaximumRange()) {
                    // Detected something nearby
                vista.setText("Detectado");
                    //getWindow().getDecorView().setBackgroundColor(Color.RED);
                } else {
                    // Nothing is nearby
                    vista.setText("No Detectado");
                   // getWindow().getDecorView().setBackgroundColor(Color.GREEN);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }

        };
start();
    }
    public void start(){
        sensorManager.registerListener(sensorEventListener,sensor,200+1000);
    }
    public void stop(){
        sensorManager.unregisterListener(sensorEventListener);
    }

    @Override
    protected void onPause() {
        stop();
        super.onPause();
    }

    @Override
    protected void onResume() {
        start();
        super.onResume();
    }

}