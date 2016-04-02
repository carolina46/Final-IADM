package com.interfacesmoviles.carolinaperez;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class CategoriasActivity extends AppCompatActivity implements OnClickListener {


    private Typeface TF;
    private Jugador j;
    private RoundedBitmapDrawable roundedDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias);
        //TAMAÑO DE PANTALLA
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width=dm.widthPixels;
        //ORIENTACION PANTALLA
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //PARAMETRO
        j=(Jugador) getIntent().getExtras().getSerializable("jugador");
        //ESTILO LETRAS
        String font_path = "fonts/sketchMatch.ttf";
        TF = Typeface.createFromAsset(getAssets(), font_path);
        //TITULO
        TextView TVCategorias=(TextView) findViewById(R.id.textCategorias);
        TVCategorias.setTypeface(TF);
        //BOTON MATEMATICA
        Button BM= (Button) findViewById(R.id.buttonMatematica);
        BM.setOnClickListener(this);
        BM.setTypeface(TF);
        BM.setTextSize(width * 1 / 28);
        BM.getLayoutParams().height=((width * 1 / 28)*5);
        BM.getLayoutParams().width=((width * 1 / 28)*17);
        //BOTON NATURALES
        Button BN= (Button) findViewById(R.id.buttonNaturales);
        BN.setOnClickListener(this);
        BN.setTypeface(TF);
        BN.setTextSize(width * 1 / 28);
        BN.getLayoutParams().height=((width * 1 / 28)*5);
        BN.getLayoutParams().width=((width * 1 / 28)*17);
        //BOTON LENGUA
        Button BL= (Button) findViewById(R.id.buttonLengua);
        BL.setOnClickListener(this);
        BL.setTypeface(TF);
        BL.setTextSize(width * 1 / 28);
        BL.getLayoutParams().height=((width * 1 / 28)*5);
        BL.getLayoutParams().width=((width * 1 / 28)*17);
        //BOTON MIX
        Button BMix= (Button) findViewById(R.id.buttonMix);
        BMix.setOnClickListener(this);
        BMix.setTypeface(TF);
        BMix.setTextSize(width * 1 / 28);
        BMix.getLayoutParams().height=((width * 1 / 28)*5);
        BMix.getLayoutParams().width=((width * 1 / 28)*17);
        //BOTON SALIDA
        Button BExit= (Button) findViewById(R.id.buttonExitCategoria);
        BExit.setOnClickListener(this);
        BExit.getLayoutParams().height=((width * 1 / 7));
        BExit.getLayoutParams().width=((width * 1 / 7));
        //BOTON RANKING
        Button BRanking= (Button) findViewById(R.id.buttonRanking);
        BRanking.setOnClickListener(this);
        BRanking.getLayoutParams().height=((width * 1 / 7));
        BRanking.getLayoutParams().width=((width * 1 / 7));
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonMatematica: nuevoJuego(1,"Matemática");
                break;
            case R.id.buttonNaturales: nuevoJuego(3,"Naturales");
                break;
            case R.id.buttonLengua: nuevoJuego(2,"Lengua");
                break;
            case R.id.buttonMix: nuevoJuego(4,"Mix");
                break;
            case R.id.buttonExitCategoria:
                Intent intent = new Intent(CategoriasActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.buttonRanking:
                Intent intent1 = new Intent(CategoriasActivity.this, RankingActivity.class);
                intent1.putExtra("jugador", j);
                startActivity(intent1);
                break;


        }
    }




    public void nuevoJuego(int id, String nombre){
        Intent intent = new Intent(CategoriasActivity.this, JuegoActivity.class);
        intent.putExtra("jugador", j);
        intent.putExtra("categoriaCodigo", id);
        intent.putExtra("categoriaNombre", nombre);
        startActivity(intent);
    }


}
