package com.example.checkplease;

import java.util.ArrayList;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Lista extends Activity implements OnClickListener {
	
	// Definicion de los botones presentes en la vista
	Button regresa;
	Button invitar;
	ImageButton agregar;
	ImageButton facebook;
	ImageButton eliminar;
	
	// Variables que maneja la vista para calculos
	PersonAdapter adapter;
	ArrayList<Person> usuarios;
	EditText etTip;
	TextView tvgTotal;
	TextView tvFalta;
	float gTotal;
	float falta;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lista_usuarios);
		TextView titulo = (TextView)findViewById(R.id.titulo);
		titulo.setText("Pago Individual");
		
		etTip = (EditText)findViewById(R.id.etTip);
		
		regresa = (Button)findViewById(R.id.regresabtn);
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
		adapter = new PersonAdapter(this, R.layout.lista_usuarios_item, usuarios, Float.valueOf(etTip.getText().toString()));
		ListView layout = (ListView)findViewById(R.id.lvUsuarios);
		layout.setAdapter(adapter);
		tvgTotal = (TextView)findViewById(R.id.tvgTotal);
		tvFalta = (TextView)findViewById(R.id.tvgFalta);

		for(Person p : usuarios)
			gTotal += p.getTotal();
		updateSum();
		tvgTotal.setText(String.valueOf(gTotal));
		regresa.setOnClickListener(new View.OnClickListener(){
        	public void onClick(View view){
        		Lista.this.finish();
        	}
        });
	}
	
	public void updateSum() {
		falta = 0;
		for(Person p : usuarios) {
			if(!p.isPaid())
				falta += p.getTotal();
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
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }
	@Override
	public void onClick(View v) {
		Toast toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
		switch(v.getId()) {
		case R.id.bAgregar:
			toast = Toast.makeText(this, "Agregar una persona", Toast.LENGTH_SHORT);
			break;
		case R.id.bInvitar:
			showInfo();
			break;
		case R.id.bFacebook:
			toast = Toast.makeText(this, "Compartir en Facebook", Toast.LENGTH_SHORT);
			break;
		case R.id.bEliminar:
			toast = Toast.makeText(this, "Eliminar persona(s)", Toast.LENGTH_SHORT);
			break;
		case R.id.cbPaid:
			updateSum();
			break;
		}
		toast.show();
	}
	
	public void showInfo() {
		   
	    //se crea una nueva alerta de dialogo
	    AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
	   	//se le asigna el titulo a la ventana de dialogo
	    helpBuilder.setTitle("Invitar");
	   	 
	    //helpBuilder.setMessage("This is a Simple Pop Up");
	    
	    /*String precios[] = {"Cesar","Mario","Raul"};
		
		//se declara la lista asociada con la lista del layout
		ListView list = (ListView) findViewById(R.id.preciosList);
		//se crea el adapter para llenar los elemtnos de la lista con los datos de frutas
		LazyAdapter adapter = new LazyAdapter(this, precios);
		//se agrega los elementos a la lista
		list.setAdapter( adapter );
		//se habilita el evente OnCLick en cada elemto de la lista*/
	   	 
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

}
