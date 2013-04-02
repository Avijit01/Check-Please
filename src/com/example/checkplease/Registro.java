package com.example.checkplease;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Registro extends Activity{
	private Button regresa;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registro);
		TextView titulo = (TextView)findViewById(R.id.titulo);
		titulo.setText("Registro");
		regresa = (Button)findViewById(R.id.regresabtn);
		regresa.setOnClickListener(new  View.OnClickListener(){
        	public void onClick(View view){
        		Registro.this.finish();
        	}
        });
	}

}
