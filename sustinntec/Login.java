/*
Instituto Tecnológico de Matehuala
02 de Mayo de 2020
Desarrollo de Aplicaciones para Dispositivos Móviles
Jesús Miguel Arredondo Quijano
Diana Laura Cardenas Rocha
Sthepanie Anabel Ramos Vega
* */
package com.example.sustinntec;//paquete en el que se encuentra la clase
//importación de paquetes

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
//nombre y extención de la clase

public class Login extends AppCompatActivity {
    private Button btnRegistro;
    private Button btnIniciar;
    private Button btnInfo;
    private Button btnVen;

    private TextView txtUsuario;
    private TextView txtContra;
    private RadioButton RBsesion;
    String estado;

    private SQLiteDatabase db;

    private boolean isActivateRadioButton;
    String STRING_PREFERENCES = "Sustinntec.Login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnRegistro = (Button) findViewById(R.id.btnReg);
        btnInfo = (Button) findViewById(R.id.btnIn);
        btnVen = (Button) findViewById(R.id.btnVen);
        btnIniciar = (Button) findViewById(R.id.btnIni);
        RBsesion = (RadioButton) findViewById(R.id.RBSecion);
        txtContra = (TextView) findViewById(R.id.txtContras);
        txtUsuario = (TextView) findViewById(R.id.txtUsuario);

        isActivateRadioButton = RBsesion.isChecked(); //DESACTIVADO

        RBsesion.setOnClickListener(new View.OnClickListener() {
            //ACTIVADO
            @Override
            public void onClick(View v) {
                if (isActivateRadioButton) {
                    RBsesion.setChecked(false);
                }
                isActivateRadioButton = RBsesion.isChecked();
            }

        });

        DatabaseReference dbRef =
                FirebaseDatabase.getInstance().getReference()
                        .child("Viviendas");

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Registro.class);
                startActivity(intent);
            }
        });

        btnVen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Ventas.class);
                startActivity(intent);
            }
        });

        btnInfo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Informacion.class);
                startActivity(intent);
            }
        });

        btnIniciar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String cor = txtUsuario.getText().toString();
                STRING_PREFERENCES = cor;
                final String con = txtContra.getText().toString();

                if (TextUtils.isEmpty(cor)) {
                    txtUsuario.setError("Usuario requerido");
                    return;
                }

                if (TextUtils.isEmpty(con)) {
                    txtContra.setError("Contraseña requerida");
                    return;
                }

                if (con.length() < 6) {
                    txtContra.setError("La contraseña debe contener más de 6 carácteres");
                    return;
                }
                {
                    {
                        if (RBsesion.isChecked()) {
                            estado = "true";
                            DatabaseReference dbRef =
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("Sesion");

                            Map<String, String> vivienda = new HashMap<>();
                            vivienda.put("Tipo", estado);
                            vivienda.put("User", cor);
                            dbRef.child("Estado").setValue(vivienda);

                        } else {
                            estado = "false";
                            DatabaseReference dbRef =
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("Sesion");

                            Map<String, String> vivienda = new HashMap<>();
                            vivienda.put("Tipo", estado);
                            vivienda.put("User", "");

                            dbRef.child("Estado").setValue(vivienda);
                        }
                        //Validamos el usuario y la contraseña
                        if (cor.equals("SustinAdmin") && con.equals("Sustin2020")) {
                            Toast.makeText(getApplicationContext(), "Iniciando...", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Login.this, Administrador.class);
                            startActivity(intent);
                        } else {
                            Query query = FirebaseDatabase.getInstance().getReference()
                                    .child("Viviendas")
                                    .child(cor);
                            final ValueEventListener eventListener = new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    if (dataSnapshot.exists()) {
                                        String uno = dataSnapshot.child("contraseña").getValue().toString();
                                        if (uno.equals(con)) {
                                            Intent intent = new Intent(Login.this, ListadoU.class);
                                            //Creamos la información a pasar entre actividades
                                            Bundle b = new Bundle();
                                            b.putString("Viv", txtUsuario.getText().toString());
                                            //Añadimos la información al intent
                                            intent.putExtras(b);
                                            startActivity(intent);
                                            finish();

                                        } else {
                                            Toast.makeText(getApplicationContext(), "Usuario inexistente o erroneo, intenta nuevamente", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    //
                                }
                            };
                            query.addValueEventListener(eventListener);

                        }
                    }
                }
            }

        });

    }}