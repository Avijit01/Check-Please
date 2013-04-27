package com.example.checkplease;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Clase que maneja la crecaion de la lista de Detalles
 */
public class DetallesAdapter extends BaseAdapter {
    
    private Activity activity;
    private List<String> data = new ArrayList<String>();
    private static LayoutInflater inflater=null;

    /**
     * Metodo contructor del adater
     */
    public DetallesAdapter(Activity a, List<String> d) {
        activity = a;
        for( int i = 0; i < d.size(); i++ ){
        	data.add( d.get(i) );
        }
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    /**
     * Metodo que regresa el tamaño de la lista
     * @int
     * @
     */
    public int getCount() {
        return data.size();
    }
    /**
     * Metodo que regresa el objeto de la position deseada
     * @return Object 
     * @param position
     */
    public Object getItem(int position) {
        return position;
    }
    /**
     * Metodo que regresa el id de la posicion deseada
     * @return long
     * @param position
     */
    public long getItemId(int position) {
        return position;
    }
    
    /**
     * Metodo que genera la vista en cada esacio de la lista
     * @return View
     *  @param position
     *   @param View
     *    @param ViewGroup
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.detalles_item, null);
        
        EditText text=(EditText)vi.findViewById(R.id.precio);
        text.setTextColor(Color.parseColor("#787878"));
        text.setText(data.get(position));
        return vi;
    }
}