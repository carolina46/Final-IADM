package com.interfacesmoviles.carolinaperez;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class InicioActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        //ORIENTACION PANTALLA
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //ASIGNACION DE LISTENER
        Button instrucciones=(Button) findViewById(R.id.instrucciones);
        instrucciones.setOnClickListener(this);

        Button jugar=(Button) findViewById(R.id.jugar);
        jugar.setOnClickListener(this);

        Button creditos=(Button) findViewById(R.id.creditos);
        creditos.setOnClickListener(this);
        //TAMAÑO DE PANTALLA
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width=dm.widthPixels;

        //TAMAÑO LOGO
        LinearLayout logo=(LinearLayout) findViewById(R.id.logoInicio);
        logo.getLayoutParams().height=width-20;
        logo.getLayoutParams().width=width-20;

        //TAMAÑO BOTONES
        RelativeLayout jugarLayout=(RelativeLayout) findViewById(R.id.jugarLayout);
        jugarLayout.getLayoutParams().height=width*1/5;
        jugarLayout.getLayoutParams().width=width*1/5;

        RelativeLayout instruccionesLayout=(RelativeLayout) findViewById(R.id.instruccionesLayout);
        instruccionesLayout.getLayoutParams().height=width*1/6;
        instruccionesLayout.getLayoutParams().width=width*1/6;

        RelativeLayout creditosLayout=(RelativeLayout) findViewById(R.id.creditosLayout);
        creditosLayout.getLayoutParams().height=width*1/6;
        creditosLayout.getLayoutParams().width=width*1/6;


    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.instrucciones:
                intent = new Intent(InicioActivity.this, InstruccionesActivity.class);
                startActivity(intent);
                this.finish();
                break;
            case R.id.jugar:
                intent = new Intent(InicioActivity.this, MainActivity.class);
                startActivity(intent);
                this.finish();
                break;
            case R.id.creditos:
                intent = new Intent(InicioActivity.this, CreditosActivity.class);
                startActivity(intent);
                this.finish();
                break;
        }
    }
}
