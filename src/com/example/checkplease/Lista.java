package com.example.checkplease;

import java.util.ArrayList;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
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
	EditText etTip;
	float gTotal;
	float falta;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lista_usuarios);
		TextView titulo = (TextView)findViewById(R.id.titulo);
		titulo.setText("Pago Individual");
		
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
					facebook.setColorFilter(null); // or null
				}
				return false;
			}
		 });
        
		ArrayList<Person> usuarios = new ArrayList<Person>();
		usuarios.add(new Person("Derp", 120.0f, false));
		usuarios.add(new Person("Hurr", 137.50f, false));
		usuarios.add(new Person("Herpa", 85.0f, true));
		usuarios.add(new Person("Derpa", 32.75f, true));
		PersonAdapter adapter = new PersonAdapter(this, R.layout.lista_usuarios_item, usuarios, 0.0f);
		ListView layout = (ListView)findViewById(R.id.lvUsuarios);
		layout.setAdapter(adapter);
		TextView tvgTotal = (TextView)findViewById(R.id.tvgTotal);
		TextView tvFalta = (TextView)findViewById(R.id.tvgFalta);
		

		for(Person p : usuarios)
			gTotal += p.getTotal();
		for(Person p : usuarios) {
			if(!p.isPaid())
				falta += p.getTotal();
		}
		tvgTotal.setText(String.valueOf(gTotal));
		tvFalta.setText(String.valueOf(falta));
		regresa.setOnClickListener(new View.OnClickListener(){
        	public void onClick(View view){
        		Lista.this.finish();
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
			toast = Toast.makeText(this, "Invitar a un amigo", Toast.LENGTH_SHORT);
			break;
		case R.id.bFacebook:
			toast = Toast.makeText(this, "Compartir en Facebook", Toast.LENGTH_SHORT);
			break;
		case R.id.bEliminar:
			toast = Toast.makeText(this, "Eliminar persona(s)", Toast.LENGTH_SHORT);
			break;
		}
		toast.show();
	}
	public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
			long arg3) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, Detalles.class);
        startActivity(intent);
	}
}
