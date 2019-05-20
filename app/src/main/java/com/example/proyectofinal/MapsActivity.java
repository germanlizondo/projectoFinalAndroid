package com.example.proyectofinal;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.proyectofinal.Utilities.BackendConection;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Socket mSocket;
    private String id;
    private String nickname;
    private Marker marker;
    private double latAnterior = 0;
    private double lngAnterior = 0;
    {
        try {
            mSocket = IO.socket(BackendConection.SERVER);
        } catch (URISyntaxException e) {}
    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            MapsActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];


                    updateMap(data);
                }
            });
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        this.id= intent.getStringExtra("idUser");
        this.nickname = intent.getStringExtra("nickname").toUpperCase();



    }

    @Override
    protected void onResume() {
        super.onResume();
        mSocket.connect();


        mSocket.emit("join-geolocation",id+"-geo");

        mSocket.on("geolocation-send", onNewMessage);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSocket.emit("leave-geolocation",id+"-geo");

    }


    public void updateMap(JSONObject data){
        try {


            double lat = data.getDouble("latitude");
        double lng = data.getDouble("longitud");

            if(lat==latAnterior && lng==lngAnterior){

            }else{
                this.marker.remove();

                LatLng ubicacion = new LatLng(lat, lng);
              this.marker =  mMap.addMarker(new MarkerOptions().position(ubicacion).title(nickname));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion,15));

                latAnterior = lat;
                lngAnterior = lng;
            }




        } catch (JSONException e) {
            e.printStackTrace();
        }
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
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng ubicacion = new LatLng(41.3828939, 2.1774322);
      this.marker =  mMap.addMarker(new MarkerOptions().position(ubicacion).title(nickname));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion,15));
    }
}
