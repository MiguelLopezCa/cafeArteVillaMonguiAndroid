package BD;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends  SQLiteOpenHelper {
    //Constante para creación de la tabla USUARIOS
    private static final String CREAR_TABLA_USUARIOS = "CREATE TABLE " + Constantes.TABLA_USUARIOS +
            " (ID INTEGER PRIMARY KEY AUTOINCREMENT," +
            " NOMBRE TEXT NOT NULL," +
            " CONTRASEÑA TEXT NOT NULL)";
    //Constante para creación de la tabla PROVEEDORES
    private static final String CREAR_TABLA_PROVEEDORES= "CREATE TABLE " + Constantes.TABLA_PROVEEDORES +
            " (ID INTEGER PRIMARY KEY AUTOINCREMENT ," +
            "NIT INTEGER NOT NULL,"+
            "NOM_PROV TEXT NOT NULL,"+
            "DIREC_PROV	TEXT NOT NULL,"+
            "TEL_PROV	INTEGER NOT NULL)";

    //Constante para creación de la tabla ELEMENTOS
    private static final String CREAR_TABLA_ELEMENTOS= "CREATE TABLE " + Constantes.TABLA_ELEMENTOS +
            " (CODIGO_ELEMENTO INTEGER PRIMARY KEY AUTOINCREMENT," +
            " NOMBRE TEXT NOT NULL," +
            " DESCRIPCION TEXT NOT NULL)";
    //Constante para creación de la tabla ENTRADAS
    private static final String CREAR_TABLA_ENTRADAS= "CREATE TABLE " + Constantes.TABLA_ENTRADAS +
            " (ID_ENTRADA INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE," +
            " FECHA_ENTRADA TEXT NOT NULL," +
            "CODIGO_ELEMENTO INTEGER NOT NULL,"+
            "CANT INTEGER NOT NULL,"+
            "ID_PROV INTEGER NOT NULL,"+
            "PRECIO_COMPRA INTEGER NOT NULL,"+
            "FOREIGN KEY(ID_PROV) REFERENCES TABLA_PROVEEDORES (ID)," +
            "FOREIGN KEY(CODIGO_ELEMENTO) REFERENCES TABLA_ELEMENTOS(CODIGO_ELEMENTO))";
    //Constante para creación de la tabla SALIDAS
    private static final String CREAR_TABLA_SALIDAS= "CREATE TABLE " + Constantes.TABLA_SALIDAS +
            " (ID_SALIDA INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE," +
            " FECHA_SALIDA TEXT NOT NULL," +
            "CODIGO_ELEMENTO INTEGER NOT NULL,"+
            "CANT_ELE_SAL INTEGER NOT NULL,"+
            "ID_PROV INTEGER NOT NULL,"+
            "FOREIGN KEY(ID_PROV) REFERENCES TABLA_PROVEEDORES (ID)," +
            "FOREIGN KEY(CODIGO_ELEMENTO) REFERENCES TABLA_ELEMENTOS(CODIGO_ELEMENTO))";


    //Constructor con la estructura para generar la baase de datos
    public DBHelper(@Nullable Context context) {
        super(context, Constantes.NOMBRE_BD, null, Constantes.VERSION_BD);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Creación de la tabla USUARIOS en La BD
        db.execSQL(CREAR_TABLA_USUARIOS);
        db.execSQL(CREAR_TABLA_PROVEEDORES);
        db.execSQL(CREAR_TABLA_ELEMENTOS);
        db.execSQL(CREAR_TABLA_ENTRADAS);
        db.execSQL(CREAR_TABLA_SALIDAS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Actualización de la BD por cambiso de versión
        db.execSQL("DROP TABLE IF EXISTS " + Constantes.TABLA_USUARIOS);//Usuarios
        db.execSQL(CREAR_TABLA_USUARIOS);//Después de eliminar, crea nuevamente la tabla
        db.execSQL("DROP TABLE IF EXISTS " + Constantes.TABLA_PROVEEDORES);//proveedores
        db.execSQL(CREAR_TABLA_PROVEEDORES);//Después de eliminar, crea nuevamente la tabla
        db.execSQL("DROP TABLE IF EXISTS " + Constantes.TABLA_ELEMENTOS);//Elementos
        db.execSQL(CREAR_TABLA_ELEMENTOS);//Después de eliminar, crea nuevamente la tabla
        db.execSQL("DROP TABLE IF EXISTS " + Constantes.TABLA_ENTRADAS);//ENTRADAS
        db.execSQL(CREAR_TABLA_ENTRADAS);//Después de eliminar, crea nuevamente la tabla
        db.execSQL("DROP TABLE IF EXISTS " + Constantes.TABLA_SALIDAS);//SALIDAS
        db.execSQL(CREAR_TABLA_SALIDAS);//Después de eliminar, crea nuevamente la tabla

    }
    public boolean TablaVacia(String nombreTabla) {
        SQLiteDatabase db = getReadableDatabase();
        String countQuery = "SELECT COUNT(*) FROM " + nombreTabla;
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count == 0;
    }

}
