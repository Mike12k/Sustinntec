package com.example.sustinntec;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import harmony.java.awt.Color;

public class Resultados  extends AppCompatActivity {
    private Button btnRes;
    private ImageButton btnTemp;
    private ImageButton  btnDet;
    private ImageButton  btnLit;

    private final static String NOMBRE_DIRECTORIO = "ShowerLog";
    private final static String ETIQUETA_ERROR = "ERROR";

    private TextView txtResull;
    private TextView txtResull2;
    private TextView txtUsuario;

    private ArrayList<String> telefonos;
    private ArrayAdapter<String> adaptador1;
    private ListView lv1;
private Button btnCerrar;
    private Button btnRet;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado);

        telefonos = new ArrayList<String>();
        adaptador1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, telefonos);
        lv1 = (ListView) findViewById(R.id.listt);
        lv1.setAdapter(adaptador1);

//Obtenemos las referencias a los controles
        txtResull = (TextView) findViewById(R.id.txtCorre);
        txtResull2 = (TextView) findViewById(R.id.txtVivie);
        txtUsuario = (TextView) findViewById(R.id.txtUser);

        btnRet = (Button) findViewById(R.id.btnIni);
        btnCerrar= (Button) findViewById(R.id.btnCerrar);
        btnRes = (Button) findViewById(R.id.btnRe);
        btnDet = (ImageButton) findViewById(R.id.btnMan);
        btnTemp = (ImageButton) findViewById(R.id.btnAut);
        btnLit = (ImageButton) findViewById(R.id.btnMan2);

        Bundle bundle = this.getIntent().getExtras();
        String usu = (bundle.getString("Corr"));
        String viv = (bundle.getString("Viv"));
        txtResull.setText(usu);
        txtResull2.setText(viv);

        btnCerrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DatabaseReference dbRef =
                        FirebaseDatabase.getInstance().getReference()
                                .child("Sesion");

                Map<String, String> vivienda = new HashMap<>();
                vivienda.put("Tipo", "false");
                vivienda.put("User", "");

                dbRef.child("Estado").setValue(vivienda);

                Intent intent = new Intent(Resultados.this, SplashScreen.class);
                Bundle b = new Bundle();
                //Anadimos la informacion al intent
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        // Permisos.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,}, 1000);
        } else {
        }

        btnDet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Resultados.this, Temperatura.class);
                Bundle b = new Bundle();
                b.putString("Corr", txtResull.getText().toString());
                b.putString("Viv", txtResull2.getText().toString());
                //Añadimos la información al intent
                intent.putExtras(b);
                startActivity(intent);
            }
        });
        btnRet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Resultados.this, Tipo.class);
                Bundle b = new Bundle();
                b.putString("Corr", txtResull.getText().toString());
                b.putString("Viv", txtResull2.getText().toString());
                //Añadimos la información al intent
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        btnTemp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Resultados.this, Proximidad.class);
                Bundle b = new Bundle();
                b.putString("Corr", txtResull.getText().toString());
                b.putString("Viv", txtResull2.getText().toString());
                //Añadimos la información al intent
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        btnLit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Resultados.this, Litros.class);
                Bundle b = new Bundle();
                b.putString("Corr", txtResull.getText().toString());
                b.putString("Viv", txtResull2.getText().toString());
                //Añadimos la información al intent
                intent.putExtras(b);
                startActivity(intent);
            }
        });
        btnRes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                adaptador1.notifyDataSetChanged();
                telefonos.add("Registro");
                telefonos.add("Registro1");
                telefonos.add("Registro2");
                telefonos.add("Registro3");
                telefonos.add("Registro4");
                }
        });

        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                final String text = (String) lv1.getItemAtPosition(arg2);
                Toast.makeText(getApplicationContext(), "Generando " +text + " PDF...", Toast.LENGTH_SHORT).show();

                //String usu = txtUsuario.getText().toString();
                final Query query = FirebaseDatabase.getInstance().getReference()
                        ;
                final ValueEventListener eventListener = new ValueEventListener() {
                    @SuppressLint("Assert")
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        String NOMBRE_DOCUMENTO = text + ".pdf";
                        if (dataSnapshot.exists()) {
                            String cor = userNameFromEmail(txtResull.getText().toString());
                            String viv = txtResull2.getText().toString();

                            String cero = dataSnapshot.child("Viviendas").child(viv).child("apellidos").getValue().toString();
                            String uno = dataSnapshot.child("Viviendas")
                                    .child(viv)
                                    .child("Usuarios")
                                    .child(cor)
                                    .child("Duchas")
                                    .child(text).child("Tipo").getValue().toString();
                            String dos = dataSnapshot.child("Viviendas")
                                    .child(viv)
                                    .child("Usuarios")
                                    .child(cor)
                                    .child("Duchas")
                                    .child(text).child("fecha").getValue().toString();
                            String tres = dataSnapshot.child("Viviendas")
                                    .child(viv)
                                    .child("Usuarios")
                                    .child(cor)
                                    .child("Duchas")
                                    .child(text).child("hora").getValue().toString();
                            String cuatro = dataSnapshot.child("Viviendas")
                                    .child(viv)
                                    .child("Usuarios")
                                    .child(cor)
                                    .child("Duchas")
                                    .child(text).child("temperatura").getValue().toString();
                            String cinco = dataSnapshot.child("Viviendas").child(viv).child("Usuarios").child(cor).child("nombre").getValue().toString();
                            String seis = dataSnapshot.child("Viviendas").child(viv).child("Usuarios").child(cor).child("apellido paterno").getValue().toString();
                            String siete = dataSnapshot.child("Viviendas").child(viv).child("Usuarios").child(cor).child("apellido materno").getValue().toString();
                            String ocho = dataSnapshot.child("Viviendas").child(viv).child("Usuarios").child(cor).child("edad").getValue().toString();
                            String nueve = dataSnapshot.child("Viviendas").child(viv).child("Usuarios").child(cor).child("teléfono").getValue().toString();
                            String diez= dataSnapshot.child("Viviendas")
                                    .child(viv)
                                    .child("Usuarios")
                                    .child(cor)
                                    .child("Duchas")
                                    .child(text).child("litros").getValue().toString();

                            // Creamos el documento.
                            Document documento = new Document();
                            try {

                                File f = crearFichero(NOMBRE_DOCUMENTO);

                                // Creamos el flujo de datos de salida para el fichero donde
                                // guardaremos el pdf.
                                FileOutputStream ficheroPdf = new FileOutputStream(
                                        f.getAbsolutePath());

                                // Asociamos el flujo que acabamos de crear al documento.
                                PdfWriter writer = PdfWriter.getInstance(documento, ficheroPdf);

                                // Incluimos el pie de pagina y una cabecera
                                HeaderFooter cabecera = new HeaderFooter(new Phrase(
                                        "SUSTINNTEC (Sustentable-Innovador-Tecnológico)"), false);
                                HeaderFooter pie = new HeaderFooter(new Phrase(
                                        "Contribuyendo a la preservación del agua"), false);

                                documento.setHeader(cabecera);
                                documento.setFooter(pie);
                                // Abrimos el documento.
                                documento.open();

                                Font font = FontFactory.getFont(FontFactory.HELVETICA, 28,
                                        Font.BOLD, Color.BLUE);
                                Paragraph p =new Paragraph("Registro de datos en la ducha \n", font);
                                p.setAlignment(Element.ALIGN_CENTER);
                                documento.add(p);
                                // Insertamos una imagen que se encuentra en los recursos de la
                                // aplicacion.
                                Bitmap bitmap = BitmapFactory.decodeResource(Resultados.this.getResources(),
                                        R.drawable.log);
                                Bitmap imagenFinal = Bitmap.createScaledBitmap(bitmap,200,200,false);
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                imagenFinal.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                Image imagen = Image.getInstance(stream.toByteArray());
                                imagen.scaleAbsolute(150f, 150f);
                                imagen.setAlignment(Element.ALIGN_CENTER);
                                documento.add(imagen);
                                // Añadimos un titulo con la fuente por defecto.
                                documento.add(new Paragraph ("\n Fecha: " + dos));
                                documento.add(new Paragraph ("\n Hora: " + tres));
                                documento.add(new Paragraph ("\n Nombre de la Vivienda: " + cero));
                                documento.add(new Paragraph ("\n Nombre de la Persona: " + cinco + " " + seis + " "+ siete));
                                documento.add(new Paragraph ("\n Edad: " + ocho));
                                documento.add(new Paragraph ("\n Telefono: " + nueve));
                                documento.add(new Paragraph ("\n Tipo de Ducha: " + uno));
                                documento.add(new Paragraph ("\n Temperatura: " + cuatro));
                                documento.add(new Paragraph ("\n Cantidad de Litros Consumidos: " + diez));
                                // Agregar marca de agua
                                font = FontFactory.getFont(FontFactory.HELVETICA, 42, Font.BOLD,
                                        Color.GRAY);
                                ColumnText.showTextAligned(writer.getDirectContentUnder(),
                                        Element.ALIGN_CENTER, new Paragraph(
                                                "SUSTINNTEC", font), 297.5f, 421,
                                        writer.getPageNumber() % 2 == 1 ? 45 : -45);
                                Toast.makeText(getApplicationContext(), "PDF guardado en Download/ShowerLog", Toast.LENGTH_SHORT).show();

                            } catch (DocumentException e) {

                                Log.e(ETIQUETA_ERROR, e.getMessage());

                            } catch (IOException e) {

                                Log.e(ETIQUETA_ERROR, e.getMessage());

                            } finally {
                                // Cerramos el documento.
                                documento.close();

                            }
                        }}
                    public File crearFichero(String nombreFichero) throws IOException {
                        File ruta = getRuta();
                        File fichero = null;
                        if (ruta != null)
                            fichero = new File(ruta, nombreFichero);
                        return fichero;
                    }

                    /**
                     * Obtenemos la ruta donde vamos a almacenar el fichero.
                     *
                     * @return
                     */
                    public File getRuta() {

                        // El fichero sera almacenado en un directorio dentro del directorio
                        // Descargas
                        File ruta = null;
                        if (Environment.MEDIA_MOUNTED.equals(Environment
                                .getExternalStorageState())) {
                            ruta = new File(
                                    Environment
                                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                                    NOMBRE_DIRECTORIO);

                            if (ruta != null) {
                                if (!ruta.mkdirs()) {
                                    if (!ruta.exists()) {
                                        return null;
                                    }
                                }
                            }
                        } else {
                        }

                        return ruta;
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //
                    }
                };
                query.addValueEventListener(eventListener);

            }

        });
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