package com.example.checkplease;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Entra extends Activity {

	EditText etTotal;
	EditText etPropina;
	EditText etPersonas;
	TextView tvPagoPorPersona;
	RelativeLayout divIgual;

	private Button regresa, igual, individual;
	private float total, propina;
	private int personas;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forma_de_pago);
		regresa = (Button)findViewById(R.id.regresabtn);
		igual = (Button)findViewById(R.id.igual);
		individual = (Button)findViewById(R.id.individual);

		etTotal = (EditText)findViewById(R.id.total);
		etPropina = (EditText)findViewById(R.id.propina);
		etPersonas = (EditText)findViewById(R.id.personas);
		tvPagoPorPersona = (TextView)findViewById(R.id.pagoPorPersona);
		divIgual = (RelativeLayout)findViewById(R.id.divIgual);

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

		etTotal.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence seq, int start, int before, int count) {
				try {
					total = Float.parseFloat(etTotal.getText().toString());
				} catch(Exception e) {
					total = 0.0f;
				}
				getTotalPerPerson();
			}

			@Override
			public void afterTextChanged(Editable arg0) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}
		});

		etPropina.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence seq, int start, int before, int count) {
				try {
					propina = Float.parseFloat(etPropina.getText().toString());
				} catch (Exception e) {
					propina = 0.0f;
				}
				getTotalPerPerson();
			}

			@Override
			public void afterTextChanged(Editable arg0) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}
		});

		etPersonas.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence seq, int start, int before, int count) {
				try {
					personas = Integer.parseInt(etPersonas.getText().toString());
				} catch (Exception e) {
					personas = 0;
				}
				getTotalPerPerson();
			}

			@Override
			public void afterTextChanged(Editable arg0) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

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

	/**
	 * Calcula el total de cada persona
	 */
	private void getTotalPerPerson() {
		float ppp = (total + (total * (propina / 100.0f))) / personas;
		if(!Float.isNaN(ppp) && !Float.isInfinite(ppp))
			tvPagoPorPersona.setText("$" + String.valueOf(ppp) + " por persona");
	}
}
