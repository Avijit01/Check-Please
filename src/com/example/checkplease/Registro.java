package com.example.checkplease;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Registro extends Activity{
	private Button regresa;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registro);
		regresa = (Button)findViewById(R.id.regresabtn);
		regresa.setOnClickListener(new  View.OnClickListener(){
        	public void onClick(View view){
        		Registro.this.finish();
        	}
        });
	}

}
