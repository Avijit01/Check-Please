package com.example.checkplease;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class Lista  extends Activity{
	
	EditText etTip;
	Button regresa;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lista_usuarios);
		TextView titulo = (TextView)findViewById(R.id.titulo);
		titulo.setText("Pago Individual");
		regresa = (Button)findViewById(R.id.regresabtn);
        
		ArrayList<Person> usuarios = new ArrayList<Person>();
		usuarios.add(new Person("Derp", 500, false));
		usuarios.add(new Person("Hurr", 200, true));
		/*PersonAdapter adapter = new PersonAdapter(this, R.layout.lista_usuarios_item, usuarios, Float.parseFloat(etTip.getText().toString()));
		ListView layout = (ListView)findViewById(R.id.lvUsuarios);
		layout.setAdapter(adapter);*/
		regresa.setOnClickListener(new  View.OnClickListener(){
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
}
