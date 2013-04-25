package com.example.checkplease;

import java.util.ArrayList;

import com.example.checkplease.libreria.UserFunctions;
import com.facebook.Session;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.OnNavigationListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
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

public class Entra extends Activity {

	EditText etTotal;
	EditText etPropina;
	EditText etPersonas;
	TextView tvPagoPorPersona;
	RelativeLayout divIgual;
	UserFunctions userFunctions = new UserFunctions();//carga la case userFunctions


	private Button regresa, igual, individual;
	private float total, propina;
	private int personas;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forma_de_pago);
		
		
		cargaMenu();
		
		
		igual = (Button)findViewById(R.id.igual);
		individual = (Button)findViewById(R.id.individual);

		etTotal = (EditText)findViewById(R.id.total);
		etPropina = (EditText)findViewById(R.id.propina);
		etPersonas = (EditText)findViewById(R.id.personas);
		tvPagoPorPersona = (TextView)findViewById(R.id.pagoPorPersona);
		divIgual = (RelativeLayout)findViewById(R.id.divIgual);

		divIgual.setVisibility(RelativeLayout.INVISIBLE);


		
		igual.setOnClickListener(new  View.OnClickListener(){
			public void onClick(View view){
				ActionBar actionBar = getActionBar();
			    actionBar.setTitle("Pago igual ");
			    etTotal.requestFocus ();


				divIgual.setVisibility(view.VISIBLE);
			}

		});
		individual.setOnClickListener(new  View.OnClickListener(){
			public void onClick(View view){
				divIgual.setVisibility(RelativeLayout.INVISIBLE);
				Intent intent = new Intent(view.getContext(), Lista.class);
				startActivity(intent);
			}
		});

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

		etPersonas.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence seq, int start, int before, int count) {
				try {
					personas = Integer.parseInt(etPersonas.getText().toString());
				} catch (Exception e) {
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
		float ppp = (total + (total * (propina / 100.0f))) / personas;
		if(!Float.isNaN(ppp) && !Float.isInfinite(ppp))
			tvPagoPorPersona.setText("$" + String.valueOf(ppp) + " por persona");
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
	    actionBar.setTitle("Forma pago    ");//pone el titulo
	    
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
