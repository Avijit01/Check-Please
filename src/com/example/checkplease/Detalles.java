package com.example.checkplease;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

public class Detalles extends Activity implements OnItemClickListener, OnClickListener{
	
	List<String> precios = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detalles);
		
		String precios[] = {"10","20","30"};
		
		//se declara la lista asociada con la lista del layout
		ListView list = (ListView) findViewById(R.id.preciosList);
		//se crea el adapter para llenar los elemtnos de la lista con los datos de frutas
		LazyAdapter adapter = new LazyAdapter(this, precios);
		//se agrega los elementos a la lista
		list.setAdapter( adapter );
		//se habilita el evente OnCLick en cada elemto de la lista
		list.setOnItemClickListener(this);
		
		Button btn = (Button) findViewById(R.id.agregaOrden);
		btn.setOnClickListener(new  View.OnClickListener(){
        	public void onClick(View view){
        		Intent intent = new Intent(view.getContext(), MainActivity.class);
                startActivity(intent);
        	}
        });
		
		/*Spinner spinner = (Spinner)findViewById(R.id.spinner);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(this,
		        R.array.item_array, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);*/
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
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
