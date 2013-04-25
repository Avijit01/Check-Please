package com.example.checkplease;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

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
	ImageButton  pickFriendsButton;
	ImageButton agregar;
	ImageButton facebook;
	ImageButton eliminar;

	// Variables que maneja la vista para calculos
	ListView layout;
	PersonAdapter adapter;
	ArrayList<Person> usuarios;
	EditText etTip;
	TextView tvgTotal;
	TextView tvFalta;
	float gTotal;
	float falta;

    private static final int PICK_FRIENDS_ACTIVITY = 1;
	UserFunctions userFunctions = new UserFunctions();//carga la case userFunctions



	private ViewGroup controlsContainer;
	private GraphUser user;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lista_usuarios);

		Bundle extras = getIntent().getExtras();
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

		usuarios = new ArrayList<Person>();
		usuarios.add(new Person("Derp", 120.0f, false));
		usuarios.add(new Person("Hurr", 137.50f, false));
		usuarios.add(new Person("Herpa", 85.0f, true));
		usuarios.add(new Person("Derpa", 32.75f, true));

		if(extras !=null) {
			usuarios.get(extras.getInt("position")).setTotal((float)extras.getDouble("totalIndi"));
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

	public void updatePersonAdapter(float tip) {
		adapter = new PersonAdapter(this, R.layout.lista_usuarios_item, usuarios, tip, 0);
		layout = (ListView)findViewById(R.id.lvUsuarios);
		layout.setAdapter(adapter);
	}

	public void updateTotal() {
		gTotal = 0;
		for(Person p : usuarios)
			gTotal += p.getTotalTip();
		tvgTotal.setText(String.valueOf(gTotal));
	}

	public void updateRemaining() {
		falta = 0;
		for(Person p : usuarios) {
			if(!p.isPaid())
				falta += p.getTotalTip();
		}
		tvFalta.setText(String.valueOf(falta));
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
			Session session = Session.getActiveSession();
			if (session == null) {
				Toast.makeText(getApplicationContext(),"No esta logeado con Facebook",Toast.LENGTH_SHORT).show();
			}else{
				if (session.isOpened()) {
					onClickPickFriends();
				}else{
					Toast.makeText(getApplicationContext(),"No esta logeado con Facebook",Toast.LENGTH_SHORT).show();

				}
			}
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
			Toast toast = Toast.makeText(this, "Selecciona la(s) persona(s) que deseas eliminar", Toast.LENGTH_SHORT);
			toast.show();
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
				Toast.makeText(getApplicationContext(),user.getId(),Toast.LENGTH_SHORT).show();

				names.add(user.getName());
			}
			results = TextUtils.join(", ", names);
		} else {
			results = "<No friends selected>";
		}
		Toast.makeText(getApplicationContext(),results,Toast.LENGTH_SHORT).show();

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

	public void showInfo() {

		//se crea una nueva alerta de dialogo
		AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
		//se le asigna el titulo a la ventana de dialogo
		helpBuilder.setTitle("Invitar");

		//se toma el Layout Inflater
		LayoutInflater inflater = getLayoutInflater();
		//se toma el layout correspondiente a la ventana del pop up
		View checkboxLayout = inflater.inflate(R.layout.invitar, null);
		//se asigna esa vista a la ventana de dialogo
		helpBuilder.setView(checkboxLayout);


		//para manejar la acción del boton OK, de la ventana de dialogo
		helpBuilder.setPositiveButton("Ok",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// No hace nada mas que cerrar la ventana de dialogo
			}
		});

		// Se crea la ventana de dialogo
		AlertDialog helpDialog = helpBuilder.create();
		//se muestra la ventana de dialogo
		helpDialog.show();
	}

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
		PersonAdapter deleteAdapter = new PersonAdapter(this, R.layout.lista_usuarios_delete_item,usuarios, 0.0f, 1);
		lvDelete.setAdapter(deleteAdapter);
		//se asigna esa vista a la ventana de dialogo
		helpBuilder.setView(lvView);


		//para manejar la acción del boton OK, de la ventana de dialogo
		helpBuilder.setPositiveButton("Ok",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// No hace nada mas que cerrar la ventana de dialogo
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
		/* final FriendPickerFragment fragment = new FriendPickerFragment();
        Log.d("1", "ONCLICK");

        setFriendPickerListeners(fragment);
        Log.d("2", "ONCLICK");

        showPickerFragment(fragment);*/

    }
    private void facebook() {
		startActivity(new Intent(this, Facebook.class));
	}
	private void Inicio() {
		startActivity(new Intent(this, MainActivity.class));
	}
	private void Acerca(){
		startActivity(new Intent(this, Acerca.class));
		
	}
	@Override
	protected void onResume() {
	    super.onResume();
	    cargaMenu();
	    // Normal case behavior follows
	}
	void cargaMenu(){
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
	    actionBar.setBackgroundDrawable(getResources().getDrawable(
	            R.drawable.bar_color));
	    actionBar.setTitle("Pago Individual");
	    
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
