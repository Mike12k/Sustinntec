/*Alumna:Diana Laura Cradenas Rocha
  Escuela: Instituto Tecnologico de Matehuala
  Materia: Desarrollo de Aplicaciones para Dispositivos Moviles
  Fecha: 11 de mayo de 2020
 */
package com.example.sustinntec;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class NuevoPV extends AppCompatActivity {

    private Button btnDest, btnReg, btnEli;

    private EditText txtNombre;
    private EditText txtLongitud;
    private EditText txtLatitud;
    private TextView txtRes3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevoedi);

        btnDest= (Button) findViewById(R.id.btnIni);
        btnReg= (Button) findViewById(R.id.btnIni2);
        btnEli= (Button) findViewById(R.id.btnIni3);

        txtNombre= (EditText) findViewById(R.id.txtNom);
        txtLatitud= (EditText) findViewById(R.id.txtLatFin);
        txtLongitud= (EditText) findViewById(R.id.txtLongFin);
        txtRes3= (TextView) findViewById(R.id.txtResultado6);

        btnDest.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
         Destino();
            }});
        btnReg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                act();
            }});
        btnEli.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                eli();
            }});

        //Recuperamos la información pasada en el intent
        Bundle bundle = this.getIntent().getExtras();
        final String nombre=  (bundle.getString("Ven"));
        txtNombre.setText(nombre);
        String text = txtNombre.getText().toString();
        Query query = FirebaseDatabase.getInstance().getReference()
                .child("Ventas")
                .child(text);
        final ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    //String uno = dataSnapshot.child(text).getValue().toString();
                    String dos = dataSnapshot.child("Longitud").getValue().toString();
                    String tres = dataSnapshot.child("Latitud").getValue().toString();
                    txtLatitud.setText(tres);
                    txtLongitud.setText(dos);


                    } else {
                        Toast.makeText(getApplicationContext(), "Usuario inexistente o erroneo, intenta nuevamente", Toast.LENGTH_SHORT).show();
                    }
                }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //
            }
        };
        query.addValueEventListener(eventListener);

    }
        private void Destino(){
        String text = txtNombre.getText().toString();
            String lat = txtLatitud.getText().toString();
            String lon = txtLongitud.getText().toString();
            DatabaseReference dbRef =
                    FirebaseDatabase.getInstance().getReference()
                            .child("Ventas");

            Map<String, String> vivienda = new HashMap<>();
            vivienda.put("Latitud", lat);
            vivienda.put("Longitud", lon);

            dbRef.child(text).setValue(vivienda);
            Toast.makeText(getApplicationContext(), "Punto de venta registrado", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(NuevoPV.this, Administrador.class);
            //Creamos la información a pasar entre actividades
            Bundle b = new Bundle();
            b.putString("Nom", txtNombre.getText().toString());
            b.putString("Id", txtLatitud.getText().toString());
            b.putString("Id2", txtLongitud.getText().toString());
            //Añadimos la información al intent
            intent.putExtras(b);
            startActivity(intent);
            finish();
            Toast.makeText(getApplicationContext(), "El nuevo punto de venta es: " + text, Toast.LENGTH_LONG).show();
            }
    private void act(){
        String text = txtNombre.getText().toString();
        String lat = txtLatitud.getText().toString();
        String lon = txtLongitud.getText().toString();
        DatabaseReference dbRef =
                FirebaseDatabase.getInstance().getReference()
                        .child("Ventas");

        Map<String, String> vivienda = new HashMap<>();
        vivienda.put("Latitud", lat);
        vivienda.put("Longitud", lon);

        dbRef.child(text).setValue(vivienda);
        Toast.makeText(getApplicationContext(), "Punto de venta actualizado", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(NuevoPV.this, Administrador.class);
        //Creamos la información a pasar entre actividades
        Bundle b = new Bundle();
        b.putString("Nom", txtNombre.getText().toString());
        b.putString("Id", txtLatitud.getText().toString());
        b.putString("Id2", txtLongitud.getText().toString());
        //Añadimos la información al intent
        intent.putExtras(b);
        startActivity(intent);
        finish();

    }
    private void eli(){
        String text = txtNombre.getText().toString();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query applesQuery = ref.child("Ventas").child(text);

        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                    Intent intent = new Intent(NuevoPV.this, Administrador.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "Punto de venta eliminado", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }});}
    }





