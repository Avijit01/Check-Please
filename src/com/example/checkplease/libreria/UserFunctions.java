
package com.example.checkplease.libreria;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;

/**
 * Clase que genera la extraccion de datos de la DB
 * @author Cesar
 *
 */
public class UserFunctions {
	
	private JSONParser jsonParser;
	//link donde se encuentra el php con las acciones a realizar
	private static String loginURL = "http://devlock.systheam.com/android_login_api/";
	private static String registerURL = "http://devlock.systheam.com/android_login_api/";
	//tad para la accion a realizar
	private static String login_tag = "login";
	private static String register_tag = "register";
	private static String usuarios_tag = "usuarios";
	private static String restaurante_tag = "restaurante";
	private static String updateMesa_tag = "mesa";
	
	// constructor
	public UserFunctions(){
		jsonParser = new JSONParser();
	}
	
	/**
	 * Funcion loginUser
	 * Envia al jsonParser, la funcion de login y los parametros para checar si se logea de maenra correcto
	 * @param email
	 * @param password
	 * @return	json: con los datos de email, usuario
	 * @see MainActivity.java: entrar
	 * */
	public JSONObject loginUser(String email, String password){
		//se construyen los parametros
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", login_tag));
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("password", password));
		JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
		return json;
	}
	
	/**
	 * funcion registerUser
	 * Envia al jsonParser, la funcion de registrar y los parametros para checar si se logea de maenra correcto
	 * @param name
	 * @param email
	 * @param password
	 * */
	public JSONObject registerUser(String name, String email, String password){
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", register_tag));
		params.add(new BasicNameValuePair("name", name));
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("password", password));
		JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
		return json;
	}
	public JSONObject usuarios(){
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", usuarios_tag));
		JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
		return json;
	}
	public JSONObject agregaRestaurante(String restaurante, int tipo){
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", restaurante_tag));
		params.add(new BasicNameValuePair("restaurante", restaurante));
		params.add(new BasicNameValuePair("tipo", Integer.toString(tipo)));
		JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
		return json;
	}
	public JSONObject agregaUsuarioMesa(int mesa, String nombre, String id, String uid){
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "agregaUsuarioMesa"));
		params.add(new BasicNameValuePair("mesa", Integer.toString(mesa)));
		params.add(new BasicNameValuePair("nombre", nombre));
		params.add(new BasicNameValuePair("id", id));
		params.add(new BasicNameValuePair("uid", uid));
		JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
		return json;
	}
	public JSONObject eliminaUsuarioMesa(int mesa, int idSistema){
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "eliminaUsuarioMesa"));
		params.add(new BasicNameValuePair("mesa", Integer.toString(mesa)));
		params.add(new BasicNameValuePair("idSistema", Integer.toString(idSistema)));
		JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
		return json;
	}
	public JSONObject obtenerUsuarioMesa(int mesa){
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "obtenerUsuarioMesa"));
		params.add(new BasicNameValuePair("mesa", Integer.toString(mesa)));
		JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
		return json;
	}
	public JSONObject obtenerMesasUsuario(String idUsr){
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "obtenerMesasUsuario"));
		params.add(new BasicNameValuePair("idUsr", idUsr));
		JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
		return json;
	}
	public JSONObject updateMesa(int mesa, float total, float propina, int personas){
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", updateMesa_tag));
		params.add(new BasicNameValuePair("mesa", Integer.toString(mesa)));
		params.add(new BasicNameValuePair("total", Float.toString(total)));
		params.add(new BasicNameValuePair("propina", Float.toString(propina)));
		params.add(new BasicNameValuePair("personas",Integer.toString(personas)));
		JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
		return json;
	}
	public JSONObject updateAmigos(int mesa, String  amigos){
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "updateAmigos"));
		params.add(new BasicNameValuePair("mesa", Integer.toString(mesa)));
		params.add(new BasicNameValuePair("amigos", amigos));
		JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
		return json;
	}
	public JSONObject getInfoMesa(int mesa){
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "getInfoMesa"));
		params.add(new BasicNameValuePair("mesa", Integer.toString(mesa)));
		JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
		return json;
	}	
	public JSONObject getAmigos(int mesa){
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "getAmigos"));
		params.add(new BasicNameValuePair("mesa", Integer.toString(mesa)));
		JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
		return json;
	}
	
	public JSONObject guardaLista(int mesa, int idSistema, String nombre, float total, int isPaid, String path, int posicion, String uid){
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "guardaLista"));
		params.add(new BasicNameValuePair("mesa", Integer.toString(mesa)));
		params.add(new BasicNameValuePair("idSistema", Integer.toString(idSistema)));
		params.add(new BasicNameValuePair("nombre", nombre));
		params.add(new BasicNameValuePair("total", Float.toString(total)));
		params.add(new BasicNameValuePair("pago", Integer.toString(isPaid)));
		params.add(new BasicNameValuePair("path", path));
		params.add(new BasicNameValuePair("posicion", Integer.toString(posicion)));
		params.add(new BasicNameValuePair("uid", uid));

		JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
		return json;
	}
	
	/**
	 * funcion isUserLoggedIn
	 * Checa en la base de datos local si tiene iniciada la sesion el usuario
	 * @param context
	 * @return boolean
	 * */
	public boolean isUserLoggedIn(Context context){
		DatabaseHandler db = new DatabaseHandler(context);
		int count = db.getRowCount();
		if(count > 0){
			// usuario registrado
			return true;
		}
		return false;
	}
	public HashMap<String, String> getUsuarioId(Context context){
		DatabaseHandler db = new DatabaseHandler(context);
			return db.getUserDetails();
	}
	
	/**
	 * Funcion logoutUser
	 * Borra la base da datos que exista
	 * @param context
	 * @return boolean 
	 * */
	public boolean logoutUser(Context context){
		DatabaseHandler db = new DatabaseHandler(context);
		db.resetTables();
		return true;
	}
	
}
