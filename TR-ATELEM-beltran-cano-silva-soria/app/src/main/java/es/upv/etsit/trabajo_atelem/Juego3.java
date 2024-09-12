package es.upv.etsit.trabajo_atelem;

//La clase Juego3 se encarga de manejar la lógica principal del
//juego entre dos jugadores. Esta clase mantiene un registro de los
//dos jugadores, del tablero del juego y de quién es el jugador actual.

public class Juego3 {
    private Jugador Jugador1;
    private Jugador Jugador2;
    private Tablero Tablero;
    private Jugador JugadorActual;

    public Juego3(Jugador Jugador1, Jugador Jugador2) {
        this.Jugador1 = Jugador1;
        this.Jugador2 = Jugador2;
        this.Tablero = new Tablero();
        this.JugadorActual = Jugador1; // Por ejemplo, el jugador 1 comienza
    }

    public Jugador getJugadorActual() {
        return JugadorActual;
    }

    public Tablero getTablero() {
        return Tablero;
    }

    public void switchJugador() {
        /* no se usa */
        JugadorActual = (JugadorActual == Jugador1) ? Jugador2 : Jugador1;
    }
}
