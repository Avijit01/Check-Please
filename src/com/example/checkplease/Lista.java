package com.example.checkplease;

import com.checkplease.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.checkplease.libreria.UserFunctions;
import com.facebook.FacebookException;
import com.facebook.FacebookRequestError;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;
import com.facebook.widget.FriendPickerFragment;
import com.facebook.widget.PickerFragment;
import com.facebook.widget.ProfilePictureView;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ActionBar.OnNavigationListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Clase que  accesa a los elementos y maneja sus acciones de la vista list_usuarios
 * @author Mario Trujillo
 *
 */
public class Lista extends FragmentActivity  implements OnClickListener {

	private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
	private final String PENDING_ACTION_BUNDLE_KEY = "com.facebook.samples.lista:PendingAction";

	// Definicion de los botones presentes en la vista
	Button regresa;
	Button invitar;
	ImageButton pickFriendsButton;
	ImageButton agregar;
	ImageButton facebook;
	ImageButton eliminar;
	private ProfilePictureView profilePictureView;
	// Variables que maneja la vista para calculos
	SharedPreferences sharedPrefs;
	SharedPreferences.Editor editor;
	ListView layout;
	String amigos = "";
	String restaurante;
	PersonAdapter adapter;
	ArrayList<Person> usuarios;
	EditText etTip;
	TextView tvgTotal;
	TextView tvFalta;
	float gTotal;
	float falta;
	int idMesa = 0;
	int pagado = 0;
	int posicionLista = 0;//al momento de guardar la lista

	int guardaPrimera = 0;
	ArrayList<Integer> positions;
	String seleccionaAmigos = "";//si abrira el popup para selccionar amigos

	private static final int PICK_FRIENDS_ACTIVITY = 1;
	UserFunctions userFunctions = new UserFunctions();//carga la case userFunctions

	private ViewGroup controlsContainer;
	private GraphUser user;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lista_usuarios);

		String[] users;
		String[] usersFacebook;//los usuarios que se obtienen de facebook
		// Arreglo para saber que posiciones se desean eliminar
		positions = new ArrayList<Integer>();
		// Preferencias para guardar la informacion de la aplicacion
		sharedPrefs = getSharedPreferences("Prefs", MODE_PRIVATE);
		editor = sharedPrefs.edit();
		Log.d("Prefs", sharedPrefs.getAll().toString());

		// Informacion enviada por otras actividades
		Bundle extras = getIntent().getExtras();
		//Log.d("Extras", extras.getInt("idMesa")+"");
		// Vistas presentes
		etTip = (EditText)findViewById(R.id.etTip);
		etTip.setTextColor(Color.parseColor("#787878"));
		invitar = (Button)findViewById(R.id.bInvitar);
		agregar = (ImageButton)findViewById(R.id.bAgregar);
		facebook = (ImageButton)findViewById(R.id.bFacebook);
		eliminar = (ImageButton)findViewById(R.id.bEliminar);
		//	  profilePictureView = (ProfilePictureView) findViewById(R.id.profilePicture);

		// Agregar click listener
		agregar.setOnClickListener(this);
		invitar.setOnClickListener(this);
		eliminar.setOnClickListener(this);
		facebook.setOnClickListener(this);
		facebook.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent me) {
				if (me.getAction() == MotionEvent.ACTION_DOWN) {
					facebook.setColorFilter(Color.argb(100, 0, 0, 0));
					dispatchTouchEvent(me);
				} else if (me.getAction() == MotionEvent.ACTION_UP) {
					facebook.setColorFilter(null);
				}
				return false;
			}
		});

		HashMap<String, String> user = userFunctions.getUsuarioId(getApplicationContext());
		idMesa = Integer.parseInt(user.get("mesa"));

		if(extras != null) {
			if(extras.get("clearPrefs") != null && extras.getBoolean("clearPrefs")) {
				editor.clear();
				editor.commit();
			}
		}

		// Parse al string para saber los valores guardados
		users = sharedPrefs.getAll().toString().replaceAll("\\{|\\}", "").split(",.?");

		usuarios = new ArrayList<Person>();
		JSONObject json = new JSONObject();
		JSONArray jArray;
		try {
			json = userFunctions.obtenerUsuarioMesa(idMesa);
			if (json.getString("success") != null) {
				String res = json.getString("success");
				if(Integer.parseInt(res) == 1){
					jArray = json.getJSONArray("usuariosMesa");
					if(jArray.length()==0){
						HashMap<String, String> useractual = userFunctions.getUsuarioId(getApplicationContext());
						HashMap<String, String> user2 = userFunctions.getUsuarioId(getApplicationContext());
						idMesa = Integer.parseInt(user.get("mesa"));
						usuarios.add(new Person(usuarios.size(), (String)useractual.get("name"), 0.0f, false));
						userFunctions.agregaUsuarioMesa(idMesa, (String)useractual.get("name"),Integer.toString(usuarios.size()-1), (String)useractual.get("uid"));
						Person person = usuarios.get(usuarios.size()-1);
						person.setuId((String)useractual.get("uid"));
					}else{
						Log.e("dimension","==="+jArray.length());
						for(int i=0;i<jArray.length();i++){
							JSONObject json_data = jArray.getJSONObject(i);
							boolean pagado2 = false;
							String imagen;
							if(json_data.getInt("pagado")==1){
								pagado2 = true;
							}
							if(json_data.getString("path").equals("path")){
								imagen = "null";
							}else{
								imagen = json_data.getString("path");
							}
							etTip.setText(json_data.getString("propina"));
							Person p = new Person(json_data.getInt("idSistema"), json_data.getString("nombre"), (float)json_data.getDouble("total"), pagado2, imagen);
							p.setuId(json_data.getString("idUsuario"));
							usuarios.add(p);
						}}
				}else{
					if(!users[0].equalsIgnoreCase("")) { // Existe algun usuario en la lista
						Arrays.sort(users);
						for(int i = 0; i < users.length; i++) {
							String s = users[i];
							String[] usr = s.split("=|;");
							Log.d("Usr", s);
							Person p = new Person(Integer.parseInt(usr[0]), usr[1], Float.parseFloat(usr[2]), Boolean.parseBoolean(usr[3]), usr[4]);
							p.setuId("1");
							usuarios.add(p);
						}

					}else {
						//agrega el prime usuario que es la persona que esta logeada
						HashMap<String, String> useractual = userFunctions.getUsuarioId(getApplicationContext());
						HashMap<String, String> user2 = userFunctions.getUsuarioId(getApplicationContext());
						idMesa = Integer.parseInt(user.get("mesa"));    
						usuarios.add(new Person(usuarios.size(), (String)useractual.get("name"), 0.0f, false));
						userFunctions.agregaUsuarioMesa(idMesa, (String)useractual.get("name"),Integer.toString(usuarios.size()-1), (String)useractual.get("uid"));
						Person person = usuarios.get(usuarios.size()-1);
						person.setuId((String)useractual.get("uid"));
						//Person p = new Person(usuarios.size(), "Yo", 0.0f, false, "null");
						//usuarios.add(p);
					}

				}
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Recorre las SharedPreferences y crea el ArrayList con esta informacion


		if(extras != null) {
			if(extras.getString("viene").equals("calculadora")){
				Person person = usuarios.get(extras.getInt("Position"));
				person.setTotal((float)extras.getDouble("totalIndi"));
			}
			if(extras.getString("viene").equals("detalles")){
				Person person = usuarios.get(extras.getInt("Position"));
				//person.setTotal((float)extras.getDouble("totalIndi"));
				//person.setPicture(extras.getString("Path"));
				person.setName(extras.getString("Nombre"));	
				//editor.putString(String.valueOf(person.getId()), person.getName() + ";" + person.getTotal() + ";" + person.isPaid() + ";" + person.getPicture());
				//editor.commit();
			}
			//si viene de la vista de estra
			if(extras.getString("viene").equals("entra")){
				restaurante = extras.getString("restaurante");
				idMesa = extras.getInt("idMesa");
			}
			//si viene de la vista de facebook
			if(extras.getString("viene").equals("facebook")){
				//seleccionaAmigos = extras.getInt("friends");
				seleccionaAmigos = extras.getString("selecciono");
				amigos = extras.getString("amigos");
				if(seleccionaAmigos.equals("")){
					Log.e("entra",":amigos");
					//onClickPickFriends();
				}else{
					usersFacebook = seleccionaAmigos.split(",.?");
					for(int i = 0; i < usersFacebook.length; i++) {
						String s = usersFacebook[i];
						Log.e("Los que selecciono===",s);
						usuarios.add(new Person(usuarios.size(), usersFacebook[i]));
						userFunctions.agregaUsuarioMesa(idMesa, usersFacebook[i], Integer.toString(usuarios.size()-1), Integer.toString(usuarios.size()-1));
						Person person = usuarios.get(usuarios.size()-1);
						person.setuId("1");

					}

				}
			}

		}

		updatePersonAdapter(Float.valueOf(etTip.getText().toString()));
		//agrega el total de la mesa  en la pantalla
		etTip.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence seq, int start, int before, int count) {
				float tip;
				try {
					tip = Float.parseFloat(etTip.getText().toString());
				} catch (Exception e) {
					tip = 0.0f;
				}
				updatePersonAdapter(tip);
			}

			@Override
			public void afterTextChanged(Editable arg0) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}
		});

		//inicializacion de los elemntos total y falta
		tvgTotal = (TextView)findViewById(R.id.tvgTotal);
		tvFalta = (TextView)findViewById(R.id.tvgFalta);

		/*if(extras != null)
			for(Person p : usuarios)
				userFunctions.guardaLista(extras.getInt("mesa"), p.getPicture(), p.getTotal(), p.isPaid());*/
	}

	@Override
	protected void onPause() {
		super.onPause();
		posicionLista = 0;

		for(Person p : usuarios) {
			editor.putString(String.valueOf(p.getId()), p.getName() + ";" + p.getTotal() + ";" + p.isPaid() + ";" + p.getPicture());
			Log.e("pagado",":"+p.isPaid());
			pagado = 1;
			if(!p.isPaid())pagado = 0; //si esta pagado pone uno, sino 0
			Log.e("totalpersona", ":"+p.getTotal());
			userFunctions.guardaLista(idMesa, p.getId(), p.getName(), p.getTotal(), pagado, p.getPicture(), posicionLista);
			Log.d("Uploaded", idMesa + " " + p.getId() + " " + p.getName() + " " + p.getTotal() + " " + pagado + " " + p.getPicture()+" "+posicionLista );
			posicionLista++;
		}
		userFunctions.updateMesa(idMesa, gTotal, Float.parseFloat(etTip.getText().toString()), 0);
		editor.commit();
		finish();
	}

	/**
	 * Metodo para actualizar la lista de usuarios y sus totales
	 * @param tip
	 */
	public void updatePersonAdapter(float tip) {
		adapter = new PersonAdapter(this, R.layout.lista_usuarios_item, usuarios, tip, 0, positions);
		layout = (ListView)findViewById(R.id.lvUsuarios);
		layout.setAdapter(adapter);
	}

	/**
	 *  Actualiza el total de todas las personas
	 *  
	 */
	public void updateTotal() {
		gTotal = 0;
		for(Person p : usuarios)
			//gTotal += p.getTotalTip();
			gTotal += p.getTotal();
		tvgTotal.setText(String.valueOf(gTotal));
	}

	/**
	 *  Actualiza la cantidad que falta por pagar
	 */
	public void updateRemaining() {
		falta = 0;
		for(Person p : usuarios) {
			if(!p.isPaid())
				//falta += p.getTotalTip();
				falta += p.getTotal();
		}
		tvFalta.setText(String.valueOf(falta));
	}

	/**
	 * Metodo: onCreateOptionsMenu(),
	 * Metodo que agrega las opciones que se hicieron en menu->main.xml
	 * @param menu
	 * @return bolean, true se hizo corectamente
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	/**
	 * Metodo que maneja las respuesta de selccionar una aprte del menu o elemento de android
	 *@param item
	 *elemento que se selecciono
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		//respond to menu item selection
		switch (item.getItemId()) {
		case R.id.acerca://si se presiona la opcion de acerca
			startActivity(new Intent(this, Acerca.class));
			return true;
		case android.R.id.home://si se presiona el regresar a la activad actual
			Lista.this.finish();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.bAgregar:
			addPerson();
			break;
		case R.id.bInvitar:
			showInfo();
			break;
		case R.id.bFacebook:

			//Log.e("restaurante",restaurante);
			Intent intent = new Intent(this, Facebook.class);
			intent.putExtra("viene", "postea");
			intent.putExtra("IdMesa", idMesa);
			intent.putExtra("amigos", amigos);
			startActivity(intent);


			break;
		case R.id.bEliminar:
			positions.clear();
			deleteItem();
			break;
		}
	}
	private void postStatusUpdate() {
		Log.d("entra", "ONCLICK");
		if (user != null ) {
			String friends = "623811006";
			final String message = getString(R.string.status_update, user.getFirstName(), (new Date().toString()));
			Bundle postParams = new Bundle();
			postParams.putString("message", message);
			postParams.putString("tags", friends);
			Request request = Request

					.newStatusUpdateRequest(Session.getActiveSession(), message, new Request.Callback() {

						@Override
						public void onCompleted(Response response) {
							showPublishResult(message, response.getGraphObject(), response.getError());
						}
					});
			Log.d("entra", "ONCLICK");

			request.executeAsync();
		} else {
			Log.d("sale", "ONCLICK");

			//pendingAction = PendingAction.POST_STATUS_UPDATE;
		}
	}
	private interface GraphObjectWithId extends GraphObject {
		String getId();
	}
	private void showPublishResult(String message, GraphObject result, FacebookRequestError error) {
		String title = null;
		String alertMessage = null;
		if (error == null) {
			title = getString(R.string.aceptar);
			String id = result.cast(GraphObjectWithId.class).getId();
			alertMessage = getString(R.string.aceptar, message, id);
		} else {
			title = getString(R.string.rechazar);
			alertMessage = error.getErrorMessage();
		}

		new AlertDialog.Builder(this)
		.setTitle(title)
		.setMessage(alertMessage)
		.setPositiveButton(R.string.aceptar, null)
		.show();
	}

	// Agregar una persona a la lista de usuarios
	public void addPerson() {
		//se crea una nueva alerta de dialogo
		AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
		//se le asigna el titulo a la ventana de dialogo
		helpBuilder.setTitle("Agregar");

		//se toma el Layout Inflater
		LayoutInflater inflater = getLayoutInflater();
		//se toma el layout correspondiente a la ventana del pop up
		final View view = inflater.inflate(R.layout.agregar_persona, null);
		final EditText etNombre = (EditText)view.findViewById(R.id.etNombre);
		//se asigna esa vista a la ventana de dialogo
		helpBuilder.setView(view);

		//para manejar la acción del boton OK, de la ventana de dialogo
		helpBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if(!etNombre.getText().toString().equals(""))
					//agrega la persona que se agregoa la base y servidor
					usuarios.add(new Person(usuarios.size(), etNombre.getText().toString().trim()));
				if(userFunctions.agregaUsuarioMesa(idMesa, etNombre.getText().toString().trim(),Integer.toString(usuarios.size()-1), Integer.toString(usuarios.size()-1)) == null) {
					usuarios.remove(usuarios.size()-1);
					Toast toast = Toast.makeText(Lista.this, "Error con la conexión a internet", Toast.LENGTH_LONG);
					toast.show();
				}
				Person person = usuarios.get(usuarios.size()-1);
				person.setuId("1");//pone uno al no estar registrado
			}
		});
		helpBuilder.setNeutralButton("Facebook", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

				Intent intent = new Intent(view.getContext(), Facebook.class);
				intent.putExtra("viene", "Invita");
				startActivity(intent);

			}
		});

		// Se crea la ventana de dialogo
		AlertDialog helpDialog = helpBuilder.create();
		//se muestra la ventana de dialogo
		helpDialog.show();
	}

	/**
	 * Metodo que genera el popup para invitar gente
	 */
	public void showInfo() {
		//se crea una nueva alerta de dialogo
		AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
		//se le asigna el titulo a la ventana de dialogo
		helpBuilder.setTitle("Invitar");

		//se toma el Layout Inflater
		LayoutInflater inflater = getLayoutInflater();
		//se toma el layout correspondiente a la ventana del pop up
		View checkboxLayout = inflater.inflate(R.layout.invitar, null);
		final AutoCompleteTextView buscar = (AutoCompleteTextView) checkboxLayout.findViewById(R.id.sugerencias);
		buscar.setTextColor(Color.parseColor("#787878"));
		ArrayList<String> sugerencia = new ArrayList<String>();//arreglo que guardara las acciones de menu del action bar

		JSONObject json = userFunctions.usuarios();
		JSONArray jArray;
		try {
			jArray = json.getJSONArray("usuarios");
			for(int i=0;i<jArray.length();i++){
				JSONObject json_data = jArray.getJSONObject(i);
				//agrega las opciones al menu
				sugerencia.add(json_data.getString("nombre"));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//agrega las opciones al menu
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, sugerencia);

		buscar.setAdapter(adapter);

		//se asigna esa vista a la ventana de dialogo
		helpBuilder.setView(checkboxLayout);


		//para manejar la acción del boton OK, de la ventana de dialogo
		helpBuilder.setPositiveButton("Ok",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if(!buscar.getText().toString().equals("")){
					//agrega a un usuario existente a la mesa 
					usuarios.add(new Person(usuarios.size(), buscar.getText().toString()));
					JSONObject json = userFunctions.agregaUsuarioMesa(idMesa, buscar.getText().toString(), Integer.toString(usuarios.size()-1),"si");
					try {
						if(json.getString("success") != null) {
							String res = json.getString("success"); 
							if(Integer.parseInt(res) == 1){//si es uno el succes, entro con exito
								//se crea la base de datos interna
								Person person = usuarios.get(json.getInt("id"));
								person.setuId(json.getString("regresa"));
								Log.e("id-regresa","-"+ json.getInt("id")+":"+person.getuId());
							}else{//error en la conexion
							}
						} }catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				}
			}
		});

		// Se crea la ventana de dialogo
		AlertDialog helpDialog = helpBuilder.create();
		//se muestra la ventana de dialogo
		helpDialog.show();
		adapter.notifyDataSetChanged();		

	}

	// Elimina elementos de la lista de usuarios
	public void deleteItem() {
		Log.d("Prefs", sharedPrefs.getAll().toString());
		//se toma el Layout Inflater
		LayoutInflater inflater = getLayoutInflater();
		View lvView = inflater.inflate(R.layout.lista_usuarios_delete, null);
		ListView lvDelete = (ListView)lvView.findViewById(R.id.lvDelete);
		//se crea una nueva alerta de dialogo
		AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
		//se le asigna el titulo a la ventana de dialogo
		helpBuilder.setTitle("Eliminar");

		//se toma el layout correspondiente a la ventana del pop up
		View checkboxLayout = inflater.inflate(R.layout.lista_usuarios_delete, null);
		PersonAdapter deleteAdapter = new PersonAdapter(this, R.layout.lista_usuarios_delete_item,usuarios, 0.0f, 1, positions);
		lvDelete.setAdapter(deleteAdapter);
		//se asigna esa vista a la ventana de dialogo
		helpBuilder.setView(lvView);


		//para manejar la acción del boton OK, de la ventana de dialogo
		helpBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				positions = adapter.getPositions();
				for(Integer i : positions) {
					//usuarios.remove(i.intValue());
					Person person = usuarios.get(i.intValue());
					Log.e("elimina", ":"+i.intValue()+person.getId());

					userFunctions.eliminaUsuarioMesa(idMesa, person.getId());
					usuarios.get(i.intValue()).setId(-1);
				}
				int index = 0;
				for(int i = 0; i < positions.size(); i++) {
					for(Person p : usuarios) {
						if(p.getId() == -1) {
							usuarios.remove(p);
							break;
						}
					}
				}
				editor.clear();
				for(Person p : usuarios) {
					p.setId(index++);
					editor.putString(String.valueOf(p.getId()), p.getName() + ";" + p.getTotal() + ";" + p.isPaid() + ";" + p.getPicture());
				}
				editor.commit();
				Log.d("Prefs", sharedPrefs.getAll().toString());
				adapter.notifyDataSetChanged();
			}
		});

		// Se crea la ventana de dialogo
		AlertDialog helpDialog = helpBuilder.create();
		//se muestra la ventana de dialogo
		helpDialog.show();
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
		actionBar.setDisplayHomeAsUpEnabled(true);//habilita la opcion de regresar a la actividad anterios
		actionBar.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.bar_color));//pone color gris
		actionBar.setTitle("Pago Individ");//pone el titulo

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
}
