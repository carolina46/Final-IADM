package com.interfacesmoviles.carolinaperez;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Carolina on 02/03/2016.
 */
public class ListJugadoresAdapter extends BaseAdapter {

    private final Activity context;
    private final List<Jugador> jugadores;
    private int imageWidth;

    public ListJugadoresAdapter(Activity context, List<Jugador> jugadores, int imageWidth) {
        this.context=context;
        this.jugadores=jugadores;
        this.imageWidth=imageWidth;
    }

    @Override
    public int getCount() {
        return this.jugadores.size();
    }

    @Override
    public Object getItem(int position) {
        return this.jugadores.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public List<Jugador> getElements(){
        return this.jugadores;
    }


    public View getView(int position, View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.jugador, null, true);

        FrameLayout layoutFoto=(FrameLayout) rowView.findViewById(R.id.fotoColor);
        layoutFoto.getLayoutParams().height=(imageWidth*3/4)/2;
        layoutFoto.getLayoutParams().width=(imageWidth*3/4)/2;

        ImageView foto=(ImageView) rowView.findViewById(R.id.foto);
        foto.getLayoutParams().height=(imageWidth*3/7)/2;
        foto.getLayoutParams().width=(imageWidth*3/7)/2;

        TextView nombre=(TextView) rowView.findViewById(R.id.nombre);
        nombre.setTextSize(imageWidth*1/20);


        //ESTILO DE LA FUENTE
        String font_path = "fonts/sketchMatch.ttf";
        Typeface TF = Typeface.createFromAsset(context.getAssets(), font_path);

        //UN JUGADOR
        Jugador j= jugadores.get(position);

        //NOMBRE JUGADOR
        TextView txtTitle = (TextView) rowView.findViewById(R.id.nombre);
        txtTitle.setTypeface(TF);
        txtTitle.setText(j.getNombre());



        //FOTO JUGADOR
        ImageView imageView = (ImageView) rowView.findViewById(R.id.foto);
        BitmapWorkerTask task = new BitmapWorkerTask(imageView, context.getResources(),j.getTieneFoto());
        String res;
        if(j.getTieneFoto() == 1){
            res= Environment.getExternalStorageDirectory() + "/fotosJugadores/" + j.getNombreFoto();
        }
        else{
            res=j.getNombreFoto();
        }
        task.execute(res);

        return rowView;

    };
}
