package com.interfacesmoviles.carolinaperez;

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
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RankingActivity extends AppCompatActivity implements SensorEventListener, View.OnClickListener {

    private SensorManager sensorManager = null;
    private Sensor sensorDeProximidad = null;
    private boolean sensorProximidadDisponible=false;

    private Typeface TF;

    private TextView rankingTitulo;
    private TextView titulo;
    private int posTitulo=0;
    private String[] titulos={"MATEMATICA", "NATURALES", "LENGUA", "MIX"};

    private CountDownTimer reloj;
    private boolean habilitarSensor=true;

    private TextView aux;

    private TextView puntosPrimero;
    private TextView nombrePrimero;
    private ImageView fotoPrimero;
    private TextView puntosSegundo;
    private TextView nombreSegundo;
    private ImageView fotoSegundo;
    private TextView puntosTercero;
    private TextView nombreTercero;
    private ImageView fotoTercero;

    private Button salida;
    private Jugador j;

    private boolean inicio=true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        //TAMAÑO DE PANTALLA
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width=dm.widthPixels;

        //ORIENTACION PANTALLA
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //ESTILO LETRAS
        TF = Typeface.createFromAsset(getAssets(), "fonts/sketchMatch.ttf");

        //PARAMETRO
        j=(Jugador) getIntent().getExtras().getSerializable("jugador");

        //ELEMENTOS DEL LAYOUT
        titulo=(TextView)findViewById(R.id.tituloCategoriaRanking);
        titulo.setTypeface(TF);
        titulo.setText(titulos[posTitulo % 4]);
        posTitulo++;

        rankingTitulo=(TextView)findViewById(R.id.ranking);
        rankingTitulo.setTypeface(TF);

        //PODIO 1
        FrameLayout layoutPrimerPuesto=(FrameLayout )findViewById(R.id.layoutPrimerPuesto);
        layoutPrimerPuesto.getLayoutParams().height=(width*1/4);
        layoutPrimerPuesto.getLayoutParams().width=(width*1/8);
        aux=(TextView)findViewById(R.id.primerPuesto);
        aux.setTypeface(TF);

        //PODIO 2
        aux.setTextSize(width * 1 / 20);
        aux=(TextView)findViewById(R.id.segundoPuesto);
        aux.setTypeface(TF);
        aux.setTextSize(width * 1 / 20);
        FrameLayout layoutSegundoPuesto=(FrameLayout )findViewById(R.id.layoutSegundoPuesto);
        layoutSegundoPuesto.getLayoutParams().height=(width*1/4);
        layoutSegundoPuesto.getLayoutParams().width=(width*1/8);

        //PODIO 3
        aux=(TextView)findViewById(R.id.tercerPuesto);
        aux.setTypeface(TF);
        aux.setTextSize(width * 1 / 20);
        FrameLayout layoutTercerPuesto=(FrameLayout )findViewById(R.id.layoutTercerPuesto);
        layoutTercerPuesto.getLayoutParams().height=(width*1/4);
        layoutTercerPuesto.getLayoutParams().width=(width*1/8);

        //JUGADOR 1
        LinearLayout layoutfotoPimero=(LinearLayout)findViewById(R.id.layoutfotoPimero);
        layoutfotoPimero.getLayoutParams().height=((width*30)/100);
        layoutfotoPimero.getLayoutParams().width=((width*23)/100);

        fotoPrimero=(ImageView) findViewById(R.id.fotoPrimero);
        fotoPrimero.getLayoutParams().height=(width*1/5);
        fotoPrimero.getLayoutParams().width=(width*1/5);

        nombrePrimero=(TextView)findViewById(R.id.nombrePrimero);
        nombrePrimero.setTypeface(TF);
        nombrePrimero.setTextSize(width * 1 / 30);

        puntosPrimero=(TextView)findViewById(R.id.puntuacionPrimero);
        puntosPrimero.setTypeface(TF);
        puntosPrimero.setTextSize(width * 1 / 24);

        RelativeLayout layoutPpuntuacionPrimero=(RelativeLayout )findViewById(R.id.layoutPpuntuacionPrimero);
        layoutPpuntuacionPrimero.getLayoutParams().height=(width*1/7);
        layoutPpuntuacionPrimero.getLayoutParams().width=(width*1/3);


        //JUGADOR 2
        LinearLayout layoutfotoSegundo=(LinearLayout)findViewById(R.id.layoutfotoSegundo);
        layoutfotoSegundo.getLayoutParams().height=((width*30)/100);
        layoutfotoSegundo.getLayoutParams().width=((width*23)/100);

        nombreSegundo=(TextView)findViewById(R.id.nombreSegundo);
        nombreSegundo.setTypeface(TF);
        nombreSegundo.setTextSize(width * 1 / 30);

        puntosSegundo=(TextView)findViewById(R.id.puntuacionSeundo);
        puntosSegundo.setTypeface(TF);
        puntosSegundo.setTextSize(width * 1 / 24);

        fotoSegundo=(ImageView) findViewById(R.id.fotoSegundo);
        fotoSegundo.getLayoutParams().height=(width*1/5);
        fotoSegundo.getLayoutParams().width=(width*1/5);

        RelativeLayout layoutPpuntuacionSegundo=(RelativeLayout )findViewById(R.id.layoutPpuntuacionSegundo);
        layoutPpuntuacionSegundo.getLayoutParams().height=(width*1/7);
        layoutPpuntuacionSegundo.getLayoutParams().width=(width*1/3);


        //JUGADOR 3
        LinearLayout layoutfotoTercero=(LinearLayout)findViewById(R.id.layoutfotoTercero);
        layoutfotoTercero.getLayoutParams().height=((width*30)/100);
        layoutfotoTercero.getLayoutParams().width=((width*23)/100);

        nombreTercero=(TextView)findViewById(R.id.nombreTercero);
        nombreTercero.setTypeface(TF);
        nombreTercero.setTextSize(width * 1 / 30);

        puntosTercero=(TextView)findViewById(R.id.puntuacionTercero);
        puntosTercero.setTypeface(TF);
        puntosTercero.setTextSize(width * 1 / 24);

        fotoTercero=(ImageView) findViewById(R.id.fotoTercero);
        fotoTercero.getLayoutParams().height=(width*1/5);
        fotoTercero.getLayoutParams().width=(width*1/5);

        RelativeLayout layoutPpuntuacionTercero=(RelativeLayout )findViewById(R.id.layoutPpuntuacionTercero);
        layoutPpuntuacionTercero.getLayoutParams().height=(width*1/7);
        layoutPpuntuacionTercero.getLayoutParams().width=(width*1/3);


        //BOTON SALIDA
        salida=(Button)findViewById(R.id.exitRanking);
        salida.setOnClickListener(this);
        salida.getLayoutParams().height=((width * 1 / 8));
        salida.getLayoutParams().width=((width * 1 / 8));



        //SENSORES
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorDeProximidad = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        if(sensorDeProximidad != null){
            sensorManager.registerListener(this, sensorDeProximidad, SensorManager.SENSOR_DELAY_NORMAL);
            sensorProximidadDisponible=true;
        }
        if(!sensorProximidadDisponible){
            sensorDeProximidad = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, sensorDeProximidad, SensorManager.SENSOR_DELAY_NORMAL);
            posTitulo=0;
        }

        cargarPuestos();
    }



    @Override
    public void onSensorChanged(SensorEvent event) {
        if(sensorProximidadDisponible) {
            float[] masData;
            masData = event.values;
            if (masData[0] != 0) {
                if (habilitarSensor) {
                    titulo.setText(titulos[posTitulo % 4]);
                    cargarPuestos();
                    posTitulo++;
                    habilitarSensor = false;
                    if (inicio) {
                        inicio = false;
                    } else {
                        MediaPlayer mp = MediaPlayer.create(this, R.raw.bo);
                        mp.start();
                    }

                    reloj = new CountDownTimer(1500, 500) {
                        public void onTick(long millisUntilFinished) {
                        }

                        public void onFinish() {
                            habilitarSensor = true;
                        }
                    }.start();

                }

            }
        }
        else{
            if (habilitarSensor) {
                if (event.values[SensorManager.DATA_X] < -7) {
                    posTitulo++;
                    titulo.setText(titulos[Math.abs(posTitulo) % 4]);
                    cargarPuestos();
                    MediaPlayer mp = MediaPlayer.create(this, R.raw.bo);
                    mp.start();
                    habilitarSensor = false;
                    reloj = new CountDownTimer(2000, 500) {
                        public void onTick(long millisUntilFinished) {
                        }

                        public void onFinish() {
                            habilitarSensor = true;
                        }
                    }.start();

                }
                if (event.values[SensorManager.DATA_X] > 7) {
                    posTitulo--;
                    titulo.setText(titulos[Math.abs(posTitulo) % 4]);
                    cargarPuestos();
                    MediaPlayer mp = MediaPlayer.create(this, R.raw.bo);
                    mp.start();
                    habilitarSensor = false;
                    reloj = new CountDownTimer(2000, 500) {
                        public void onTick(long millisUntilFinished) {
                        }

                        public void onFinish() {
                            habilitarSensor = true;
                        }
                    }.start();
                }
            }


        }
    }

    private void vaciarPuestos() {
        nombrePrimero.setText("");
        puntosPrimero.setText("0");
        fotoPrimero.setVisibility(View.INVISIBLE);

        nombreSegundo.setText("");
        puntosSegundo.setText("0");
        fotoSegundo.setVisibility(View.INVISIBLE);

        nombreTercero.setText("");
        puntosTercero.setText("0");
        fotoTercero.setVisibility(View.INVISIBLE);

    }


    private void cargarPuestos() {
        vaciarPuestos();
        Ranking r=(new DataBaseManager(getApplicationContext())).getRanking();
        Puestos p=null;
        BitmapWorkerTask task;
        String res;
        if(!sensorProximidadDisponible){
            posTitulo=Math.abs(posTitulo);
        }
        switch (posTitulo % 4){
            case 0: p=r.getMatematica();
                break;
            case 1: p=r.getNaturales();
                break;
            case 2: p=r.getLengua();
                break;
            case 3:p=r.getMix();
                break;

        }

        if(p.getPrimeroPuntos()!=0){
            puntosPrimero.setText(Integer.toString(p.getPrimeroPuntos()));
            nombrePrimero.setText(p.getPrimeroJugador().getNombre());
            fotoPrimero.setVisibility(View.VISIBLE);
            task = new BitmapWorkerTask(fotoPrimero, getResources(),p.getPrimeroJugador().getTieneFoto());
            if(p.getPrimeroJugador().getTieneFoto() == 1){res= Environment.getExternalStorageDirectory() + "/fotosJugadores/" + p.getPrimeroJugador().getNombreFoto();}
            else{res=p.getPrimeroJugador().getNombreFoto();}
            task.execute(res);

            if(p.getSegundoPuntos()!=0){
                puntosSegundo.setText(Integer.toString(p.getSegundoPuntos()));
                nombreSegundo.setText(p.getSegundoJugador().getNombre());
                fotoSegundo.setVisibility(View.VISIBLE);
                task = new BitmapWorkerTask(fotoSegundo, getResources(),p.getSegundoJugador().getTieneFoto());
                if(p.getSegundoJugador().getTieneFoto() == 1){res= Environment.getExternalStorageDirectory() + "/fotosJugadores/" + p.getSegundoJugador().getNombreFoto();}
                else{res=p.getSegundoJugador().getNombreFoto();}
                task.execute(res);

                if(p.getTerceroPuntos()!=0){
                    puntosTercero.setText(Integer.toString(p.getTerceroPuntos()));
                    nombreTercero.setText(p.getTerceroJugador().getNombre());
                    fotoTercero.setVisibility(View.VISIBLE);
                    task = new BitmapWorkerTask(fotoTercero, getResources(),p.getTerceroJugador().getTieneFoto());
                    if(p.getTerceroJugador().getTieneFoto() == 1){res= Environment.getExternalStorageDirectory() + "/fotosJugadores/" +p.getTerceroJugador().getNombreFoto();}
                    else{res=p.getTerceroJugador().getNombreFoto();}
                    task.execute(res);

                }
            }
        }



    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.exitRanking){
            Intent intent = new Intent(RankingActivity.this, CategoriasActivity.class);
            intent.putExtra("jugador", j);
            startActivity(intent);
            this.finish();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(sensorProximidadDisponible){
            sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY), SensorManager.SENSOR_DELAY_NORMAL);
        }
        else {
            sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        }

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


}
