package com.example.checkplease;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class Lista  extends Activity{
	
	EditText etTip;
	Button regresa;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lista_usuarios);
		
		regresa = (Button)findViewById(R.id.regresabtn);
        EditText etTotal = (EditText)findViewById(R.id.etTotal);

		etTip = (EditText)findViewById(R.id.etTip);
		ArrayList<Person> usuarios = new ArrayList<Person>();
		usuarios.add(new Person("Derp", 500, false));
		usuarios.add(new Person("Hurr", 200, true));
		PersonAdapter adapter = new PersonAdapter(this, R.layout.lista_usuarios_item, usuarios, Float.parseFloat(etTip.getText().toString()));
		ListView layout = (ListView)findViewById(R.id.lvUsuarios);
		layout.setAdapter(adapter);
		regresa.setOnClickListener(new  View.OnClickListener(){
        	public void onClick(View view){
        		Lista.this.finish();
        	}
        });
		
	}
}
