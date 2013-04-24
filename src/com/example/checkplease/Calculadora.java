package com.example.checkplease;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Calculadora extends Activity{
	private Button regresa, igual, uno, dos, tres, cuatro, cinco, seis, siete, ocho, nueve, cero;
	private Button suma, resta, multi, div, erase, delete, asignar, point;
	private TextView result;
	private String texto = "";
	private double calculo = 0.0;
	private double aux = 0.0;
	private char op = 's';
	private String split[];

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calculadora);

		SharedPreferences prefs = getSharedPreferences("PREFS_KEY",Activity.MODE_PRIVATE);

		TextView titulo = (TextView)findViewById(R.id.titulo);
		titulo.setText("Calculadora");
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

		regresa = (Button)findViewById(R.id.regresabtn);
		igual = (Button)findViewById(R.id.igual);
		asignar = (Button)findViewById(R.id.asignar);

		result = (TextView)findViewById(R.id.cuadroCalc);

		regresa.setOnClickListener(new  View.OnClickListener(){
			public void onClick(View view){
				Calculadora.this.finish();
			}
		});
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
				texto = texto + "1";
				result.setText(texto);
			}
		});

		dos.setOnClickListener(new  View.OnClickListener(){
			public void onClick(View view){
				texto = texto + "2";
				result.setText(texto);
			}
		});

		tres.setOnClickListener(new  View.OnClickListener(){
			public void onClick(View view){
				texto = texto + "3";
				result.setText(texto);
			}
		});

		cuatro.setOnClickListener(new  View.OnClickListener(){
			public void onClick(View view){
				texto = texto + "4";
				result.setText(texto);
			}
		});

		cinco.setOnClickListener(new  View.OnClickListener(){
			public void onClick(View view){
				texto = texto + "5";
				result.setText(texto);
			}
		});

		seis.setOnClickListener(new  View.OnClickListener(){
			public void onClick(View view){
				texto = texto + "6";
				result.setText(texto);
			}
		});

		siete.setOnClickListener(new  View.OnClickListener(){
			public void onClick(View view){
				texto = texto + "7";
				result.setText(texto);
			}
		});

		ocho.setOnClickListener(new  View.OnClickListener(){
			public void onClick(View view){
				texto = texto + "8";
				result.setText(texto);
			}
		});

		nueve.setOnClickListener(new  View.OnClickListener(){
			public void onClick(View view){
				texto = texto + "9";
				result.setText(texto);
			}
		});

		cero.setOnClickListener(new  View.OnClickListener(){
			public void onClick(View view){
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
				texto = "";
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
		case R.id.acerca://se cierra el menu
			startActivity(new Intent(this, Acerca.class));
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

}

