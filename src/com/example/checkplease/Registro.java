package com.example.checkplease;


import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.checkplease.libreria.DatabaseHandler;
import com.example.checkplease.libreria.UserFunctions;


import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.OnNavigationListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Registro extends Activity{
	private Button regresa, registro;
	EditText user, pass, passcon, mail;
	TextView mensajeError;
	RelativeLayout header;
	private static String KEY_SUCCESS = "success";
	private static String KEY_ERROR = "error";
	private static String KEY_ERROR_MSG = "error_msg";
	private static String KEY_UID = "uid";
	private static String KEY_NAME = "name";
	private static String KEY_EMAIL = "email";
	private static String KEY_CREATED_AT = "created_at";
	UserFunctions userFunctions = new UserFunctions();//carga la case userFunctions

		@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registro);
		
		registro = (Button)findViewById(R.id.registrobtn);
		user = (EditText) findViewById(R.id.user);
		pass = (EditText) findViewById(R.id.pss);
		passcon = (EditText) findViewById(R.id.pssConf);
		mail = (EditText) findViewById(R.id.mail);
		mensajeError = (TextView)findViewById(R.id.error);
		header = (RelativeLayout)findViewById(R.id.header);

		 
       	
		registro.setOnClickListener(new  View.OnClickListener(){
        	public void onClick(View view){
        		String nombre = user.getText().toString();
				String email = mail.getText().toString();
				String password = pass.getText().toString();
				String passwordcon = passcon.getText().toString();
				passcon.setBackgroundResource(R.drawable.blanco_btn);
    			pass.setBackgroundResource(R.drawable.blanco_btn);
    			mail.setBackgroundResource(R.drawable.blanco_btn);
    			user.setBackgroundResource(R.drawable.blanco_btn);
				if(nombre.equals("")||email.equals("")||password.equals("")||passwordcon.equals(""))
				{
					mensajeError.setText("Completar datos necesarios");
					if(passwordcon.equals(""))
	        		{		        			
	        			passcon.setBackgroundResource(R.drawable.rojo_btn);
        				passcon.requestFocus ();
	        		}
					if(password.equals(""))
	        		{		        			
	        			pass.setBackgroundResource(R.drawable.rojo_btn);
        				pass.requestFocus ();
	        		}
					if(email.equals(""))
	        		{		        			
	        			mail.setBackgroundResource(R.drawable.rojo_btn);
        				mail.requestFocus ();
	        		}
					if(nombre.equals(""))
	        		{		        			
	        			user.setBackgroundResource(R.drawable.rojo_btn);
        				user.requestFocus ();
	        		}
				}
				else if(password.equals(passwordcon)){
					mensajeError.setText("");
					passcon.setBackgroundResource(R.drawable.blanco_btn);
        			pass.setBackgroundResource(R.drawable.blanco_btn);
        			mail.setBackgroundResource(R.drawable.blanco_btn);
        			user.setBackgroundResource(R.drawable.blanco_btn);
					UserFunctions userFunction = new UserFunctions();
					JSONObject json = userFunction.registerUser(nombre, email, password);
					try {
						if (json.getString(KEY_SUCCESS) != null) {
							//registerErrorMsg.setText("");
							String res = json.getString(KEY_SUCCESS); 
							String error_msg = json.getString(KEY_ERROR); 
							if(Integer.parseInt(error_msg) == 3){
								mensajeError.setText("El user ya existe");	
							}else 
								if(Integer.parseInt(error_msg) == 2){
									mensajeError.setText("El mail ya existe");	
								}else 
							if(Integer.parseInt(res) == 1){
								// user successfully registred
								// Store user details in SQLite Database
								DatabaseHandler db = new DatabaseHandler(getApplicationContext());
								JSONObject json_user = json.getJSONObject("user");
								
								// Clear all previous data in database
								userFunction.logoutUser(getApplicationContext());
								db.addUser(json_user.getString(KEY_NAME), json_user.getString(KEY_EMAIL), json.getString(KEY_UID), json_user.getString(KEY_CREATED_AT));						
								// Launch Dashboard Screen
								Registro.this.finish();
								Toast.makeText(getApplicationContext(),"Bienvenido",Toast.LENGTH_SHORT).show();
				        		
								Intent dashboard = new Intent(getApplicationContext(), Entra.class);
								// Close all views before launching Dashboard
								dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(dashboard);
								// Close Registration Screen
								finish();
							}else{
								// Error in registration
								//registerErrorMsg.setText("Error occured in registration");
							}
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}else{
					
        			mail.setBackgroundResource(R.drawable.blanco_btn);
        			user.setBackgroundResource(R.drawable.blanco_btn);
					mensajeError.setText("Las contraseñas no coinciden");
					
				}
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
        case android.R.id.home://se cierra el menu
			Registro.this.finish();
			return true;
        
        default:
        return super.onOptionsItemSelected(item);
    }
    }
    private void facebook() {
		startActivity(new Intent(this, Facebook.class));
	}
	private void Inicio() {
		startActivity(new Intent(this, MainActivity.class));
	}
	private void Acerca(){
		startActivity(new Intent(this, Acerca.class));
		
	}
	@Override
	protected void onResume() {
	    super.onResume();
	    cargaMenu();
	    // Normal case behavior follows
	}
	void cargaMenu(){
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
	    actionBar.setBackgroundDrawable(getResources().getDrawable(
	            R.drawable.bar_color));
	    actionBar.setTitle("Registro   ");
	    
	    ArrayList<String> actions = new ArrayList<String>();//arreglo que guardara las acciones de menu del action bar
	    //agrega las opciones al menu
		actions.add("Opciones");
		actions.add("Cerrar Sesion");
		actions.add("Facebook");
		actions.add("Acerca");
		//Crea el adaptar del dropDown del header
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, actions);
        //Habilita la navegacion del DropDown del action bar
        getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        //Degine la navegacion del dropdown
        
        ActionBar.OnNavigationListener navigationListener = new OnNavigationListener() {
			
        	@Override
			public boolean onNavigationItemSelected(int itemPosition, long itemId) {				
        			if(itemPosition==1){//opcion de cerrar cesion
						userFunctions.logoutUser(getApplicationContext());
						Inicio();
						return true;
					}
					if(itemPosition==2){//opcion de facebook
						facebook();
						return true;	
					}
					if(itemPosition==3){//opcion de acerca
						Acerca();
					}
				return false;
        	}
		};
		//set los elementos del dropdown del actionbar
		getActionBar().setListNavigationCallbacks(adapter, navigationListener); 
		
	}
	
}
