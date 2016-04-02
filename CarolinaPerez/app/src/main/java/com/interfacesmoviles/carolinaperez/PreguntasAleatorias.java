package com.interfacesmoviles.carolinaperez;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Carolina on 17/03/2016.
 */
public class PreguntasAleatorias{

    private int valorInicial;
    private int valorFinal;
    private ArrayList listaNumero;
    private List<Pregunta> preguntas;

    public PreguntasAleatorias(int valorInicial, int valorFinal, ArrayList<Pregunta> preguntas){
        this.valorInicial = valorInicial;
        this.valorFinal = valorFinal;
        this.preguntas=preguntas;
        listaNumero = new ArrayList();
    }

    private int numeroAleatorio(){
        return (int)(Math.random()*(valorFinal-valorInicial+1)+valorInicial);
    }

    public Pregunta generarPregunta(){
        if(listaNumero.size() < (valorFinal - valorInicial) +1){//Aun no se han generado todos los numeros
            int numero = numeroAleatorio();// genero un numero
            if(listaNumero.isEmpty()){//si la lista esta vacia
                listaNumero.add(numero);
                return preguntas.get(numero);
            }
            else{//si no esta vacia
                if(listaNumero.contains(numero)){//Si el numero que generé esta contenido en la lista
                    return generarPregunta();//recursivamente lo mando a generar otra vez
                }
                else{//Si no esta contenido en la lista
                    listaNumero.add(numero);
                    return preguntas.get(numero);
                }
            }
        }else{// ya se generaron todos los numeros
            return null;
        }
    }
}