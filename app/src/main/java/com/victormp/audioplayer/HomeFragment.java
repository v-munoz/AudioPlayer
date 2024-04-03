package com.victormp.audioplayer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.victormp.audioplayer.databinding.FragmentHomeBinding;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    // Declaración de variables
    protected ListView listView;
    protected Button btn_addMedia;
    protected DataBaseSQL db;
    private ArrayList<MediaModel> all_media;     // Variable para el array con los objetos MediaModel con la información de las canciones
    private ArrayList<String> all_media_names;  // Variable para el array con los nombres de las canciones para mostrar en la ListView
    private ArrayAdapter<String> adapter;       // Variable para el adaptador de la ListView
    private FragmentHomeBinding binding;        // Variable para el binding con el layout


    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Obtener el contexto de la actividad que contiene el fragmento. Obligatorio ya que no se puede usar el comando 'this' en los fragments
        Context context = requireContext();
        // Inicializar base de datos
        db = new DataBaseSQL(context);


        // Inicializar elementos de la UI
        listView = view.findViewById(R.id.listView);
        btn_addMedia = view.findViewById(R.id.addAudioBtn);

        // Inicializar el array de objetos MediaModel y el adaptador para la lista
        all_media = new ArrayList<>();
        // Llamada al método getAllMediaItems de DataBaseSQL para obtener todos los elementos de la bbdd y añadirlos al array all_media
        all_media.addAll(db.getAllMediaItems());

        // Crear un array <String> que almacene el nombre e id por cada elemento de la bbdd (en formato: 'id'. 'nombre')
        all_media_names = new ArrayList<>();
        for (MediaModel item : all_media) {
            String id = String.valueOf(item.getId());
            String name = item.getName();
            all_media_names.add(id + ". " + name);
        }

        // Cargar la listView con los datos del array all_media_names usando el adapter por defecto
        adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, all_media_names);
        listView.setAdapter(adapter);

        // Listener para cada elemento de la ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Obtener el objeto MediaModel seleccionado
                MediaModel mediaItem = all_media.get(i);

                if (mediaItem != null) {
                    // Obtener información del MediaModel seleccionado
                    int media_id = mediaItem.getId();
                    String media_name = mediaItem.getName();
                    String media_url = mediaItem.getUrl();

                    // Crear un intent para iniciar la MusicPlayerActivity, enviando los datos del audio como extras
                    Intent player_activity = new Intent(context, MusicPlayerActivity.class);
                    player_activity.putExtra("media_id", media_id);
                    player_activity.putExtra("media_name", media_name);
                    player_activity.putExtra("media_url", media_url);

                    // Cerrar la BBDD
                    db.close();
                    // Iniciar la actividad del reproductor de música
                    startActivity(player_activity);
                }
            }
        });

        // Listener de navegación entre Fragments hacia AddMediaFragment
        binding.btnAddMedia.setOnClickListener(v ->
                NavHostFragment.findNavController(HomeFragment.this)
                        .navigate(R.id.action_HomeFragment_to_AddMediaFragment)
        );
    }

    // Método para liberar los bindings del fragmento al destruirle
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        db.close();
    }

}