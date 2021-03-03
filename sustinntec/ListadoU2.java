package com.example.sustinntec;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListadoU2 extends AppCompatActivity {

    private ArrayList<String> telefonos;
    private ArrayAdapter<String> adaptador1;
    private ListView lv1;
    private TextView txtRes , txtRes2, txtRes3;
    private Button btnNue;
    private Button btnCerrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listausuarios);

        telefonos = new ArrayList<String>();
        btnNue = (Button) findViewById(R.id.btnRegg);
        btnCerrar = (Button) findViewById(R.id.btnCerrar);
        adaptador1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, telefonos);
        lv1 = (ListView) findViewById(R.id.list1);
        txtRes = (TextView) findViewById(R.id.txtResultado);
        txtRes2 = (TextView) findViewById(R.id.txtResultado4);
        txtRes3 = (TextView) findViewById(R.id.txtResultado5);
        lv1.setAdapter(adaptador1);



        btnCerrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DatabaseReference dbRef =
                        FirebaseDatabase.getInstance().getReference()
                                .child("Sesion");

                Map<String, String> vivienda = new HashMap<>();
                vivienda.put("Tipo", "false");
                vivienda.put("User", "");

                dbRef.child("Estado").setValue(vivienda);

                Intent intent = new Intent(ListadoU2.this, Login.class);
                startActivity(intent);
            }
        });
        btnNue.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ListadoU2.this, RegistroUser.class);

                Bundle b = new Bundle();
                b.putString("Nom", "Prueba");
                //Anadimos la informacion al intent
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        Query query = FirebaseDatabase.getInstance().getReference()
                .child("Sesion")
                .child("Estado");
        final ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    String uno = dataSnapshot.child("User").getValue().toString();
                    txtRes2.setText(uno);
                    FirebaseDatabase.getInstance().getReference()
                            .child("Viviendas")
                            .child(uno)
                            .child("Usuarios")
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    //  List<String> usuarios = new ArrayList<>();
                                    //adaptador1.notifyDataSetChanged();
                                    if (dataSnapshot.exists()) {
                                        //Opcion 2
                                        String uno = dataSnapshot.child("Laura").getKey().toString();
                                        String dos = dataSnapshot.child("sarv_steph").getKey().toString();
                                        String tres = dataSnapshot.child("mike").getKey().toString();
                                        String cuatro = dataSnapshot.child("sad").getKey().toString();
                                        String cinco = dataSnapshot.child("luciano").getKey().toString();
                                        // String seis = dataSnapshot.child("6").getValue().toString();
                                        //  String siete = dataSnapshot.child("7").getValue().toString();
                                        //  String ocho = dataSnapshot.child("8").getValue().toString();
                                        // String nueve = dataSnapshot.child("9").getValue().toString();
                                        // String diez = dataSnapshot.child("10").getValue().toString();

                                        adaptador1.notifyDataSetChanged();
                                        telefonos.add("" + uno +"");
                                        telefonos.add("" + dos + "");
                                        telefonos.add("" + tres + "\n");
                                        telefonos.add("" + cuatro + "\n");
                                        telefonos.add("" + cinco + "\n");
                                        //telefonos.add("" + seis + "\n");
                                        // telefonos.add("" + siete + "\n");
                                        // telefonos.add("" + ocho + "\n");
                                        // telefonos.add("" + nueve + "\n");
                                        // telefonos.add("" + diez + "\n");
                                        adaptador1.notifyDataSetChanged();
                                        // usuarios ahora contiene los nombres de todos los usuarios.
                                    }}

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    //

                }});} }
                @Override
                public void onCancelled (DatabaseError databaseError){
                    //
                }
            }

            ;
        query.addValueEventListener(eventListener);

        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                String text = (String) lv1.getItemAtPosition(arg2);
                 txtRes.setText(text);

                Intent intent = new Intent(ListadoU2.this, EdicionRegistroUser.class);
                Bundle b = new Bundle();
                b.putString("User", txtRes.getText().toString());
                b.putString("Viv", txtRes3.getText().toString());
                //Aadimos la informacion al intent
                intent.putExtras(b);
                startActivity(intent);
                finish();
        }

       });}
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
