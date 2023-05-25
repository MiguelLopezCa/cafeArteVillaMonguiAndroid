package com.example.cafearte;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import BD.Constantes;
import BD.DBHelper;

public class GestionProveedores extends AppCompatActivity {
    Button btnAgregar;
    ListView lvProveedores;
    ArrayList<String> listaInformacion; // Lista los nombres de los usuarios
    ArrayList<Proveedores> listaProveedores; // Lista objetos de tipo Usuario
    ArrayAdapter adapter; // Adapter para manejo de la lista

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proveedores);

        // Obtener referencias a los elementos del layout
        btnAgregar = findViewById(R.id.btnAgregar);
        TableLayout tableLayout = findViewById(R.id.tableLayout);

        // Configurar el evento click del botón Agregar
        btnAgregar.setOnClickListener(v -> {
            startActivity(new Intent(this, RegistroProveedores.class));
        });

        consultarProveedoresExistentes(); // Cargar contenido a los arreglos
    }

    // Menú de la parte superior asociado al recurso
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Evento del ítem que integra el recurso Menú
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Conectamos con el layout de configuración
        if (item.getItemId() == R.id.inventarioID) {
            // Inicia como actividad la clase ConfiguracionFragment
            startActivity(new Intent(this, Inventario.class));
        }
        return super.onOptionsItemSelected(item);
    }

    // Consulta proveedores existentes en la BD
    public void consultarProveedoresExistentes() {
        TableLayout tableLayout = findViewById(R.id.tableLayout);
        try {
            // Conexión a la base de datos
            DBHelper helper = new DBHelper(this);
            SQLiteDatabase base_datos = helper.getReadableDatabase();

            // Consulta los proveedores existentes
            Cursor cursor = base_datos.rawQuery("SELECT NIT, NOM_PROV FROM " +
                    Constantes.TABLA_PROVEEDORES + " ORDER BY ID", null);

            while (cursor.moveToNext()) {
                // Obtiene los datos del proveedor
                long nit = cursor.getLong(0);
                String nombre = cursor.getString(1);
                // Crea una nueva fila en la tabla
                TableRow tableRow = new TableRow(this);

                // Crea y configura los TextView para cada campo de datos
                TextView nitTextView = new TextView(this);
                nitTextView.setText(String.valueOf(nit));
                nitTextView.setGravity(Gravity.CENTER);
                // Crear y configurar TextView para el campo "nombre"
                TextView nombreTextView = new TextView(this);
                nombreTextView.setText(nombre);
                nombreTextView.setGravity(Gravity.CENTER);
                // Crea y modifica los botones con las acciones
                Button btnModificar = new Button(this);
                btnModificar.setText("+");
                btnModificar.setOnClickListener(v -> {
                    startActivity(new Intent(this, RegistroProveedores.class));
                });
                Button btnEliminar = new Button(this);
                btnEliminar.setText("-");
                btnEliminar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //llamar metodo de verificacion para eliminar
                        ConfirmarEliminarUsuario( nit);
                    }
                });
                // Agrega los TextView a la fila de la tabla
                tableRow.addView(nitTextView);
                tableRow.addView(nombreTextView);
                tableRow.addView(btnModificar);
                tableRow.addView(btnEliminar);
                // Agrega la fila a la tabla
                tableLayout.addView(tableRow);
            }
        } catch (Exception e) {
            Toast.makeText(this, "ERROR: No fue posible consultar la información de los proveedores", Toast.LENGTH_LONG).show();
        }
    }

    private void eliminarUsuario(long nit){
        try{
            //Conexión
            DBHelper helper = new DBHelper(this);
            SQLiteDatabase base_datos = helper.getWritableDatabase();//Objeto para escritura
            String[] parametrosEliminacion = {String.valueOf(nit)};//Parámetro de eliminación
            base_datos.delete(Constantes.TABLA_PROVEEDORES,"NIT=?",//Sentencia delete
                    parametrosEliminacion);
            base_datos.close();//Cierra la conexión

            Toast.makeText(this,"Información de usuario eliminada",Toast.LENGTH_LONG).show();
        }
        catch(Exception e){
            Toast.makeText(this,"ERROR:  No fue posible eliminar la información de los Proveedores",Toast.LENGTH_LONG).show();
        }
    }
    private void ConfirmarEliminarUsuario(long nit){
        TableLayout tableLayout = findViewById(R.id.tableLayout);
        TableRow tableRow = new TableRow(this);
        // Crear el cuadro de diálogo de confirmación
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmación");
        builder.setMessage("¿Deseas eliminar este proveedor?");
// Configurar el botón "Eliminar"
        builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                eliminarUsuario(nit);
                tableLayout.removeView(tableRow);

            }
        });
// Configurar el botón "Cancelar"
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override

            public void onClick(DialogInterface dialog, int which) {
                // Cancelar la operación de eliminación
                dialog.dismiss();
            }
        });
// Mostrar el cuadro de diálogo
        builder.create().show();

    }
}
