package com.interfacesmoviles.carolinaperez;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class InstruccionesActivity extends AppCompatActivity implements View.OnClickListener {

    private Typeface TF;
    private int instruccionActual=0;
    private FrameLayout FrameBN;
    private FrameLayout FrameBP;
    private ImageView I;
    private SensorManager sensorManager = null;
    private Sensor sensorDeProximidad = null;
    private boolean sensorProximidadDisponible=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instrucciones);
        //ORIENTACION PANTALLA
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //ESTILO LETRAS
        TF = Typeface.createFromAsset(getAssets(), "fonts/sketchMatch.ttf");
        //TITULO
        TextView titulo=(TextView) findViewById(R.id.tituloInstrucciones);
        titulo.setTypeface(TF);
        //BOTON ANTERIOR
        Button BP= (Button) findViewById(R.id.previous);
        BP.setOnClickListener(this);

        //BOTON SIGUIENTE
        Button BN= (Button) findViewById(R.id.next);
        BN.setOnClickListener(this);
        //BOTON SALIDA
        Button BE= (Button) findViewById(R.id.exitInstrucciones);
        BE.setOnClickListener(this);
        //FRAME BOTON SIGUIENTE
        FrameBN=(FrameLayout) findViewById(R.id.frameNext);
        //FRAME BOTON ANTERIOR
        FrameBP=(FrameLayout) findViewById(R.id.framePrevious);
        //PRIMERA INSTRUCCION
        FrameBP.setVisibility(View.INVISIBLE);
        I=(ImageView) findViewById(R.id.contenidoInstrucciones);
        I.setImageResource(R.drawable.iniciar_juego);
        //SENSORES
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorDeProximidad = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        if(sensorDeProximidad != null){sensorProximidadDisponible=true;}

        //TAMAÑO DE PANTALLA
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width=dm.widthPixels;

        //TAMAÑO RELATIVO TITULO
        titulo.setTextSize(width * 1 / 21);

        //TAMAÑO RELATIVO BOTON SALIDA
        BE.getLayoutParams().height=width*1/7;
        BE.getLayoutParams().width=width*1/7;

        //TAMAÑO RELATIVO BOTON ANTERIOR
        BP.getLayoutParams().height=width*1/8;
        BP.getLayoutParams().width=width*1/12;

        //TAMAÑO RELATIVO BOTON SIGUIENTE
        BN.getLayoutParams().height=width*1/8;
        BN.getLayoutParams().width=width*1/12;





    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.previous:
                instruccionActual=instruccionActual-1;

                break;
            case R.id.next:
                instruccionActual=instruccionActual+1;

                break;
            case R.id.exitInstrucciones:
                Intent intent = new Intent(InstruccionesActivity.this, InicioActivity.class);
                startActivity(intent);
                this.finish();
                break;
        }

        //DETERMINA INSTRUCCION A MOSTRAR
        switch (instruccionActual) {
            case 0:
                FrameBP.setVisibility(View.INVISIBLE);
                I.setImageResource(R.drawable.iniciar_juego);
                break;
            case 1:
                FrameBP.setVisibility(View.VISIBLE);
                I.setImageResource(R.drawable.select_respuesta);
                break;
            case 2:
                I.setImageResource(R.drawable.next_pregunta);
                break;
            case 3:
                FrameBN.setVisibility(View.VISIBLE);
                if(sensorProximidadDisponible){ I.setImageResource(R.drawable.ranking_instruccion);}
                else{ I.setImageResource(R.drawable.ranking_instruccion2);}
                break;
            case 4:
                FrameBN.setVisibility(View.INVISIBLE);
                I.setImageResource(R.drawable.press);
                break;
        }
    }
}
