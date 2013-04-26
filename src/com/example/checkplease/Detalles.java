package com.example.checkplease;

import java.util.ArrayList;
import java.util.List;


import com.example.checkplease.libreria.UserFunctions;

import android.net.Uri;
import android.os.Bundle;
import android.app.ActionBar;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ActionBar.OnNavigationListener;

import android.content.DialogInterface;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class Detalles extends Activity implements OnItemClickListener, OnClickListener{

	UserFunctions userFunctions = new UserFunctions();//carga la case userFunctions

	private List<String> precios = new ArrayList<String>();
	private Button agregar, terminar, okBtn;
	private TextView totalView, name;
	private EditText nameChange;
	private ImageView foto;
	private static final int SELECT_PICTURE = 1;
	private String path = "";
	private String nombrePref = "";
	private String total = "0.0";
	private LazyAdapter adapter;
	private ListView l;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detalles);
		
		Bundle extras = getIntent().getExtras(); //si tiene parametos que envio la actividad Main
		if(extras !=null){//se agarra el parametro "position" y se le asigna la variable post
			total = "" + extras.getFloat("Total");
			nombrePref = extras.getString("Nombre");
			Toast.makeText(getApplicationContext(),nombrePref,Toast.LENGTH_SHORT).show();
		}
		
		SharedPreferences prefs = getSharedPreferences("PREFS_KEY",Activity.MODE_PRIVATE);
        path = prefs.getString("path","");
        //nombrePref = prefs.getString("name", "");

		//String precios2[] = {"10","20","30"};
		
        totalView = (TextView)findViewById(R.id.total);
		name = (TextView)findViewById(R.id.name);
		foto = (ImageView)findViewById(R.id.foto);
		agregar = (Button)findViewById(R.id.agregar);
		terminar = (Button)findViewById(R.id.terminar);
		
		if( !path.equals("") )	foto.setImageBitmap( BitmapFactory.decodeFile(path));
		if( !nombrePref.equals("") ) name.setText(nombrePref);
		
		totalView.setText(total);
		precios.add(total);
		
		//se declara la lista asociada con la lista del layout
		l = (ListView) findViewById(R.id.preciosList);
		//se crea el adapter para llenar los elemtnos de la lista con los datos de frutas
		adapter = new LazyAdapter(this, precios);
		//se agrega los elementos a la lista
		l.setAdapter( adapter );
		//se habilita el evente OnCLick en cada elemto de la lista
		l.setOnItemClickListener(this);
		
		terminar.setOnClickListener(new  View.OnClickListener(){
        	public void onClick(View view){
        		Intent intent = new Intent(view.getContext(), Lista.class);
                startActivity(intent);
        	}
        });
		
		agregar.setOnClickListener(new  View.OnClickListener(){
        	public void onClick(View view){
        		float f = Float.parseFloat(precios.get(precios.size()-1)) - Float.parseFloat(total);
        		precios.add("" + f);
        		adapter = new LazyAdapter(Detalles.this, precios);
        		l.setAdapter( adapter );
        	}
        });
		
		name.setOnClickListener(new  View.OnClickListener(){
        	public void onClick(View view){
        		cambiarNombre();
        	}
        });
		foto .setOnClickListener( new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(Intent.createChooser(intent,  "Selecciona Imagen"), SELECT_PICTURE);
			}
		});
		
	}
	
	public void cambiarNombre() {
		//se crea una nueva alerta de dialogo
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		//se le asigna el titulo a la ventana de dialogo
		dialog.setTitle("Cambiar nombre");

		//se toma el Layout Inflater
		LayoutInflater inflater = getLayoutInflater();
		//se toma el layout correspondiente a la ventana del pop up
		View view = inflater.inflate(R.layout.cambiar_nombre, null);
		//se asigna esa vista a la ventana de dialogo
		dialog.setView(view);
		
		nameChange = (EditText)view.findViewById(R.id.nameChange);

		//para manejar la acción del boton OK, de la ventana de dialogo
		dialog.setPositiveButton("Ok",	new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if( !nameChange.getText().toString().equals("") )
					name.setText(nameChange.getText().toString());
			}
		});

		// Se crea la ventana de dialogo
		AlertDialog helpDialog = dialog.create();
		//se muestra la ventana de dialogo
		dialog.show();
	}
	
	protected void onActivityResult( int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		if( resultCode == RESULT_OK && null != data){
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Cursor cursor = getContentResolver().query( selectedImage, filePathColumn, null, null, null);
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex( filePathColumn[0] );
			String picturePath = cursor.getString(columnIndex);
			cursor.close();
			foto.setImageBitmap( BitmapFactory.decodeFile(picturePath));
		}
	}
	
	@Override
    /**
     * Metodo que guarda los valores de las variables al voltear el android
     * @return void
     */
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		SharedPreferences prefs = getSharedPreferences("PREFS_KEY",Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("path",  path);
		editor.putString("name",  name.getText().toString());
		editor.commit();
	}
	
	@Override
	public void onClick(View v) {}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {}

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
		    case R.id.acerca://si se presiona la opcion de acerca
		    	startActivity(new Intent(this, Acerca.class));
		    return true;
		    case android.R.id.home://si se presiona el regresar a la activad actual
				Detalles.this.finish();
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
	    actionBar.setTitle("Detalles    ");//pone el titulo
	    
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
