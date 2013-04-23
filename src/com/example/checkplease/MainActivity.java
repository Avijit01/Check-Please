package com.example.checkplease;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.json.JSONException;
import org.json.JSONObject;


import com.example.checkplease.libreria.DatabaseHandler;
import com.example.checkplease.libreria.UserFunctions;

import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
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

public class MainActivity extends FragmentActivity   {

	/*private static final String URL_PREFIX_FRIENDS = "https://graph.facebook.com/me/friends?access_token=";

    private TextView textInstructionsOrLink;
    private Button buttonLoginLogout;
    private Session.StatusCallback statusCallback = new SessionStatusCallback();*/
	/*public static final String APP_ID = "533323013384570";
	private MainFragment mainFragment;
	private static final String TAG = "MainFragment";
	private UiLifecycleHelper uiHelper;
*/
	private Button entrar;
	private Button registrar, facebook;
	EditText usuario, pass, passcon;
	UserFunctions userFunctions;
	TextView mensajeError;

	// JSON Respuestas que se tienen
		private static String KEY_SUCCESS = "success";
		private static String KEY_ERROR = "error";
		private static String KEY_ERROR_MSG = "error_msg";
		private static String KEY_UID = "uid";
		private static String KEY_NAME = "name";
		private static String KEY_EMAIL = "email";
		private static String KEY_CREATED_AT = "created_at";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		 userFunctions = new UserFunctions();
		 //Permite usar algunas acciones que son restinguidas
		 StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		 StrictMode.setThreadPolicy(policy); 
		 //checa en la base de datos local si esta logeado, y se habre la clase Entra
		 if(userFunctions.isUserLoggedIn(getApplicationContext())){
			 Intent intent = new Intent(this, Entra.class);
     		startActivity(intent);
		 }else{//sino se habre la calse principal
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
		        		if(user.equals("")||password.equals(""))
		        		{	mensajeError.setText("Completar: ");
			        		if(user.equals(""))
			        		{		        			
			        			usuario.setBackgroundResource(R.drawable.rojo_btn);
			        			mensajeError.setText(mensajeError.getText() + "Usuario ");
		        				usuario.requestFocus ();

			        		}else{usuario.setBackgroundResource(R.drawable.blanco_btn);}
			        		if(password.equals(""))
			        		{
			        			pass.setBackgroundResource(R.drawable.rojo_btn);
			        			mensajeError.setText(mensajeError.getText() + " Contraseña");
			        			if(!user.equals("")){
			        				pass.requestFocus ();
			        			}
			        			
			        		}else{pass.setBackgroundResource(R.drawable.blanco_btn);}
		        		}
		        		else{
		        			usuario.setBackgroundResource(R.drawable.blanco_btn);
		        			pass.setBackgroundResource(R.drawable.blanco_btn);
		        			mensajeError.setText("");

							JSONObject json = userFunctions.loginUser(user, password);
							// check la respuesta del login
							try {//si la respuesta de KEY_Succes contiene algo
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
										Intent intent = new Intent(view.getContext(), Entra.class);
										intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						        		startActivity(intent);
										
									}else{
										// Error in login
										mensajeError.setText("Usuario y/o contraseña incorrectos");
									}
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
		        		}
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
	        		Intent intent = new Intent(view.getContext(), Facebook.class);
	                startActivity(intent);	        		
	        	}
	        });
        }
		
		
		
		/*super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonLoginLogout = (Button)findViewById(R.id.facebookbtn);
        textInstructionsOrLink = (TextView)findViewById(R.id.error);

        Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);

        Session session = Session.getActiveSession();
        if (session == null) {
            if (savedInstanceState != null) {
                session = Session.restoreSession(this, null, statusCallback, savedInstanceState);
            }
            if (session == null) {
                session = new Session(this);
            }
            Session.setActiveSession(session);
            if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
                session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
            }
        }

        updateView();
		    
*/
	
		/*uiHelper = new UiLifecycleHelper(this, callback);
	    uiHelper.onCreate(savedInstanceState);
		if (savedInstanceState == null) {
			Toast.makeText(getApplicationContext(),"entana de Facebook",Toast.LENGTH_SHORT).show();

	        // Add the fragment on initial activity setup
	        mainFragment = new MainFragment();
	        getSupportFragmentManager().beginTransaction().add(android.R.id.content, mainFragment)
	        .commit();
	    } else {
			Toast.makeText(getApplicationContext(),"acebook",Toast.LENGTH_SHORT).show();

	        // Or set the fragment from restored state info
	        mainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(android.R.id.content);
	    }*/
	
		/*
				*/
		
	}/*
	  @Override
	    public void onStart() {
	        super.onStart();
	        Session.getActiveSession().addCallback(statusCallback);
	    }

	    @Override
	    public void onStop() {
	        super.onStop();
	        Session.getActiveSession().removeCallback(statusCallback);
	    }

	    @Override
	    public void onActivityResult(int requestCode, int resultCode, Intent data) {
	        super.onActivityResult(requestCode, resultCode, data);
	        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	    }

	    @Override
	    protected void onSaveInstanceState(Bundle outState) {
	        super.onSaveInstanceState(outState);
	        Session session = Session.getActiveSession();
	        Session.saveSession(session, outState);
	    }

	    private void updateView() {
	        Session session = Session.getActiveSession();
	        if (session.isOpened()) {
	        	Intent intent = new Intent(this, Entra.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        		startActivity(intent);
    			Toast.makeText(getApplicationContext(),session.getAccessToken(),Toast.LENGTH_SHORT).show();

	            buttonLoginLogout.setText("logout");
	            buttonLoginLogout.setOnClickListener(new OnClickListener() {
	                public void onClick(View view) { onClickLogout(); }
	            });
	            
	        } else {
	            textInstructionsOrLink.setText("instr");
	            buttonLoginLogout.setText("login");
	            buttonLoginLogout.setOnClickListener(new OnClickListener() {
	                public void onClick(View view) { onClickLogin(); }
	            });
	        }
	    }

	    private void onClickLogin() {
	        Session session = Session.getActiveSession();
	        if (!session.isOpened() && !session.isClosed()) {
	            session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
	        } else {
	        	
	            Session.openActiveSession(this, true, statusCallback);
	        }
	    }

	    private void onClickLogout() {
	        Session session = Session.getActiveSession();
	        if (!session.isClosed()) {
	            session.closeAndClearTokenInformation();
	        }
	    }

	    private class SessionStatusCallback implements Session.StatusCallback {
	        @Override
	        public void call(Session session, SessionState state, Exception exception) {
	            updateView();
	        }
	    }
	*/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
