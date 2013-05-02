package com.example.checkplease;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.example.checkplease.libreria.DatabaseHandler;
import com.example.checkplease.libreria.UserFunctions;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.widget.UserSettingsFragment;

import com.facebook.widget.*;
import com.facebook.*;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphPlace;
import com.facebook.model.GraphUser;

/**
 * Clase Inicial que permite registrar y entrar con el sistema y facebook
 * Extiende a la clase FragmentActivity
 * Si ya esta logeado entra al sistema a la clase Entra.java
 * @see Checkplease.libreria 
 *
 */
public class MainActivity extends FragmentActivity   {
	//Inicializa botones, funciones y textos
	private Button entrar, registrar, facebook;
	EditText usuario, pass, passcon;
	UserFunctions userFunctions;
	TextView mensajeError;
	private boolean isOnline;

	// JSON Respuestas que se tienen
	private static String KEY_SUCCESS = "success";
	private static String KEY_ERROR = "error";
	private static String KEY_ERROR_MSG = "error_msg";
	private static String KEY_UID = "uid";
	private static String KEY_NAME = "name";
	private static String KEY_EMAIL = "email";
	private static String KEY_CREATED_AT = "created_at";

	/**
	 * Metodo onCreate()
	 * Manejara la funcionalidad de entar al sistema
	 * Se manda llamar al entrar a la actividad
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		userFunctions = new UserFunctions();
		//Permite usar algunas acciones que son restinguidas
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
		//checa en la base de datos local si esta logeado, y se habre la clase Entra
		if(userFunctions.isUserLoggedIn(getApplicationContext())){
			HashMap<String, String> user = userFunctions.getUsuarioId(getApplicationContext());
			Intent intent = new Intent(this, Entra.class);
			intent.putExtra("logeado",1);//envia el parametro de que esta logeado
			intent.putExtra("nombre",(String)user.get("name"));
			intent.putExtra("mesa",Integer.parseInt(user.get("mesa")));
			// Log.e("id", (String)user.get("uid"));
			startActivity(intent);
		} else {//sino se habre la calse principal
			setContentView(R.layout.activity_main);
			//se importan todos los elementos como butones, textos o edits
			usuario = (EditText) findViewById(R.id.usuario);
			usuario.requestFocus ();
			pass = (EditText) findViewById(R.id.pass);
			entrar = (Button)findViewById(R.id.entrarbtn);

			// Revisa si hay conexion a internet (Wifi o red movil)
			ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if ((wifi != null && wifi.isConnectedOrConnecting()) || (mobile != null && mobile.isConnectedOrConnecting())) {
				isOnline = true;
			} else {
				isOnline = false;
			}

			// No hay conexion a internet
			if(!isOnline) {
				Intent intent = new Intent(this, Entra.class);
				intent.putExtra("logeado", 0);//envia el parametro de que esta logeado
				intent.putExtra("nombre", "Yo");
				intent.putExtra("mesa", 0);
				intent.putExtra("online", "false");
				startActivity(intent);
			}
			//checa en la base de datos local si esta logeado, y se habre la clase Entra
			else if(userFunctions.isUserLoggedIn(getApplicationContext())){
				HashMap<String, String> user = userFunctions.getUsuarioId(getApplicationContext());
				Intent intent = new Intent(this, Entra.class);
				intent.putExtra("logeado",1);//envia el parametro de que esta logeado
				intent.putExtra("nombre",(String)user.get("name"));
				intent.putExtra("mesa",Integer.parseInt(user.get("mesa")));
				intent.putExtra("online", "true");
				startActivity(intent);
			} else {//sino se habre la calse principal
				setContentView(R.layout.activity_main);
				//se importan todos los elementos como butones, textos o edits
				usuario = (EditText) findViewById(R.id.usuario);
				usuario.requestFocus ();
				pass = (EditText) findViewById(R.id.pass);
				entrar = (Button)findViewById(R.id.entrarbtn);
				registrar = (Button)findViewById(R.id.registrobtn);
				facebook = (Button)findViewById(R.id.facebookbtn);
				mensajeError = (TextView)findViewById(R.id.error);
				mensajeError.setText("");

				//boton de entrar Click Event
				entrar.setOnClickListener(new  View.OnClickListener(){
					public void onClick(View view){
						String user = usuario.getText().toString();
						String password =  pass.getText().toString();
						//checa que los campos no sean vacios
						if(user.equals("")||password.equals(""))//si hay vacios
						{
							mensajeError.setText("Completar: ");
							if(user.equals(""))//si el campo usuarios es vacio
							{//se marca en rojo y se despliega mensaje       			
								usuario.setBackgroundResource(R.drawable.rojo_btn);
								mensajeError.setText(mensajeError.getText() + "Usuario ");
								usuario.requestFocus ();

							} else {usuario.setBackgroundResource(R.drawable.blanco_btn);}
							if(password.equals(""))//si el campo password es  vacio
							{//se marca en rojo y se despliega mensaje
								pass.setBackgroundResource(R.drawable.rojo_btn);
								mensajeError.setText(mensajeError.getText() + " Contraseña");
								if(!user.equals("")){//si el usuario no es null se marca el de pass en focus
									pass.requestFocus ();
								}
							} else {pass.setBackgroundResource(R.drawable.blanco_btn);}
						}
						else{//si no son null, se pone todo en blanco y el mensaje se borra
							usuario.setBackgroundResource(R.drawable.blanco_btn);
							pass.setBackgroundResource(R.drawable.blanco_btn);
							mensajeError.setText("");
							//se manda llamar el metodo loginUser de la clase userFunctions
							JSONObject json = new JSONObject();
							// check la respuesta del login
							try {
								json = userFunctions.loginUser(user, password);//si la respuesta de KEY_Succes contiene algo
								if (json.getString(KEY_SUCCESS) != null) {
									String res = json.getString(KEY_SUCCESS); 
									if(Integer.parseInt(res) == 1){//si se accedio
										//se crea de manera local(celular) el usuario 
										DatabaseHandler db = new DatabaseHandler(getApplicationContext());
										JSONObject json_user = json.getJSONObject("user");
										userFunctions.logoutUser(getApplicationContext());
										//se agrega el usuario a la base de datos con el nombre, email, id
										db.addUser(json_user.getString(KEY_NAME), json_user.getString(KEY_EMAIL), json.getString(KEY_UID), json_user.getString(KEY_CREATED_AT));						
										//se manda a la clase Entra
										//Intent intent = new Intent(view.getContext(), Entra.class);
										//HashMap<String, String> user2 = userFunctions.getUsuarioId(getApplicationContext());
										//intent.putExtra("nombre",(String)user2.get("name"));
										//intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
										//startActivity(intent);

									}else{
										// Error in login
										mensajeError.setText("Usuario y/o contraseña incorrectos");
									}
								}
							} catch (JSONException e) {
								//e.printStackTrace();
								Log.d("JSON", json.toString());
							}
						}
					}
				});
				//al darle click al boton de registrar, se inicia la activad registrar
				registrar.setOnClickListener(new  View.OnClickListener(){
					public void onClick(View view){
						Intent intent = new Intent(view.getContext(), Registro.class);
						startActivity(intent);
					}
				});
				//al darle click al boton de facebook, se inicia la activad de facebook
				facebook.setOnClickListener(new  View.OnClickListener(){
					public void onClick(View view){
						Intent intent = new Intent(view.getContext(), Facebook.class);
						startActivity(intent);	        		
					}
				});
			}
		}
	}
	/**
	 * Metodo: onCreateOptionsMenu(),
	 * Metodo que agrega las opciones que se hicieron en menu->main.xml
	 * @param menu
	 * @return bolean, true se hizo corectamente
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//Agrega el menu que se creo para el acerca
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	/**
	 * Metodo: onOptionsItemSelected(),
	 * Metodo que le asigna una accion a realizar a cada opcion de android que se seleccione en android
	 * @param item
	 * @return bolean, true se hizo corectamente
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		//responde a la seleccion del menu
		switch (item.getItemId()) {//valor a switchear
		case R.id.acerca://se abre la clase Acerca al seleccionarla
			startActivity(new Intent(this, Acerca.class));
			return true;	 	
		default:
			return super.onOptionsItemSelected(item);//si no accion, no hace nada
		}
	}


}
