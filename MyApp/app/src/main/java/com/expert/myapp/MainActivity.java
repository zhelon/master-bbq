package com.expert.myapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.expert.myapp.adapter.PhotoAdapter;
import com.expert.myapp.pojo.Photo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private RecyclerView lstLista;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lstLista = (RecyclerView) findViewById(R.id.id_list);

        lstLista.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        lstLista.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL  ));
        lstLista.setItemAnimator(new DefaultItemAnimator());
        adapter = new PhotoAdapter(getAllPhoto());
        lstLista.setAdapter(adapter);
        
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this
                            , new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 110);

                } else {

                    //Intent camara
                    Intent it = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    Snackbar.make(view, "Tomando foto", Snackbar.LENGTH_LONG).show();
                    startActivityForResult(it, 200);
                }
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data != null) {

            if (requestCode == 200) {

                //Obtenemos la foto, viene dentro de un bundle como "data"
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                //La imagen se comprime, usando formato JPEG, calidad y el outputstream
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString()+ "image.jpg");
                Log.i("j", file.getAbsolutePath());

                try {
                    file.createNewFile();
                    FileOutputStream fo = new FileOutputStream(file);
                    fo.write(bytes.toByteArray());
                    fo.close();
                    Toast.makeText(getApplicationContext(), "Guardando foto...", Toast.LENGTH_SHORT).show();

                    persistPhoto(bytes.toByteArray());

                    adapter = new PhotoAdapter(getAllPhoto());
                    lstLista.setAdapter(adapter);

                } catch (IOException e) {
                    Log.e("MUrio", e.getMessage());
                    Toast.makeText(getApplicationContext(), "Murio...", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }
    }

    private void persistPhoto(byte[] image) {
        Realm realms = Realm.getDefaultInstance();

        realms.beginTransaction();
        Photo photo = realms.createObject(Photo.class);
        photo.setComment("Hola Mundo");
        photo.setId(UUID.randomUUID().toString());
        photo.setImage(image);
        realms.commitTransaction();

    }


    private List<Photo> getAllPhoto() {
        Realm realms = Realm.getDefaultInstance();
        RealmResults<Photo> result = realms.where(Photo.class).findAll();
        return result;
    }
}
