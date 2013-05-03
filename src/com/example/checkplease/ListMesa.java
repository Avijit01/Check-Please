package com.example.checkplease;

import java.util.ArrayList;
import java.util.List;

import com.example.checkplease.libreria.UserFunctions;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ListMesa extends Activity implements OnItemClickListener, OnClickListener{
	
	UserFunctions userFunctions = new UserFunctions();//carga la case userFunctions

	private List<String> totales = new ArrayList<String>(); //lista de precios en la lista
	private List<String> restaurantes = new ArrayList<String>(); //lista de precios en la lista
	private List<String> idMesas = new ArrayList<String>();
	private DetallesAdapter adapter;//adapter de la lista de productos
	private ListView l; //vista de la lista
	private Button regrear; // boton  agregar y terminar de la vista  detalles
	
	@Override
	/**
	 * Metodo que maneja los  datos y eventos  de la actividad Detalles
	 * @return void
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_mesa);
		
		regrear = (Button)findViewById(R.id.agregar);
		
		/*HashMap<String, String> useractual = userFunctions.getUsuarioId(getApplicationContext());
		JSONObject json = userFunctions.obtenerMesasUsuario((String)useractual.get("uid"));
		JSONArray jArray;
		try {
			jArray = json.getJSONArray("mesasUsuario");
			for(int i=0;i<jArray.length();i++){
				JSONObject json_data = jArray.getJSONObject(i);
				//json_data.getInt("idMesa");
				//json_data.getString("restaurante");
				//json_data.getDouble("total");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		idMesas.add("0");
		idMesas.add("1");
		restaurantes.add("KFC");
		restaurantes.add("Pizza Hutt");
		totales.add("100.00");
		totales.add("235.50");
		//se declara la lista asociada con la lista del layout
		l = (ListView) findViewById(R.id.mesasList);
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
		regrear.setOnClickListener(new  View.OnClickListener(){
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
	public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
		String idmesa = adapter.getMesaId(pos);
		Intent intent = new Intent(view.getContext(), Mesa.class);
		startActivity(intent);
	}

}
