package es.upv.etsit.trabajo_atelem;

//La clase Tablero se encarga de manejar el estado del tablero en el juego.
//Tiene un array de enteros que representa las posiciones del tablero.
//Cuando creas un Tablero, inicializa todas las posiciones a 0,
//significa que están vacías.

//Hay métodos para inicializar y resetear el tablero, lo que simplemente
//pone todos los valores a 0 nuevamente.
//También hay un método para obtener el estado actual del tablero,
//Esto mantiene el tablero organizado y fácil de reiniciar entre partidas.

public class Tablero {
    private  Integer[] tablero;

    public Tablero() {
        tablero = new Integer[]{0,0,0,0,0,0,0,0,0};
        inicializarTablero();
    }

    public void inicializarTablero() {
        for (int i = 0; i < 9; i++) {
            tablero[i]= 0;
        }
    }

    public Integer [] getTablero() {
        return tablero;
    }

    public void resetTablero() {
        inicializarTablero();
    }
}
