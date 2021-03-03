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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

public class LoginUser extends AppCompatActivity {
    private Button btnRegistro;
    private Button btnIniciar;

    private TextView txtUsuario;
    private TextView txtContra;
    private TextView txtViv;
    private RadioButton RBsesion;
    String estado;

    private SQLiteDatabase db;

    private boolean isActivateRadioButton;
    String STRING_PREFERENCES = "Sustinntec.Login";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginuser);

        btnRegistro = (Button) findViewById(R.id.btnReg);
        btnIniciar = (Button) findViewById(R.id.btnIni);
        RBsesion = (RadioButton) findViewById(R.id.RBSecion);
        txtContra = (TextView) findViewById(R.id.txtContras);
        txtViv = (TextView) findViewById(R.id.txtViv);
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
                        .child("Usuarios");

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(LoginUser.this, RegistroUser.class);
                Bundle b = new Bundle();
                b.putString("Corr", txtUsuario.getText().toString());
                b.putString("Nom", txtViv.getText().toString());
                b.putString("Viv", txtViv.getText().toString());
                //Añadimos la información al intent
                intent.putExtras(b);
                startActivity(intent);
            }
        });
//Recuperamos la información pasada en el intent
        Bundle bundle = this.getIntent().getExtras();
        String usu = (bundle.getString("User"));
        String viv = (bundle.getString("Viv"));

        txtUsuario.setText(usu);
        txtViv.setText(viv);

        btnIniciar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String cor = userNameFromEmail(txtUsuario.getText().toString());
                String viv = txtViv.getText().toString();
                STRING_PREFERENCES = cor;
                final String con = txtContra.getText().toString();

                if (TextUtils.isEmpty(cor)) {
                    txtUsuario.setError("Correo requerido");
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
                                            .child("Sesión");

                            Map<String, String> vivienda = new HashMap<>();
                            vivienda.put("Tipo", estado);
                            vivienda.put("User", cor);

                            dbRef.child("Estado").setValue(vivienda);

                        } else {
                            estado = "false";
                            DatabaseReference dbRef =
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("Sesión");

                            Map<String, String> vivienda = new HashMap<>();
                            vivienda.put("Tipo", estado);
                            vivienda.put("User", "");
                            dbRef.child("Estado").setValue(vivienda);
                        }
                        //Validamos el usuario y la contraseña
                        {
                            Query query = FirebaseDatabase.getInstance().getReference()
                                    .child("Viviendas")
                                    .child(viv)
                                    .child("Usuarios")
                                    .child(cor);
                            final ValueEventListener eventListener = new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    if (dataSnapshot.exists()) {
                                        String uno = dataSnapshot.child("contraseña").getValue().toString();
                                        if (uno.equals(con)) {
                                            Intent intent = new Intent(LoginUser.this, Tipo.class);
                                            Bundle b = new Bundle();
                                            b.putString("Corr", txtUsuario.getText().toString());
                                            b.putString("Viv", txtViv.getText().toString());
                                            //Añadimos la información al intent
                                            intent.putExtras(b);
                                            startActivity(intent);
                                            finish();
                                        }else{
                                            Toast.makeText(getApplicationContext(), "Usuario inexistente o erroneo, intenta nuevamente", Toast.LENGTH_SHORT).show();
                                    }}
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
    }
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_settings:
                    Log.i("ActionBar", "Cerrar");;
                    finish();
                    Toast.makeText(getApplicationContext(), "Cerrando...", Toast.LENGTH_SHORT).show();
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
    }
    private  String userNameFromEmail(String email)
    {
        if(email.contains("@"))
        {
            return email.split("@")[0];
        }
        else
        {
            return email;
        }
    }
}