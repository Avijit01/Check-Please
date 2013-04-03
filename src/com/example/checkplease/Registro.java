package com.example.checkplease;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Registro extends Activity{
	private Button regresa, registro;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registro);
		TextView titulo = (TextView)findViewById(R.id.titulo);
		titulo.setText("Registro");
		regresa = (Button)findViewById(R.id.regresabtn);
		registro = (Button)findViewById(R.id.registrobtn);

		regresa.setOnClickListener(new  View.OnClickListener(){
        	public void onClick(View view){
        		Registro.this.finish();
        	}
        });
		registro.setOnClickListener(new  View.OnClickListener(){
        	public void onClick(View view){
        		Registro.this.finish();
				Toast.makeText(getApplicationContext(),"Bienvenido",Toast.LENGTH_SHORT).show();
        		Intent intent = new Intent(view.getContext(), Entra.class);
                startActivity(intent);
        	}
        });
	}

}
