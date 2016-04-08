package com.interfacesmoviles.carolinaperez;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class JuegoActivity extends Activity implements SensorEventListener, OnClickListener {


    private Typeface TF;
    private Typeface TF2;
    private RelativeLayout respuestaDerecha;
    private RelativeLayout respuestaIzquierda;
    private LinearLayout panelRespuestas;
    private TextView textViewReloj;
    private TextView textViewPuntuacion;
    private TextView textPregunta;
    private TextView textRespuestaIzquierda;
    private TextView textRespuestaDerecha;
    private TextView titulo;
    private Button buttonSonido;
    private Button buttonVibracion;
    private Button buttonSalida;
    private int puntaje=0;
    //CENSORES
    private Vibrator v;
    private SensorManager sensorManager = null;
    private Sensor sensorAcelerometro = null;
    //CONTADOR
    private CountDownTimer reloj;
    //CONTROL DE PREGUNTAS
    private PreguntasAleatorias preguntasAleatorias;
    private ArrayList<Pregunta> preguntas;
    private Pregunta preguntaActual;
    //LUMINOCIDAD PANTALLA
    private PowerManager.WakeLock wakelock;
    //VARIABLES DE CONTROL
    private Boolean respondio=false;
    private Boolean enJuego=true;
    private Boolean inicio=true;
    private String respuestaCorrecta;
    //PARAMETROS
    private int categoriaCodigo;
    private String categoriaNombre;
    private Jugador jugador;

    private boolean salida=true;
    private  long s1=0;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);
        //TAMAÑO DE PANTALLA
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width=dm.widthPixels;
        //PARAMETRO
        categoriaCodigo=(int) getIntent().getExtras().getSerializable("categoriaCodigo");
        categoriaNombre=(String) getIntent().getExtras().getSerializable("categoriaNombre");
        jugador=(Jugador) getIntent().getExtras().getSerializable("jugador");
        //CONTROL LUMINOCIDAD PANTALLA
        final PowerManager pm=(PowerManager)getSystemService(Context.POWER_SERVICE);
        this.wakelock=pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "etiqueta");
        wakelock.acquire();
        //OBTENGO PREGUNTAS
        preguntas=(new DataBaseManager(this)).listPreguntas(categoriaCodigo);
        preguntasAleatorias= new PreguntasAleatorias(0, preguntas.size()-1, preguntas);
        //ESTILO DE FUENTES
        TF = Typeface.createFromAsset(getAssets(),"fonts/sketchMatch.ttf");
        TF2 = Typeface.createFromAsset(getAssets(),"fonts/TitaniaR.ttf");
        //ORIENTACOIN PANTALLA
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
        //RELOJ
        textViewReloj=(TextView)findViewById(R.id.reloj);
        textViewReloj.setTypeface(TF);
        textViewReloj.setTextSize(width*1/30);
        reloj = new CountDownTimer(61000, 1000) {

            public void onTick(long millisUntilFinished) {
                textViewReloj.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                textViewReloj.setText("0");enJuego=false;
                jugador.compararPuntos(puntaje,categoriaCodigo,getApplicationContext());
                finDelJuego(puntaje);
            }
        };

        RelativeLayout layoutTiempo=(RelativeLayout )findViewById(R.id.layoutTiempo);
        layoutTiempo.getLayoutParams().height=(width*1/12);
        layoutTiempo.getLayoutParams().width=(width*1/6);

        //PUNTACION
        textViewPuntuacion=(TextView)findViewById(R.id.puntuacion);
        textViewPuntuacion.setTypeface(TF);
        textViewPuntuacion.setTextSize(width *1/30);

        RelativeLayout layoutPuntos=(RelativeLayout )findViewById(R.id.layoutPuntos);
        layoutPuntos.getLayoutParams().height=(width*1/12);
        layoutPuntos.getLayoutParams().width=(width*1/4);

        //CUERPO DEL JUEGO
        respuestaDerecha=(RelativeLayout)findViewById(R.id.frameDerecho);
        respuestaIzquierda=(RelativeLayout)findViewById(R.id.frameIzquierdo);
        panelRespuestas=(LinearLayout)findViewById(R.id.bordePregunta);
        textPregunta=(TextView)findViewById(R.id.textViewPregunta);
        textRespuestaIzquierda=(TextView)findViewById(R.id.respuestaIzquierda);
        textRespuestaDerecha=(TextView)findViewById(R.id.respuestaDerecha);

        titulo=(TextView)findViewById(R.id.textCategoriaJuego);
        titulo.setTypeface(TF);
        titulo.setText(categoriaNombre);
        titulo.setTextSize(width * 1 / 23);

        textPregunta.setTypeface(TF2);
        textPregunta.setTextSize(width * 1 / 50);
        textRespuestaIzquierda.setTypeface(TF2);
        textRespuestaIzquierda.setTextSize(width * 1 / 52);
        textRespuestaDerecha.setTypeface(TF2);
        textRespuestaDerecha.setTextSize(width * 1 / 52);

        buttonSonido=(Button)findViewById(R.id.buttonSound);
        buttonSonido.setOnClickListener(this);
        buttonSonido.getLayoutParams().height=(width*1/20);
        buttonSonido.getLayoutParams().width=(width*1/20);
        if(! jugador.isHabilitarSonido()){
            buttonSonido.setBackgroundResource(R.drawable.no_sound);
        }

        //VIBRACION
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        buttonVibracion=(Button)findViewById(R.id.buttonVibracion);
        buttonVibracion.setOnClickListener(this);
        buttonVibracion.getLayoutParams().height=(width*1/20);
        buttonVibracion.getLayoutParams().width=(width*1/20);

        if(! jugador.isHabilitarVibracion()){
            buttonVibracion.setBackgroundResource(R.drawable.no_vibracion);
        }
        buttonSalida=(Button)findViewById(R.id.buttonExitJuego);
        buttonSalida.setOnClickListener(this);
        buttonSalida.getLayoutParams().height=(width*1/20);
        buttonSalida.getLayoutParams().width=(width*1/20);

        //SENSORES
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorAcelerometro = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(sensorAcelerometro == null){
            //NO SE PUEDE JUGAR
        }
        else{sensorManager.registerListener(this, sensorAcelerometro, SensorManager.SENSOR_DELAY_NORMAL);}


    }


    @Override
    protected void onDestroy(){
        super.onDestroy();
        this.wakelock.release();
    }





    @Override
    protected void onResume(){
        super.onResume();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        wakelock.acquire();
    }



    @Override
    public void onSaveInstanceState(Bundle icicle) {
        super.onSaveInstanceState(icicle);
        this.wakelock.release();
    }




    @Override
    protected void onPause(){
        sensorManager.unregisterListener(this);
        super.onPause();
    }




    @Override
    protected void onStop(){
        sensorManager.unregisterListener(this);
        super.onStop();
    }




    @Override
    public void onSensorChanged(SensorEvent event) {
        if(enJuego){
            if (inicio){
                if (event.values[SensorManager.DATA_Z] < 0 && event.values[SensorManager.DATA_X] < -3 && (event.values[SensorManager.DATA_Y]<1 || event.values[SensorManager.DATA_Y]>-1 )) {iniciarJuego();}
            }
            else{
                if(respondio){
                    if (event.values[SensorManager.DATA_Z] < 0 && event.values[SensorManager.DATA_X] < -3 && (event.values[SensorManager.DATA_Y]<1 || event.values[SensorManager.DATA_Y]>-1 )) {siguientePregunta();}
                }
                else{
                    if(event.values[SensorManager.DATA_Y] < -7){evaluarRespuesta("DERECHA");}
                    else {if(event.values[SensorManager.DATA_Y] > 7){evaluarRespuesta("IZQUIERDA");}}
                }
            }
        }
    }









    private void evaluarRespuesta(String repuesta) {
        if(repuesta.equals(respuestaCorrecta)){
            if(jugador.isHabilitarVibracion()){v.vibrate(new long[]{0, 50, 100, 50}, -1);}
            panelRespuestas.setBackgroundResource(R.drawable.correcto);
            puntaje=puntaje+10;
            textViewPuntuacion.setText(Integer.toString(puntaje));
            if(jugador.isHabilitarSonido()) {
                MediaPlayer mp1 = MediaPlayer.create(this, R.raw.ok);
                mp1.start();
            }
        }//correcta
        else {
            if(jugador.isHabilitarVibracion()){v.vibrate(300);}
            panelRespuestas.setBackgroundResource(R.drawable.incorrecto);
            if(jugador.isHabilitarSonido()) {
                MediaPlayer mp2 = MediaPlayer.create(this, R.raw.error);
                mp2.start();
            }
        }//incorrecta

        respondio=true;
    }





    private void siguientePregunta() {
        preguntaActual = preguntasAleatorias.generarPregunta();
        if(preguntaActual == null){
            textViewReloj.setText("0");enJuego=false;
            jugador.compararPuntos(puntaje,categoriaCodigo,getApplicationContext());
            finDelJuego(puntaje);
        }
        else {
            textPregunta.setText(preguntaActual.getPregunta());

            //POSICION DE LAS RESPUESTAS
            int aux = (int) (Math.random() * 2 + 1);
            if ((aux % 2) == 0) {
                respuestaCorrecta = "IZQUIERDA";
                textRespuestaIzquierda.setText(preguntaActual.getRespuestaCorrecta());
                textRespuestaDerecha.setText(preguntaActual.getRespuestaIncorrecta());
            } else {
                respuestaCorrecta = "DERECHA";
                textRespuestaIzquierda.setText(preguntaActual.getRespuestaIncorrecta());
                textRespuestaDerecha.setText(preguntaActual.getRespuestaCorrecta());
            }

            panelRespuestas.setBackgroundResource(R.drawable.background);
            respondio = false;
        }
    }





    private void iniciarJuego(){
        reloj.start();
        siguientePregunta();
        inicio=false;
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonSound:
                if(jugador.isHabilitarSonido()){
                    jugador.setHabilitarSonido(false);
                    buttonSonido.setBackgroundResource(R.drawable.no_sound);
                }
                else{
                    jugador.setHabilitarSonido(true);
                    buttonSonido.setBackgroundResource(R.drawable.sound);

                }

                break;
            case R.id.buttonVibracion:
                if(jugador.isHabilitarVibracion()){
                    jugador.setHabilitarVibracion(false);
                    buttonVibracion.setBackgroundResource(R.drawable.no_vibracion);
                }
                else{
                    jugador.setHabilitarVibracion(true);
                    buttonVibracion.setBackgroundResource(R.drawable.vibracion);

                }
                break;
            case R.id.buttonExitJuego:
                Intent intent = new Intent(JuegoActivity.this, CategoriasActivity.class);
                intent.putExtra("jugador", jugador);
                startActivity(intent);
                break;
        }
    }


    //DIALOGO FIN DEL JUEGO
    private void finDelJuego(int p) {
        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.fin_del_juego, null);


        TextView nombre=(TextView) dialoglayout.findViewById(R.id.tituloFinJuego);
        nombre.setTypeface(TF);

        TextView puntos=(TextView) dialoglayout.findViewById(R.id.puntuacionFinJuego);
        puntos.setTypeface(TF);
        puntos.setText(Integer.toString(p));

        Button salir = (Button) dialoglayout.findViewById(R.id.exitFinJuego);
        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(JuegoActivity.this, CategoriasActivity.class);
                intent.putExtra("jugador", jugador);
                startActivity(intent);
            }
        });


        Button jugar = (Button) dialoglayout.findViewById(R.id.otaVezFinJuego);
        jugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(JuegoActivity.this, JuegoActivity.class);
                intent.putExtra("jugador", jugador);
                intent.putExtra("categoriaCodigo", categoriaCodigo);
                intent.putExtra("categoriaNombre", categoriaNombre);
                startActivity(intent);
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(JuegoActivity.this);
        builder.setView(dialoglayout);
        builder.show();
    }


}