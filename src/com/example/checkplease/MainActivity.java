package com.example.checkplease;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
	private Button entrar;
	private Button registrar, facebook;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		entrar = (Button)findViewById(R.id.entrarbtn);
		registrar = (Button)findViewById(R.id.registrobtn);
		facebook = (Button)findViewById(R.id.facebookbtn);
		entrar.setOnClickListener(new  View.OnClickListener(){
			public void onClick(View view){
				Intent intent = new Intent(view.getContext(), Entra.class);
				startActivity(intent);
			}
		});
		registrar.setOnClickListener(new  View.OnClickListener(){
			public void onClick(View view){
				Intent intent = new Intent(view.getContext(), Registro.class);
				startActivity(intent);
			}
		});
		facebook.setOnClickListener(new  View.OnClickListener(){
			public void onClick(View view){

				Toast.makeText(getApplicationContext(),"Se desplegara con funcionalidad la ventana de Facebook",Toast.LENGTH_SHORT).show();

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
