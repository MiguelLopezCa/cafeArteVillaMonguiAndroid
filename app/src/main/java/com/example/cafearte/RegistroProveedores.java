package com.example.cafearte;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import BD.Constantes;
import BD.DBHelper;

public class RegistroProveedores extends AppCompatActivity {
   GestionProveedores gestionPoveedores = new GestionProveedores();
    EditText txtNit,txtNombre,txtDireccion,txtTelefono;
    Button btnCrearProveedor,btnRegresar;
    ArrayList<String> listaInformacion;//Lista los nombres de los usuarios
    ArrayList<Proveedores> listaProveedores;//Lista objetos de tipo Usuario
    ArrayAdapter adapter;//Adpater para manejo de la lista
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_proveedores);

    //Relación con elementos del layout
    txtNit = findViewById(R.id.txtNit_NuevoProveedor);
    txtNombre = findViewById(R.id.txtNombre_NuevoProveedor);
    txtDireccion = findViewById(R.id.txtDireccion_NuevoProveedor);
    txtTelefono = findViewById(R.id.txtTelefono_NuevoProveedor);
    btnCrearProveedor = findViewById(R.id.btnCrearProveedor);
    btnRegresar = findViewById(R.id.btnRegresar);

    //Inicializa listados
    listaInformacion = new ArrayList<>();
        listaProveedores = new ArrayList<>();
       // gestionPoveedores.consultarProveedoresExistentes();//Carga contenido a los arreglos
        btnCrearProveedor.setOnClickListener(v->{
        registrarUsuarioBD();
          //  gestionPoveedores.consultarProveedoresExistentes();//Actualiza listado
        limiarCampos();//Limpia campos de interfaz
    });

        btnRegresar.setOnClickListener(v->{
        startActivity(new Intent(this, GestionProveedores.class));
    });

}
    //Limpia contenido de los textos en la interfaz
    public void limiarCampos(){
        txtNit.setText("");
        txtNombre.setText("");
        txtDireccion.setText("");
        txtTelefono.setText("");
    }

    //Método para la creación de usuarios
    public void registrarUsuarioBD(){
        try{
            //Conexion
            DBHelper helper = new DBHelper(this);
            //Objeto para escritura
            SQLiteDatabase base_datos = helper.getWritableDatabase();

            if(!consultarDuplicidad(txtNit.getText().toString())) {
                //inserción clave-valor
                ContentValues values = new ContentValues();
                values.put("NIT", txtNit.getText().toString());
                values.put("NOM_PROV", txtNombre.getText().toString());
                values.put("DIREC_PROV", txtDireccion.getText().toString());
                values.put("TEL_PROV", txtTelefono.getText().toString());

                //Inserción en la BD, retorna el ID
                long id = base_datos.insert(Constantes.TABLA_PROVEEDORES,
                        null, values);

                if (id > 0) {
                    Toast.makeText(this, "Proveedor CREADO", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Error al crear el Proveedor", Toast.LENGTH_LONG).show();
                }
            }
            else {
                Toast.makeText(this, "El proveedor ya se encuentra refgstrado ", Toast.LENGTH_LONG).show();
            }
        }
        catch(Exception e){
            Toast.makeText(this,"ERROR:  No fue posible registrar la información",Toast.LENGTH_LONG).show();
        }
    }

    private boolean consultarDuplicidad(String nit){
        try{
            //Conexion
            DBHelper helper = new DBHelper(this);
            //Objeto para lectura
            SQLiteDatabase base_datos = helper.getReadableDatabase();
            //Arreglo con parámetros
            String[] parametrosConsulta = {nit};
            //Sentencia SELECT
            Cursor cursor = base_datos.rawQuery("SELECT NIT FROM " +
                    Constantes.TABLA_PROVEEDORES + " WHERE NIT"+"=?",parametrosConsulta);
            if(cursor.getCount()>0) {
                return true;
            }
            else{
                return false;
            }

        }
        catch(Exception e){
            return false;
        }
    }
}