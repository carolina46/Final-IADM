package com.interfacesmoviles.carolinaperez;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PuntosJugadorActivity extends AppCompatActivity {

    private Typeface TF;
    private Jugador j;
    private AppCompatActivity activity=this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puntos_jugador);
        //TAMAÑO DE PANTALLA
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width=dm.widthPixels;

        j=(Jugador) getIntent().getExtras().getSerializable("jugador");

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        String font_path = "fonts/sketchMatch.ttf";
        TF = Typeface.createFromAsset(getAssets(),font_path);

        //JUGADOR
        RelativeLayout cabecera=(RelativeLayout)findViewById(R.id.cabeceraPuntajes);
        cabecera.getLayoutParams().height=width*3/5;

        FrameLayout layoutFoto=(FrameLayout) findViewById(R.id.fotoColor);
        layoutFoto.getLayoutParams().height=(width*2/5);
        layoutFoto.getLayoutParams().width=(width*2/5);

        ImageView foto=(ImageView) findViewById(R.id.foto);
        foto.getLayoutParams().height=(width*2/8);
        foto.getLayoutParams().width=(width*2/8);

        TextView nombre=(TextView) findViewById(R.id.nombre);
        nombre.setTextSize(width*1/20);

        //NOMBRE JUGADOR
        TextView txtTitle = (TextView) findViewById(R.id.nombre);
        txtTitle.setTypeface(TF);
        txtTitle.setText(j.getNombre());

        //FOTO JUGADOR
        ImageView imageView = (ImageView) findViewById(R.id.foto);
        BitmapWorkerTask task = new BitmapWorkerTask(imageView, getResources(),j.getTieneFoto());
        String res;
        if(j.getTieneFoto() == 1){
            res= Environment.getExternalStorageDirectory() + "/fotosJugadores/" + j.getNombreFoto();
        }
        else{
            res=j.getNombreFoto();
        }
        task.execute(res);


        //TITULO PUNTUACION
        TextView puntuacionesMaximas=(TextView) findViewById(R.id.maximaJugadorPuntajes);
        puntuacionesMaximas.setTypeface(TF);
        puntuacionesMaximas.setTextSize(width * 1 / 22);

        //PUNTUACION MATEMATICA
        TextView puntosMatematicaTitulo=(TextView) findViewById(R.id.puntuacionMateTitulo);
        puntosMatematicaTitulo.setTypeface(TF);
        puntosMatematicaTitulo.setTextSize(width * 1 / 28);
        puntosMatematicaTitulo.getLayoutParams().width=width/2;
        TextView puntosMatematica=(TextView) findViewById(R.id.puntuacionMate);
        puntosMatematica.setTypeface(TF);
        puntosMatematica.setTextSize(width * 1 / 28);
        puntosMatematica.setText(Integer.toString(j.getPuntosMatematica()));
        //PUNTUACION NATURALES
        TextView puntosNaturalesTitulo=(TextView) findViewById(R.id.puntuacionNatuTitulo);
        puntosNaturalesTitulo.setTypeface(TF);
        puntosNaturalesTitulo.setTextSize(width * 1 / 28);
        puntosNaturalesTitulo.getLayoutParams().width=width/2;
        TextView puntosNaturales=(TextView) findViewById(R.id.puntuacionNatu);
        puntosNaturales.setTypeface(TF);
        puntosNaturales.setTextSize(width * 1 / 28);
        puntosNaturales.setText(Integer.toString(j.getPuntosNaturales()));
        //PUNTUACION LENGUA
        TextView puntosLenguaTitulo=(TextView) findViewById(R.id.puntuacionLenTitulo);
        puntosLenguaTitulo.setTypeface(TF);
        puntosLenguaTitulo.setTextSize(width * 1 / 28);
        puntosLenguaTitulo.getLayoutParams().width=width/2;
        TextView puntosLengua=(TextView) findViewById(R.id.puntuacionLen);
        puntosLengua.setTypeface(TF);
        puntosLengua.setTextSize(width * 1 / 28);
        puntosLengua.setText(Integer.toString(j.getPuntosLengua()));
        //PUNTUACION MIX
        TextView puntosMixTitulo=(TextView) findViewById(R.id.puntuacionMixTitulo);
        puntosMixTitulo.setTypeface(TF);
        puntosMixTitulo.setTextSize(width * 1 / 28);
        puntosMixTitulo.getLayoutParams().width=width/2;
        TextView puntosMix=(TextView) findViewById(R.id.puntuacionMix);
        puntosMix.setTypeface(TF);
        puntosMix.setTextSize(width * 1 / 28);
        puntosMix.setText(Integer.toString(j.getPuntosMix()));
        //BOTON SALIR
        Button salir = (Button) findViewById(R.id.exitFinJuego);
        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PuntosJugadorActivity.this, OpcionesJugadorActivity.class);
                intent.putExtra("jugador", j);
                startActivity(intent);
                activity.finish();

            }
        });
        salir.getLayoutParams().height=width*1/10;
        salir.getLayoutParams().width=width*1/10;

    }
}

