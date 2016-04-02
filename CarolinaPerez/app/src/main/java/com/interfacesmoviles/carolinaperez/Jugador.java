package com.interfacesmoviles.carolinaperez;

import android.content.Context;

import java.io.Serializable;

/**
 * Created by Carolina on 02/03/2016.
 */
public class Jugador implements Serializable {

    private int id;
    private String nombre;
    private String nombreFoto;
    private int tieneFoto;
    private int puntosMatematica;
    private int puntosNaturales;
    private int puntosLengua;
    private int puntosMix;
    private boolean habilitarVibracion=true;
    private boolean habilitarSonido=true;


    public Jugador(int id, String nombre, String nombreFoto, int tieneFoto, int puntosMatematica, int puntosNaturales, int puntosLengua, int puntosMix) {
        this.id = id;
        this.nombre = nombre;
        this.nombreFoto = nombreFoto;
        this.tieneFoto = tieneFoto;
        this.puntosMatematica = puntosMatematica;
        this.puntosNaturales = puntosNaturales;
        this.puntosLengua = puntosLengua;
        this.puntosMix = puntosMix;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombreFoto() {
        return nombreFoto;
    }

    public void setNombreFoto(String nombreFoto) {
        this.nombreFoto = nombreFoto;
    }

    public int getTieneFoto() {
        return tieneFoto;
    }

    public void setTieneFoto(int tieneFoto) {
        this.tieneFoto = tieneFoto;
    }

    public int getPuntosMatematica() {
        return puntosMatematica;
    }

    public void setPuntosMatematica(int puntosMatematica) {
        this.puntosMatematica = puntosMatematica;
    }

    public int getPuntosNaturales() {
        return puntosNaturales;
    }

    public void setPuntosNaturales(int puntosNaturales) {
        this.puntosNaturales = puntosNaturales;
    }

    public int getPuntosLengua() {
        return puntosLengua;
    }

    public void setPuntosLengua(int puntosLengua) {
        this.puntosLengua = puntosLengua;
    }

    public int getPuntosMix() {
        return puntosMix;
    }

    public void setPuntosMix(int puntosMix) {
        this.puntosMix = puntosMix;
    }

    public void compararPuntos(int puntos, int categoria, Context c){
        switch (categoria){
            case 1: if(puntos>puntosMatematica){
                        puntosMatematica=puntos;
                        (new DataBaseManager(c)).modificarJugado(this);
                    }
                    break;
            case 2: if(puntos>puntosLengua){
                        puntosLengua=puntos;
                        (new DataBaseManager(c)).modificarJugado(this);
                    }
                    break;
            case 3: if(puntos>puntosNaturales){
                         puntosNaturales=puntos;
                        (new DataBaseManager(c)).modificarJugado(this);
                    }
                    break;
            case 4: if(puntos>puntosMix){
                        puntosMix=puntos;
                        (new DataBaseManager(c)).modificarJugado(this);
                    }
                    break;
        }



    }

    public boolean isHabilitarVibracion() {
        return habilitarVibracion;
    }

    public void setHabilitarVibracion(boolean habilitarVibracion) {
        this.habilitarVibracion = habilitarVibracion;
    }

    public boolean isHabilitarSonido() {
        return habilitarSonido;
    }

    public void setHabilitarSonido(boolean habilitarSonido) {
        this.habilitarSonido = habilitarSonido;
    }
}
