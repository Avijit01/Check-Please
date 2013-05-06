package com.example.checkplease;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Clase que maneja la crecaion de la lista de Detalles
 */
public class MesaViewAdapter extends BaseAdapter {
    
    private Activity activity;
    private List<Mesa> m = new ArrayList<Mesa>();
    private static LayoutInflater inflater=null;

    /**
     * Metodo contructor del adater
     */
    public MesaViewAdapter(Activity a, List<Mesa> usrMesa){
        activity = a;
        for( int i = 0; i < usrMesa.size(); i++ ){
        	m.add( usrMesa.get(i) );
        }
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    /**
     * Metodo que regresa el tamaño de la lista
     * @int
     * @
     */
    public int getCount() {
        return m.size();
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
            vi = inflater.inflate(R.layout.mesa_item, null);
        
        TextView name = (TextView)vi.findViewById(R.id.ibNombreMesa);
        TextView totalUsr=(TextView)vi.findViewById(R.id.tvTotalMesa);
        TextView totalUsrPropina=(TextView)vi.findViewById(R.id.tvTotalPropina);
       // final CheckBox pago=(CheckBox)vi.findViewById(R.id.cbPaidMesa);
        totalUsr.setTextColor(Color.parseColor("#555555"));
        name.setTextColor(Color.parseColor("#555555"));
        totalUsrPropina.setTextColor(Color.parseColor("#555555"));
        totalUsr.setText(m.get(position).getTotal()+"");
        name.setText(m.get(position).getNombre());
        totalUsrPropina.setText(m.get(position).getPropina()+"");
        /*if(m.get(position).getPago() == 1) pago.setChecked(true);
        pago.setOnClickListener(new  View.OnClickListener(){
			public void onClick(View view){
				pago.setChecked(true);
			}
		});*/
        return vi;
    }
}