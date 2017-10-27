package com.ticarte.rafa.demoandroidpermission;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    final private int CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 123;
    private Context myContext;
    private ConstraintLayout constraintLayoutMainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Almacenamos el contexto de la actividad para utilizar en las clases internas
        myContext = this;

        // Recuperamos el Layout donde mostrar el Snackbar con las notificaciones
        constraintLayoutMainActivity = findViewById(R.id.constraintLayoutMainActivity);

        // Los permisos normales se aceptan al instalar la aplicación
        // Aún así es mejor comprobar que se tienen permisos antes de proceder a utilizarlos
        // Los permisos deben aparecer en Manifest.xml
        Button buttonNormal = findViewById(R.id.buttonNormal);
        buttonNormal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int InternetPermission = ContextCompat.checkSelfPermission(myContext, Manifest.permission.INTERNET);
                Log.d("MainActivity", "INTERNET Permission: " + InternetPermission);

                if (InternetPermission != PackageManager.PERMISSION_GRANTED) {
                    // Permiso denegado
                    Snackbar.make(constraintLayoutMainActivity, getResources().getString(R.string.internet_permission_denied), Snackbar.LENGTH_LONG)
                            .show();
                } else {
                    // Permiso aceptado
                    Snackbar.make(constraintLayoutMainActivity, getResources().getString(R.string.internet_permission_granted), Snackbar.LENGTH_LONG)
                            .show();
                }
            }
        });

        // Los permisos peligrosos se deben solicitar en tiempo de ejecución si no se poseen
        // Si se acepta un permiso del grupo al que pertenezca se están aceptando también el resto de permisos
        // Los permisos deben aparecer en Manifest.xml
        Button buttonDangerous = findViewById(R.id.buttonDangerous);
        buttonDangerous.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int WriteExternalStoragePermission = ContextCompat.checkSelfPermission(myContext, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                Log.d("MainActivity", "WRITE_EXTERNAL_STORAGE Permission: " + WriteExternalStoragePermission);

                if (WriteExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
                    // Permiso denegado
                    // A partir de Marshmallow (6.0) se pide aceptar o rechazar el permiso en tiempo de ejecución
                    // En las versiones anteriores no es posible hacerlo
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, CODE_WRITE_EXTERNAL_STORAGE_PERMISSION);
                        // Una vez que se pide aceptar o rechazar el permiso se ejecuta el método "onRequestPermissionsResult" para manejar la respuesta
                        // Si el usuario marca "No preguntar más" no se volverá a mostrar este diálogo
                    } else {
                        Snackbar.make(constraintLayoutMainActivity, getResources().getString(R.string.write_permission_denied), Snackbar.LENGTH_LONG)
                                .show();
                    }
                } else {
                    // Permiso aceptado
                    Snackbar.make(constraintLayoutMainActivity, getResources().getString(R.string.write_permission_granted), Snackbar.LENGTH_LONG)
                            .show();
                }

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case CODE_WRITE_EXTERNAL_STORAGE_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permiso aceptado
                    Snackbar.make(constraintLayoutMainActivity, getResources().getString(R.string.write_permission_accepted), Snackbar.LENGTH_LONG)
                            .show();
                } else {
                    // Permiso rechazado
                    Snackbar.make(constraintLayoutMainActivity, getResources().getString(R.string.write_permission_not_accepted), Snackbar.LENGTH_LONG)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}

