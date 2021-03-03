package com.example.sustinntec;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class RegistroUser extends AppCompatActivity {
    private Button btnInsertarPush;
    private EditText txtNombre;
    private EditText txtApellidoP;
    private EditText txtApellidoM;
    private EditText txtEdad;
    private EditText txtCorreo;
    private EditText txtContra;
    private EditText txtTele;
    private EditText txtViv;
    private TextView txtVivi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_user);

//Obtenemos las referencias a los controles
        txtNombre = (EditText) findViewById(R.id.txtUser);
        txtApellidoP = (EditText) findViewById(R.id.txtUser2);
        txtApellidoM = (EditText) findViewById(R.id.txtUser3);
        txtEdad= (EditText) findViewById(R.id.txtUser4);
        txtCorreo = (EditText) findViewById(R.id.txtUsuario);
        txtContra= (EditText) findViewById(R.id.txtContra);
        txtTele = (EditText) findViewById(R.id.txtTel);
        txtViv = (EditText) findViewById(R.id.txtVivie);
        txtVivi = (TextView) findViewById(R.id.txtVivie3);

        btnInsertarPush = (Button)findViewById(R.id.btnIni);

//Recuperamos la información pasada en el intent
        Bundle bundle = this.getIntent().getExtras();
        final String nombre=  (bundle.getString("Nom"));
        txtVivi.setText(nombre);


        //String usu = txtUsuario.getText().toString();
        final Query query = FirebaseDatabase.getInstance().getReference()
                .child("Viviendas")
                .child(nombre);
        final ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    String uno = dataSnapshot.child("apellidos").getValue().toString();
                    // String reg = txtContra.getText().toString();
                    txtViv.setText(uno);
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
                String nom = txtNombre.getText().toString();
                String apeP = txtApellidoP.getText().toString();
                String apeM = txtApellidoM.getText().toString();
                String eda = txtEdad.getText().toString();
                String cor = userNameFromEmail(txtCorreo.getText().toString());
                String con = txtContra.getText().toString();
                String tel = txtTele.getText().toString();
//EncodeString(cor);
//DecodeString(cor);
                if (TextUtils.isEmpty(nom)) {
                    txtNombre.setError("Nombre requerido");
                    return;
                }

                if (TextUtils.isEmpty(apeP)) {
                    txtApellidoP.setError("Apellido Paterno requerido");
                    return;
                }

                if (TextUtils.isEmpty(apeM)) {
                    txtApellidoM.setError("Apellido Materno requerido");
                    return;
                }

                if (TextUtils.isEmpty(eda)) {
                    txtEdad.setError("Edad requerida");
                    return;
                }
                final String compruebaemail = txtCorreo.getEditableText().toString().trim();

                if (TextUtils.isEmpty(cor)) {
                    if (!compruebaemail.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+"))
                    {
                        Toast.makeText(RegistroUser.this, "Por favor, introduce bien su email", Toast.LENGTH_LONG).show();
                    }
                    txtCorreo.setError("Correo requerido");
                    return;
                }

                if (TextUtils.isEmpty(con)) {
                    txtContra.setError("Contraseña requerida");
                    return;
                }

                if (TextUtils.isEmpty(tel)) {
                    txtTele.setError("Teléfono requerido");
                    return;
                }

                if (eda.length() > 2) {
                    txtEdad.setError("La edad no debe contener más de 3 carácteres");
                    return;
                }

                if (con.length() < 6) {
                    txtContra.setError("La contraseña debe contener más de 6 carácteres");
                    return;
                }

                if (tel.length() < 10) {
                    txtTele.setError("El teléfono debe contener más de 9 carácteres");
                    return;
                }

                DatabaseReference dbRef =
                        FirebaseDatabase.getInstance().getReference()
                                .child("Viviendas")
                                .child(nombre)
                                .child("Usuarios");

                Map<String, String> usuario = new HashMap<>();
                usuario.put("nombre", nom);
                usuario.put("apellido paterno", apeP);
                usuario.put("apellido materno", apeM);
                usuario.put("edad", eda);
                usuario.put("correo", cor);
                usuario.put("contraseña", con);
                usuario.put("teléfono", tel);

                dbRef.child(cor).setValue(usuario);
                Toast.makeText(getApplicationContext(), "Usuario registrado", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegistroUser.this, Tipo.class);
                //Creamos la información a pasar entre actividades
                Bundle b = new Bundle();
                b.putString("Corr", txtCorreo.getText().toString());
                b.putString("Viv", txtVivi.getText().toString());
                //Añadimos la información al intent
                intent.putExtras(b);
                startActivity(intent);
                finish();
            }
        });

    }public static String EncodeString(String string) {
        return string.replace(".", ",");
    }

    public static String DecodeString(String string) {
        return string.replace(",", ".");
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
