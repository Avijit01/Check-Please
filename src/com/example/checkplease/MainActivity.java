package com.example.checkplease;



import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	private Button entrar;
	private Button registrar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		entrar = (Button)findViewById(R.id.entrarbtn);
		registrar = (Button)findViewById(R.id.registrobtn);

		entrar.setOnClickListener(new  View.OnClickListener(){
        	public void onClick(View view){
        		Intent intent = new Intent(view.getContext(), Detalles.class);
                startActivity(intent);
        	}
        });
		registrar.setOnClickListener(new  View.OnClickListener(){
        	public void onClick(View view){
        		Intent intent = new Intent(view.getContext(), Registro.class);
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
     *pone el valor de despliega en 0 para tomar acciones en los metodos de animacion
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
