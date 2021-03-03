/*Alumna:Diana Laura Cradenas Rocha
  Escuela: Instituto Tecnologico de Matehuala
  Materia: Desarrollo de Aplicaciones para Dispositivos Moviles
  Fecha: 11 de mayo de 2020
 */
package com.example.sustinntec;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;


public class Ventas extends FragmentActivity implements OnMapReadyCallback, LocationListener {
    private TextView txtResultado, txtResultado2, txtResultado3;
    private GoogleMap mMap;
private double longitud, latitud;
    private Marker currentLocationMaker;
    private LatLng currentLocationLatLong;

    private Button btnNinguno;
    private Button btnNormal;
    private Button btnSatelite;
    private Button btnHibrido;
    private Button btnTierra;
    private Button btnVer;
    private Button btnCerrar;

    //private DatabaseReference mDatabase;
    private ArrayList<String> telefonos;
    private ArrayAdapter<String> adaptador1;
    private ListView lv1;
    Polyline line, line1, line2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventas);

        telefonos = new ArrayList<String>();

        //adaptador1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, telefonos);
        //lv1 = (ListView) findViewById(R.id.list1);
        // lv1.setAdapter(adaptador1);

        btnNinguno = (Button) findViewById(R.id.btnNinguno);
        btnNormal = (Button) findViewById(R.id.btnnor);
        btnSatelite = (Button) findViewById(R.id.btnSate);
        btnHibrido = (Button) findViewById(R.id.btnHibri);
        btnTierra = (Button) findViewById(R.id.btnTierra);
        btnVer = (Button) findViewById(R.id.btnVer);
        btnCerrar = (Button) findViewById(R.id.btnCerrar);

        txtResultado = (TextView) findViewById(R.id.txtResultado);
        txtResultado2 = (TextView)findViewById(R.id.txtResultado2);
        txtResultado3 = (TextView)findViewById(R.id.txtResultado3);

        //txtResultado2 = (TextView)findViewById(R.id.txtResultado2);
/*        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                String text = (String) lv1.getItemAtPosition(arg2);
                txtResultado2.setText(text);
        }
        });
*/
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map2);
        showSettingsAlert();
        mapFragment.getMapAsync(this);
        //startGettingLocations();
        //mDatabase = FirebaseDatabase.getInstance().getReference();
        //getMarkers();

        btnNinguno.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
            }
        });

        btnNormal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }
        });

        btnSatelite.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            }
        });

        btnHibrido.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            }
        });

        btnTierra.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            }
        });

        btnCerrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Cerrar();
            }
        });

        btnVer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String names[] ={"Ferretería Martínez","Mercantil de la Rosa - Ferretería","Tecnológico de Matehuala"};
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(Ventas.this);
                LayoutInflater inflater = getLayoutInflater();
              //  ImageView image = new ImageView(Ventas.this);
               // image.setImageResource(R.drawable.excursion);
                View convertView = (View) inflater.inflate(R.layout.activity_lista, null);
              //  alertDialog.setView(image);
                alertDialog.setView(convertView);
                alertDialog.setTitle("Selecciona el punto de venta al que te diríges:");
                final ListView lv = (ListView) convertView.findViewById(R.id.list1);
                ArrayAdapter adapter = new ArrayAdapter(Ventas.this, android.R.layout.simple_list_item_1, names);
                lv.setAdapter(adapter);

                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        String text = (String) lv.getItemAtPosition(arg2);
                        txtResultado.setText(text);

                    }
                });

                alertDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        if (txtResultado.getText().toString().trim().equalsIgnoreCase("Ferretería Martínez")) {
                            if (line1 != null) {
                                line1.remove();
                            }else if (line2 != null) {
                                line2.remove();
                            }
                            line = mMap.addPolyline(new PolylineOptions()
                                    .add(new LatLng(23.65, -100.65), new LatLng(23.646227,-100.642893))
                                    .width(5)
                                    .color(Color.GREEN));
                        }else if (txtResultado.getText().toString().trim().equalsIgnoreCase("Mercantil de la Rosa - Ferretería")) {
                            if (line != null) {
                                line.remove();
                            }else if (line2 != null) {
                                line2.remove();
                            }
                                line1 = mMap.addPolyline(new PolylineOptions()
                                        .add(new LatLng(23.65, -100.65), new LatLng(23.647004, -100.639900))
                                        .width(5)
                                        .color(Color.RED));

                        }else if (txtResultado.getText().toString().trim().equalsIgnoreCase("Tecnológico de Matehuala")) {
                            if (line != null) {
                                line.remove();
                            }else if (line1 != null) {
                                line1.remove();
                            }
                                line2 = mMap.addPolyline(new PolylineOptions()
                                        .add(new LatLng(23.65, -100.65), new LatLng(23.668544, -100.630421))
                                        .width(5)
                                        .color(Color.BLUE));
                        }}
                });
                alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                    }
                });
                alertDialog.show();

            }
        });
    }

    private  void Cerrar(){
        Intent intent = new Intent(Ventas.this, Login.class);
        startActivity(intent);
        finish();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    //Construimos el mensaje a mostrar
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng marcador = new LatLng(23.65, -100.65);
        mMap.addMarker(new MarkerOptions()
                .position(marcador)
                .title("Matehuala")
                //.snippet("Longitud: " +lati +" Latitud: "+lo)
                .rotation(1)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
                ));

        LatLng marcador1 = new LatLng(23.646227,-100.642893);
        mMap.addMarker(new MarkerOptions()
                .position(marcador1)
                .title("Ferretería Martínez")
                //.snippet("Longitud: " +lati +" Latitud: "+lo)
                .rotation(1)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                ));

        LatLng marcador2 = new LatLng(23.647004,-100.639900);
        mMap.addMarker(new MarkerOptions()
                .position(marcador2)
                .title("Mercantil de la Rosa - Ferretería")
                //.snippet("Longitud: " +lati +" Latitud: "+lo)
                .rotation(1)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                ));

        LatLng marcador3 = new LatLng(23.668544,-100.630421);
        mMap.addMarker(new MarkerOptions()
                .position(marcador3)
                .title("Tecnológico de Matehuala")
                //.snippet("Longitud: " +lati +" Latitud: "+lo)
                .rotation(1)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                ));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(marcador));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(marcador1));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(marcador2));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(marcador3));

        LatLngBounds.Builder constructor = new LatLngBounds.Builder();

        constructor.include(marcador);
        constructor.include(marcador1);
        constructor.include(marcador2);
        constructor.include(marcador3);

        LatLngBounds limites = constructor.build();

        int ancho = getResources().getDisplayMetrics().widthPixels;
        int alto = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (alto * 0.25); // 25% de espacio (padding) superior e inferior
        CameraUpdate centrarmarcadores = CameraUpdateFactory.newLatLngBounds(limites, ancho, alto, padding);
        mMap.animateCamera(centrarmarcadores);
//      mMap.setMyLocationEnabled(true);

        // Centrar Marcadores
        //LatLngBounds.Builder constructor = new LatLngBounds.Builder();
        mMap.isMyLocationEnabled();
        mMap.isTrafficEnabled();
        mMap.isIndoorEnabled();
        mMap.isBuildingsEnabled();
        //CameraPosition cameraPosition = new CameraPosition.Builder().zoom(15).target(recife).build();
        //  mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        mMap.getUiSettings().setZoomControlsEnabled(true);

    }


    @Override
    public void onLocationChanged(Location location) {
        //String res= (String) txtResultado.getText();
        //Recuperamos la información pasada en el intent
//if(res.equals("Ferretería Martínez"))
    if (currentLocationMaker != null) {
        currentLocationMaker.remove();
    }
    //Add marker
        currentLocationLatLong = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(currentLocationLatLong);
        markerOptions.title("Matehuala");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        currentLocationMaker = mMap.addMarker(markerOptions);

        //Move to new location
        CameraPosition cameraPosition = new CameraPosition.Builder().zoom(15).target(currentLocationLatLong).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

       // LocationData locationData = new LocationData(location.getLatitude(), location.getLongitude());
        //mDatabase.child("location").child(String.valueOf(new Date().getTime())).setValue(locationData);

        Toast.makeText(this, "Localización actualizada", Toast.LENGTH_SHORT).show();
        //getMarkers();
}


    private ArrayList findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList result = new ArrayList();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canAskPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canAskPermission() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("GPS desactivado!");
        alertDialog.setMessage("Activar GPS?");
        alertDialog.setPositiveButton("Activar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }


    private void startGettingLocations() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        boolean isGPS = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetwork = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        boolean canGetLocation = true;
        int ALL_PERMISSIONS_RESULT = 101;
        long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;// Distance in meters
        long MIN_TIME_BW_UPDATES = 1000 * 10;// Time in milliseconds

        ArrayList<String> permissions = new ArrayList<>();
        ArrayList<String> permissionsToRequest;

        permissions.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        permissionsToRequest = findUnAskedPermissions(permissions);

        //Check if GPS and Network are on, if not asks the user to turn on
        if (!isGPS && !isNetwork) {
            showSettingsAlert();
        } else {
            // check permissions

            // check permissions for later versions
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (permissionsToRequest.size() > 0) {
                    requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]),
                            ALL_PERMISSIONS_RESULT);
                    canGetLocation = false;
                }
            }
        }


        //Checks if FINE LOCATION and COARSE Location were granted
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(this, "Permiso negado", Toast.LENGTH_SHORT).show();
            return;
        }

        //Starts requesting location updates
        if (canGetLocation) {
            if (isGPS) {
                lm.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

            } else if (isNetwork) {
                // from Network Provider

                lm.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

            }
        } else {
            Toast.makeText(this, "No es posible obtener localización", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
