package com.example.checkplease;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Acerca extends Activity{
	private Button regresa;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acerca_check);
		TextView titulo = (TextView)findViewById(R.id.titulo);
		titulo.setText("Acerca");
		regresa = (Button)findViewById(R.id.regresabtn);

		regresa.setOnClickListener(new  View.OnClickListener(){
        	public void onClick(View view){
        		Acerca.this.finish();
        	}
        });
	}

}
