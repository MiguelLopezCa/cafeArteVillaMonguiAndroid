package com.example.cafearte;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import BD.Constantes;
import BD.DBHelper;

public class MainActivity extends AppCompatActivity {
    int validar=0;// variable para validar la existencia del usuario
    //Recursos del layout
    EditText txtUsuario,txtContraseña;
    Button btnIngresar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Relación con elementos del layout
        txtUsuario = findViewById(R.id.txtUsuario);
        txtContraseña = findViewById(R.id.txtContraseña);
        btnIngresar = findViewById(R.id.btnIngresar);

            DBHelper helper = new DBHelper(this);
            String nombreTabla = "USUARIOS"; // Nombre de la tabla a validar
        if (helper.TablaVacia(nombreTabla)) {
            // No hay registros en la tabla, crear usuario
            registrarUsuarioBD();
        } else {
            // La tabla ya contiene registros
        }
        btnIngresar.setOnClickListener(v->{
            consultarUsuario();

        });
    }
    private void registrarUsuarioBD(){
            try {
                //Conexion
                DBHelper helper = new DBHelper(this);
                //Objeto para escritura
                SQLiteDatabase base_datos = helper.getWritableDatabase();
                //inserción
                base_datos.execSQL("INSERT INTO " + Constantes.TABLA_USUARIOS +
                        " (NOMBRE,CONTRASEÑA) " +
                        " VALUES ('GERENTE','12345')"
                );

                base_datos.close();
            }
            catch(Exception e){
                Toast.makeText(this,"ERROR:  No fue posible registrar la información",Toast.LENGTH_LONG).show();
            }

    }

    //Método para la consulta de usuarios
    private void consultarUsuario(){
        try{
            // Conexión
            DBHelper helper = new DBHelper(this);
            // Objeto para la lectura en la BD
            SQLiteDatabase base_datos = helper.getReadableDatabase();
            // Arreglo con las condiciones de la consulta -> WHERE
            String[] parametrosConsulta = {txtUsuario.getText().toString(), txtContraseña.getText().toString()};
            // Arreglo con los campos de la consulta -> SELECT
            String[] camposConsulta = {"NOMBRE"};
            // Cursor con el resultado de la consulta
            Cursor cursor = base_datos.query(Constantes.TABLA_USUARIOS,
                    camposConsulta, "NOMBRE=? AND CONTRASEÑA=?",
                    parametrosConsulta, null, null, null);

            if (cursor.moveToFirst()) {
                // Si se encuentra un registro válido, se muestra un mensaje de bienvenida
                Toast.makeText(this, "Bienvenido: " + cursor.getString(0), Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, Inventario.class));
            } else {
                // Si no se encuentra un registro válido, se muestra un mensaje de error
                Toast.makeText(this, "Nombre de usuario o contraseña incorrectos", Toast.LENGTH_LONG).show();
                txtUsuario.setText(""); // Limpia campo usuario después del error
                txtContraseña.setText(""); // Limpia campo contraseña después del error
            }
        }
        catch(Exception e){
            Toast.makeText(this, "Error al consultar el usuario", Toast.LENGTH_LONG).show();
            txtUsuario.setText(""); // Limpia campo usuario después del error
            txtContraseña.setText(""); // Limpia campo contraseña después del error
        }
    }
}