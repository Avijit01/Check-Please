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
    private List<String> tot = new ArrayList<String>();
    private List<String> res = new ArrayList<String>();
    private List<String> mes = new ArrayList<String>();
    private static LayoutInflater inflater=null;

    /**
     * Metodo contructor del adater
     */
    public DetallesAdapter(Activity a, List<String> r, List<String> t, List<String> m) {
        activity = a;
        for( int i = 0; i < res.size(); i++ ){
        	res.add( r.get(i) );
        	tot.add( t.get(i) );
        	mes.add( m.get(i) );
        }
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    /**
     * Metodo que regresa el tamaño de la lista
     * @int
     * @
     */
    public int getCount() {
        return res.size();
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
     * Metodo que regresa el id de la mesa seleccionada
     * @return String
     * @param position
     */
    public String getMesaId(int position) {
        return mes.get(position);
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
            vi = inflater.inflate(R.layout.list_mesa_item, null);
        
        TextView to=(TextView)vi.findViewById(R.id.restauranteMesa);
        TextView re=(TextView)vi.findViewById(R.id.totalMesa);
        re.setTextColor(Color.parseColor("#ffffff"));
        to.setTextColor(Color.parseColor("#ffffff"));
        re.setText(res.get(position));
        to.setText(tot.get(position));
        return vi;
    }
}