package com.example.checkplease;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.checkplease.libreria.DatabaseHandler;
import com.example.checkplease.libreria.UserFunctions;
import com.facebook.Session;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.OnNavigationListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Clase que maneja la vista de forma de pago
 * @author Raul Gomez
 *
 */
public class Entra extends Activity {

	EditText etTotal;
	EditText etPropina;
	EditText etPersonas, restaurante;
	TextView tvPagoPorPersona, mensaje;
	RelativeLayout divIgual;
	UserFunctions userFunctions = new UserFunctions();//carga la case userFunctions
	int numeroMesa = 0, prevNumeroMesa;
	int estaLogeado = 0;//1 cuando este logeado desde antes y entra a la actividad
	String usuario, nombreRestaurante;
	private Button regresa, igual, individual;
	private float total, propina;
	private int personas;
	boolean esIgual = false;
	//DatabaseHandler db = new DatabaseHandler(getApplicationContext());

	/**
	 * Metodo que maneja la funcionalidad de la vista forma de pago
	 * @param Bundle savedInstanceState
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forma_de_pago);

		cargaMenu();
		SharedPreferences prefs = getSharedPreferences("PREFS_KEY",Activity.MODE_PRIVATE); 
		numeroMesa = prefs.getInt("Mesa", 0);
		nombreRestaurante = prefs.getString("RESTAURANTE", "");
		Bundle extras = getIntent().getExtras(); //si tiene parametos que envio la actividad anterios

		if(extras !=null){//si no es nulo
			estaLogeado = extras.getInt("logeado");//toma el valor de 1
			usuario = extras.getString("nombre");

			if(extras.getInt("mesa")!=0)
			{ numeroMesa = extras.getInt("mesa");}
		}
		//Toast.makeText(getApplicationContext(),"la mesa es:"+numeroMesa,Toast.LENGTH_SHORT).show();
		igual = (Button)findViewById(R.id.igual);
		individual = (Button)findViewById(R.id.individual);
		restaurante = (EditText)findViewById(R.id.restaurante);
		restaurante.setTextColor(Color.parseColor("#787878"));
		etTotal = (EditText)findViewById(R.id.total);
		etPropina = (EditText)findViewById(R.id.propina);
		etPersonas = (EditText)findViewById(R.id.personas);
		tvPagoPorPersona = (TextView)findViewById(R.id.pagoPorPersona);
		divIgual = (RelativeLayout)findViewById(R.id.divIgual);
		mensaje = (TextView)findViewById(R.id.textView1);

		divIgual.setVisibility(RelativeLayout.INVISIBLE);

		if(numeroMesa != 0){//si tiene una mesa cargada
			try {//si la respuesta de KEY_Succes contiene algo
				JSONObject json = userFunctions.getInfoMesa(numeroMesa);

				if (json.getString("success") != null) {
					String res = json.getString("success"); 					
					if(Integer.parseInt(res) == 1){//si se accedio
						JSONObject json_mesa = json.getJSONObject("mesa");
						restaurante.setText(json_mesa.getString("restaurante"));
						restaurante.setInputType(InputType.TYPE_NULL);
						mensaje.setText("Tu mesa actual es de manera:");
						if(json_mesa.getInt("tipo")==0){
							individual.setVisibility(RelativeLayout.INVISIBLE);
							divIgual.setVisibility(RelativeLayout.VISIBLE);
							total = (float)json_mesa.getDouble("total");
							propina = (float)json_mesa.getDouble("propina");
							personas = json_mesa.getInt("personas");
							etTotal.setHint(" "+total); 
							etPropina.setHint(" "+propina); 
							etPersonas.setHint(" "+personas);
							getTotalPerPerson();
							
						}else
							igual.setVisibility(RelativeLayout.INVISIBLE);
						//cambiar color a null					
					}else{
						// Error al cargar los datos
						//mensajeError.setText("Usuario y/o contraseña incorrectos");
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}

		igual.setOnClickListener(new  View.OnClickListener(){
			public void onClick(View view){
				Log.e("restaurante",":"+restaurante);
				if(restaurante.getText().toString().equals(""))//si falta el restaurante lo pone en rojo y focus
				{	restaurante.setBackgroundResource(R.drawable.rojo_btn);
					restaurante.requestFocus ();
				}else{//si el numero de mesa es cero
					ActionBar actionBar = getActionBar();
					actionBar.setTitle("Pago igual ");
					etTotal.requestFocus ();
					restaurante.setBackgroundResource(R.drawable.blanco_btn);
					divIgual.setVisibility(view.VISIBLE);
					individual.setVisibility(view.INVISIBLE);
					restaurante.setInputType(InputType.TYPE_NULL);
					if(numeroMesa == 0){
						numeroMesa = agregaRestaurante(restaurante, 0);
						nombreRestaurante = restaurante.getText().toString();
						DatabaseHandler db = new DatabaseHandler(getApplicationContext());
						db.addMesa(usuario, numeroMesa);
					}
					esIgual = true;
				}
			}

		});

		individual.setOnClickListener(new  View.OnClickListener(){
			public void onClick(View view){
				if(restaurante.getText().toString().equals(""))//si falta el restaurante lo pone en rojo y focus
				{	restaurante.setBackgroundResource(R.drawable.rojo_btn);
				restaurante.requestFocus ();
				}else{
					if(numeroMesa == 0){//si el numero de mesa es cero la agrega
						numeroMesa = agregaRestaurante(restaurante,1);
						DatabaseHandler db = new DatabaseHandler(getApplicationContext());
						db.addMesa(usuario, numeroMesa);
					}			
					divIgual.setVisibility(RelativeLayout.INVISIBLE);
					restaurante.setInputType(InputType.TYPE_NULL);
					restaurante.setBackgroundResource(R.drawable.blanco_btn);

					//Llamada a la vista de Lista
					Intent intent = new Intent(view.getContext(), Lista.class);
					intent.putExtra("viene", "entra");
					intent.putExtra("idMesa", numeroMesa);
					intent.putExtra("restaurante", nombreRestaurante);
					if(numeroMesa != prevNumeroMesa)
						intent.putExtra("clearPrefs", true);
					prevNumeroMesa = numeroMesa;
					startActivity(intent);
				}

			}
		});

		//agrega  el total de todos los comensales
		etTotal.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence seq, int start, int before, int count) {
				try {
					total = Float.parseFloat(etTotal.getText().toString());
				} catch(Exception e) {
					total = 0.0f;
				}
				getTotalPerPerson();
			}

			@Override
			public void afterTextChanged(Editable arg0) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}
		});

		//agrega el total de la propina de los comensales
		etPropina.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence seq, int start, int before, int count) {
				try {
					propina = Float.parseFloat(etPropina.getText().toString());
				} catch (Exception e) {
					propina = 0.0f;
				}
				getTotalPerPerson();
			}

			@Override
			public void afterTextChanged(Editable arg0) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}
		});

		//agrega el numero de comensales en la mesa
		etPersonas.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence seq, int start, int before, int count) {
				try {
					personas = Integer.parseInt(etPersonas.getText().toString());
				} catch (NumberFormatException e) {
					personas = 0;
				}
				getTotalPerPerson();
			}

			@Override
			public void afterTextChanged(Editable arg0) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}
		});
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	/**
	 * Metodo onOptionsItemSelected
	 * Defiene las acciones que se tomaran al seleccionar cada menu
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		//respond to menu item selection
		switch (item.getItemId()) {
		case R.id.acerca://se cierra el menu
			startActivity(new Intent(this, Acerca.class));
			return true;	 
		case android.R.id.home://se cierra el menu
			Entra.this.finish();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Calcula el total de cada persona
	 */
	private void getTotalPerPerson() {
		Log.e("entra","calcula"+" "+total+" "+propina+" "+personas);
		float ppp = (total + (total * (propina / 100.0f))) / personas;
		if(!Float.isNaN(ppp) && !Float.isInfinite(ppp))
			tvPagoPorPersona.setText("$" + String.valueOf(ppp) + " por persona");
		esIgual = true;
		
		
	//	updateMesa(total, propina, personas);
	}
	/**
	 * Metodo: agregaRestaurante,
	 * Metodo que realiza la accion de agregar a la base de datos el restaurante, creando la mesa
	 * @param restaurante - EditText del nombre del restaurante
	 * @return boolean 0 si esta en blanco el campo de nombre, 1 tiene algo
	 * si no tiene nada como quiera se crea la mesa para tener un id de la mesa
	 */
	private void updateMesa(float total, float propina, int personas){

		userFunctions.updateMesa(numeroMesa, total, propina, personas);

	}
	/**
	 * Metodo: agregaRestaurante,
	 * Metodo que realiza la accion de agregar a la base de datos el restaurante, creando la mesa
	 * @param restaurante - EditText del nombre del restaurante
	 * @return  idMesa, el id que se creo de la mesa
	 * si no tiene nada como quiera se crea la mesa para tener un id de la mesa
	 */
	private int agregaRestaurante(EditText restaurante, int tipo){
		String restaurante2 = restaurante.getText().toString();
		Log.e("tipo",":"+tipo);
		int idMesa=0;
		if(restaurante.equals("")){
			JSONObject json = userFunctions.agregaRestaurante("-", tipo);
			try {//si la respuesta de KEY_Succes contiene algo
				if (json.getString("success") != null) {
					idMesa = json.getInt("mesa"); 
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
			return idMesa;
		}else{
			JSONObject json = userFunctions.agregaRestaurante(restaurante2, tipo);
			try {//si la respuesta de KEY_Succes contiene algo
				if (json.getString("success") != null) {
					idMesa = json.getInt("mesa");  
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return idMesa;
		}
	}
	/**
	 * Metodo: mesaNueva
	 * Metodo que realiza la accion de  guardar todo lo actual y crear una mesa nueva
	 */
	private void mesaNueva() {
		SharedPreferences prefs = getSharedPreferences("PREFS_KEY",Activity.MODE_PRIVATE); 
		SharedPreferences.Editor editor = prefs.edit();
		editor.clear();
		editor.putInt("MESA", 0);
		editor.commit();
		numeroMesa = 0;
		DatabaseHandler db = new DatabaseHandler(getApplicationContext());
		db.addMesa(usuario, 0);
		restaurante.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
		restaurante.setText("");
		restaurante.requestFocus();
		individual.setVisibility(RelativeLayout.VISIBLE);
		igual.setVisibility(RelativeLayout.VISIBLE);
		divIgual.setVisibility(RelativeLayout.INVISIBLE);
		etTotal.setHint("Total"); 
		etPropina.setHint("Propina"); 
		etPersonas.setHint("Persona");

	}
	
	/**
	/**
	 * Metodo: mesas usuario,
	 * Metodo que realiza la accion de abrir la actividad de MesaView
	 */
	private void mesasUsuario() {
		HashMap<String, String> useractual = userFunctions.getUsuarioId(getApplicationContext());
		Intent intent = new Intent(this, ListMesa.class);
		startActivity(intent);
	}
	/**
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
	/**
	 * Metodo: cargaMenu(),
	 * Metodo que personaliza la vista del ActionBar con el color, titulo, y opciones
	 */
	void cargaMenu(){
		ActionBar actionBar = getActionBar();//obtiene el ActionBar
		if(estaLogeado==0){
			actionBar.setDisplayHomeAsUpEnabled(true);//habilita la opcion de regresar a la actividad anterios
		}else{
			actionBar.setDisplayHomeAsUpEnabled(false);//habilita la opcion de regresar a la actividad anterios
		}
		actionBar.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.bar_color));//pone color gris
		actionBar.setTitle("Forma pago    ");//pone el titulo

		ArrayList<String> actions = new ArrayList<String>();//arreglo que guardara las acciones de menu del action bar
		//agrega las opciones al menu
		actions.add("Opciones");
		actions.add("Cerrar Sesion");
		actions.add("Mesa Nueva");
		actions.add("Mesas Usuario");
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
				if(itemPosition==2){//opcion de mesa nueva
					mesaNueva();
					return true;
				}				
				if(itemPosition==3){//opcion de mesas del usuario
					mesasUsuario();
					return true;
				}
				if(itemPosition==4){//opcion de facebook
					facebook();
					return true;	
				}
				if(itemPosition==5){//opcion de acerca
					Acerca();
				}
				return false;
			}
		};
		//set los elementos del dropdown del actionbar
		getActionBar().setListNavigationCallbacks(adapter, navigationListener); 

	}
	@Override
	/**
	 * Metodo que se activa cuando se sale de esta actividad
	 */
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(esIgual){
			userFunctions.updateMesa(numeroMesa, total, propina, personas);
		}
		SharedPreferences prefs = getSharedPreferences("PREFS_KEY",Activity.MODE_PRIVATE); 
		SharedPreferences.Editor editor = prefs.edit(); 
		editor.putInt("MESA", numeroMesa); 
		editor.putString("RESTAURANTE", nombreRestaurante); 
		editor.commit(); 
	}

}
