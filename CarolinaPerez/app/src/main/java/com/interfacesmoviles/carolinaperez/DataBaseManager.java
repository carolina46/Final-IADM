package com.interfacesmoviles.carolinaperez;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Carolina on 25/01/2016.
 */
public class DataBaseManager {

    private DataBaseHelper helper;
    private SQLiteDatabase db;
    private List<String> configuration;

    public DataBaseManager(Context context) {
        helper = new DataBaseHelper(context);

    }

    public List<Jugador> listJugadores(){
        db = helper.getWritableDatabase();
        List<Jugador> aux = new ArrayList<Jugador>();
        Cursor jugadores = db.rawQuery("select * from jugador", null);
        if (jugadores.moveToFirst()) {
            do {
               aux.add(new Jugador(jugadores.getInt(0), jugadores.getString(1), jugadores.getString(3), jugadores.getInt(2), jugadores.getInt(4), jugadores.getInt(5), jugadores.getInt(6), jugadores.getInt(7)));
            }while (jugadores.moveToNext());
        }
        jugadores.close();
        db.close();
        return aux;
    }

    public void addJugador(Jugador jugador) {
        db = helper.getWritableDatabase();
        ContentValues registro = new ContentValues();
        registro.put("nombre",jugador.getNombre());
        registro.put("nombreFoto", jugador.getNombreFoto() );
        registro.put("tieneFoto", jugador.getTieneFoto());
        registro.put("m",0);
        registro.put("l",0);
        registro.put("n",0);
        registro.put("mix",0);
        db.insert("jugador", null, registro);
        db.close();
    }

    public void deleteJugador(int id) {
        db = helper.getWritableDatabase();
        db.delete("jugador", "id_jugador=" + Integer.toString(id), null);
        db.close();
    }

    public ArrayList<Pregunta> listPreguntas(int categoria){
        db = helper.getWritableDatabase();
        ArrayList<Pregunta> aux = new ArrayList<Pregunta>();
        Cursor preguntas;
        if(categoria==4){preguntas = db.rawQuery("select * from pregunta", null);}
        else{preguntas = db.rawQuery("select * from pregunta where categoria="+categoria, null);}

        if (preguntas.moveToFirst()) {
            do {
                aux.add(new Pregunta(preguntas.getString(2), preguntas.getString(3),preguntas.getString(4)));
            }while (preguntas.moveToNext());
        }
        preguntas.close();
        db.close();
        return aux;
    }

    public void modificarJugado(Jugador jugador) {
        db = helper.getWritableDatabase();
        ContentValues registro = new ContentValues();
        registro.put("nombre",jugador.getNombre());
        registro.put("nombreFoto", jugador.getNombreFoto() );
        registro.put("tieneFoto", jugador.getTieneFoto());
        registro.put("m",jugador.getPuntosMatematica());
        registro.put("l",jugador.getPuntosLengua());
        registro.put("n",jugador.getPuntosNaturales());
        registro.put("mix",jugador.getPuntosMix());
        db.update("jugador", registro, "id_jugador=" + jugador.getId(), null);
        db.close();

    }

    public Ranking getRanking(){
        Puestos[] puestos=new Puestos[4];
        String[] categorias={"m","n","l","mix"};
        for(int i=0;i<4;i++) {
            puestos[i]=obtenerPuesto(categorias[i]);
        }
        return new Ranking(puestos[0],puestos[1],puestos[2],puestos[3]);
    }

    public Puestos obtenerPuesto(String categoria){
        int total=0;
        int[] puntos= {0,0,0};
        Jugador[] jugadores={null,null,null};
        db = helper.getWritableDatabase();
        //consulta
        Cursor rankings= db.rawQuery("select id_jugador, "+ categoria +" from jugador order by "+ categoria + " DESC", null);
        //proceso resultado consulta
        if (rankings.moveToFirst()) {
            do {
                if (rankings.getInt(1) > 0) {
                    puntos[total] = rankings.getInt(1);
                    jugadores[total] = getJugador(rankings.getInt(0));
                    total++;

                }
                else {total=3;}
            } while (rankings.moveToNext() && total != 3);
        }
        rankings.close();
        db.close();
        return new Puestos(puntos[0], puntos[1], puntos[2], jugadores[0], jugadores[1], jugadores[2]);
    }

    public Jugador getJugador(int id){
        db = helper.getWritableDatabase();
        Jugador aux=null;
        Cursor jugador = db.rawQuery("select * from jugador where id_jugador="+id, null);
        if (jugador.moveToFirst()) {
            System.out.println("Existe el jugador");
           aux=(new Jugador(jugador.getInt(0), jugador.getString(1), jugador.getString(3), jugador.getInt(2), jugador.getInt(4), jugador.getInt(5), jugador.getInt(6), jugador.getInt(7)));
        }
        jugador.close();
        db.close();
        return aux;
    }

    public void updateJugador(Jugador jugador) {
        db = helper.getWritableDatabase();
        ContentValues registro = new ContentValues();
        registro.put("nombre",jugador.getNombre());
        registro.put("nombreFoto",jugador.getNombreFoto());
        db.update("jugador", registro, "id_jugador=" + jugador.getId(), null);
        db.close();
    }


    public class DataBaseHelper extends SQLiteOpenHelper {
        private static final String DB_NAME = "vueltaAlCole";
        private static final int DB_VERSION = 1;
        public DataBaseHelper(Context context) {super(context, DB_NAME, null, DB_VERSION);}


        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table jugador (id_jugador Integer primary key autoincrement,nombre text, tieneFoto int, nombreFoto text, m int, n int,  l int, mix int)");
            db.execSQL("create table pregunta (id_pregunta Integer primary key autoincrement, categoria int,pregunta text, respuestaCorrecta text, respuestaIncorrecta text)");
            db.execSQL("create table ranking (id_ranking Integer primary key autoincrement, categoria int, primeroPuntos int, segundoPuntos int, terceroPuntos int, primeroJugador int, segundoJugador int, terceroJugador int )");


            //RANKING POR CATEGORIA
            db.execSQL("insert into ranking ( categoria, primeroPuntos, segundoPuntos, terceroPuntos, primeroJugador, segundoJugador, terceroJugador) values (1,0,0,0,0,0,0)");
            db.execSQL("insert into ranking ( categoria, primeroPuntos, segundoPuntos, terceroPuntos, primeroJugador, segundoJugador, terceroJugador) values (2,0,0,0,0,0,0)");
            db.execSQL("insert into ranking ( categoria, primeroPuntos, segundoPuntos, terceroPuntos, primeroJugador, segundoJugador, terceroJugador) values (3,0,0,0,0,0,0)");
            db.execSQL("insert into ranking ( categoria, primeroPuntos, segundoPuntos, terceroPuntos, primeroJugador, segundoJugador, terceroJugador) values (4,0,0,0,0,0,0)");

            //MATEMATICA=1
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, '¿Qué es una incógnita?', 'Un número del que desconocemos su valor', 'Un número del que conocemos su valor')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, '¿Qué es una raíz de índice n?', 'Un número que elevado a n da como resultado el radicando', 'Un número que dividido por n da como resultado el radicando')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, '¿Qué es un exponente?', 'En una potencia, es el número que expresa cuántas veces hay que multiplicar la base por sí misma', 'En una potencia, es el número que expresa cuántas veces hay que dividir la base por sí misma')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, '¿Cómo se llaman los triángulos con dos los lados iguales y uno desigual?', 'Isósceles', 'Escaleno')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, '¿Qué es un sistema de ecuación lineal?', 'Es un conjunto de ecuaciones de primer grado', 'Es un conjunto de ecuaciones de segundo grado')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, '¿Cuál es el eje de ordenadas?', 'El eje de las x', 'El eje de las y')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, '¿Cuándo un sistema lineal es indeterminado?', 'Cuando tiene múltiples soluciones', 'Cuando no tiene solución')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, 'El área de una circunferencia es igual a...', 'El radio al cuadrado por pi', '2 veces el radio por el número pi')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, '¿Cuándo es una función creciente?', 'Es creciente en un punto dado cuando es mayor que cualquier punto inmediato anterior', 'Es creciente en un punto dado cuando es menor que cualquier punto inmediato anterior')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, 'Una función exponencial viene dada por la expresión...', 'b = t elevado a x', 'y = raíz de a elevado a x')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, '¿Qué es la periodicidad de una función?', 'Es cuando en ciertos intervalos se va repitiendo', 'Es cuando nunca se repite')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, 'Al representar los números en una semirrecta, ¿Cómo se denomina a la distancia entre cada uno de los números que la forman?', 'Intervalo', 'Diferencia')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, 'Indicar en cuál de los siguientes números la centena de millón está representada por una cifra impar', '769.937.180', '231.537.980')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, 'Tengo $20.000 y presté a un amigo las dos quintas partes. ¿Cuánto me sobró?', '$12.000', '$14.000')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, '¿Cuántos metros recorrí en 8 pasos, si por cada paso me desplazo 50 cm?', '4m', '380cm')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, '2 centenas es lo mismo que...', '20 decenas', '20 unidades')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, 'El número 5 del número 65.873 corresponde a: ', 'unidades de millar', 'decenas de millar')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, '¿Qué expresa una fracción?', 'Expresa una cantidad de las partes del todo', 'Expresa la parte que falta del todo')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, 'Indica cuál es el denominador en la fracción tres cuartos', '4', '3')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, '¿Cuál es la representación numérica de Quince veinteavos? ', '15/20', '15/22')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, 'Si Erika se come un tercio de su gelatina, y Pedro se come 2/6, ¿quién come más gelatina?', 'Comen la misma cantidad', 'Come más Erika')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, '¿1/3 y 3/9 son fracciones equivalentes?', 'Verdadero', 'Falso')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, '¿Qué es una fracción impropia?', 'Aquella cuyo denominador es menor que el numerador', 'Aquella cuyo  denominador es mayor que el numerador')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, '¿Qué fracción representa una unidad?', '7/7', '2230/1122')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, '2/5 es menor que 2/10', 'Verdadero', 'Falso')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, '¿Cuál es la fracción que no es equivalente? 4/9, 4/6 y 2/3', '4/9', '2/3 y 4/6')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, 'Si los lados de un rectángulo son 4cm y 7cm, su perímetro es de:', '22cm', '11cm')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, 'Según sus lados un triángulo puede ser:', 'Equilátero, isósceles, escaleno', 'Rectángulo, acutángulo, obtusángulo')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, 'Según la amplitud de sus ángulos un triángulo puede ser:', 'Rectángulo, acutángulo, obtusángulo', 'Equilátero, isósceles, escaleno')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, 'El perímetro de un polígono es:', 'La suma de todos sus lados', 'La suma de los dos lados más grandes')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, 'Un polígono de 5 lados se llama:', 'Pentágono', 'Hexágono')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, 'Un polígono de 9 lados se llama:', 'Eneágono', 'Pentágono')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, 'Un polígono de 7 lados se llama:', 'Heptágono', 'Eneágono')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, 'Cuál de las siguientes relaciones de números es incorrecta', '105.896.486 < 98.658.273', '58.486.486 > 58.480.987')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, 'Al representar los números en una semirrecta, ¿Cuáles de ellos se pone en el origen?', '0', 'Depende del intervalo')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, 'Indicar en cuál de los siguientes números las decenas de millón esta representadas por una cifra par', '186.074.747', '417.224.953')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, 'Indicar cuál de las siguientes equivalencias de números romanos es correcta', 'XLV = 45', 'XVI = 15')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, 'DIVISOR de 15…', '3', '2')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, 'DIVISOR de 18…', '6', '8')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, 'DIVISOR de 21…', '7', '2')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, 'DIVISOR de 42…', '7', '4')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, 'DIVISOR de 49…', '7', '3')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, 'Representación del número romano 46', 'XLVI', 'XXVI')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, 'Representación del número romano 109', 'CIX', 'CVII')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, 'Representación del número romano 4', 'IV', 'VI')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, 'Representación del número romano 9', 'IX', 'VIII')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, 'Representación del número romano 7', 'VII', 'VIII')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, 'Representación del número romano 15', 'XV', 'XIV')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, 'Representación del número romano 19', 'XIX', 'XVIII')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, 'Representación del número romano 21', 'XXI', 'XXIV')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, 'Representación del número romano 49', 'XLIX', 'XLX')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, '¿Cuál expresión da como resultado el valor 1?', '5 - ( 7 - 3 )', '7 - ( 3 + 5 )')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, '¿Cuál expresión da como resultado el valor 2?', '( 3 + 7 ) / 5', '( 5 + 7 ) / 3')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, '¿Cuál expresión da como resultado el valor 4?', '( 7 + 5 ) / 3', '7 + 3 - 5')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, '¿Cuál expresión da como resultado el valor 5?', '7 - ( 5 - 3 )', '5 + 3 - 7')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, '¿Cuál expresión da como resultado el valor 6?', '( 7 - 5 ) x 3', '7 - 5 + 3')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, '¿Cuál expresión da como resultado el valor 8?', '3 x 5 - 7', '3 x ( 7 - 5 )')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, '¿Cuál expresión da como resultado el valor 9?', '7 - 3 + 5', '5 x 3 - 7')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, '¿Cuál expresión da como resultado el valor 14?', '( 5 - 3 ) x 7', '7 x ( 5 + 3 )')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, '¿Cuál expresión da como resultado el valor 15?', '7 + 3 + 5', '5 x ( 7 - 3 )')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, '¿Cuál expresión da como resultado el valor 16?', '3 x 7 - 5', '3 x ( 7 - 5 )')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, '¿Cuál expresión da como resultado el valor 20?', '( 7 - 3 ) x 5', '7 x 3 - 5')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, '¿Cuál expresión da como resultado el valor 22?', '7 + 3 x 5', '5 x ( 7 - 3 )')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, '¿Cuál expresión da como resultado el valor 26?', '7 x 3 + 5', '7 x 5 - 3')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, '¿Cuál expresión da como resultado el valor 36?', '( 5 + 7 ) x 3', '5 x 7 + 3')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, '¿Cuál expresión da como resultado el valor 38?', '7 x 5 + 3', '7 x 3 + 5')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, '¿Qué número es  múltiplo de 3?', '42', '43')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, '¿Qué número es  múltiplo de 3?', '12', '19')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, '¿Qué número es  divisor de 77?', '11', '115')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, '¿Qué número es  divisor de 20?', '4', '8')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, '¿Qué número es  múltiplo de 3?', '87', '89')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, '¿Qué número es  divisor de 40?', '8', '6')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, '¿Qué número es  múltiplo de 7?', '35', '37')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, '¿Qué número es  divisor de 23?', '1', '7')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, 'Si tengo una parcela de 25m por 50 m, ¿Cuántos metros de valla necesito para vallar mi parcela?', '150m', '120m')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, 'Resolver: 5 - ( 6 + (2-3) ) + 1', '1', '-1')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, 'Resolver: 5 + ( 2 – 3 ) - ( -5 - 2 + 6 )', '5', '4')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, 'Los números enteros negativos se representan en la recta con respecto del cero a …', 'La izquierda', 'La derecha')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, 'Si multiplicamos un número negativo por otro positivo y por uno negativo obtenemos uno de signo…', 'positivo', 'negativo')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, '¿Qué son números opuestos?', 'Números con el mismo valor absoluto', 'Números que se contraponen')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, 'Los elementos de una resta son minuendo y…', 'Sustraendo', 'Resto')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, '¿Cuántos números naturales hay?', 'Infinitos', 'Finitos')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, 'Una potencia es…', 'Un producto de factores iguales', 'Una operación combinada')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, 'En una división exacta se cumple que el dividendo es igual al…', 'Divisor por cociente', 'Divisor por resto')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, '¿Cuantos grados suman los ángulos internos de los triángulos? ?', 'La suma de sus ángulos es igua a 180 grados', 'La suma de sus ángulos es igual a 360 grados')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, '¿Que es un triángulo acutángulo?', 'Un triángulo en el cual todos sus ángulos miden menos de 90 grados', 'Un triángulo en el cual todos sus ángulos miden mas de 90 grados')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, '¿Que es un triángulo obtusángulo?', 'El triángulo obtusángulo tiene un ángulo obtuso', 'El triángulo obtusángulo tiene tres lados iguales')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, '¿Que son los cuadriláteros?', 'Polígono que tiene cuatro lados y cuatro ángulos', 'Polígono que tienes seis lados y seis ángulos')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, '¿Qué es un cuadrado?', 'Un polígono con 4 lados iguales y 4 ángulos rectos', 'Un polígono con 2 ángulos agudos y 2 ángulos obtusos')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, '¿Que es un rectángulo?', 'Un polígono de lados opuestos paralelos y de la misma longitud y 4 ángulos rectos', 'Un polígono con dos lados paralelos y dos lados no paralelos')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, '¿Que es un rombo?', 'Un polígono de cuatro lados iguales, paralelos y cuyos ángulos opuestos son iguales', 'Un polígono de 4 ángulos rectos y lados opuestos paralelos e iguales')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, '¿Que es un trapecio?', 'Un polígono con solo dos de sus lados paralelos', 'Un polígono con 4 ángulos iguales')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, '¿Qué es un paralelogramo?', 'Un polígono con los lados opuestos paralelos, de igual longitud y los ángulos opuestos iguales', 'Un Polígono con 4 lados desiguales y 4 ángulos desiguales')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, '¿Cuales de los siguientes cuadriláteros son también paralelogramos?', 'Rectángulo, cuadrado y rombos', 'Rombo, trapecio y rectángulo')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (1, '¿Cuál es la suma de los ángulos interiores de un cuadrilátero?', '360 grados', '180 grados')");

            //LENGUA=2
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, 'Identifica la abreviatura que le corresponde a alteza', 'A.', 'Al.')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, 'Identifica la abreviatura que le corresponde a atentamente', 'Atte.', 'Aten.')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, 'Identifica la abreviatura que le corresponde a aceptación', 'acept.', 'acet.')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, 'Identifica la abreviatura que le corresponde a administración', 'admón.', 'admin.')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, 'Identifica la abreviatura que le corresponde a afectísimo', 'afmo.', 'afect.')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, 'Identifica la abreviatura que le corresponde a arquitecto', 'Arq.', 'Ar.')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, 'Atto. es la abreviatura de atento', 'Verdadero', 'Falso')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, 'Identifica la abreviatura que le corresponde a artículo', 'art.', 'artc.')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, 'Identifica la abreviatura que le corresponde a almirante', 'Almte.', 'Alm.')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, 'Identifica la abreviatura que le corresponde a banco', 'Bco.', 'Bnc.')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, 'Identifica la abreviatura que le corresponde a biblioteca', 'Bibl.', 'Btc.')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, 'Antónimo de la palabra castigar', 'Premiar', 'Perdonar')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, 'Antónimo de la palabra arbitrario', 'Justo', 'Recto')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, 'Antónimo de la palabra innato', 'Adquirido', 'Heredado')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, 'Antónimo de la palabra belicoso', 'Apacible', 'Sereno')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, 'Antónimo de la palabra falaz', 'Veraz', 'Sincero')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, 'Antónimo de la palabra afrontar', 'Rehuir', 'Escapar')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, 'Antónimo de la palabra auténtico', 'Falso', 'Dudoso')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, 'Antónimo de la palabra escuálido', 'Robusto', 'Nutrido')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, 'Determina el sinónimo de la palabra alegoría', 'Símbolo', 'Imagen')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, 'Determina el sinónimo de la palabra estrategia', 'Táctica', 'Agilidad')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, 'Determina el sinónimo de la palabra declive', 'Pendiente', 'Cumbre')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, 'Determina el sinónimo de la palabra rescindir', 'Anular', 'Prescindir')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, 'Determina el sinónimo de la palabra estigma', 'Señal', 'Enigma')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, 'Ordena estas palabras por orden alfabético: resfriado, ardilla, serpiente y jarabe', 'ardilla, jarabe, resfriado y serpiente', 'ardilla, jarabe, serpiente y resfriado')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, 'Los gentilicios se escriben con mayúscula', 'Falso', 'Verdadero')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, 'Los pronombres personales sustituyen a los...', 'Nombres', 'Determinantes artículos')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, 'El punto y coma dura más que la coma pero menos que el punto', 'Verdadero', 'Falso')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, 'La comparación consiste en decir que dos personas, animales o cosas distintas tienen algo en común', 'Verdadero', 'Falso')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, 'Primero en orden alfabético...', 'DOMINGO', 'JUEVES')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, '¿Cuál de las palabras es trisílaba?', 'SÁBADO', 'VIERNES')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, '¿Cuál de las palabras es esdrújula?', 'MIÉRCOLES', 'DOMINGO')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, '¿En cuál de las palabras su forma singular es igual a la plural?', 'LUNES', 'DOMINGO')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, '¿Cuál de las palabras tiene diptongo?', 'JUEVES', 'DOMINGO')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, '¿Cuál de las palabras es bisílaba?', 'VIERNES', 'SÁBADO')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, '¿Cuál de las palabras es trisílaba?', 'DOMINGO', 'VIERNES')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, '¿En cuál de las palabras su forma singular es igual a la plural?', 'MIÉRCOLES', 'SÁBADO')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, 'Último en orden alfabético...', 'VIERNES', 'SÁBADO')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, '¿Cuál de las palabras es bisílaba?', 'JUEVES', 'DOMINGO')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, '¿En cuál de las palabras su forma singular es igual a la plural?', 'JUEVES', 'SÁBADO')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, '¿Cuál de las palabras es esdrújula?', 'SÁBADO', 'DOMINGO')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, '¿Cuál de las palabras tiene diptongo?', 'VIERNES', 'DOMINGO')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, '¿En qué parte de la palabra se sitúan los prefijos?', 'Al comienzo', 'Al final')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, '¿Qué significado tiene el prefijo anti-?', 'Opuesto a', 'Anterior a')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, '¿En qué parte de la palabra se sitúan los sufijos?', 'Al final', 'Al comienzo')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, '¿Qué prefijo se utiliza para indicar dentro?', 'En-', 'Pre-')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, '¿Qué prefijo se utiliza para indicar delante?', 'Ante-', 'En-')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, 'Murciélago tiene las 5 vocales', 'Verdadero', 'Falso')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, 'Balneario tiene las 5 vocales', 'Falso', 'Verdadero')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, 'Murciélago tiene diptongo', 'Verdadero', 'Falso')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, 'Balneario tiene diptongo', 'Verdadero', 'Falso')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, 'Murciélago tiene hiato', 'Falso', 'Verdadero')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, 'Balneario tiene hiato',  'Verdadero', 'Falso')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, 'Murciélago es trisílaba', 'Falso', 'Verdadero')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, 'Balneario es trisílaba ', 'Falso', 'Verdadero')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, 'Murciélago es polisílaba ', 'Verdadero', 'Falso')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, 'Balneario es polisílaba ', 'Verdadero', 'Falso')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, 'Murciélago es palabra aguda', 'Falso', 'Verdadero')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, 'Balneario es  palabra aguda ', 'Falso', 'Verdadero')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, 'Murciélago es palabra graves', 'Falso', 'Verdadero')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, 'Balneario es palabra graves', 'Verdadero', 'Falso')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, 'Murciélago es palabra esdrújula', 'Verdadero', 'Falso')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, 'Balneario es palabra esdrújula', 'Falso', 'Verdadero')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, 'Murciélago rima con cartílago ', 'Falso', 'Verdadero')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, 'Balneario rima con canario ', 'Verdadero', 'Falso')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, 'Murciélago rima con archipiélago', 'Verdadero', 'Falso')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, 'Balneario rima con imperio', 'Falso', 'Verdadero')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, 'Murciélago rima con halago', 'Falso', 'Verdadero')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, 'Balneario rima con Darío', 'Falso', 'Verdadero')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, '¿Cuál de los prefijos se añaden a las palabras para indicar lugar o situación?', 'Sub-', 'In-')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, '¿Cuál de los prefijos se añaden a las palabras para modificar su intensidad?', 'Subper-', 'Contra-')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, '¿Cuál de las palabras está mal escrita?', 'CIÉNCIA', 'POÉTICA')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, 'Elige la expresión correcta: “Allí se ______ La cueva que te dije”', 'halla', 'haya')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, 'Elige la expresión correcta: “Vende los productos ______ precio”', 'A BAJO', 'ABAJO')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, 'Elige la expresión correcta: “Corrió escaleras ______”', 'ABAJO', 'A BAJO')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, '¿Cuál de las palabras está mal escrita?', 'AQUEDUCTO', 'EXACTITUD')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, 'Elige la expresión correcta: “______, procura cantar muy bajito”', 'Sobre todo', 'Sobre, todo')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, 'El sujeto de la oración concuerda con el verbo en…', 'Número y persona', 'Género y número')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, 'El sujeto de la oración concuerda con el atributo en…', 'Género y número', 'Número y persona')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, 'El modo verbal que se utiliza para dar órdenes o hacer ruegos se llama…', 'Imperativo', 'Indicativo')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, 'Las obras de teatro conforman el género llamado:', 'Dramático', 'Novelesco')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, 'Las obras literarias que tratan sobre los sentimientos íntimos del autor se denominan…', 'Líricas', 'Dramáticas')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, 'Las obras literarias que narran hechos y aventuras varias se denominan…', 'Épicas', 'Dramáticas')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, 'La rima puede ser…', 'Consonante y asonante', 'Par e impar')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, 'Las palabras que aparecen en la misma línea de un poema forman', 'Un verso', 'Una estrofa')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, '¿Qué tipo de palabra es “Hasta”?', 'Preposición', 'Adverbio')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, '¿Qué tipo de palabra es “Cuestión”?', 'Sustantivo', 'Verbo')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, '¿Qué tipo de palabra es “Arriba”?', 'Adverbio de lugar', 'Adverbio de tiempo')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, '¿Qué tipo de palabra es “Derivación”?', 'Sustantivo', 'Adjetivo')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, '¿Qué tipo de palabra es “Haya”?', 'Verbo o Sustantivo', 'Adjetivo o verbo')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, '¿Qué tipo de palabra es “Cálculo”?', 'Sustantivo', 'Verbo')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, '¿Qué tipo de palabra es “Torpemente”?', 'Adverbio de modo', 'Adverbio de negación')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, '¿Qué tipo de palabra es “Varia”?', 'Adjetivo', 'Verbo')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, '¿Qué tipo de palabra es “Halla”?', 'Verbo', 'Sustantivo')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, '¿Qué tipo de palabra es “Halla”?', 'Adverbio de duda', 'Verbo')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, 'La palabra SUPIERON no lleva tilde porque...', 'Es aguda y acaba en N', 'En realidad lleva tilde porque acaba en ON')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, 'Elige la palabra correcta para completar la frase: “¿_______ lo has hecho?”', 'Por qué', 'Porque')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, 'Elige la palabra correcta para completar la frase: “De esas cosas tiene que  _______ por aquí”', 'haber', 'a ver')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, 'Elige la palabra correcta para completar la frase: “Intentaremos averiguar el  __________ de su negativa”', 'Porqué', 'Por qué')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, 'Elige la palabra correcta para completar la frase: “Nos vamos _____________ es muy tarde”', 'Porque', 'Por qué')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (2, 'Elige la palabra correcta para completar la frase: “Vamos _____________  si lo conseguimos”', 'A ver', 'haber')");

            //NATURALES=3
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, 'Entendemos por ambiente al sistema constituido por elementos naturales, artificiales y sus interacciones, en permanente modificación por la acción humana', 'Falso', 'Verdadero')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, 'Al hablar de ambiente, solo estamos considerando a la naturaleza', 'Falso', 'Verdadero')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, 'El hombre es el principal modificador del ambiente pero no el único, la naturaleza lo modifica constantemente', 'Verdadero', 'Falso')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, 'El ambiente está compuesto por factores bióticos y...', 'Factores abióticos', 'Factores físicos')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, 'Todos los ambientes son iguales, en todos ellos encontramos los mismos factores bióticos y abióticos', 'Falso', 'Verdadero')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, 'Todos los organismos vivos de un ambiente se alimentan únicamente de plantas', 'Falso', 'Verdadero')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, 'Cada cadena trófica se compone de productores, consumidores y descomponedores', 'Verdadero', 'Falso')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, 'Un fármaco y un medicamento son lo mismo', 'Falso', 'Verdadero')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, 'Proceso de división mediante el cual se multiplican las células eucarióticas', 'Mitosis', 'Citosis')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, 'Proceso mediante el cual la mayoría de los vegetales transforma la materia inorgánica en orgánica empleando la energía de la luz solar', 'Fotosíntesis', 'Metamorfosis')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, 'Colaboración mutua entre dos organismos vivos que resulta beneficiosa a ambas partes', 'Simbiosis', 'Parasitismo')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, 'Una jirafa y un humano tienen el mismo número de vértebras cervicales', 'Verdadero', 'Falso')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, 'Las neuronas no están conectadas entre sí y transmiten su información de unas a otras mediante la sinapsis', 'Verdadero', 'Falso')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, 'La orbita que realiza la tierra sobre el sol al girar sobre la tierra (translación) es un movimiento completamente circular', 'Falso', 'Verdadero')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, '¿Cuál es el organismo vivo más pequeño que existe?', 'Las bacterias', 'Los ácaros')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, 'Las cadenas montañosas fueron formadas a lo largo de miles de años por los choques de la corteza terrestre producidos por el movimiento de las placas tectónicas y el magma', 'Verdadero', 'Falso')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, 'Los rayos producidos en una tormenta generan un gas conocido como ozono (o3)', 'Verdadero', 'Falso')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, 'Aunque su proceso de elaboración sería más costoso que su propio valor, sería posible crear oro a partir del plomo', 'Verdadero', 'Falso')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, '¿Cuantas extremidades tiene un pulpo?', '8', '6')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, 'La cafeína es un alcaloide presente en bebidas como el café y en algunos refrescos, que puede producir dependencia', 'Verdadero', 'Falso')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, 'Proceso de transformación que realizan ciertos seres vivos, sufriendo grandes reestructuraciones morfológicas y anatómicas', 'Metamorfosis', 'Simbiosis')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, 'Los seres vivos se clasifican en tres reinos:', 'Plantas, animales y hongos', 'Animales, plantas y mamíferos')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, '¿A qué temperatura se congela el agua?', '0 grados centígrados', '-5 grados centígrados')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, '¿A qué temperatura hierve el agua?', '100 grados', '45 grados')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, '¿Dónde se producen los fenómenos atmosféricos?', 'En la atmósfera', 'En la geosfera')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, '¿Qué es una aurora boreal?', 'Una aparición en el cielo de manchas y columnas luminosas', 'Una capa de la atmósfera')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, '¿Qué caracteriza a los seres pluricelulares y que los diferencia de los unicelulares?', 'Están formados por más de una célula', 'Realizan las tres funciones básicas')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, 'Todos los seres vivos que mueren se fosilizan', 'Falso', 'Verdadero')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, 'Las funciones vitales que realizan los seres vivos son:', 'Nacen, crecen, se relacionan, se reproducen y mueren', 'Nacen, se mueve, crecen, se relacionan, se reproducen y mueren')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, 'Las bacterias son…', 'Útiles y perjudiciales para el hombre', 'Perjudiciales para el hombre')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, 'Los virus pueden reproducirse sólo dentro de otro ser vivo', 'Verdadero', 'Falso')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, '¿Cómo se llama la teoría de Darwin?', 'Selección natural', 'Teoría biológica')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, '¿Qué estudia la paleontología?', 'Fósiles', 'Piedras')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, '¿Qué sonido emite una Ballena?', 'Canto', 'Barrito')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, '¿Qué sonido emite un Elefante?', 'Barrito', 'Balido')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, '¿Qué sonido emite un Delfín?', 'Chasquido', 'Chillido')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, 'El Gorila produce un Gruñido', 'Verdadero', 'Falso')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, '¿Cuál es el símbolo del siguiente elemento Litio?', 'Li', 'Lu')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, '¿Cuál es el símbolo del siguiente elemento Sodio?', 'Na', 'S')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, '¿Cuál es el símbolo del siguiente elemento Berilio?', 'Be', 'B')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, '¿Cuál es el símbolo del siguiente elemento Magnesio?', 'Mg', 'Mn')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, '¿Cuál es el símbolo del siguiente elemento Calcio?', 'Ca', 'K')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, '¿Cuál es el símbolo del siguiente elemento Rubidio?', 'Rb', 'Ru')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, '¿Cuál es el símbolo del siguiente elemento Estroncio?', 'Sr', 'Es')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, '¿Cuál es el símbolo del siguiente elemento Bario?', 'Ba', 'B')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, '¿Cuál es el símbolo del siguiente elemento Cesio?', 'Cs', 'Ce')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, '¿Cuál es el símbolo del siguiente elemento Potasio?', 'K', 'Po')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, '¿Cuál es el símbolo del siguiente elemento Francio?', 'Fr', 'Fe')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, '¿Cuál es el símbolo del siguiente elemento Radio?', 'Ra', 'Rb')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, '¿Dónde está ubicado el tímpano?', 'Oído medio', 'Oído externo')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, 'Individuo que vive a expensas de otro individuo', 'Parasito', 'Bacteria')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, '¿Cuántos huesos tiene la mano?', '27', '19')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, 'Incapacidad de distinguir algunos colores, principalmente el color rojo:', 'Daltonismo', 'Estrabismo')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, 'Tiempo de gestación del ser humano', '40 semanas', '37 semanas')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, '¿Cuántos dientes tiene una persona adulta?', '32 dientes', '38 dientes')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, '¿Cuál es el hueso más largo del cuerpo humano?', 'El fémur', 'El húmero')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, 'Principal arteria del cuello', 'Carótida', 'Aorta')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, 'Lesión producida en los ligamentos y tendones', 'Esguince', 'Tendinitis')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, '¿Qué tipo de orbita describe la Luna sobre su planeta?', 'Elipse', 'Circular')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, '¿Por qué desde la Tierra solo podemos ver una cara de la luna?', 'Tarda igual en girar sobre si que sobre la Tierra', 'El sol solo ilumina la cara que podemos ver')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, '¿Cuándo se produce un eclipse lunar?', 'Cuando la Tierra se pone entre el Sol y la Luna', 'Cuando el Sol se pone entre la Tierra y la Luna')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, '¿Por qué no se erosiona la superficie lunar?', 'Por la ausencia de aire y vientos', 'Por la composición de su corteza terrestre')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, '¿Cuál es la causa más habitual en la generación de los terremotos?', 'Movimiento de placas tectónicas', 'Actividad volcánica')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, '¿Qué suele causar un terremoto si se produce en medio del mar?', 'Un tsunami', 'Un enorme cráter')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, '¿Cómo se llama el aparato que se encarga de medir la instanciada de un terremoto?', 'Sismógrafo', 'Espectrógrafo')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, '¿Cómo se denomina al punto exacto donde se produce la fractura de la corteza terrestre que provoca un terremoto?', 'Hipocentro', 'Epicentro')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, 'El monte Olimpo es la montaña más alta del sistema solar ¿En qué planeta está?', 'En Marte', 'En Venus')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, '¿Cuál es el planeta que gira más rápido sobre su propio eje?', 'Júpiter', 'Saturno')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, '¿Cuál es el planeta más grande del sistema solar?', 'Júpiter', 'Urano')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, '¿A que deben su nombre todos los planetas?', 'A antiguos dioses griegos y romanos', 'A animales mitológicos')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, '¿Cuántos huesos hay en el cuerpo humano?', '206', '220')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, '¿Cuánto pesa el cerebro de un adulto?', '1 kg', '2 kg')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, '¿Cuántos metros miden los intestinos?', 'Entre 8 y 9', 'Entre 9 y 10')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, '¿Aproximadamente cuantos pelos tenemos en la cabeza?', 'Entre 100.0000 y 200.000', 'Entre 200.000 y 300.000')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, '¿Cuánta sangre tiene una persona adulta en el cuerpo?', '5 litros', '6 litros')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, '¿A qué velocidad crece el pelo?', '15 cm al año', '20 cm al año')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, '¿Cuántas papilas gustativas tenemos en la lengua?', '10.000', '8.000')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, '¿Qué han de tener dos objetos para que entre ellos exista gravedad?', 'Masa', 'Energía')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, 'La gravedad es una de las “fuerzas fundamentales” que considera la Física moderna ¿Cuántas son en total?', '4', '5')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, '¿Qué proceso biológico potencia la evolución?', 'La reproducción sexual', 'La reproducción asexual')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, '¿Qué islas fueron determinantes para que Darwin madurara sus ideas evolutivas?', 'Las islas de los Galápagos', 'Las islas Filipinas')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, '¿Cómo se llama a la temperatura necesaria para que un combustible inflame?', 'Punto de ignición', 'Temperatura de inflexión')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, '¿Cómo se llama al intercambio de calor de una sustancia en combustión a través del aire?', 'Convección', 'Conducción')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, '¿Por qué apagan el fuego los extintores?', 'Cortan el oxígeno del fuego', 'Expulsan agua y aire para esparcir el fuego')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, '¿De qué color es la llama producida por la mayoría de los hidrocarburos utilizados domésticamente?', 'Azul', 'Anaranjada')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, 'El fuego es un proceso exotérmico ¿Qué significa ese término?', 'Desprende calor', 'Genera energía')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, 'Los catalizadores inhibidores…', 'Disminuyen la velocidad de reacción', 'Imposibilitan la reacción')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, 'Si una reacción genera un producto que sirve de catalizador de la misma, este se llama…', 'Autocatalizador', 'Catalizador propio')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, 'Los hidrocarburos están formados por átomos de…', 'Carbono e Hidrogeno', 'Hidrogeno y Oxigeno')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, 'El elemento presente en todos los compuestos orgánicos es el…', 'Carbono', 'Oxígeno')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, 'El Perclorato de potasio se simboliza', 'KCLO4', 'HCLO4')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, 'La molécula HCL se llama', 'Cloruro de Hidrogeno', 'Clorato (VII) de hidrogeno')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, 'La fórmula correcta del Ácido Hipocloroso es…', 'HCLO', 'HCLO3')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, 'El nombre correcto del compuesto PbO es…', 'Oxido Plumboso', 'Oxido Plumbico')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, '¿Qué edad se calcula a nuestro planeta?', '4.600 millones de años', '4.000 millones de años')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, '¿Qué es Pangea?', 'Un supercontinente', 'Una inundación continental')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, 'La región del mundo con más variedad de aves es:', 'América del sur', 'África')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, 'Hay un ave que tiene nombre de fruta ¿Cuál es?', 'El kiwi', 'El albaricoque')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, 'La única ave que sabe colar hacia atrás es:', 'El colibrí', 'Las cotorras')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, '¿Cuál de estas aves esta extinta desde hace cuatro siglos?', 'El dodó', 'La fragata')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, '¿De que esta hecho el nido del hornero?', 'De lodo', 'De pastos secos')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, 'Las personas que se dedican al estudio de las aves se llaman', 'Ornitólogos', 'Espeleólogos')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, '¿Cómo se comunican entre sí las hormigas principalmente?', 'Por feromonas', 'Mediante las antenas')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, '¿Cuándo se puede decir que una especie es endémica?', 'Cuando existe y ha existido en un único lugar', 'Cuando solo se puede encontrar en un lugar')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, 'Las plantas no son seres vivos', 'Falso, todas las plantas son seres vivos', 'Falso, algunas plantas si son seres vivos')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, 'Las plantas son seres vivos que:', 'Nacen, crecen, se reproducen y mueren', 'Nacen, crecen, de desplazan, se reproducen y muere')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, 'Las plantas no fabrican su alimento', 'Falso', 'Verdadero')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, '¿Qué cuatro cosas principales necesitan las plantas para vivir?', 'Agua, aire, tierra y luz solar', 'Frio, tierra, aire y luz')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, '¿Qué es un herbario?', 'Una colección de plantas y su información', 'Colección de hierbas')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, '¿Qué función realiza el tallo en la planta?', 'Sostiene la planta', 'Absorbe el agua y las sales minerales')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, 'Las hojas de las plantas', 'Nacen de las ramas y toman el oxígeno del aire', 'Nacen de las ramas y sostiene la planta')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, 'La raíz de las plantas:', 'Absorbe el agua y las sales minerales', 'Se encuentran bajo tierra y sostiene la planta')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, 'Las partes de una planta son:', 'Raíz, tallo, hojas y frutos', 'Raíz, tallo, hojas y flores')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, 'Al nacer, los anfibios respiran por…', 'Branquias', 'Piel')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, '¿Qué animales son ovíparos?', 'Anfibios, reptiles, aves y peces', 'Anfibios, reptiles, aves, peces y mamíferos')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, 'Los animales vertebrados se dividen en…', 'Mamíferos, aves, peces, anfibios y reptiles', 'Mamíferos, aves, peces, anfibios e insectos')");
            db.execSQL("insert into pregunta ( categoria, pregunta, respuestaCorrecta, respuestaIncorrecta) values (3, 'La serpiente es…', 'Un reptil', 'Un anfibio')");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }
    }

}
