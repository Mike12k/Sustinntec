/*
Instituto Tecnológico de Matehuala
02 de Mayo de 2020
Desarrollo de Aplicaciones para Dispositivos Móviles
Jesús Miguel Arredondo Quijano
Diana Laura Cardenas Rocha
Sthepanie Anabel Ramos Vega
* */
package com.example.sustinntec;//Paquete en el que se encuentra la clase
//librerias importadas

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.felipecsl.gifimageview.library.GifImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

//nombre de la clase y extensión
public class SplashScreen extends AppCompatActivity {
    //atributo privado que contendra la imagen gif
    private GifImageView gifImageView;

    //creación y despliegue en pantalla
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //instanciación de apartado con atributo
        gifImageView = (GifImageView) findViewById(R.id.gifImageView);

        //se manda llamar el gif para que aparezca en pantalla
        try {
            InputStream inputStream = getAssets().open("reega.gif");
            byte[] bytes = IOUtils.toByteArray(inputStream);
            gifImageView.setBytes(bytes);
            gifImageView.startAnimation();
        } catch (IOException ex)//exepción de la instrucción
        {

        }

        //esperar 4 segundos para cerrar la actividad y lanzar otra
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
            }
        }, 4000); // 3000 = 3seconds
    }

    {
        Query query = FirebaseDatabase.getInstance().getReference()
                .child("Sesion")
                .child("Estado");
        final ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    String uno = dataSnapshot.child("Tipo").getValue().toString();
                    if (uno.equals("true")) {
                            Query query1 = FirebaseDatabase.getInstance().getReference()
                                    .child("Sesión")
                                    .child("Estado");
                            final ValueEventListener eventListener = new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    if (dataSnapshot.exists()) {
                                        String uno = dataSnapshot.child("Tipo").getValue().toString();
                                        if (uno.equals("true")) {
                                            Intent intent = new Intent(SplashScreen.this, Tipo.class);
                                            startActivity(intent);
                                            finish();

                                            List<ClipData.Item> list = new ArrayList<>();
                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            }
                                        } else if (uno.equals("false")) {
                                            Intent intent = new Intent(SplashScreen.this, ListadoU.class);
                                            startActivity(intent);
                                            finish();

                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            };
                            query1.addValueEventListener(eventListener);

                        List<ClipData.Item> list = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        }
                    } else if (uno.equals("false")) {
                        Toast.makeText(getApplicationContext(), "Bienvenido", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(SplashScreen.this, Login.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        query.addValueEventListener(eventListener);
    }
}
