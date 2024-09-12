// Universitat Politècnica de València
// Escuela Técnica Superior de Ingeniería de Telecomunicación
// --------------------------------------------------------------------
// APLICACIONES TELEMÁTICAS
// Curso 2023 - 2024
// --------------------------------------------------------------------
// Nombre del archivo: JuegoActivity.java
// --------------------------------------------------------------------
// Autores:         Grupos A y B (3):
//                  Daniel Silva Romero
//                  Carlos Soria Mora
//                  Alejandro Cano Segovia
//                  Hugo Beltrán Sanz
// --------------------------------------------------------------------

package es.upv.etsit.trabajo_atelem;

import android.content.Intent;
//import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
//import android.view.animation.Animation;
//import android.view.animation.AnimationUtils;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
import java.util.Arrays;
import java.util.Random;
/*import android.os.Handler;*/

public class JuegoActivity extends AppCompatActivity implements View.OnClickListener{
    /* usamos tres objetos */
    public Jugador jugador1;
    public Jugador jugador2;
    public Juego3  juego3raya;
    
    MediaPlayer botonsound; //sonidos boton
    MediaPlayer botonsoundplay;
    MediaPlayer P1win;
    MediaPlayer P2win;
    MediaPlayer Loser;
    MediaPlayer Empate;

    TextView TextoGanador;
    TextView TextoTurnos;
    Integer[] Botones; /*lista de ids de los 9 botones */
    int[] Tablero=new int[]{0,0,0,0,0,0,0,0,0}; /*estado de cada botón */
    int Estado=0; /* 0=seguir jugando , 2=empate, 1=gana1, -1=gana2 */
    int NumJugadas=0; /* numero de jugadas*/
    int Turno=1; /* 1=persona, -1=ordenador */
    int[] PosicionGanadora =new int[]{-1,-1,-1};
    int modoJuego = 0; // 1 = JvsJ, 2 = JvsPC

    Button buttonRestart;
    Button buttonMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_juego);
        // esta pantalla siempre em modo portrait
        // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //recibimos el intent con el extra modoJuego de la pantalla anterior
        Intent intento = getIntent();
        if (intento != null){
            Bundle bundle = intento.getExtras();
            if (bundle!=null){
                modoJuego = bundle.getInt("modojuego");
            }
        }
                
        // Instanciamos los objetos ahora que sabemos el modo de juego
        jugador1=new Jugador("Jugador 1",'O');
        if (modoJuego==1){
            jugador2=new Jugador("Jugador 2",'X');
        }
        else{
            jugador2=new Jugador("Maquina",'X');
        }
        juego3raya=new Juego3(jugador1,jugador1);
        
        botonsound = MediaPlayer.create(this, R.raw.touch);
        botonsoundplay = MediaPlayer.create(this, R.raw.touchplay);
        P1win = MediaPlayer.create(this, R.raw.p1winsound);
        P2win = MediaPlayer.create(this, R.raw.p2winsound);
        Loser = MediaPlayer.create(this, R.raw.losersound);
        Empate = MediaPlayer.create(this, R.raw.empate);

        /* Damos valor a textos y botones */
        TextoTurnos= findViewById(R.id.textoTurno ); /*enganchamos la variable con el layout*/
        TextoGanador= findViewById(R.id.TextoGanador ); /*enganchamos la variable con el layout*/
        TextoGanador.setVisibility(View.INVISIBLE);
        TextoTurnos.setVisibility(View.VISIBLE);
        TextoTurnos.setText(R.string.j1 );
        Botones= new Integer[]{
                R.id.b1,R.id.b2,R.id.b3,
                R.id.b4,R.id.b5,R.id.b6,
                R.id.b7,R.id.b8,R.id.b9
        }; /* se llena la lista de ids*/
        // imagen por defecto de los 9 botones:
        for(int id:Botones){
            Button b = findViewById(id);
            b.setBackgroundResource(android.R.drawable.btn_default);
        }
        buttonRestart = findViewById(R.id.Reset);
        buttonMenu = findViewById(R.id.VolverMenu);

        buttonRestart.setVisibility(View.INVISIBLE);
        buttonMenu.setVisibility(View.INVISIBLE);

        findViewById(R.id.Reset).setOnClickListener(this);
        findViewById(R.id.VolverMenu).setOnClickListener(this);
    }

    //implementación del metodo onClick de la interfaz SetOnClickListener
    public void onClick(View elemento) {
        botonsound.start();
        if (elemento.getId()==buttonRestart.getId() ) {
            resetGame();
        }
        if (elemento.getId()==buttonMenu.getId() ) {
            goToMenu();
        }

        final Animation scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.button_scale);
        elemento.startAnimation(scaleAnimation);
    }
    
    public void PulsarBoton( View v) /*para clic de cada boton del tablero, se ejecuta esto al pulsarlo, v es el botón*/
    {
        botonsoundplay.start();
        if (Estado==0){
            TextoTurnos.setVisibility(View.VISIBLE);
            int NumBoton= Arrays.asList(Botones).indexOf(v.getId()); /* truco para saber el nº de botón pulsado: 0-8 . Gracias Borja Molina */
            /* empieza jugador 1 aqui */
            if (Tablero[NumBoton]==0){ /* lo primero comprobamos que no este ocupada */
                if (modoJuego==1) {
                    TextoTurnos.setVisibility(View.VISIBLE);
                    if (Turno == 1) {
                        v.setBackgroundResource(R.drawable.verde3);
                        Tablero[NumBoton] = 1; /*1 usado por jugador 1*/
                    } else {
                        v.setBackgroundResource(R.drawable.rojo0);
                        Tablero[NumBoton] = -1; /*usado por jugador2*/
                    }
                    NumJugadas = NumJugadas + 1;
                    Estado = ComprobarEstado();
                    TerminarPartida();
                    if (Estado==0){
                        Turno = Turno == 1 ? -1 : 1; /*cambiar turno*/
                    }
                    if (Turno==1){
                        TextoTurnos.setText(R.string.j1 );
                    }
                    else if (Turno==-1){
                        TextoTurnos.setText(R.string.j2);
                    }
                }
                else if (modoJuego==2){
                    Turno=1;
                    TextoTurnos.setText(R.string.j1 );
                    v.setBackgroundResource(R.drawable.verde3);
                    Tablero[NumBoton] = 1; /*1 usado por jugador 1*/
                    NumJugadas = NumJugadas + 1;
                    Estado = ComprobarEstado();
                    TerminarPartida();

                    /* ahora juega el ordenador */
                    if (Estado == 0) { /*comprobamos si ha cambiado el estado*/
                        Turno = -1;
                        Maquina(); /*respuesta del ordenador*/
                        NumJugadas = NumJugadas + 1;
                        Estado = ComprobarEstado();
                        TerminarPartida();
                    }
                }
            }
        }
    }

    public void Maquina() /* version 2: mas inteligente */
    {
        int Pos;

        if (Tablero[4]== 0){ /* 1º si puede elije el centro */
            Pos=4;
        }
        else if (Math.abs( Tablero[0]+ Tablero[2]+ Tablero[6]+ Tablero[8])<4){ /* si puede, elije una esquina */
            int[] esquinas = {0, 2, 6, 8};
            Random random = new Random();
            int indiceAleatorio = random.nextInt(esquinas.length);
            Pos = esquinas[indiceAleatorio];
            if (Tablero[Pos]!=0){
                indiceAleatorio = random.nextInt(esquinas.length);
                Pos = esquinas[indiceAleatorio];
                if (Tablero[Pos]!=0){
                    Random ran =new Random();
                    Pos=ran.nextInt(Tablero.length); /* nº aleatorio entre 0 y 8, representa el boton pulsado */
                    while (Tablero[Pos]!= 0){
                        Pos=ran.nextInt(Tablero.length);
                    }
                }
            }
        }
        else{
            Random ran =new Random();
            Pos=ran.nextInt(Tablero.length); /* nº aleatorio entre 0 y 8, representa el boton pulsado */
            while (Tablero[Pos]!= 0){
                Pos=ran.nextInt(Tablero.length);
            }
        }
        Button b= findViewById(Botones[Pos]);
        b.setBackgroundResource(R.drawable.rojo0);
        Tablero[Pos]=-1; /* usado por el ordenador (jugador 2) */
    }

    public void TerminarPartida() /* muestra el texto y el icono adecuado */
    {
        int FichaVictoria=R.drawable.verde;
        if (Estado==1 || Estado==-1){
            TextoGanador.setVisibility(View.VISIBLE );
            TextoTurnos.setVisibility(View.INVISIBLE );
            if (Estado==1){
                P1win.start();
                TextoGanador.setText(R.string.gana1);
                TextoGanador.setTextColor(Color.BLUE);
                buttonRestart.setVisibility(View.VISIBLE);
                buttonMenu.setVisibility(View.VISIBLE);
            }
            else if(Estado ==-1) {
                if(modoJuego==1) {
                    P2win.start();
                    TextoGanador.setText(R.string.gana2);
                    TextoGanador.setTextColor(Color.RED);
                    buttonRestart.setVisibility(View.VISIBLE);
                    buttonMenu.setVisibility(View.VISIBLE);
                    TextoTurnos.setVisibility(View.INVISIBLE );
                    FichaVictoria=R.drawable.rojo;
                }
                else if(modoJuego==2){
                    Loser.start();
                    TextoGanador.setText(R.string.pierde);
                    TextoGanador.setTextColor(Color.RED);
                    buttonRestart.setVisibility(View.VISIBLE);
                    buttonMenu.setVisibility(View.VISIBLE);
                    TextoTurnos.setVisibility(View.INVISIBLE );
                    FichaVictoria=R.drawable.rojo;
                }
            }
            // remarcamos la posicion ganadora
            for (int element : PosicionGanadora) {
                Button b=findViewById(Botones[element]);
                b.setBackgroundResource(FichaVictoria);
            }
        }
        else  if (Estado==2){
            TextoGanador.setVisibility(View.VISIBLE );
            Empate.start();
            TextoGanador.setText(R.string.empate);
            TextoGanador.setTextColor(Color.BLUE);
            buttonRestart.setVisibility(View.VISIBLE);
            buttonMenu.setVisibility(View.VISIBLE);
            TextoTurnos.setVisibility(View.INVISIBLE );
        }
    }

    public int ComprobarEstado() /* contiene las combinaciones ganadoras y empate */
    {
        int NuevoEstado=0;
        /* Usamos el truco de sumar los valores absolutos, y si da 3 es que estan todos a 1 ó -1. Gracias Borja Molina */
        if (Math.abs(Tablero[0]+Tablero[1]+Tablero[2])==3){
            NuevoEstado=Turno;
            PosicionGanadora=new int[]{0,1,2};
        }
        else if (Math.abs(Tablero[3]+Tablero[4]+Tablero[5])==3){
            NuevoEstado=Turno;
            PosicionGanadora=new int[]{3,4,5};
        }
        else if (Math.abs(Tablero[6]+Tablero[7]+Tablero[8])==3){
            NuevoEstado=Turno;
            PosicionGanadora=new int[]{6,7,8};
        }
        else if (Math.abs(Tablero[0]+Tablero[3]+Tablero[6])==3){
            NuevoEstado=Turno;
            PosicionGanadora=new int[]{0,3,6};
        }
        else if (Math.abs(Tablero[1]+Tablero[4]+Tablero[7])==3){
            NuevoEstado=Turno;
            PosicionGanadora=new int[]{1,4,7};
        }
        else if (Math.abs(Tablero[2]+Tablero[5]+Tablero[8])==3){
            NuevoEstado=Turno;
            PosicionGanadora=new int[]{2,5,8};
        }
        else if (Math.abs(Tablero[0]+Tablero[4]+Tablero[8])==3){
            NuevoEstado=Turno;
            PosicionGanadora=new int[]{0,4,8};
        }
        else if (Math.abs(Tablero[2]+Tablero[4]+Tablero[6])==3){
            NuevoEstado=Turno;
            PosicionGanadora=new int[]{2,4,6};
        }
        else if (NumJugadas==9){
            NuevoEstado= 2; /* empate */
        }

        return NuevoEstado;
    }
    
    private void resetGame(){ /* inicializamos todas las variables y visibles */
        Tablero=new int[]{0,0,0,0,0,0,0,0,0};
        NumJugadas=0;
        Turno=1;
        PosicionGanadora=new int[]{-1,-1,-1};

        TextoGanador.setVisibility(View.INVISIBLE);
        TextoTurnos.setVisibility(View.VISIBLE);
        TextoTurnos.setText(R.string.j1);
        buttonRestart.setVisibility(View.INVISIBLE);
        buttonMenu.setVisibility(View.INVISIBLE);

        // Deja de sonar cuando nos salimos
        if (Estado == 1 || Estado==-1) {
            P1win.pause();
            P1win.seekTo(0);
        }
        if (Estado == -1){
            if(modoJuego == 1){
                P2win.pause();
                P2win.seekTo(0);
            }
            else {
                Loser.pause();
                Loser.seekTo(0);
            }
        }
        else  if(Estado == 2) {
            Empate.pause();
            Empate.seekTo(0);
        }

        Estado =0;
        //botones con aspecto inicial:
        for(int id:Botones){
            Button b = findViewById(id);
            b.setBackgroundResource(android.R.drawable.btn_default);
        }
    }
    
    private void goToMenu(){ // volvemos a MainActivity
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        // Deja de sonar cuando nos salimos
        if (Estado == 1 || Estado==-1) {
            P1win.pause();
            P1win.seekTo(0);
        }
        if (Estado == -1){
            if(modoJuego == 1){
                P2win.pause();
                P2win.seekTo(0);
            }
            else {
                Loser.pause();
                Loser.seekTo(0);
            }
        }
        else  if(Estado == 2) {
            Empate.pause();
            Empate.seekTo(0);
        }

        finish();
    }
}
