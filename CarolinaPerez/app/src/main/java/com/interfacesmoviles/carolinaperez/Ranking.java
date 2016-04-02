package com.interfacesmoviles.carolinaperez;

/**
 * Created by Carolina on 18/03/2016.
 */
public class Ranking {

    private Puestos matematica;
    private Puestos naturales;
    private Puestos lengua;
    private Puestos mix;

    public Ranking(Puestos matematica, Puestos naturales, Puestos lengua, Puestos mix) {
        this.matematica = matematica;
        this.naturales = naturales;
        this.lengua = lengua;
        this.mix = mix;
    }

    public Puestos getMatematica() {
        return matematica;
    }

    public void setMatematica(Puestos matematica) {
        this.matematica = matematica;
    }

    public Puestos getNaturales() {
        return naturales;
    }

    public void setNaturales(Puestos naturales) {
        this.naturales = naturales;
    }

    public Puestos getLengua() {
        return lengua;
    }

    public void setLengua(Puestos lengua) {
        this.lengua = lengua;
    }

    public Puestos getMix() {
        return mix;
    }

    public void setMix(Puestos mix) {
        this.mix = mix;
    }
}
