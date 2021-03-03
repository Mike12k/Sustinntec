package com.example.sustinntec;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class ConfiguracionMa extends AppCompatActivity {
    private Button btnIniciar;
    private ImageButton btnFria;
    private ImageButton  btnRegular;
    private ImageButton  btnCaliente;

    private TextView txtResull;
    private TextView txtResull2;
    private TextView txtResull3;
    private TextView txtUsuario;

    //atributo privado que contendra la imagen gif
    private GifImageView gifImageView;;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracionma);

//Obtenemos las referencias a los controles
        txtResull = (TextView) findViewById(R.id.txtResull);
        txtResull2 = (TextView) findViewById(R.id.txtResull2);
        txtResull3 = (TextView) findViewById(R.id.txtResull5);
        txtUsuario = (TextView) findViewById(R.id.txtUser);

        btnIniciar = (Button)findViewById(R.id.btnIni);
        btnFria = (ImageButton ) findViewById(R.id.btnFria);
        btnRegular = (ImageButton ) findViewById(R.id.btnRegular);
        btnCaliente = (ImageButton )findViewById(R.id.btnCaliente);

        Bundle bundle = this.getIntent().getExtras();
        String usu = (bundle.getString("Corr"));
        String viv = (bundle.getString("Viv"));
        txtUsuario.setText(usu);
        txtResull2.setText(viv);

        btnFria.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Temperatura Fría", Toast.LENGTH_SHORT).show();
                txtResull.setText("15°-18°");
                txtResull3.setText("27.145");

            }
        });

        btnRegular.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Temperatura Regular", Toast.LENGTH_SHORT).show();
        txtResull.setText("19°-25°");
                txtResull3.setText("42.387");
            }
        });

        btnCaliente.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Temperatura Caliente", Toast.LENGTH_SHORT).show();
            txtResull.setText("26°-35°");
                txtResull3.setText("38.321");
            }
        });

        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                String tem = txtResull.getText().toString();
                String viv = txtResull2.getText().toString();
                String usu = userNameFromEmail(txtUsuario.getText().toString());
                String li  = txtResull3.getText().toString();
                DatabaseReference dbRef =
                        FirebaseDatabase.getInstance().getReference()
                                .child("Viviendas")
                                .child(viv)
                        .child("Usuarios")
                        .child(usu)
                        .child("Duchas");

                Map<String, String> usuario = new HashMap<>();
                usuario.put("temperatura", tem);
                usuario.put("litros", li);
                usuario.put("Tipo", "Manual");
                dbRef.child("Registro").setValue(usuario);
                Toast.makeText(getApplicationContext(), "Ducha configurada", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ConfiguracionMa.this);
                alertDialog.setTitle("Alerta");
                alertDialog.setMessage("DUCHA LISTA !!!");
                alertDialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(ConfiguracionMa.this, Resultados2.class);
                        Bundle b = new Bundle();
                        b.putString("Corr", txtUsuario.getText().toString());
                        b.putString("Viv", txtResull2.getText().toString());
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
    }
            }
