package com.victormp.audioplayer;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteException;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DataBaseSQL extends SQLiteOpenHelper {

    // Declaración de variables
    private SQLiteDatabase db;
    private static final String DATABASE_NAME = "audio.db";     // Nombre de la base de datos
    private static final int DATABASE_VERSION = 1;      // Versión de la base de datos
    private static final String TABLE_MEDIA = "media";      // Nombre de la tabla de MEDIA
    private ArrayList<MediaModel> allMediaItems;


    // Constructor de la BBDD
    public DataBaseSQL(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear la tabla 'media' con los campos id, media_name y url
        db.execSQL("CREATE TABLE " + TABLE_MEDIA + " (id INTEGER PRIMARY KEY AUTOINCREMENT, media_name TEXT, url TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEDIA);
        onCreate(db);
    }

    // Método para añadir un nuevo audio a la BBDD (Obligatorio indicar NOMBRE y URL)
    public void addMedia(String media_name, String url) {
        // Poner la BBDD en modo escritura
        db = this.getWritableDatabase();
        // Ejecutar query SQL para añadir el nombre y URL
        db.execSQL("INSERT INTO " + TABLE_MEDIA + " (media_name, url) VALUES ('" + media_name + "', '" + url + "')");
    }

    // Método para obtener un array con todos los elementos de la base de datos como objetos MediaModel
    public ArrayList<MediaModel> getAllMediaItems() {
        // Array para almacenar todos los medios
        allMediaItems = new ArrayList<>();
        try {
            // Poner la BBDD en modo lectura
            db = this.getReadableDatabase();
            // Ejecutar query SQL para obtener todos los elementos ordenados por ID
            Cursor cursor = db.rawQuery("SELECT id, media_name, url FROM " + TABLE_MEDIA + " ORDER BY id ASC", null);
            if (cursor.moveToFirst()) {
                do {
                    // Obtener los valores de ID, nombre y URL del cursor
                    int id = cursor.getInt(cursor.getColumnIndex("id"));
                    String name = cursor.getString(cursor.getColumnIndex("media_name"));
                    String url = cursor.getString(cursor.getColumnIndex("url"));

                    // Crear un nuevo objeto MediaModel con los datos obtenidos
                    MediaModel mediaItem = new MediaModel(id, name, url);
                    // Añadir el objeto al array
                    allMediaItems.add(mediaItem);
                } while (cursor.moveToNext());
            }
            // Cerrar el cursor
            cursor.close();
        } catch (SQLiteException e) {
            e.printStackTrace();
            db.close();
        } finally {
            // Cerrar la BBDD y liberar recursos
            if (db != null) {
                db.close();
            }
        }
        // Devolver la lista de objetos MediaItems
        return allMediaItems;
    }

    // Método para eliminar todos los elementos de la tabla (aunque no era necesario quise añadirlo para poder limpiar la ListView facilmente)
    public void deleteAll() {
        // Poner la BBDD en modo escritura
        db = this.getWritableDatabase();
        // Ejecutar la consulta SQL para eliminar todos los elementos de la tabla
        db.execSQL("DELETE FROM " + TABLE_MEDIA);
        // Restablecer el contador del ID
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + TABLE_MEDIA + "'");
    }
}

