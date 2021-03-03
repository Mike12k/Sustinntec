package com.example.sustinntec;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class EdicionRegistro extends AppCompatActivity {
    private Button btnInsertarPush, btnAct, btnEli;
    private EditText txtUsuario;
    private EditText txtApellidos;
    private EditText txtContrasena;
    private EditText txtTelefono;
    private EditText txtDireccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registroedi);

//Obtenemos las referencias a los controles
        txtUsuario = (EditText) findViewById(R.id.txtUser);
        txtApellidos = (EditText) findViewById(R.id.txtUser2);
        txtContrasena = (EditText) findViewById(R.id.txtContra);
        txtTelefono = (EditText) findViewById(R.id.txtTel);
        txtDireccion = (EditText) findViewById(R.id.txtTel2);

        btnInsertarPush = (Button)findViewById(R.id.btnIni);
        btnAct = (Button)findViewById(R.id.btnIni2);
        btnEli = (Button)findViewById(R.id.btnIni3);

        btnAct.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                act();
            }});
        btnEli.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //eli();
            }});
        //Recuperamos la informacion pasada en el intent
        Bundle bundle = this.getIntent().getExtras();
        final String nombre=  (bundle.getString("Viv"));
        txtUsuario.setText(nombre);

        //String usu = txtUsuario.getText().toString();
        final Query query = FirebaseDatabase.getInstance().getReference()
                ;
        final ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String nom = txtUsuario.getText().toString();
                if (dataSnapshot.exists()) {
                    String uno = dataSnapshot.child("Viviendas").child(nom).child("usuario").getValue().toString();
                    String dos = dataSnapshot.child("Viviendas").child(nom).child("apellidos").getValue().toString();
                    String tres = dataSnapshot.child("Viviendas").child(nom).child("contraseña").getValue().toString();
                    String cuatro = dataSnapshot.child("Viviendas").child(nom).child("teléfono").getValue().toString();
                    String cinco = dataSnapshot.child("Viviendas").child(nom).child("dirección").getValue().toString();

                    // String reg = txtContra.getText().toString();
                    txtUsuario.setText(uno);
                    txtApellidos.setText(dos);
                    txtContrasena.setText(tres);
                    txtTelefono.setText(cuatro);
                    txtDireccion.setText(cinco);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //
            }
        };
        query.addValueEventListener(eventListener);

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

                Intent intent = new Intent(EdicionRegistro.this,Administrador.class);
                //Creamos la información a pasar entre actividades
                Bundle b = new Bundle();
                b.putString("Nom", txtUsuario.getText().toString());
                //Añadimos la información al intent
                intent.putExtras(b);
                startActivity(intent);
                finish();
            }
        });}
        private void act(){
            String nom = txtUsuario.getText().toString();
            String ape = txtApellidos.getText().toString();
            String con = txtContrasena.getText().toString();
            String tel = txtTelefono.getText().toString();
            String dir = txtDireccion.getText().toString();

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
            Toast.makeText(getApplicationContext(), "Vivienda actualizads", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(EdicionRegistro.this, Administrador.class);
            //Creamos la información a pasar entre actividades
            Bundle b = new Bundle();
            b.putString("Viv", txtApellidos.getText().toString());
            //Añadimos la información al intent
            intent.putExtras(b);
            startActivity(intent);
            finish();

        }
        private void eli(){
            String text = txtUsuario.getText().toString();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            Query applesQuery = ref.child("Viviendas").child(text);

            applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                        appleSnapshot.getRef().removeValue();
                        Intent intent = new Intent(EdicionRegistro.this, Administrador.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "Punto de venta eliminado", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {


                }});
    }
}
