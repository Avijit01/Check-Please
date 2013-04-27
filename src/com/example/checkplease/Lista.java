package com.example.checkplease;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
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

	// Variables que maneja la vista para calculos
	SharedPreferences sharedPrefs;
	SharedPreferences.Editor editor;
	ListView layout;
	PersonAdapter adapter;
	ArrayList<Person> usuarios;
	EditText etTip;
	TextView tvgTotal;
	TextView tvFalta;
	float gTotal;
	float falta;
	ArrayList<Integer> positions;
	int seleccionaAmigos = 0;//si abrira el popup para selccionar amigos

	private static final int PICK_FRIENDS_ACTIVITY = 1;
	UserFunctions userFunctions = new UserFunctions();//carga la case userFunctions

	private ViewGroup controlsContainer;
	private GraphUser user;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lista_usuarios);

		String[] users;
		// Arreglo para saber que posiciones se desean eliminar
		positions = new ArrayList<Integer>();
		// Preferencias para guardar la informacion de la aplicacion
		sharedPrefs = getSharedPreferences("Prefs", MODE_PRIVATE);
		editor = sharedPrefs.edit();

		// Informacion enviada por otras actividades
		Bundle extras = getIntent().getExtras();
		// Vistas presentes
		etTip = (EditText)findViewById(R.id.etTip);
		etTip.setTextColor(Color.parseColor("#787878"));
		invitar = (Button)findViewById(R.id.bInvitar);
		agregar = (ImageButton)findViewById(R.id.bAgregar);
		facebook = (ImageButton)findViewById(R.id.bFacebook);
		eliminar = (ImageButton)findViewById(R.id.bEliminar);

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

		//if(extras != null) {
		//	editor.putString(String.valueOf(extras.getInt("position")), "null;" + (float)extras.getDouble("totalIndi") + ";false");
		//	editor.commit();
		//}
		
		// Parse al string para saber los valores guardados
		users = sharedPrefs.getAll().toString().replaceAll("\\{|\\}", "").split(",.?");
		
		usuarios = new ArrayList<Person>();
		// Recorre las SharedPreferences y crea el ArrayList con esta informacion
		if(!users[0].equalsIgnoreCase("")) {
			for(int i = users.length-1; i >= 0; i--) {
				String s = users[i];
				String[] usr = s.split("=|;");
				Person p = new Person(Integer.parseInt(usr[0]), usr[1], Float.parseFloat(usr[2]), Boolean.parseBoolean(usr[3]));
				usuarios.add(p);
			}
		} else {
			usuarios.add(new Person(usuarios.size(), "Yo", 0.0f, false));
		}
		if(extras != null) {
			Person person = usuarios.get(extras.getInt("position"));
			person.setTotal((float)extras.getDouble("totalIndi"));
			seleccionaAmigos = extras.getInt("friends");
			editor.putString(String.valueOf(person.getId()), person.getPicture() + ";" + person.getTotal() + ";" + person.isPaid());
			editor.commit();
		}
		if(seleccionaAmigos == 1){
			onClickPickFriends();
		}
		updatePersonAdapter(Float.valueOf(etTip.getText().toString()));
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

		tvgTotal = (TextView)findViewById(R.id.tvgTotal);
		tvFalta = (TextView)findViewById(R.id.tvgFalta);
	}

	@Override
	protected void onPause() {
		super.onPause();
		for(Person p : usuarios) {
			editor.putString(String.valueOf(p.getId()), p.getPicture() + ";" + p.getTotal() + ";" + p.isPaid());
		}
		editor.commit();
	}

	// Metodo para actualizar la lista de usuarios y sus totales
	public void updatePersonAdapter(float tip) {
		adapter = new PersonAdapter(this, R.layout.lista_usuarios_item, usuarios, tip, 0, positions);
		layout = (ListView)findViewById(R.id.lvUsuarios);
		layout.setAdapter(adapter);
	}

	// Actualiza el total de todas las personas
	public void updateTotal() {
		gTotal = 0;
		for(Person p : usuarios)
			gTotal += p.getTotalTip();
		tvgTotal.setText(String.valueOf(gTotal));
	}

	// Actualiza la cantidad que falta por pagar
	public void updateRemaining() {
		falta = 0;
		for(Person p : usuarios) {
			if(!p.isPaid())
				falta += p.getTotalTip();
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
			Session session2 = Session.getActiveSession();
			if (session2 == null) {
				Toast.makeText(getApplicationContext(),"No esta logeado con Facebook",Toast.LENGTH_SHORT).show();
			}else{
				if (session2.isOpened()) {
					postStatusUpdate();
				}else{
					Toast.makeText(getApplicationContext(),"No esta logeado con Facebook",Toast.LENGTH_SHORT).show();
				}
			}
			Toast.makeText(this, "Compartir en Facebook", Toast.LENGTH_SHORT);
			break;
		case R.id.bEliminar:
			positions.clear();
			deleteItem();
			break;
		}
	}
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case PICK_FRIENDS_ACTIVITY:
			displaySelectedFriends(resultCode);
			break;
		default:
			Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
			break;
		}
	}
	private void displaySelectedFriends(int resultCode) {
		String results = "";
		FriendPickerApplication application = (FriendPickerApplication) getApplication();

		Collection<GraphUser> selection = application.getSelectedUsers();
		if (selection != null && selection.size() > 0) {
			ArrayList<String> names = new ArrayList<String>();
			for (GraphUser user : selection) {
				//Toast.makeText(getApplicationContext(),user.getId(),Toast.LENGTH_SHORT).show();

				names.add(user.getName());
				usuarios.add(new Person(usuarios.size(), user.getName().toString()));

			}
			results = TextUtils.join(", ", names);
		} else {
			results = "<No friends selected>";
		}
		Toast.makeText(getApplicationContext(),results,Toast.LENGTH_SHORT).show();
		adapter.notifyDataSetChanged();		
		// mensajeFace.setText(results);
	}
	private void postStatusUpdate() {
		if (user != null ) {
			final String message = getString(R.string.aceptar, user.getFirstName(), (new Date().toString()));
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

		//para manejar la acci�n del boton OK, de la ventana de dialogo
		helpBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if(!etNombre.getText().toString().equals(""))
					usuarios.add(new Person(usuarios.size(), etNombre.getText().toString().trim()));
			}
		});
		helpBuilder.setNeutralButton("Facebook", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Session session = Session.getActiveSession();
				if (session == null) {
					Intent intent = new Intent(view.getContext(), Facebook.class);
					intent.putExtra("viene", "Invita");
					startActivity(intent);
				}else{
					if (session.isOpened()) {
						onClickPickFriends();
					}else{
						Intent intent = new Intent(view.getContext(), Facebook.class);
						startActivity(intent);					}
				}
			}
		});

		// Se crea la ventana de dialogo
		AlertDialog helpDialog = helpBuilder.create();
		//se muestra la ventana de dialogo
		helpDialog.show();
	}

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


		//para manejar la acci�n del boton OK, de la ventana de dialogo
		helpBuilder.setPositiveButton("Ok",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if(!buscar.getText().toString().equals("")){
				//agrega a un usuario existente a la mesa 
				usuarios.add(new Person(usuarios.size(), buscar.getText().toString()));
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


		//para manejar la acci�n del boton OK, de la ventana de dialogo
		helpBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				positions = adapter.getPositions();
				for(Integer i : positions) {
					//usuarios.remove(i.intValue());
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
					editor.putString(String.valueOf(p.getId()), p.getPicture() + ";" + p.getTotal() + ";" + p.isPaid());
				}
				editor.commit();
				adapter.notifyDataSetChanged();
			}
		});

		// Se crea la ventana de dialogo
		AlertDialog helpDialog = helpBuilder.create();
		//se muestra la ventana de dialogo
		helpDialog.show();
	}

	private void onClickPickFriends() {
		FriendPickerApplication application = (FriendPickerApplication) getApplication();
		application.setSelectedUsers(null);

		Intent intent = new Intent(this, FriendPicker.class);
		// Note: The following line is optional, as multi-select behavior is the default for
		// FriendPickerFragment. It is here to demonstrate how parameters could be passed to the
		// friend picker if single-select functionality was desired, or if a different user ID was
		// desired (for instance, to see friends of a friend).
		FriendPicker.populateParameters(intent, null, true, true);
		startActivityForResult(intent, PICK_FRIENDS_ACTIVITY);


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
