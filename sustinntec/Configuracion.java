package com.example.sustinntec;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.felipecsl.gifimageview.library.GifImageView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Configuracion extends AppCompatActivity {
    private Button btnIniciar;
    private ImageButton btnFria;
    private ImageButton btnRegular;
    private ImageButton btnCaliente;
    private EditText txtFecha;
    private EditText txtHora;
    private TextView txtResull;
    private TextView txtResull3;
    private TextView txtResull4;
    private TextView txtUsuario;

    //atributo privado que contendra la imagen gif
    private GifImageView gifImageView;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

//Obtenemos las referencias a los controles
        txtFecha = (EditText) findViewById(R.id.txtFecha);
        txtHora = (EditText) findViewById(R.id.txtHora);

        txtResull = (TextView) findViewById(R.id.txtResull);
        txtResull3 = (TextView) findViewById(R.id.txtResull3);
        txtResull4 = (TextView) findViewById(R.id.txtResull4);
        txtUsuario = (TextView) findViewById(R.id.txtUser);

        btnIniciar = (android.widget.Button) findViewById(R.id.btnIni);
        btnFria = (ImageButton) findViewById(R.id.btnFria);
        btnRegular = (ImageButton) findViewById(R.id.btnRegular);
        btnCaliente = (ImageButton) findViewById(R.id.btnCaliente);

        Bundle bundle = this.getIntent().getExtras();
        String usu = (bundle.getString("Corr"));
        String viv = (bundle.getString("Viv"));
        txtUsuario.setText(usu);
        txtResull3.setText(viv);

        btnFria.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Temperatura Fría", Toast.LENGTH_SHORT).show();
                txtResull.setText("15°-18°");
                txtResull4.setText("22.152");

            }
        });

        btnRegular.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Temperatura Regular", Toast.LENGTH_SHORT).show();
                txtResull.setText("19°-25°");
                txtResull4.setText("35.345");
            }
        });


    //despliegue de cada uno de los datos cada vez que se actualice mediante un ciclo

        btnCaliente.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Temperatura Caliente", Toast.LENGTH_SHORT).show();
            txtResull.setText("26°-35°");
                txtResull4.setText("28.167");
            }
        });

        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                String fec = txtFecha.getText().toString();
                String hor = txtHora.getText().toString();
                String tem = txtResull.getText().toString();
                String usu = userNameFromEmail(txtUsuario.getText().toString());
                String viv = txtResull3.getText().toString();
                String li = txtResull4.getText().toString();
                DatabaseReference dbRef =
                        FirebaseDatabase.getInstance().getReference()
                        .child("Viviendas")
                        .child(viv)
                        .child("Usuarios")
                        .child(usu)
                        .child("Duchas");

                Map<String, String> usuario = new HashMap<>();
                usuario.put("fecha", fec);
                usuario.put("hora", hor);
                usuario.put("temperatura", tem);
                usuario.put("litros", li);
                usuario.put("Tipo", "Automático");

                dbRef.child("Registro").setValue(usuario);
                Toast.makeText(getApplicationContext(), "Ducha configurada", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(Configuracion.this);
                alertDialog.setTitle("Alerta");
                alertDialog.setMessage("DUCHA LISTA !!!");
                alertDialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Configuracion.this, Resultados.class);
                        Bundle b = new Bundle();
                        b.putString("Corr", txtUsuario.getText().toString());
                        b.putString("Viv", txtResull3.getText().toString());
                        //Anadimos la informacion al intent
                        intent.putExtras(b);
                        startActivity(intent);
                    }
                });
                alertDialog.show();

               }});}
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
    }}

