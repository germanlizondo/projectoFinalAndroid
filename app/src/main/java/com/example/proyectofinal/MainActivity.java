package com.example.proyectofinal;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectofinal.Fragments.ChatsFragment;
import com.example.proyectofinal.Fragments.SettingsFragment;
import com.example.proyectofinal.Fragments.ContactsFragment;
import com.example.proyectofinal.Model.UserClient;
import com.example.proyectofinal.Services.GeolocationService;
import com.example.proyectofinal.Utilities.Session;

public class MainActivity extends AppCompatActivity {
    private TextView mTextMessage;
    private Fragment selectedFragment = null;
    private Session session;
    private Bundle bundle;
    private UserClient user;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_user:
                    selectedFragment = new ContactsFragment();
                    selectedFragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment,
                            selectedFragment).commit();
                    return true;
                case R.id.navigation_chats:
                    selectedFragment = new ChatsFragment();
                    selectedFragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment,
                            selectedFragment).commit();
                    return true;
                case R.id.navigation_settings:
                    selectedFragment = new SettingsFragment();
                    selectedFragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment,
                            selectedFragment).commit();
                    return true;
            }


            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.adduser) {
            startActivity(new Intent(this, AddUserActivity.class));
            return true;
        }else if(id==R.id.logout){
            session.logout();
            stopService(new Intent(MainActivity.this, GeolocationService.class));
            startActivity(new Intent(this,LoginActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        this.session = new Session(this);
        this.user =  session.getUser();

        bundle = new Bundle();
        bundle.putParcelable("user", this.user);
        if (this.session.getUser().getNickname().equals("") || this.session.getUser()==null){
            startActivity(new Intent(this,LoginActivity.class));
        }


        mTextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        selectedFragment = new ContactsFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment,
                selectedFragment).commit();


    }


    public void requestPermissionsGeo(){
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            },10);
            return;
        }else{

            startService(new Intent(MainActivity.this, GeolocationService.class));

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case 10:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    startService(new Intent(MainActivity.this, GeolocationService.class));
                }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestPermissionsGeo();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(MainActivity.this, GeolocationService.class));
    }
}
