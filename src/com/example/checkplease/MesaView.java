package com.example.checkplease;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.checkplease.libreria.UserFunctions;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.OnNavigationListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * Clase que maneja la vista de Mess  seleccionada por el usuario 
 * @author Cesar Amaro
 *
 */
public class MesaView extends Activity implements OnItemClickListener, OnClickListener{
	
	UserFunctions userFunctions = new UserFunctions();//carga la case userFunctions

	private List<Mesa> usrMesa = new ArrayList<Mesa>(); //lista de precios en la lista
	private MesaViewAdapter adapter;//adapter de la lista de productos
	private ListView l; //vista de la lista
	private Button cerrar; // boton  agregar y terminar de la vista  detalles
	private TextView total; 
	private int idMesa = 0;
	private String totalS = "";
	private String nombrePref = "";
	private String path = "";
	private int position = 0;
	private String idUsr = "";
	private String vieneDe = "";
	private int paid = 0;
	
	@Override
	/**
	 * Metodo que maneja los  datos y eventos  de la actividad Detalles
	 * @return void
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mesa);
		
		cerrar = (Button)findViewById(R.id.cerrarMesa);
		total = (TextView)findViewById(R.id.tvgTotal);
		
		//Recoleta  los parametros recibidos de la vista Lista
		Bundle extras = getIntent().getExtras(); //si tiene parametos que envio la actividad Main
		if(extras !=null){//se agarra el parametro "position" y se le asigna la variable post
			idMesa = extras.getInt("IdMesa");
			if(extras.getString("viene").equals("detalles")){
				totalS = "" + extras.getFloat("Total");
				Log.e("total en mesaVie",":"+totalS);
				nombrePref = extras.getString("Nombre");
				path = extras.getString("Picture");
				position = extras.getInt("Position");
				idUsr = extras.getString("IdUsr");
				//if( extras.getBoolean("Paid") ) paid = 1; else paid = 0;
			}else if(extras.getString("viene").equals("mesas")){
				vieneDe = "mesas";
			}
		}
		JSONObject json = new JSONObject();
		JSONArray jArray;
		try {
			json = userFunctions.obtenerUsuarioMesa(idMesa);
			if (json.getString("success") != null) {
				String res = json.getString("success"); 
				if(Integer.parseInt(res) == 1){
					jArray = json.getJSONArray("usuariosMesa");
					Log.e("dimension","==="+jArray.length());
					for(int i=0;i<jArray.length();i++){
						JSONObject json_data = jArray.getJSONObject(i);
						//usrMesa.add(new Mesa(0, 1, json_data.getString("nombre"), (float)json_data.getDouble("total")*(1+(float)json_data.getDouble("propina")/100), 1, "null"));		
						usrMesa.add(new Mesa(0, 1, json_data.getString("nombre"), (float)json_data.getDouble("total"),(float)json_data.getDouble("total")*((float)json_data.getDouble("propina")/100), 1, "null"));		
					}}
				}
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		//HashMap<String, String> useractual = userFunctions.getUsuarioId(getApplicationContext());
		//JSONObject json = userFunctions.obtenerMesasUsuario((String)useractual.get("uid"));
		//JSONArray jArray;
		//try {
		//	jArray = json.getJSONArray("mesasUsuario");
		//	for(int i=0;i<jArray.length();i++){
		//		JSONObject json_data = jArray.getJSONObject(i);
		//		json_data.getInt("idMesa");
		//		json_data.getString("restaurante");
		//		json_data.getDouble("total");
		//	}
		//} catch (JSONException e) {
		//	// TODO Auto-generated catch block
		//	e.printStackTrace();
		//}
		//se declara la lista asociada con la lista del layout
		l = (ListView) findViewById(R.id.lvUsuarios);
		//se crea el adapter para llenar los elemtnos de la lista con los datos de frutas
		adapter = new MesaViewAdapter(this, usrMesa);
		//se agrega los elementos a la lista
		l.setAdapter( adapter );
		//se habilita el evente OnCLick en cada elemto de la lista
		l.setOnItemClickListener(this);
		
		float t = 0;
		for( int i = 0; i < usrMesa.size(); i++ ){
			t+=usrMesa.get(i).getTotal();
		}
		total.setText(t+"");

		/**
		 * Metodo del evento OnClick que se asgina al boton terminar que regresa a Lista
		 * @return void
		 */
		cerrar.setOnClickListener(new  View.OnClickListener(){
			public void onClick(View view){
				if(!vieneDe.equals("")){
					finish();
				}else{
					Log.e("total al salir de mesaview",":"+totalS);

				Intent intent = new Intent(view.getContext(), Detalles.class);
				intent.putExtra("viene","mesas");
				intent.putExtra("Total",totalS);
				intent.putExtra("Nombre",nombrePref);
				intent.putExtra("Picture",path);
				intent.putExtra("Position",position);
				intent.putExtra("IdUsr",idUsr);
				//intent.putExtra("Paid",paid);
				startActivity(intent);
				finish();}
			}
		});
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
		//Intent intent = new Intent(view.getContext(), Mesa.class);
		//startActivity(intent);
	}
	
	/**
	 * Metodo: facebook,
	 * Metodo que realiza la accion de abrir la actividad de Facebook
	 */
	private void facebook() {
		startActivity(new Intent(this, Facebook.class));
	}
	/**
	 * Metodo: Inicio,
	 * Metodo que realiza la accion de abrir la actividad de Inicio
	 */
	private void Inicio() {
		startActivity(new Intent(this, MainActivity.class));
	}
	/**
	 * Metodo: Acerca,
	 * Metodo que realiza la accion de abrir la actividad de Acerca
	 */
	private void Acerca(){
		startActivity(new Intent(this, Acerca.class));

	}
	
	/**
	 * Metodo: cargaMenu(),
	 * Metodo que personaliza la vista del ActionBar con el color, titulo, y opciones
	 */
	void cargaMenu(){
		ActionBar actionBar = getActionBar();//obtiene el ActionBar
		actionBar.setDisplayHomeAsUpEnabled(false);//habilita la opcion de regresar a la actividad anterios
		actionBar.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.bar_color));//pone color gris
		actionBar.setTitle("Detalles Mesa");//pone el titulo

		ArrayList<String> actions = new ArrayList<String>();//arreglo que guardara las acciones de menu del action bar
		//agrega las opciones al menu
		actions.add("Opciones");
		actions.add("Cerrar Sesion");
		actions.add("Facebook");
		actions.add("Acerca");
		//Crea el adaptar del dropDown del header
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, actions);
		//Habilita la navegacion del DropDown del action bar
		getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		//Degine la navegacion del dropdown

		ActionBar.OnNavigationListener navigationListener = new OnNavigationListener() {

			@Override
			public boolean onNavigationItemSelected(int itemPosition, long itemId) {				
				if(itemPosition==1){//opcion de cerrar cesion
					userFunctions.logoutUser(getApplicationContext());
					Inicio();
					return true;
				}
				if(itemPosition==2){//opcion de facebook
					facebook();
					return true;	
				}
				if(itemPosition==3){//opcion de acerca
					Acerca();
				}
				return false;
			}
		};
		//set los elementos del dropdown del actionbar
		getActionBar().setListNavigationCallbacks(adapter, navigationListener); 

	}
	/**
	 * Metodo: onResume,
	 * Metodo que se manda llamar al regresar a la activadad desde otra activdad o desde otra app
	 * carga nuevamente el Menu para reinicar los valores en cero
	 */
	@Override
	protected void onResume() {
		super.onResume();
		cargaMenu();
		// Normal case behavior follows
	}
	

}
