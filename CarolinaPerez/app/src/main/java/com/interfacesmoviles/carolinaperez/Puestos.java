package com.interfacesmoviles.carolinaperez;

/**
 * Created by Carolina on 18/03/2016.
 */
public class Puestos {
    private int primeroPuntos;
    private int segundoPuntos;;
    private int terceroPuntos;;
    private Jugador primeroJugador;
    private Jugador segundoJugador;
    private Jugador terceroJugador;

    public Puestos(int primeroPuntos, int segundoPuntos, int terceroPuntos, Jugador primeroJugador, Jugador segundoJugador, Jugador terceroJugador) {
        this.primeroPuntos = primeroPuntos;
        this.segundoPuntos = segundoPuntos;
        this.terceroPuntos = terceroPuntos;
        this.primeroJugador = primeroJugador;
        this.segundoJugador = segundoJugador;
        this.terceroJugador = terceroJugador;
    }

    public int getPrimeroPuntos() {
        return primeroPuntos;
    }

    public void setPrimeroPuntos(int primeroPuntos) {
        this.primeroPuntos = primeroPuntos;
    }

    public int getSegundoPuntos() {
        return segundoPuntos;
    }

    public void setSegundoPuntos(int segundoPuntos) {
        this.segundoPuntos = segundoPuntos;
    }

    public int getTerceroPuntos() {
        return terceroPuntos;
    }

    public void setTerceroPuntos(int terceroPuntos) {
        this.terceroPuntos = terceroPuntos;
    }

    public Jugador getPrimeroJugador() {
        return primeroJugador;
    }

    public void setPrimeroJugador(Jugador primeroJugador) {
        this.primeroJugador = primeroJugador;
    }

    public Jugador getSegundoJugador() {
        return segundoJugador;
    }

    public void setSegundoJugador(Jugador segundoJugador) {
        this.segundoJugador = segundoJugador;
    }

    public Jugador getTerceroJugador() {
        return terceroJugador;
    }

    public void setTerceroJugador(Jugador terceroJugador) {
        this.terceroJugador = terceroJugador;
    }
}
