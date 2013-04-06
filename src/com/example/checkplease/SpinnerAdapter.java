/*package com.example.checkplease;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class SpinnerAdapter extends Activity implements
		AdapterView.OnItemSelectedListener {
	TextView selection;
	String[] items = { "platillo", "postre", "refresco", "cerveza"};

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.detalles_item);

		Spinner spin = (Spinner) findViewById(R.id.spinner);
		spin.setOnItemSelectedListener(this);

		ArrayAdapter aa = new ArrayAdapter(
				this,
				android.R.layout.simple_spinner_item, 
				items);

		aa.setDropDownViewResource(
		   android.R.layout.simple_spinner_dropdown_item);
		spin.setAdapter(aa);
	}

	public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
	}

	public void onNothingSelected(AdapterView<?> parent) {
	}
}*/