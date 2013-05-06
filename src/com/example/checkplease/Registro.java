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
	//Inicializa botones, funciones y textos
	private Button regresa, registro;
	EditText user, pass, passcon, mail;
	TextView mensajeError, mensajeRegistro;
	RelativeLayout header;
	String vieneDe = "";
	// JSON Respuestas que se tienen
	private static String KEY_SUCCESS = "success";
	private static String KEY_ERROR = "error";
	private static String KEY_ERROR_MSG = "error_msg";
	private static String KEY_UID = "uid";
	private static String KEY_NAME = "name";
	private static String KEY_EMAIL = "email";
	private static String KEY_CREATED_AT = "created_at";
	UserFunctions userFunctions = new UserFunctions();//carga la case userFunctions

	/**
	 * Metodo que maneja la actividad de Registro, dar de alta en servidor y validaciones correspondientes
	 * Se manda llamar al entrar a la actividad
	 */
		@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registro);
		//Obtiene los elemntos de la vista
		registro = (Button)findViewById(R.id.registrobtn);
		user = (EditText) findViewById(R.id.user);
		user.requestFocus ();

		pass = (EditText) findViewById(R.id.pss);
		passcon = (EditText) findViewById(R.id.pssConf);
		mail = (EditText) findViewById(R.id.mail);
		mensajeError = (TextView)findViewById(R.id.error);
		mensajeRegistro = (TextView)findViewById(R.id.textView1);
		header = (RelativeLayout)findViewById(R.id.header);

		Bundle extras = getIntent().getExtras(); //si tiene parametos que envio la actividad Main
		if(extras !=null){//se agarra el parametro "position" y se le asigna la variable post
			if(extras.getString("viene").equals("inicio")){
				vieneDe = "inicio";
			}else vieneDe = "nada";
		}else vieneDe = "nada";
       	//al dar click a registro obtiene obtiene los string de los campos y valida
		if(vieneDe.equals("inicio")){
			mensajeRegistro.setVisibility(RelativeLayout.VISIBLE);
		}else{
			mensajeRegistro.setVisibility(RelativeLayout.INVISIBLE);
		}
			
		mensajeRegistro.setOnClickListener(new  View.OnClickListener(){
        	public void onClick(View view){
        		Intent dashboard = new Intent(getApplicationContext(), MainActivity.class);
				dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(dashboard);
				finish();//cierra la actividad
        	}
		});
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

    			//si alguno de ellos esta en blanco, entra
				if(nombre.equals("")||email.equals("")||password.equals("")||passwordcon.equals(""))
				{//mensaje de error de completar los datos necesarios
					mensajeError.setText("Completar datos necesarios");
					if(passwordcon.equals(""))// falta pass, la pone en rojo y focus
	        		{		        			
	        			passcon.setBackgroundResource(R.drawable.rojo_btn);
        				passcon.requestFocus ();
	        		}
					if(password.equals(""))//si falta la otra pass, la pone en rojo y focus
	        		{		        			
	        			pass.setBackgroundResource(R.drawable.rojo_btn);
        				pass.requestFocus ();
	        		}
					if(email.equals(""))//si falta el mail lo pone en rojo y focus
	        		{		        			
	        			mail.setBackgroundResource(R.drawable.rojo_btn);
        				mail.requestFocus ();
	        		}
					if(nombre.equals(""))//si falta el nombre, lo pone en rojo y focus
	        		{		        			
	        			user.setBackgroundResource(R.drawable.rojo_btn);
        				user.requestFocus ();
	        		}
				}
				else if(password.equals(passwordcon)){//si todo esta completo valida que sean iguaes las contraseñas
					mensajeError.setText("");//pone todo en blanco
					passcon.setBackgroundResource(R.drawable.blanco_btn);
        			pass.setBackgroundResource(R.drawable.blanco_btn);
        			mail.setBackgroundResource(R.drawable.blanco_btn);
        			user.setBackgroundResource(R.drawable.blanco_btn);
					UserFunctions userFunction = new UserFunctions();//obitnee las funciones que se crearon
					JSONObject json = userFunction.registerUser(nombre, email, password);//manda llamar a la funcion registrar Usuario
					try {//si la llave de registro es difrente de cero
						if (json.getString(KEY_SUCCESS) != null) {
							String res = json.getString(KEY_SUCCESS); 
							String error_msg = json.getString(KEY_ERROR); 
							if(Integer.parseInt(error_msg) == 3){//si es 3 el usario ya existe
								mensajeError.setText("El user ya existe");	
							}else 
								if(Integer.parseInt(error_msg) == 2){//si es 2 el mail ya existe
									mensajeError.setText("El mail ya existe");	
								}else 
							if(Integer.parseInt(res) == 1){//si es uno el succes, entro con exito
								//se crea la base de datos interna
								DatabaseHandler db = new DatabaseHandler(getApplicationContext());
								JSONObject json_user = json.getJSONObject("user");
								// Clear all previous data in database
								userFunction.logoutUser(getApplicationContext());
								//se agrega el usairo con su nombre, mail, id y datos necesario
								db.addUser(json_user.getString(KEY_NAME), json_user.getString(KEY_EMAIL), json.getString(KEY_UID), json_user.getString(KEY_CREATED_AT));						
								// Termina la ccion de Registro, con fines de que no vuelva a ella al darle regresar
								Registro.this.finish();
				        		//abre la clase entra
								if(vieneDe.equals("inicio")){
									Intent dashboard = new Intent(getApplicationContext(), Facebook.class);
									dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
									startActivity(dashboard);
									finish();//cierra la actividad

								}else{
								Intent dashboard = new Intent(getApplicationContext(), Entra.class);
								dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(dashboard);
								finish();//cierra la actividad
								}
							}else{//error en la conexion
								mensajeError.setText("Ocurrio un error en registro, intentar nuevamente");
							}
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}else{//si las contras no coinciden
        			mail.setBackgroundResource(R.drawable.blanco_btn);
        			user.setBackgroundResource(R.drawable.blanco_btn);
					mensajeError.setText("Las contraseñas no coinciden");
					
				}
        	}
        });
	}
		/**
	     * Metodo: onCreateOptionsMenu(),
	     * Metodo que agrega las opciones que se hicieron en menu->main.xml
	     * @param menu
	     * @return bolean, true se hizo corectamente
	     */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	/**
	 * Metodo que maneja las respuesta de selccionar una aprte del menu o elemento de android
	 *@param item
	 *elemento que se selecciono
	 */
    public boolean onOptionsItemSelected(MenuItem item) {
        //respond to menu item selection
    	switch (item.getItemId()) {
        case R.id.acerca://se abre la activad de acerca
        	startActivity(new Intent(this, Acerca.class));
        return true;
        case android.R.id.home://la activad actual
			Registro.this.finish();
			return true;
        
        default:
        return super.onOptionsItemSelected(item);
    }
    }
    /**
     * Metodo: facebook,
     * Metodo que realiza la accion de abrir la actividad de Facebook
     */
    private void facebook() {
		startActivity(new Intent(this, Facebook.class));
	}
    /**
     * Metodo: Inicio,
     * Metodo que realiza la accion de abrir la actividad de Inicio
     */
	private void Inicio() {
		startActivity(new Intent(this, MainActivity.class));
	}
	/**
     * Metodo: Acerca,
     * Metodo que realiza la accion de abrir la actividad de Acerca
     */
	private void Acerca(){
		startActivity(new Intent(this, Acerca.class));
		
	}
	/**
     * Metodo: onResume,
     * Metodo que se manda llamar al regresar a la activadad desde otra activdad o desde otra app
     * carga nuevamente el Menu para reinicar los valores en cero
     */
	@Override
	protected void onResume() {
	    super.onResume();
	    cargaMenu();
	    // Normal case behavior follows
	}
	/**
     * Metodo: cargaMenu(),
     * Metodo que personaliza la vista del ActionBar con el color, titulo, y opciones
     */
	void cargaMenu(){
		ActionBar actionBar = getActionBar();//obtiene el ActionBar
	    actionBar.setDisplayHomeAsUpEnabled(true);//habilita la opcion de regresar a la actividad anterios
	    actionBar.setBackgroundDrawable(getResources().getDrawable(
	            R.drawable.bar_color));//pone color gris
	    actionBar.setTitle("Registrar     ");//pone el titulo
	    
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
