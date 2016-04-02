package com.interfacesmoviles.carolinaperez;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CreditosActivity extends AppCompatActivity  implements View.OnClickListener {

    private Typeface TF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creditos);
        //ORIENTACION PANTALLA
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //ESTILO LETRAS
        TF = Typeface.createFromAsset(getAssets(), "fonts/sketchMatch.ttf");
        //TITULO
        TextView titulo=(TextView) findViewById(R.id.tituloCreditos);
        titulo.setTypeface(TF);
        //BOTON SALIDA
        Button BE= (Button) findViewById(R.id.exitCreditos);
        BE.setOnClickListener(this);
        //CONTENIDO
        ImageView I=(ImageView) findViewById(R.id.contenidoCreditos);
        I.setImageResource(R.drawable.creditos);

        //TAMAÑO DE PANTALLA
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width=dm.widthPixels;

        //TAMAÑO RELATIVO BOTON SALIDA
        BE.getLayoutParams().height=width*1/7;
        BE.getLayoutParams().width=width*1/7;

        //TAMAÑO RELATIVO TITULO
        titulo.setTextSize(width*1/14);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(CreditosActivity.this, InicioActivity.class);
        startActivity(intent);
        this.finish();

    }
}
