package com.example.checkplease;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Entra extends Activity {
	
	
	private Button regresa, igual, individual;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forma_de_pago);
		regresa = (Button)findViewById(R.id.regresabtn);
		igual = (Button)findViewById(R.id.igual);
		individual = (Button)findViewById(R.id.individual);

		final EditText total = (EditText)findViewById(R.id.total);
		final EditText propina = (EditText)findViewById(R.id.propina);
		final EditText personas = (EditText)findViewById(R.id.personas);
		final TextView pagoPorPersona = (TextView)findViewById(R.id.pagoPorPersona);
		final RelativeLayout divIgual = (RelativeLayout)findViewById(R.id.divIgual);

		divIgual.setVisibility(RelativeLayout.INVISIBLE);
		

		final TextView titulo = (TextView)findViewById(R.id.titulo);
		titulo.setText("Forma de Pago");
		
		regresa.setOnClickListener(new  View.OnClickListener(){
        	public void onClick(View view){
        		Entra.this.finish();
        	}
        });
		igual.setOnClickListener(new  View.OnClickListener(){
        	public void onClick(View view){
        		titulo.setText("Pago Igual");
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
