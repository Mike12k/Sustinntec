package com.example.sustinntec;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

public class Temperatura extends Activity implements SensorEventListener {
    private SensorManager mgr;
    private Sensor temp;
    private TextView text;
    private StringBuilder msg = new StringBuilder(2048);
    private TextView txtResull;
    private TextView txtResull2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensores);

        mgr = (SensorManager) this.getSystemService(SENSOR_SERVICE);

        txtResull = (TextView) findViewById(R.id.txtCorre2);
        txtResull2 = (TextView) findViewById(R.id.txtVivie2);

        Bundle bundle = this.getIntent().getExtras();
        String usu = (bundle.getString("Corr"));
        String viv = (bundle.getString("Viv"));
        txtResull.setText(usu);
        txtResull2.setText(viv);

        temp = mgr.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
//        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        text = (TextView) findViewById(R.id.txtResul);
        if(temp==null) {
            Toast.makeText(getApplicationContext(), "No se cuenta con el sensor, lo sentimos", Toast.LENGTH_SHORT).show();
            finish();
        }
        //esperar 4 segundos para cerrar la actividad y lanzar otra
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Temperatura.this, Resultados.class);
                Bundle b = new Bundle();
                b.putString("Corr", txtResull.getText().toString());
                b.putString("Viv", txtResull2.getText().toString());
                //Añadimos la información al intent
                intent.putExtras(b);
                startActivity(intent);
            }
        }, 6000); // 3000 = 3seconds
    }

    @Override
    protected void onResume() {
        mgr.registerListener(this, temp, SensorManager.SENSOR_DELAY_FASTEST);
        super.onResume();
    }

    @Override
    protected void onPause() {
        mgr.unregisterListener(this, temp);
        super.onPause();
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void onSensorChanged(SensorEvent sensorEvent) {
       // float fahrenheit = sensorEvent.values[0] * 9 / 5 + 35;
       // msg.insert(0, "Temperatura " + sensorEvent.values[0] + " Celsius (" +
        msg.insert(0, "Temperatura " + "23.4" + " Celsius (" +
                "35.0"  + " °C)\n");
        text.setText(msg);
        text.invalidate();
    }
}

