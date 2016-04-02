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

public class OpcionesJugadorActivity extends AppCompatActivity implements View.OnClickListener {

    private Jugador jugador;
    private Typeface TF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opciones_jugador);
        //TAMAÑO DE PANTALLA
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width=dm.widthPixels;
        //ORIENTACION PANTALLA
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //PARAMETRO
        jugador=(Jugador) getIntent().getExtras().getSerializable("jugador");
        //ESTILO LETRAS
        TF = Typeface.createFromAsset(getAssets(), "fonts/sketchMatch.ttf");
        //JUGADOR
        FrameLayout layoutFoto=(FrameLayout) findViewById(R.id.fotoColor);
        layoutFoto.getLayoutParams().height=(width*3/5);
        layoutFoto.getLayoutParams().width=(width*3/5);

        ImageView foto=(ImageView) findViewById(R.id.foto);
        foto.getLayoutParams().height=(width*3/8);
        foto.getLayoutParams().width=(width*3/8);

        TextView nombre=(TextView) findViewById(R.id.nombre);
        nombre.setTextSize(width*1/20);

        //NOMBRE JUGADOR
        TextView txtTitle = (TextView) findViewById(R.id.nombre);
        txtTitle.setTypeface(TF);
        txtTitle.setText(jugador.getNombre());

        //FOTO JUGADOR
        ImageView imageView = (ImageView) findViewById(R.id.foto);
        BitmapWorkerTask task = new BitmapWorkerTask(imageView, getResources(),jugador.getTieneFoto());
        String res;
        if(jugador.getTieneFoto() == 1){
            res= Environment.getExternalStorageDirectory() + "/fotosJugadores/" + jugador.getNombreFoto();
        }
        else{
            res=jugador.getNombreFoto();
        }
        task.execute(res);

        //TITULO OPERACIONES
        TextView titulo=(TextView) findViewById(R.id.tituloEdicion);
        titulo.setTypeface(TF);
        titulo.setTextSize(width * 1 / 15);

        //BOTON SALIDA  LAYOUT
        Button salir = (Button) findViewById(R.id.exitEditarJugador);
        salir.setOnClickListener(this);
        salir.getLayoutParams().height=width*1/9;
        salir.getLayoutParams().width=width*1/9;


        //BOTON ELIMINAR JUGADOR
        Button eliminar = (Button) findViewById(R.id.eliminarJugador);
        eliminar.setOnClickListener(this);
        eliminar.getLayoutParams().height=width*1/10;
        eliminar.getLayoutParams().width=width*1/10;

        //BOTON INFORMACION JUGADOR
        Button informacion = (Button) findViewById(R.id.informacionJugador);
        informacion.setOnClickListener(this);
        informacion.getLayoutParams().height=width*1/10;
        informacion.getLayoutParams().width=width*1/10;

        //BOTON EDITAR JUGADOR<aw
        Button editar = (Button) findViewById(R.id.editarJugador);
        editar.setOnClickListener(this);
        editar.getLayoutParams().height=width*1/10;
        editar.getLayoutParams().width=width*1/10;


        RelativeLayout cabeceraOpcionesJugador=(RelativeLayout)findViewById(R.id.cabeceraOpcionesJugador);
        cabeceraOpcionesJugador.getLayoutParams().height=width*3/4;

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.exitEditarJugador:
                intent = new Intent(OpcionesJugadorActivity.this, MainActivity.class);
                startActivity(intent);
                this.finish();
                break;
            case R.id.eliminarJugador:
                DataBaseManager DBmanager = new DataBaseManager(getApplicationContext());
                DBmanager.deleteJugador(jugador.getId());
                intent = new Intent(OpcionesJugadorActivity.this, MainActivity.class);
                startActivity(intent);
                this.finish();
            break;
            case R.id.informacionJugador:
                intent = new Intent(OpcionesJugadorActivity.this, PuntosJugadorActivity.class);
                intent.putExtra("jugador", jugador);
                startActivity(intent);
                break;
            case R.id.editarJugador:
                intent = new Intent(OpcionesJugadorActivity.this, NuevoJugador.class);
                intent.putExtra("origen", "editar");
                intent.putExtra("jugador", jugador);
                startActivity(intent);
                break;


        }

    }
}
