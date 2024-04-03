package com.victormp.audioplayer;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.victormp.audioplayer.databinding.FragmentAddmediaBinding;

public class AddMediaFragment extends Fragment {

    // Declaración de variables
    protected EditText media_name;
    protected EditText media_url;
    protected DataBaseSQL db;

    private FragmentAddmediaBinding binding;    // Variable para el binding con el layout

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentAddmediaBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Obtener el contexto de la actividad que contiene el fragmento. Obligatorio ya que no se puede usar el comando 'this' en los fragments
        Context context = requireContext();
        // Inicializar base de datos
        db = new DataBaseSQL(context);

        // Inicializar elementos de la UI
        media_name = view.findViewById(R.id.nameEditText);
        media_url = view.findViewById(R.id.urlEditText);

        // Listener del botón addAudioBtn para añadir el contenido del formulario a la bbdd, a través del binding
        binding.addAudioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Comprobar que existan ambos campos
                if (media_name != null && media_url != null) {
                    // Obtiener el texto y limpia los espacios en blanco al principio y final
                    String name = media_name.getText().toString().trim();
                    String url = media_url.getText().toString().trim();

                    // Comprobar que el usuario haya introducido algún nombre y url
                    if (!name.isEmpty() && !url.isEmpty()) {
                        // Llamada al método addMedia de DataBaseSQL para añadirlo a la bbdd
                        db.addMedia(name, url);

                        Toast.makeText(context, getText(R.string.toast_media_added), Toast.LENGTH_SHORT).show();
                        //Cerrar la BBDD
                        db.close();

                        // Handler para volver automáticamente al fragment de inicio después de 1s
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Crea un intent para regresar a la actividad principal y finaliza la actividad actual
                                NavController navController = NavHostFragment.findNavController(AddMediaFragment.this);
                                navController.popBackStack();
                            }
                        }, 1000);

                    // Si falta algún campo, indicar el error
                    } else {
                        // Mostrar un mensaje de error si el texto está vacío
                        Toast.makeText(context, getText(R.string.toast_media_failed), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    // Método para liberar los bindings del fragmento al destruirle
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}