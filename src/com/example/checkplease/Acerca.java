package com.example.checkplease;

import java.util.ArrayList;

import com.checkplease.R;
import com.example.checkplease.libreria.UserFunctions;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.OnNavigationListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Raul Gomez
 * Esta clase accesa a la vista de la informacion de los desarroladores
 */
public class Acerca extends Activity{
	private Button regresa;
	UserFunctions userFunctions = new UserFunctions();//carga la case userFunctions

	/**
	 * Este metodo permite iniciar la actividad acerca_check.xml
	 * @param savedInstanceState
	 */
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acerca_check);
		
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
	    actionBar.setTitle("Acerca         ");//pone el titulo
	    
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
        	/**
        	 * Metodo que te da la posicion en la cual el usuario dio clcik en el menu
        	 * @param int itemPosition
        	 * @param long itemId
        	 */
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
     * Metodo: onOptionsItemSelected(),
     * Metodo que le asigna una accion a realizar a cada opcion de android que se seleccione en android
     * @param item
     * @return bolean, true se hizo corectamente
     */
	public boolean onOptionsItemSelected(MenuItem item) {
		//responde a la seleccion del menu
		switch (item.getItemId()) {//valor a switchear
		case R.id.acerca://se abre la clase Acerca al seleccionarla
			startActivity(new Intent(this, Acerca.class));
			return true;	
		case android.R.id.home://se cierra la actividad acutal
			Acerca.this.finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);//si no accion, no hace nada
		}
	}

}
