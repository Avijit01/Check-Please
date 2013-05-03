package com.example.checkplease;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.example.checkplease.libreria.UserFunctions;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.OnNavigationListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
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

public class Calculadora extends Activity{
	//botones numerioc de la calculadora
	private Button regresa, igual, uno, dos, tres, cuatro, cinco, seis, siete, ocho, nueve, cero;
	//botones de signos de caluculadora
	private Button suma, resta, multi, div, erase, delete, asignar, point;
	private EditText result;//cuadro de texto donde aparece el resultado
	private String texto = ""; //lo que pararece en result
	private double calculo = 0.0; //variable que maneja los calculos realizados
	private double aux = 0.0; //auxiliar del calculo
	private char op = 's';//variable para manejar que signo esta activo
	private boolean punto = false;//variable que sirve para validar el punto decimal

	//split donde se guardan los datos dados por texto, igualText valida si se dio igual o no
	private String split[], igualText = "0.0";
	UserFunctions userFunctions = new UserFunctions();//carga la case userFunctions

	/**
	 * Metodo que maneja lacciones de la calculadora
	 */
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calculadora);

		SharedPreferences prefs = getSharedPreferences("PREFS_KEY",Activity.MODE_PRIVATE);

		//inicializacion de botnoes y vistas
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
		result.setTextColor(Color.parseColor("#787878"));

		/**
		 * Metodo que maneja el boton 1
		 * @return void
		 */
		uno.setOnClickListener(new  View.OnClickListener(){
			public void onClick(View view){
				if( texto.equals(igualText) ) texto = "";
				op = 's';
				texto = texto + "1";
				result.setText(texto);
			}
		});
		/**
		 * Metodo que maneja el boton 2
		 * @return void
		 */
		dos.setOnClickListener(new  View.OnClickListener(){
			public void onClick(View view){
				if( texto.equals(igualText) ) texto = "";
				op = 's';
				texto = texto + "2";
				result.setText(texto);
			}
		});
		/**
		 * Metodo que maneja el boton 3
		 * @return void
		 */
		tres.setOnClickListener(new  View.OnClickListener(){
			public void onClick(View view){
				if( texto.equals(igualText) ) texto = "";
				op = 's';
				texto = texto + "3";
				result.setText(texto);
			}
		});
		/**
		 * Metodo que maneja el boton 4
		 * @return void
		 */
		cuatro.setOnClickListener(new  View.OnClickListener(){
			public void onClick(View view){
				if( texto.equals(igualText) ) texto = "";
				op = 's';
				texto = texto + "4";
				result.setText(texto);
			}
		});
		/**
		 * Metodo que maneja el boton 5
		 * @return void
		 */
		cinco.setOnClickListener(new  View.OnClickListener(){
			public void onClick(View view){
				if( texto.equals(igualText) ) texto = "";
				op = 's';
				texto = texto + "5";
				result.setText(texto);
			}
		});
		/**
		 * Metodo que maneja el boton 6
		 * @return void
		 */
		seis.setOnClickListener(new  View.OnClickListener(){
			public void onClick(View view){
				if( texto.equals(igualText) ) texto = "";
				op = 's';
				texto = texto + "6";
				result.setText(texto);
			}
		});
		/**
		 * Metodo que maneja el boton 7
		 * @return void
		 */
		siete.setOnClickListener(new  View.OnClickListener(){
			public void onClick(View view){
				if( texto.equals(igualText) ) texto = "";
				op = 's';
				texto = texto + "7";
				result.setText(texto);
			}
		});
		/**
		 * Metodo que maneja el boton 8
		 * @return void
		 */
		ocho.setOnClickListener(new  View.OnClickListener(){
			public void onClick(View view){
				if( texto.equals(igualText) ) texto = "";
				op = 's';
				texto = texto + "8";
				result.setText(texto);
			}
		});
		/**
		 * Metodo que maneja el boton 9
		 * @return void
		 */
		nueve.setOnClickListener(new  View.OnClickListener(){
			public void onClick(View view){
				if( texto.equals(igualText) ) texto = "";
				op = 's';
				texto = texto + "9";
				result.setText(texto);
			}
		});
		/**
		 * Metodo que maneja el boton 0
		 * @return void
		 */
		cero.setOnClickListener(new  View.OnClickListener(){
			public void onClick(View view){
				if( texto.equals(igualText) ) texto = "";
				op = 's';
				texto = texto + "0";
				result.setText(texto);
			}
		});
		/**
		 * Metodo que maneja el boton +
		 * @return void
		 */
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
				punto = false;
			}
		});
		/**
		 * Metodo que maneja el boton -
		 * @return void
		 */
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
				punto = false;
			}
		});
		/**
		 * Metodo que maneja el boton *
		 * @return void
		 */
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
				punto = false;
			}
		});
		/**
		 * Metodo que maneja el boton /
		 * @return void
		 */
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
				punto = false;
			}
		});
		/**
		 * Metodo que maneja el boton punto
		 * @return void
		 */
		point.setOnClickListener(new  View.OnClickListener(){
			public void onClick(View view){
				if( !punto ){
					if(  texto.length() > 0 && !texto.equals("") && texto.charAt(texto.length()-1) != '.' ){
						texto = texto + ".";
						result.setText(texto);
					}
					punto = true; 
				}
			}
		});
		/**
		 * Metodo que maneja el boton erase
		 * @return void
		 */
		erase.setOnClickListener(new  View.OnClickListener(){
			public void onClick(View view){
				texto = "";
				result.setText("");
				calculo = aux = 0.0;
				op = 's';
			}
		});
		/**
		 * Metodo que maneja el boton delete
		 * @return void
		 */
		delete.setOnClickListener(new  View.OnClickListener(){
			public void onClick(View view){
				String auxTexto = "";
				for( int i = texto.length()-2; i >= 0; i--){
					auxTexto = texto.charAt(i) + auxTexto;
				}
				texto =  auxTexto;
				result.setText(texto);
				punto = false;
			}
		});
		/**
		 * Metodo que maneja el boton igual
		 * @return void
		 */
		igual.setOnClickListener(new  View.OnClickListener(){
			public void onClick(View view){
				split = texto.split(" ");
				//va verificando split al encontrarse con un numero hace el calculo con 
				//simbolo activo que por default es 's'
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

				int decimalPlaces = 2;
				BigDecimal bd = new BigDecimal(calculo);
				// setScale is immutable
				bd = bd.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
				calculo = bd.doubleValue();

				result.setText(texto + "\n = " + calculo);
				igualText = "" + calculo;
				texto = "" + calculo;
				calculo = 0.0;
				op = 's';
				punto = false;
			}
		});
		/**
		 * Metodo que maneja el boton asignar que regresa el total a la Lista
		 * @return void
		 */
		asignar.setOnClickListener(new  View.OnClickListener(){
			public void onClick(View view){
				split = texto.split(" ");
				Log.d("hola", texto);
				try{
					//va verificando split al encontrarse con un numero hace el calculo con 
					//simbolo activo que por default es 's'
					for( int i = 0; i < split.length; i++ ){
						if( split[i].equals("+") ) op = 's';
						else if( split[i].equals("-") ) op = 'r';
						else if( split[i].equals("*") ) op = 'm';
						else if( split[i].equals("/") ) op = 'd';
						else {
							aux = Double.parseDouble(split[i]);
							realizaOp(op); 
						}
					}
				} catch(NumberFormatException ex) { 
					result.setText("ERROR");
				}

				int decimalPlaces = 2;
				BigDecimal bd = new BigDecimal(calculo);
				// setScale is immutable
				bd = bd.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
				calculo = bd.doubleValue();
				result.setText(calculo+"");

				//se regresan los valores necesarios a la lista
				Intent intent = new Intent(view.getContext(), Lista.class);
				intent.putExtra("viene", "calculadora");
				intent.putExtra("totalIndi", calculo);
				intent.putExtra("calculos", split);
				intent.putExtra("Position", getIntent().getExtras().getInt("Position"));
				startActivity(intent);
				finish();
				punto = false;
			}
		});

	}
	
	
	/**
	 * Metodo que realiza las operciones de la calculadora
	 * @param op
	 */
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
			Calculadora.this.finish();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
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
		actionBar.setTitle("Calculadora ");//pone el titulo

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

