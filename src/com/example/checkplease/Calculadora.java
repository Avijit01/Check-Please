package com.example.checkplease;

import java.util.ArrayList;

import com.example.checkplease.libreria.UserFunctions;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.OnNavigationListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Calculadora extends Activity{
	private Button regresa, igual, uno, dos, tres, cuatro, cinco, seis, siete, ocho, nueve, cero;
	private Button suma, resta, multi, div, erase, delete, asignar, point;
	private EditText result;
	private String texto = "";
	private double calculo = 0.0;
	private double aux = 0.0;
	private char op = 's';

	private String split[], igualText = "0.0";
	UserFunctions userFunctions = new UserFunctions();//carga la case userFunctions


	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calculadora);

		SharedPreferences prefs = getSharedPreferences("PREFS_KEY",Activity.MODE_PRIVATE);

		
		uno = (Button)findViewById(R.id.button1);
		dos = (Button)findViewById(R.id.button2);
		tres = (Button)findViewById(R.id.button3);
		cuatro = (Button)findViewById(R.id.button4);
		cinco = (Button)findViewById(R.id.button5);
		seis = (Button)findViewById(R.id.button6);
		siete = (Button)findViewById(R.id.button7);
		ocho = (Button)findViewById(R.id.button8);
		nueve = (Button)findViewById(R.id.button9);
		cero = (Button)findViewById(R.id.button0);

		suma = (Button)findViewById(R.id.mas);
		resta = (Button)findViewById(R.id.menos);
		multi = (Button)findViewById(R.id.multi);
		div = (Button)findViewById(R.id.divi);
		erase = (Button)findViewById(R.id.delete);
		delete = (Button)findViewById(R.id.erase);
		point = (Button)findViewById(R.id.point);

		igual = (Button)findViewById(R.id.igual);
		asignar = (Button)findViewById(R.id.asignar);

		result = (EditText)findViewById(R.id.cuadroCalc);
		
		// Desactiva el teclado del EditText
		result.setInputType(InputType.TYPE_NULL);

		
		asignar.setOnClickListener(new  View.OnClickListener(){
			public void onClick(View view){
				Intent intent = new Intent(view.getContext(), Lista.class);
				intent.putExtra("totalIndi", calculo);
				intent.putExtra("calculos", split);
				startActivity(intent);

			}
		});

		uno.setOnClickListener(new  View.OnClickListener(){
			public void onClick(View view){
				if( texto.equals(igualText) ) texto = "";
				op = 's';
				texto = texto + "1";
				result.setText(texto);
			}
		});

		dos.setOnClickListener(new  View.OnClickListener(){
			public void onClick(View view){
				if( texto.equals(igualText) ) texto = "";
				op = 's';
				texto = texto + "2";
				result.setText(texto);
			}
		});

		tres.setOnClickListener(new  View.OnClickListener(){
			public void onClick(View view){
				if( texto.equals(igualText) ) texto = "";
				op = 's';
				texto = texto + "3";
				result.setText(texto);
			}
		});

		cuatro.setOnClickListener(new  View.OnClickListener(){
			public void onClick(View view){
				if( texto.equals(igualText) ) texto = "";
				op = 's';
				texto = texto + "4";
				result.setText(texto);
			}
		});

		cinco.setOnClickListener(new  View.OnClickListener(){
			public void onClick(View view){
				if( texto.equals(igualText) ) texto = "";
				op = 's';
				texto = texto + "5";
				result.setText(texto);
			}
		});

		seis.setOnClickListener(new  View.OnClickListener(){
			public void onClick(View view){
				if( texto.equals(igualText) ) texto = "";
				op = 's';
				texto = texto + "6";
				result.setText(texto);
			}
		});

		siete.setOnClickListener(new  View.OnClickListener(){
			public void onClick(View view){
				if( texto.equals(igualText) ) texto = "";
				op = 's';
				texto = texto + "7";
				result.setText(texto);
			}
		});

		ocho.setOnClickListener(new  View.OnClickListener(){
			public void onClick(View view){
				if( texto.equals(igualText) ) texto = "";
				op = 's';
				texto = texto + "8";
				result.setText(texto);
			}
		});

		nueve.setOnClickListener(new  View.OnClickListener(){
			public void onClick(View view){
				if( texto.equals(igualText) ) texto = "";
				op = 's';
				texto = texto + "9";
				result.setText(texto);
			}
		});

		cero.setOnClickListener(new  View.OnClickListener(){
			public void onClick(View view){
				if( texto.equals(igualText) ) texto = "";
				op = 's';
				texto = texto + "0";
				result.setText(texto);
			}
		});

		suma.setOnClickListener(new  View.OnClickListener(){
			public void onClick(View view){
				if(  texto.length() == 1 || !texto.equals("") 
						&& texto.charAt(texto.length()-2) != '+'
						&& texto.charAt(texto.length()-2) != '-'
						&& texto.charAt(texto.length()-2) != '*'
						&& texto.charAt(texto.length()-2) != '/'){
					texto = texto + " + ";
					result.setText(texto);
				}
			}
		});

		resta.setOnClickListener(new  View.OnClickListener(){
			public void onClick(View view){
				if(  texto.length() == 1 || !texto.equals("") 
						&& texto.charAt(texto.length()-2) != '+'
						&& texto.charAt(texto.length()-2) != '-'
						&& texto.charAt(texto.length()-2) != '*'
						&& texto.charAt(texto.length()-2) != '/'){
					texto = texto + " - ";
					result.setText(texto);
				}
			}
		});

		multi.setOnClickListener(new  View.OnClickListener(){
			public void onClick(View view){
				if(  texto.length() == 1 || !texto.equals("") 
						&& texto.charAt(texto.length()-2) != '+'
						&& texto.charAt(texto.length()-2) != '-'
						&& texto.charAt(texto.length()-2) != '*'
						&& texto.charAt(texto.length()-2) != '/'){
					texto = texto + " * ";
					result.setText(texto);
				}
			}
		});

		div.setOnClickListener(new  View.OnClickListener(){
			public void onClick(View view){
				if(  texto.length() == 1 || !texto.equals("") 
						&& texto.charAt(texto.length()-2) != '+'
						&& texto.charAt(texto.length()-2) != '-'
						&& texto.charAt(texto.length()-2) != '*'
						&& texto.charAt(texto.length()-2) != '/'){
					texto = texto + " / ";
					result.setText(texto);
				}
			}
		});
		point.setOnClickListener(new  View.OnClickListener(){
			public void onClick(View view){
				if(  texto.length() > 0 && !texto.equals("") && texto.charAt(texto.length()-1) != '.' ){
					texto = texto + ".";
					result.setText(texto);
				}
			}
		});

		erase.setOnClickListener(new  View.OnClickListener(){
			public void onClick(View view){
				texto = "";
				result.setText("");
				calculo = aux = 0.0;
				op = 's';
			}
		});

		delete.setOnClickListener(new  View.OnClickListener(){
			public void onClick(View view){
				String auxTexto = "";
				for( int i = texto.length()-2; i >= 0; i--){
					auxTexto = texto.charAt(i) + auxTexto;
				}
				texto =  auxTexto;
				result.setText(texto);
			}
		});

		igual.setOnClickListener(new  View.OnClickListener(){
			public void onClick(View view){
				split = texto.split(" ");
				for( int i = 0; i < split.length; i++ ){
					try{
							 if( split[i].equals("+") ) op = 's';
						else if( split[i].equals("-") ) op = 'r';
						else if( split[i].equals("*") ) op = 'm';
						else if( split[i].equals("/") ) op = 'd';
						else {
							aux = Double.parseDouble(split[i]); 
							realizaOp(op); 
						}
					} catch(IllegalArgumentException ex) { 
						result.setText("ERROR");
					}
				}
				result.setText(texto + "\n = " + calculo);
				igualText = "" + calculo;
				texto = "" + calculo;
				calculo = 0.0;
			}
		});
		
		asignar.setOnClickListener(new  View.OnClickListener(){
			public void onClick(View view){
				split = texto.split(" ");
				for( int i = 0; i < split.length; i++ ){
					try{
							 if( split[i].equals("+") ) op = 's';
						else if( split[i].equals("-") ) op = 'r';
						else if( split[i].equals("*") ) op = 'm';
						else if( split[i].equals("/") ) op = 'd';
						else {
							aux = Double.parseDouble(split[i]); 
							realizaOp(op); 
						}
					} catch(IllegalArgumentException ex) { 
						result.setText("ERROR");
					}
				}
				result.setText(texto + "\n = " + calculo);
				Intent intent = new Intent(view.getContext(), Lista.class);
				intent.putExtra("totalIndi", calculo);
				intent.putExtra("calculos", split);
				intent.putExtra("position", getIntent().getExtras().getInt("position"));
				startActivity(intent);
			}
		});

	}

	public void realizaOp(char op){
		switch( op ){
		case 's': calculo += aux; break;
		case 'r': calculo -= aux; break;
		case 'm': calculo *= aux; break;
		case 'd': if( aux != 0) calculo /= aux;
		else calculo = 0;	  break;
		default: break;
		}
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
		case android.R.id.home://se cierra el menu
			Calculadora.this.finish();
			return true;
		case R.id.acerca://se cierra el menu
			startActivity(new Intent(this, Acerca.class));
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
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
	    actionBar.setTitle("Calculadora");
	    
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

