// Universitat Politècnica de València
// Escuela Técnica Superior de Ingeniería de Telecomunicación
// --------------------------------------------------------------------
// APLICACIONES TELEMÁTICAS
// Curso 2023 - 2024
// --------------------------------------------------------------------
// Nombre del archivo: MainActivity.java
// --------------------------------------------------------------------
// Autores:         Grupos A y B (3):
//                  Daniel Silva Romero
//                  Carlos Soria Mora
//                  Alejandro Cano Segovia
//                  Hugo Beltrán Sanz
// --------------------------------------------------------------------

package es.upv.etsit.trabajo_atelem;

import android.content.Intent;
/*import android.content.pm.ActivityInfo;
import android.graphics.Color;*/
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
/*import android.widget.TextView;*/
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
/*import android.widget.Button;*/
/*import androidx.activity.EdgeToEdge;*/
import androidx.appcompat.app.AppCompatActivity;
/*import java.util.Arrays;*/
/*import java.util.Random;*/

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    MediaPlayer botonsound; //sonidos boton
    // Variables para almacenar el modo de juego seleccionado
    private int modoJuego = 0; // 0: No se ha seleccionado, 1: Jugador contra jugador, 2: Jugador contra máquina

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // se permite girar la pantalla
        // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //comentado = se permite

        Button startGamePvmButton = findViewById(R.id.start_game_pvm);
        Button startGamePvpButton = findViewById(R.id.start_game_pvp);
        Button exitGameButton = findViewById(R.id.exit_game);

        // interfaz para el OnClick
        startGamePvmButton.setOnClickListener(this);
        startGamePvpButton.setOnClickListener(this);
        exitGameButton.setOnClickListener(this);
        
        botonsound = MediaPlayer.create(this, R.raw.touch);
    }

    // implementación del onClick de la interfaz SetOnClickListener
    public void onClick(View elemento) {
        botonsound.start();
        if (elemento.getId()==R.id.start_game_pvp ) {
            modoJuego = 1; // Jugador contra jugador
            iniciarPartida();
        }
        if (elemento.getId()==R.id.start_game_pvm ) {
            modoJuego = 2; // Jugador contra máquina
            iniciarPartida();
        }
        if (elemento.getId()==R.id.exit_game ) {
            finishAffinity();
        }

        // Carga la animación
        final Animation scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.button_scale);
        elemento.startAnimation(scaleAnimation);
    }

    private void iniciarPartida() {
        // Crear un Intent para iniciar MainActivity
        Intent intento = new Intent(this, es.upv.etsit.trabajo_atelem.JuegoActivity.class);
        // Agregar el modo de juego como extra en el Intent segun lo visto en clase
        Bundle b = new Bundle();
        b.putInt( "modojuego",modoJuego);
        intento.putExtras( b );
        // Iniciar la actividad JuegoActivity
        startActivity(intento);
    }
}
