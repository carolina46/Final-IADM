package com.interfacesmoviles.carolinaperez;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends Activity {

    private Typeface TF;
    private TextView TV;
    private Button B;
    private GridView GV;
    private List<Jugador> jugadores;
    private ListJugadoresAdapter adapter;
    private Activity activity=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //TAMAÑO DE PANTALLA
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width=dm.widthPixels;
        //POSICION PANTALLA
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //ESTILO LETRAS
        TF = Typeface.createFromAsset(getAssets(),"fonts/sketchMatch.ttf");
        //TITULO
        TV = (TextView)findViewById(R.id.textJugadores);
        TV.setTypeface(TF);
        //BOTON NUEVO JUGADOR
        B=(Button)findViewById(R.id.BotonNuevoJugador);
        B.setTypeface(TF);
        B.setTextSize(width * 1 / 22);
        B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                Intent intent = new Intent(MainActivity.this, NuevoJugador.class);
                intent.putExtra("origen", "main");
                startActivity(intent);
                activity.finish();
            }
        });
        //BOTON SALIR
        B=(Button)findViewById(R.id.buttonExitMain);
        B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vw) {
                Intent intent = new Intent(MainActivity.this, InicioActivity.class);
                startActivity(intent);
                activity.finish();
            }
        });
        //GRID VIEW
        DataBaseManager DBmanager = new DataBaseManager(getApplicationContext());
        jugadores=DBmanager.listJugadores();
        System.out.println(jugadores.size());
        adapter=new ListJugadoresAdapter(this,jugadores,width);
        GV=(GridView)findViewById(R.id.gridJugadores);
        GV.setAdapter(adapter);
        GV.setColumnWidth(width*3/4);
        //EVENTO SIMPLE CLICK
        GV.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Jugador j = ((Jugador) GV.getAdapter().getItem(position));
                Intent intent = new Intent(MainActivity.this, CategoriasActivity.class);
                intent.putExtra("jugador", j);
                startActivity(intent);
                activity.finish();

            }
        });
        //EVENTO LONG CLICK
        GV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
                Jugador j = ((Jugador) GV.getAdapter().getItem(position));
                Intent intent = new Intent(MainActivity.this, OpcionesJugadorActivity.class);
                intent.putExtra("jugador", j);
                startActivity(intent);
                activity.finish();
                return true;

            }
        });











    }
}