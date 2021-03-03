package com.example.sustinntec;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Registro extends AppCompatActivity {
    private Button btnInsertarPush;
    private EditText txtUsuario;
    private EditText txtApellidos;
    private EditText txtContrasena;
    private EditText txtTelefono;
    private EditText txtDireccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

//Obtenemos las referencias a los controles
        txtUsuario = (EditText) findViewById(R.id.txtUser);
        txtApellidos = (EditText) findViewById(R.id.txtUser2);
        txtContrasena = (EditText) findViewById(R.id.txtContra);
        txtTelefono = (EditText) findViewById(R.id.txtTel);
        txtDireccion = (EditText) findViewById(R.id.txtTel2);

        btnInsertarPush = (Button)findViewById(R.id.btnIni);


        btnInsertarPush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Recuperamos los valores de los campos de texto
               // String cod = txtId.getText().toString();
                String nom = txtUsuario.getText().toString();
                String ape = txtApellidos.getText().toString();
                String con = txtContrasena.getText().toString();
                String tel = txtTelefono.getText().toString();
                String dir = txtDireccion.getText().toString();

                if (TextUtils.isEmpty(nom)) {
                    txtUsuario.setError("Usuario requerido");
                    return;
                }

                if (TextUtils.isEmpty(ape)) {
                    txtApellidos.setError("Apellidos requeridos");
                    return;
                }

                if (TextUtils.isEmpty(con)) {
                    txtContrasena.setError("Contraseña requerida");
                    return;
                }

                if (TextUtils.isEmpty(tel)) {
                    txtTelefono.setError("Teléfono requerido");
                    return;
                }

                if (TextUtils.isEmpty(dir)) {
                    txtDireccion.setError("Dirección requerida");
                    return;
                }

                if (con.length() < 6) {
                    txtContrasena.setError("La contraseña debe contener más de 6 carácteres");
                    return;
                }

                if (tel.length() < 10) {
                    txtTelefono.setError("El teléfono debe contener más de 9 carácteres");
                    return;
                }
                DatabaseReference dbRef =
                        FirebaseDatabase.getInstance().getReference()
                                .child("Viviendas");

                Map<String, String> vivienda = new HashMap<>();
                vivienda.put("usuario", nom);
                vivienda.put("apellidos", ape);
                vivienda.put("contraseña", con);
                vivienda.put("teléfono", tel);
                vivienda.put("dirección", dir);

                dbRef.child(nom).setValue(vivienda);
                Toast.makeText(getApplicationContext(), "Vivienda registrada", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Registro.this,RegistroUser.class);
                //Creamos la información a pasar entre actividades
                Bundle b = new Bundle();
                b.putString("Nom", txtUsuario.getText().toString());
                //Añadimos la información al intent
                intent.putExtras(b);
                startActivity(intent);
                finish();
            }
        });
    }
}
