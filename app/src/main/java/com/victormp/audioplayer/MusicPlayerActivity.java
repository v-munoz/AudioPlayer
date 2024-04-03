package com.victormp.audioplayer;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;

public class MusicPlayerActivity extends AppCompatActivity {

    protected ImageButton btnBack;
    protected Button btnPlay, btnPause, btnStop, btnPlayPause;
    protected TextView media_name, media_url;
    private int currentPosition;

    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_music_player);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar elementos de la UI
        media_name = findViewById(R.id.media_name);
        media_url = findViewById(R.id.media_url);

        btnBack = findViewById(R.id.back_btn);
        btnPlay = findViewById(R.id.btn_play);
        btnPause = findViewById(R.id.btn_pause);
        btnStop = findViewById(R.id.btn_stop);
        btnPlayPause = findViewById(R.id.btn_togglePayPause);

        // Completar el nombre del audio y su url a partir de los Extras enviados desde HomeFragment
        media_name.setText(getIntent().getStringExtra("media_name"));
        media_url.setText(getIntent().getStringExtra("media_url"));

        setBtnState(0); // Establecer los botones de reproducción en posición 0 (Play = ON, Pause y Stop = OFF)



        // Listener del botón para retroceder a la pantalla principal
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent action_to_MainActivity = new Intent(MusicPlayerActivity.this, MainActivity.class);
                startActivity(action_to_MainActivity);
                finish(); // Terminar la actividad del reproductor
            }
        });

        // Listener para el botón de play
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBtnState(1); // Establecer los botones de reproducción en posición 1 (Play = OFF, Pause y Stop = ON)

                try {
                    // 1. Iniciar la reproducción del audio
                    // Verificar que no haya un MediaPlayer iniciado
                    if (mp == null) {
                        // Crear un nuevo MediaPlayer
                        mp = new MediaPlayer();
                        // Obtener la URL de la canción y convertirla en Uri
                        Uri media_uri = Uri.parse(media_url.getText().toString());
                        // Establecer el tipo de flujo de audio
                        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        // Establecer la fuente de datos para el MediaPlayer
                        mp.setDataSource(MusicPlayerActivity.this, media_uri);
                        // Preparar de forma asíncrona el MediaPlayer ya que obtenemos el audio online
                        mp.prepareAsync();

                        // Listener para iniciar la reproducción cuando el MediaPlayer esté preparado
                        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mediaPlayer) {
                                mp.start();
                            }
                        });

                    // 2. Reanudar la reproducción
                    // Si el MediaPlayer existe pero no está reproduciendo
                    } else if (!mp.isPlaying()) {
                        // Buscar la posición guardada de reproducción
                        mp.seekTo(currentPosition);
                        // Iniciar la reproducción desde la posición guardada
                        mp.start();
                    }
                } catch (IOException e) {
                    // Capturar una excepción si ocurre un error al configurar el MediaPlayer y mostrar un mensaje de error
                    Toast.makeText(MusicPlayerActivity.this, getText(R.string.toast_url_error), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

        // Listener para el botón de pausa
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 3. Pausar la reproducción
                // Comprobar que existe un MediaPlayer y se está ejecutando
                if (mp != null && mp.isPlaying()) {
                    setBtnState(2); // Establecer los botones de reproducción en posición 2 (Play y STOP = ON, Pause = OFF)

                    // Guardar la posición actual de reproducción
                    currentPosition = mp.getCurrentPosition();
                    // Pausar la reproducción del MediaPlayer
                    mp.pause();
                }
            }
        });

        // Listener para el botón de stop
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 4. Detener la reproducción
                // Comprobar que exista una reproducción
                if (mp != null) {
                    setBtnState(0); // Establecer los botones de reproducción en posición 0 (Play = ON, Pause y Stop = OFF)

                    // Resetear la posición actual de reproducción
                    currentPosition = 0;
                    // Detener la reproducción
                    mp.stop();
                    // Liberar recursos del MediaPlayer y elimnarlo
                    mp.release();
                    mp = null;
                }
            }
        });

        // Listener para el toggle de play/pausa (debería haber hecho funciones para evitar las redundancias de este código)
        btnPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 3. Pausar la reproducción
                // Comprobar que existe un MediaPlayer y se está ejecutando
                if (mp != null && mp.isPlaying()) {
                    setBtnState(2);
                    currentPosition = mp.getCurrentPosition();
                    mp.pause();

                // 1. y 2. Iniciar la reproducción / reanudación del audio
                } else {
                    setBtnState(1);
                    try {
                        if (mp == null) {
                            mp = new MediaPlayer();
                            Uri media_uri = Uri.parse(media_url.getText().toString());
                            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                            mp.setDataSource(MusicPlayerActivity.this, media_uri);
                            mp.prepareAsync();
                            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                @Override
                                public void onPrepared(MediaPlayer mediaPlayer) {
                                    mp.start();
                                }
                            });
                        } else if (!mp.isPlaying()) {
                            mp.seekTo(currentPosition);
                            mp.start();
                        }
                    } catch (IOException e) {
                        Toast.makeText(MusicPlayerActivity.this, getText(R.string.toast_url_error), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    // Función para controlar los estados de los botones de reproducción
    private void setBtnState(int state) {
        switch (state) {
            case 1: // Reproduciendo música (Play = OFF, Pause y Stop = ON, Toggle = Pause)
                btnPlay.setEnabled(false);
                btnPause.setEnabled(true);
                btnStop.setEnabled(true);
                btnPlayPause.setBackgroundResource(R.drawable.rounded_pause_presentation_24);
                break;
            case 2: // Reprodución pausada (Play y Stop = ON y Pause = OFF, Toggle = Play)
                btnPlay.setEnabled(true);
                btnPause.setEnabled(false);
                btnStop.setEnabled(true);
                btnPlayPause.setBackgroundResource(R.drawable.rounded_play_circle_24);
                break;
            default: // Sin iniciar la reproducción o detenida (Play = ON, Pause y Stop = OFF, Toggle = Play)
                btnPlay.setEnabled(true);
                btnPause.setEnabled(false);
                btnStop.setEnabled(false);
                btnPlayPause.setBackgroundResource(R.drawable.rounded_play_circle_24);
                break;
        }
    }

    // Sobrescribir el método onDestroy() de la actividad
    @Override
    protected void onDestroy() {
        // Liberar los recursos del MediaPlayer y elimnarlo
        super.onDestroy();
        if (mp != null) {
            mp.release();
            mp = null;
        }
    }

}