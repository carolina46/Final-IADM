package com.interfacesmoviles.carolinaperez;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NuevoJugador extends AppCompatActivity {

    private Typeface TF;
    private TextView TV;
    private EditText ET;
    private Button BG;
    private Button BC;
    private Button BF;
    private ImageView IV;
    private String nombreFoto="";
    private boolean tieneFoto=false;
    private String origen;
    private Jugador jugador;
    private AppCompatActivity activity=this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_jugador);
        //TAMAÑO DE PANTALLA
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width=dm.widthPixels;
        //PARAMETRO
        origen=(String) getIntent().getExtras().getSerializable("origen");
        if(origen.equals("editar")){jugador=(Jugador)getIntent().getExtras().getSerializable("jugador");}
        //ORIENTACION PANTALLA
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //ESTILO LETRAS
        TF = Typeface.createFromAsset(getAssets(), "fonts/sketchMatch.ttf");
        //TITULO
        TV = (TextView)findViewById(R.id.textNuevoJugador);
        TV.setTypeface(TF);
        if(origen.equals("editar")){TV.setText("EDITAR JUGADOR");}
        //IMAGEN JUGADOR
        IV=(ImageView)findViewById(R.id.imageViewFoto);
        IV.getLayoutParams().height=width/2;
        IV.getLayoutParams().width=width/2;
        IV.setImageResource(R.drawable.avatar);
        if(origen.equals("editar")){
            if(jugador.getTieneFoto()==1){
                Bitmap bMap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() +"/fotosJugadores/" + jugador.getNombreFoto() );
                IV.setImageBitmap(bMap);
            }

        }

        //BOTON FOTO
        BF=(Button)findViewById(R.id.buttonTomarFoto);
        BF.getLayoutParams().height=width/5;
        BF.getLayoutParams().width=width/5;
        BF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creamos el Intent para llamar a la Camara
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                //Creamos una carpeta en la memeria del terminal
                File imagesFolder = new File(Environment.getExternalStorageDirectory(), "fotosJugadores");
                imagesFolder.mkdirs();
                //añadimos el nombre de la imagen
                nombreFoto = (new SimpleDateFormat("yyyymmddhhmmss")).format(new Date()) + ".jpg";
                File image = new File(imagesFolder, nombreFoto);
                Uri uriSavedImage = Uri.fromFile(image);
                //Le decimos al Intent que queremos grabar la imagen
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
                //Lanzamos la aplicacion de la camara con retorno (forResult)
                startActivityForResult(cameraIntent, 1);
            }
        });
        //CAMPO DEL NOMBRE
        TV = (TextView)findViewById(R.id.textNombreJugador);
        TV.setTypeface(TF);
        ET=(EditText)findViewById(R.id.editNombreJugador);
        ET.setTypeface(TF);
        if(origen.equals("editar")){ET.setText(jugador.getNombre());}
        //BOTON GUARDAR
        BG=(Button)findViewById(R.id.BotonGuardarJugador);
        BG.setTextSize(width * 1 / 26);
        BG.setTypeface(TF);
        BG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ET.getText().length()>0){
                    if(origen.equals("editar")){
                        int cant=0;
                        //MODIFICO FOTO
                        if (tieneFoto) {jugador.setNombreFoto(nombreFoto); cant++;}
                        //MODIFICO NOMBRE
                        if(!jugador.getNombre().equals(ET.getText().toString())){jugador.setNombre(ET.getText().toString()); cant++;}
                        if(cant>0){
                            DataBaseManager DBmanager = new DataBaseManager(getApplicationContext());
                            DBmanager.updateJugador(jugador);
                        }
                        Intent intent = new Intent(NuevoJugador.this, OpcionesJugadorActivity.class);
                        intent.putExtra("jugador", jugador);
                        startActivity(intent);
                        activity.finish();
                    }
                    else {
                        int foto;
                        String nomFoto;
                        if (tieneFoto) {
                            foto = 1;
                            nomFoto = nombreFoto;
                        } else {
                            foto = 0;
                            nomFoto = Integer.toString(R.drawable.avatar);
                        }

                        DataBaseManager DBmanager = new DataBaseManager(getApplicationContext());

                        Jugador j = new Jugador(0, ET.getText().toString(), nomFoto, foto, 0, 0, 0, 0);
                        DBmanager.addJugador(j);

                        Intent intent = new Intent(NuevoJugador.this, MainActivity.class);
                        startActivity(intent);
                        activity.finish();
                    }

                }
                else{
                    Toast.makeText(getApplicationContext(), "Debe agregar un nombre", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //BOTON CANCELAR
        BC=(Button)findViewById(R.id.BotonCancelarJugador);
        BC.setTypeface(TF);
        BC.setTextSize(width * 1 / 26);
        BC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.print(origen);
                if(origen.equals("editar")){
                    System.out.println("entre");
                    Intent intent = new Intent(NuevoJugador.this, OpcionesJugadorActivity.class);
                    intent.putExtra("jugador", jugador);
                    startActivity(intent);
                    activity.finish();
                }
                else {
                    Intent intent = new Intent(NuevoJugador.this, MainActivity.class);
                    startActivity(intent);
                    activity.finish();
                }
            }
        });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Comprovamos que la foto se a realizado
        if (requestCode == 1 && resultCode == RESULT_OK) {
            //Creamos un bitmap con la imagen recientemente
            //almacenada en la memoria
            Bitmap bMap = BitmapFactory.decodeFile(
                    Environment.getExternalStorageDirectory() +
                            "/fotosJugadores/" + this.nombreFoto );
            //Añadimos el bitmap al imageView para
            //mostrarlo por pantalla
            IV.setImageBitmap(bMap);
            tieneFoto=true;
        }
    }

}