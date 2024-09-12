package es.upv.etsit.trabajo_atelem;

//La clase Jugador se usa para agrupar toda la información y
//las acciones que el jugador necesita en el juego. Cada jugador tiene un nombre,
//un símbolo ('X' o 'O'), y la cantidad de puntos
//que puede ganar o perder. Esta clase permite gestionar y
//actualizar estos datos.

public class Jugador {
    private String nombre;
    private char simbolo;
    private int puntos;

    public Jugador(String nombre, char simbolo) {
        this.nombre = nombre;
        this.simbolo = simbolo;
        this.puntos = 0;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public char getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(char simbolo) {
        this.simbolo = simbolo;
    }

    public int getpuntos() {
        return puntos;
    }

    public void setpuntos(int puntos) {
        this.puntos = puntos;
    }

    public void incrementapuntos() {
        this.puntos++;
    }
}
