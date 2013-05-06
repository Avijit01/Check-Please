package com.example.checkplease;

import java.util.ArrayList;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * Clase que maneja los elementos y y maneja sus acciones de la vista list_mesa
 * @author Cesar Amaro
 *
 */
public class ListMesa extends Activity implements OnItemClickListener, OnClickListener{
	
	UserFunctions userFunctions = new UserFunctions();//carga la case userFunctions

	private List<String> totales = new ArrayList<String>(); //lista de precios en la lista
	private List<String> restaurantes = new ArrayList<String>(); //lista de precios en la lista
	private List<String> idMesas = new ArrayList<String>();
	private DetallesAdapter adapter;//adapter de la lista de productos
	private ListView l; //vista de la lista
	TextView mensaje, total, restaurante;
	private Button regresar; // boton  agregar y terminar de la vista  detalles
	
	@Override
	/**
	 * Metodo que maneja los  datos y eventos  de la actividad Detalles
	 * @return void
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_mesa);
		cargaMenu();
		regresar = (Button)findViewById(R.id.regresarMesa);
		mensaje = (TextView)findViewById(R.id.mensaje);
		restaurante = (TextView)findViewById(R.id.restauranteTitleMesa);
		total = (TextView)findViewById(R.id.totalTitleMesa);
		
		HashMap<String, String> useractual = userFunctions.getUsuarioId(getApplicationContext());
		JSONObject json = userFunctions.obtenerMesasUsuario((String)useractual.get("uid"));
		JSONArray jArray;
		try {
			String res = json.getString("success"); 
			if(Integer.parseInt(res) == 1){
			jArray = json.getJSONArray("usuariosMesa");
			for(int i=0;i<jArray.length();i++){
				JSONObject json_data = jArray.getJSONObject(i);
				idMesas.add(json_data.getString("mesa"));
				totales.add(json_data.getString("restaurante"));//estan alreves para que se muestren bien
				restaurantes.add(json_data.getString("total"));
			}}else{
			mensaje.setVisibility(RelativeLayout.VISIBLE);
			mensaje.setText("No tienes mesas individuales");
			total.setVisibility(RelativeLayout.INVISIBLE);
			restaurante.setVisibility(RelativeLayout.INVISIBLE);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//se declara la lista asociada con la lista del layout
				l = (ListView) findViewById(R.id.listMesas);
				//se crea el adapter para llenar los elemtnos de la lista con los datos de frutas
				adapter = new DetallesAdapter(this, restaurantes, totales, idMesas);
				//se agrega los elementos a la lista
				l.setAdapter( adapter );
				//se habilita el evente OnCLick en cada elemto de la lista
				l.setOnItemClickListener(this);

		/**
		 * Metodo del evento OnClick que se asgina al boton terminar que regresa a Lista
		 * @return void
		 */
		regresar.setOnClickListener(new  View.OnClickListener(){
			public void onClick(View view){
				Intent intent = new Intent(view.getContext(), Entra.class);
				//intent.putExtra("viene", "detalles");
				startActivity(intent);
				finish();
			}
		});
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	/**
	 * Metodo que maneja el click en cada item de  la lista de esta vista
	 */
	public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
		String idmesa = adapter.getMesaId(pos);
		Log.e("mesa", ":"+Integer.parseInt(idmesa));
		Intent intent = new Intent(view.getContext(), MesaView.class);
		intent.putExtra("viene","mesas");
		intent.putExtra("IdMesa", Integer.parseInt(idmesa));
		startActivity(intent);
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
		actionBar.setTitle("Lista Mesas ");//pone el titulo

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
