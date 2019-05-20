package com.example.proyectofinal.Services;

import android.Manifest;
import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.example.proyectofinal.Utilities.BackendConection;
import com.example.proyectofinal.Utilities.Session;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class GeolocationService extends Service {

    private LocationManager locationManager;
    private LocationListener locationListener;
    private JSONObject data;
    private Session session;
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(BackendConection.SERVER);
        } catch (URISyntaxException e) {}
    }

    @Override
    public void onCreate() {
      //  Toast.makeText(getBaseContext(),"GEOLOCATION START",Toast.LENGTH_SHORT).show();
        this.session = new Session(getBaseContext());
        data = new JSONObject();
        try {
            data.put("room",this.session.getUser().getId()+"-geo");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mSocket.connect();
        locationManager = (LocationManager) getSystemService(getApplicationContext().LOCATION_SERVICE);
        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                try {
                    data.put("latitude",location.getLatitude());
                    data.put("longitud",location.getLongitude());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            //    Toast.makeText(getApplicationContext(),location.getLatitude()+"   : LATITUDE",Toast.LENGTH_SHORT).show();
                mSocket.emit("geolocation", data);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        };

        requestLocation();


    }

    public void requestLocation(){
        try{

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0, locationListener);

        }catch (SecurityException e){
            e.fillInStackTrace();
        }
    }





    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
