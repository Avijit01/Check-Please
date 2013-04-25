package com.example.checkplease;

import java.util.ArrayList;
import java.util.Collection;

import com.example.checkplease.libreria.UserFunctions;
import com.facebook.FacebookException;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.FriendPickerFragment;
import com.facebook.widget.LoginButton;
import com.facebook.widget.PickerFragment;
import com.facebook.widget.UserSettingsFragment;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ActionBar.OnNavigationListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Facebook extends FragmentActivity{
	UserFunctions userFunctions = new UserFunctions();//carga la case userFunctions
    //Agrega los elementos de la vista
	private UserSettingsFragment userSettingsFragment;
    private Button acepta, rechaza;
    ImageView fondo;
    private ViewGroup controlsContainer;
    private static final int PICK_FRIENDS_ACTIVITY = 1;

/**
 * Clase que maneja las actividad de Facebook, inicio y cierre de seccion
 *
 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_fragment_activity);
		//inicializa los valores a utilizar
		fondo = (ImageView)findViewById(R.id.imageView3);
		final TextView mensajeFace = (TextView)findViewById(R.id.mensaje);
		acepta = (Button)findViewById(R.id.sigue);
		rechaza = (Button)findViewById(R.id.regresa);
		//obtiene si la session de Facebook esta activa o desactiva
		Session session = Session.getActiveSession();
		if (session == null) {//si es nulla, pone los elementos inivibles
			 acepta.setVisibility(RelativeLayout.INVISIBLE);
	    	rechaza.setVisibility(RelativeLayout.INVISIBLE);
	    	mensajeFace.setVisibility(RelativeLayout.INVISIBLE);
		}else{
        if (session.isOpened()) {//si esta abierta, despliega los botones
        	acepta.setVisibility(RelativeLayout.VISIBLE);
	    	rechaza.setVisibility(RelativeLayout.VISIBLE);
	    	mensajeFace.setVisibility(RelativeLayout.VISIBLE);
        }else{//de lo contrario los hace invisibles
        	 acepta.setVisibility(RelativeLayout.INVISIBLE);
	    	 rechaza.setVisibility(RelativeLayout.INVISIBLE);
	    	 mensajeFace.setVisibility(RelativeLayout.INVISIBLE);
        	
        }
		}
		//FragmentManager es una funcionalidad del SDK de facebook que permite visualizar
		//el inicio de sesión como si fuera un ambiente de Facebook
	    FragmentManager fragmentManager = getSupportFragmentManager();
	    userSettingsFragment = (UserSettingsFragment) fragmentManager.findFragmentById(R.id.login_fragment);
	    userSettingsFragment.setSessionStatusCallback(new Session.StatusCallback() {
	        @Override
	        public void call(Session session, SessionState state, Exception exception) {
	            Log.d("LoginUsingLoginFragmentActivity", String.format("New session state: %s", state.toString()));
	            Log.d("LoginUsingLoginFragmentActivity", String.format("New session state: %s", session.toString()));
	            //al entrar pone los botontes en visible
	            acepta.setVisibility(RelativeLayout.VISIBLE);
	    		rechaza.setVisibility(RelativeLayout.VISIBLE);
	    		mensajeFace.setVisibility(RelativeLayout.VISIBLE);
	        }
	    });
	     //al presionar el botor de aceptar, se abre la actividad de entra
	    acepta.setOnClickListener(new  View.OnClickListener(){
        	public void onClick(View view){
        		Intent intent = new Intent(view.getContext(), Entra.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        		startActivity(intent);
        		finish();//termina la activad de Facebook para que al regresar no pase por esta
        	}
        });	   
	    //al presionar el boton de rechar, se deslogea y termina la actividad
	    rechaza.setOnClickListener(new  View.OnClickListener(){
        	public void onClick(View view){
        		Session session = Session.getActiveSession();
    	        if (!session.isClosed()) {
    	            session.closeAndClearTokenInformation();
    	        }
        		Facebook.this.finish();
        	}
        });
	}
	/**
	 * Metodo que maneja las respuesta de selccionar una aprte del menu o elemento de android
	 *@param item
	 *elemento que se selecciono
	 */
	  @Override
		public boolean onOptionsItemSelected(MenuItem item) {
			switch (item.getItemId()) {
			case android.R.id.home://al seleccionar el icono de la barra de header
				Facebook.this.finish();//se cierra la actividad
				return true;
			default:
				return super.onOptionsItemSelected(item);
			}
		}

    
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        userSettingsFragment.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
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
	    actionBar.setTitle("Facebook   ");
	    
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
