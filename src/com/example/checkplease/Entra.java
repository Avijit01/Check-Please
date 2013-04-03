package com.example.checkplease;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

		total.setVisibility(EditText.INVISIBLE);
		propina.setVisibility(EditText.INVISIBLE);
		personas.setVisibility(EditText.INVISIBLE);
		TextView titulo = (TextView)findViewById(R.id.titulo);
		titulo.setText("Forma de Pago");
		regresa.setOnClickListener(new  View.OnClickListener(){
        	public void onClick(View view){
        		Entra.this.finish();
        	}
        });
		igual.setOnClickListener(new  View.OnClickListener(){
        	public void onClick(View view){
        		total.setVisibility(view.VISIBLE);
        		propina.setVisibility(view.VISIBLE);
        		personas.setVisibility(view.VISIBLE);

        	}
        	
        });
		individual.setOnClickListener(new  View.OnClickListener(){
        	public void onClick(View view){
        		Intent intent = new Intent(view.getContext(), Lista.class);
                startActivity(intent);
        	}
        });
	}
}
