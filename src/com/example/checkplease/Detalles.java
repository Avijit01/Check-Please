package com.example.checkplease;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class Detalles extends Activity implements OnItemClickListener, OnClickListener{
	
	private List<String> precios = new ArrayList<String>();
	private Button regresa;
	private TextView total, name, nameChange;
	private double sumaTotal;
	private String nombre;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detalles);
		
		Bundle extras = getIntent().getExtras(); //si tiene parametos que envio la actividad Main
		if(extras !=null){//se agarra el parametro "position" y se le asigna la variable post
			String precios[] = extras.getStringArray("calculos");
		}
		
		String precios[] = {"10","20","30"};
		
		total = (TextView)findViewById(R.id.total);
		name = (TextView)findViewById(R.id.name);
		
		for( int i = 0; i < precios.length; i++ ){
			sumaTotal+= Double.parseDouble(precios[i]);
		}
		
		total.setText(sumaTotal+"");
		
		//se declara la lista asociada con la lista del layout
		ListView list = (ListView) findViewById(R.id.preciosList);
		//se crea el adapter para llenar los elemtnos de la lista con los datos de frutas
		LazyAdapter adapter = new LazyAdapter(this, precios);
		//se agrega los elementos a la lista
		list.setAdapter( adapter );
		//se habilita el evente OnCLick en cada elemto de la lista
		list.setOnItemClickListener(this);
		TextView titulo = (TextView)findViewById(R.id.titulo);
		titulo.setText("Detalles");
		Button btn = (Button) findViewById(R.id.agregaOrden);
		btn.setOnClickListener(new  View.OnClickListener(){
        	public void onClick(View view){
        		Intent intent = new Intent(view.getContext(), Lista.class);
                startActivity(intent);
        	}
        });
		regresa = (Button)findViewById(R.id.regresabtn);

		regresa.setOnClickListener(new  View.OnClickListener(){
        	public void onClick(View view){
        		Detalles.this.finish();
        	}
        });
		
		name.setOnClickListener(new  View.OnClickListener(){
        	public void onClick(View view){
        		showInfo();
        	}
        });
		
	}
	
	public void showInfo() {

		//se crea una nueva alerta de dialogo
		AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
		//se le asigna el titulo a la ventana de dialogo
		helpBuilder.setTitle("Cambiar nombre");

		//se toma el Layout Inflater
		LayoutInflater inflater = getLayoutInflater();
		//se toma el layout correspondiente a la ventana del pop up
		View TextField = inflater.inflate(R.layout.cambiar_nombre, null);
		//se asigna esa vista a la ventana de dialogo
		helpBuilder.setView(TextField);
		
		nombre = "Nombre";
		nameChange = (TextView)findViewById(R.id.nameChange);

		//para manejar la acción del boton OK, de la ventana de dialogo
		helpBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				name.setText( nameChange.getText() );
			}
		});

		// Se crea la ventana de dialogo
		AlertDialog helpDialog = helpBuilder.create();
		//se muestra la ventana de dialogo
		helpDialog.show();
	}

	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
