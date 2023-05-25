package com.example.cafearte;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Inventario extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventario);

    }

    //Menú de la parte superior asociado al recurso
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Evento del ítem que integra el recurso Menú
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //Conectamos con el layout de configuración
        if(item.getItemId()==R.id.proveedoresID)
        {
            //Inicia como actividad la clase ConfiguracionFragment
            startActivity(new Intent(this, GestionProveedores.class));
        }
        return super.onOptionsItemSelected(item);
    }
}