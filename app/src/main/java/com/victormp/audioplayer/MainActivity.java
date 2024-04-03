package com.victormp.audioplayer;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.victormp.audioplayer.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    /* He querido seguir una estructura de navegación por Fragments para la 'StartActivity' y 'CrearActivity'.
    *  Por lo tanto, el contenido que correspondería a 'StartActivity' está en HomeFragment y el de 'CrearActivity' en AddMediaFragment.
    * 'ReproductorActivity' sigue siendo una activity independiente llamada MusicPlayerActivity
    * Además de querer practicar esta estructura de navegación, consideré que la toolbar superior se ajustaba a los requisitos de la tarea.
    * En esta actividad solo se controla las opciones del menú.
    * */

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private DataBaseSQL db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        db = new DataBaseSQL(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // Si se selecciona la opción 'close'
        if (id == R.id.action_close) {
            finish(); // Salir de la aplicación
            return true;

        /* He añadido una opción para eliminar todos los items de la tabla MEDIA.
        *  Sin embargo, no se actualiza la vista, no he conseguido implementarlo correctamente.
        *  Hay que salir y volver a entrar en la app para ver el cambio.
        * */

        // Si se selecciona la opción 'delete all media'
        } else if (id == R.id.action_clear_playlist) {
            // Limpiar la BBDD
            db.deleteAll();
            // Mostrar un texto de confirmación
            Toast.makeText(MainActivity.this, getText(R.string.toast_DeleteAll), Toast.LENGTH_SHORT).show();
            // Cerrar DataBaseSQL y liberar recursos
            db.close();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

}