package com.example.proyectofinal.Fragments;


import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyectofinal.Model.UserClient;
import com.example.proyectofinal.R;
import com.example.proyectofinal.Utilities.BackendConection;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


public class SettingsFragment extends Fragment {
    private final String CARPETA_RAIZ = "proyectoFinal/";
    private final String RUTA_IAMGEN = CARPETA_RAIZ+"imagenes";
    private final int CODIGO_SELECCIONA = 10;
    private final int CODIGO_FOTO = 20;
    private static final int PERMIS_CAMARA = 200;
    private ImageView imagen;
    private  String path = "";
    private RequestQueue mRequestQueue;
    private UserClient user;
    private Bitmap bitmap;

    private TextView nickname;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        this.mRequestQueue = Volley.newRequestQueue(getActivity());
        this.imagen = view.findViewById(R.id.imagenprofile);

        this.nickname = view.findViewById(R.id.nickname);
        this.imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarImagen();
            }
        });
        this.user = getArguments().getParcelable("user");
        this.rellenarUIText();
        this.doPetitionUser();

        return view;
    }

    private void cargarImagen() {

        CharSequence[] opciones = {"Tomar Foto","Cargar Imagen","Cancelar"};
        AlertDialog.Builder alertOpciones = new AlertDialog.Builder(getActivity());
        alertOpciones.setTitle("Selecciona una Opción");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(opciones[i].equals("Tomar Foto")){
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMIS_CAMARA);
                        return;
                    }

                    hacerFoto();
                }else if(opciones[i].equals("Cargar Imagen")){
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/");
                    startActivityForResult(intent.createChooser(intent,"Selecciona la Aplicación"),CODIGO_SELECCIONA);
                }else if(opciones[i].equals("Cancelar")){
                    dialogInterface.dismiss();
                }
            }
        });
        alertOpciones.show();

    }

    private void hacerFoto(){

        File fileImagen = new File(Environment.getExternalStorageDirectory(),RUTA_IAMGEN);
        boolean isCreada = fileImagen.exists();
        String nombre = "";

        if(isCreada==false){
            isCreada=fileImagen.mkdir();
        }
        if(isCreada==true){
             nombre = (System.currentTimeMillis()/1000)+".jpg";
        }

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        path = Environment.getExternalStorageState()+File.separator+RUTA_IAMGEN+File.separator+nombre;



        File imagen = new File(path);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(intent, CODIGO_FOTO);
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            switch (requestCode){
                case CODIGO_SELECCIONA:
                    Uri miPath = data.getData();

                    imagen.setImageURI(miPath);
                    sendIonImage(miPath.toString());
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),miPath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }



              //      sendImage();
                    break;
                case CODIGO_FOTO:
                    Bundle extras = data.getExtras();
                    bitmap = (Bitmap) extras.get("data");
                    imagen.setImageBitmap(bitmap);
              //      sendImage();
                    break;
            }
        }
    }





    public void doPetitionUser(){
        StringRequest postRequest = new StringRequest(Request.Method.GET, BackendConection.SERVER+"/get-user/"+user.getId(),
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        );
        mRequestQueue.add(postRequest);
    }


    public void rellenarUIText(){
        this.nickname.setText(this.user.getNickname());
    }

    private String bitmapToString(Bitmap bitmap){

        ByteArrayOutputStream array = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,array);
        byte[] imgByte = array.toByteArray();
        String imgString = Base64.encodeToString(imgByte,Base64.DEFAULT);

        return imgString;
    }

    public void sendImage(){
        StringRequest postRequest = new StringRequest(Request.Method.POST, BackendConection.SERVER+"/uploadprofileimage",
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("profileimage",bitmapToString(bitmap));

                return params;
            }
        };
        mRequestQueue.add(postRequest);
    }

    public void sendIonImage(String imgPath){
        System.out.println("AAAAAAAAAAAAAAAAAAAAa"+imgPath);

        System.out.println(imgPath);
        Ion.with(getContext())
                .load(BackendConection.SERVER+"/uploadprofileimage")
                .setMultipartParameter("name", "source")
                .setMultipartFile("profileimage", "image/jpeg", new File(imgPath))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        Toast.makeText(getContext(),"COMPLETE",Toast.LENGTH_SHORT).show();
                        //do stuff with result
                    }
                });
    }


}
