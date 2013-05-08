package com.example.checkplease;

import com.checkplease.R;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.checkplease.libreria.UserFunctions;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphPlace;
import com.facebook.model.GraphUser;

import com.facebook.widget.FriendPickerFragment;
import com.facebook.widget.LoginButton;
import com.facebook.widget.PickerFragment;
import com.facebook.widget.PlacePickerFragment;
import com.facebook.widget.ProfilePictureView;
import com.facebook.widget.UserSettingsFragment;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ActionBar.OnNavigationListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Facebook extends FragmentActivity{
	UserFunctions userFunctions = new UserFunctions();//carga la case userFunctions
    //Agrega los elementos de la vista
	private UserSettingsFragment userSettingsFragment;
    private Button acepta, rechaza;
    ImageView fondo;
	private SharedPreferences mPrefs;
    private final String PENDING_ACTION_BUNDLE_KEY = "com.example.checkplease:PendingAction";
    private static final Location SEATTLE_LOCATION = new Location("") {
        {
            setLatitude(47.6097);
            setLongitude(-122.3331);
        }
    };
    private static final int PICK_FRIENDS_ACTIVITY = 1;
    String vieneDe = "nada";
    String restaurante;
    int idMesa;
    int vista = 0;
    String amigos = "";
	String nombresFacebook = "";

    private Button postStatusUpdateButton;
    private Button postPhotoButton;
    private Button pickFriendsButton;
    private Button pickPlaceButton;
    private LoginButton loginButton;
    private ProfilePictureView profilePictureView;
    private TextView greeting, mensaje;
    private PendingAction pendingAction = PendingAction.NONE;
    private ViewGroup controlsContainer;
    private GraphUser user;
    private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
    private static final String PENDING_PUBLISH_KEY = "pendingPublishReauthorization";
    private boolean pendingPublishReauthorization = false;

    private enum PendingAction {
        NONE,
        POST_PHOTO,
        POST_STATUS_UPDATE
    }
    private UiLifecycleHelper uiHelper;

    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

/**
 * Metodo que maneja las actividad de Facebook, inicio y cierre de seccion
 * Se manda llamar al entrar a la actividad
 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.facebook.samples.hellofacebook", 
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                }
        } catch (NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        HashMap<String, String> user = userFunctions.getUsuarioId(getApplicationContext());
		idMesa = Integer.parseInt(user.get("mesa"));
        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras(); //si tiene parametos que envio la actividad anterios
		if(extras !=null){//si no es nulo
			if(!extras.getString("viene").equals(""))
				vieneDe = extras.getString("viene");//toma el valor de 1
			else vieneDe = "nada";
			if(vieneDe.equals("postea")){
				//restaurante = extras.getString("restaurante");
				//amigos = extras.getString("amigos");
			}
		}
        if (savedInstanceState != null) {
            String name = savedInstanceState.getString(PENDING_ACTION_BUNDLE_KEY);
            pendingAction = PendingAction.valueOf(name);
        }

        setContentView(R.layout.main);
        acepta = (Button)findViewById(R.id.sigue);
        mensaje = (TextView)findViewById(R.id.mensaje);
      //obtiene si la session de Facebook esta activa o desactiva
      		Session session = Session.getActiveSession();
      		if (session == null) {//si es nulla, pone los elementos inivibles
      			 acepta.setVisibility(RelativeLayout.INVISIBLE);
      		}else{
              if (session.isOpened()) {//si esta abierta, despliega los botones
              	acepta.setVisibility(RelativeLayout.VISIBLE);
              }else{//de lo contrario los hace invisibles
              	 acepta.setVisibility(RelativeLayout.INVISIBLE);
              }
      		}

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
            @Override
            public void onUserInfoFetched(GraphUser user) {
                Facebook.this.user = user;
                updateUI();
                // It's possible that we were waiting for this.user to be populated in order to post a
                // status update.
                handlePendingAction();
                
            }
        });
        //Servira para saber si viene del invita amigos y abrira la activad de Lista con 1 de pickfriends
        Session session2 = Session.getActiveSession();
        //si viene de la vista de invitar
        if(vieneDe.equals("Invita") && session2.isOpened()){
        	Log.e("ENTRA","ENTRA");
			onClickPickFriends();
    		/*finish();//termina la activad de Facebook para que al regresar no pase por esta
    		Intent intent = new Intent(this, Lista.class);
    		intent.putExtra("viene", "facebook");
			intent.putExtra("friends", 1);//si va abrir el popup de seleccionar amigo
			startActivity(intent);*/
		}
        
        profilePictureView = (ProfilePictureView) findViewById(R.id.profilePicture);
       greeting = (TextView) findViewById(R.id.greeting);

      /*  postStatusUpdateButton = (Button) findViewById(R.id.postStatusUpdateButton);
        postStatusUpdateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onClickPostStatusUpdate();
            }
        });

        postPhotoButton = (Button) findViewById(R.id.postPhotoButton);
        postPhotoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onClickPostPhoto();
            }
        });

        pickFriendsButton = (Button) findViewById(R.id.pickFriendsButton);
        pickFriendsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onClickPickFriends();
            }
        });

        pickPlaceButton = (Button) findViewById(R.id.pickPlaceButton);
        pickPlaceButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onClickPickPlace();
            }
        });*/
     //al presionar el botor de aceptar, se abre la actividad de entra
	    acepta.setOnClickListener(new  View.OnClickListener(){
       	public void onClick(View view){
       		Intent dashboard = new Intent(getApplicationContext(), Entra.class);
			dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(dashboard);
       		finish();//termina la activad de Facebook para que al regresar no pase por esta
       		
       	}
       });	 

        controlsContainer = (ViewGroup) findViewById(R.id.main_ui_container);

        final FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment != null) {
            // If we're being re-created and have a fragment, we need to a) hide the main UI controls and
            // b) hook up its listeners again.
            controlsContainer.setVisibility(View.GONE);
            vista = 1;
            if (fragment instanceof FriendPickerFragment) {
                setFriendPickerListeners((FriendPickerFragment) fragment);
            } else if (fragment instanceof PlacePickerFragment) {
                setPlacePickerListeners((PlacePickerFragment) fragment);
            }
        }

        // Listen for changes in the back stack so we know if a fragment got popped off because the user
        // clicked the back button.
        fm.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (fm.getBackStackEntryCount() == 0) {
                    // We need to re-show our UI.
                    controlsContainer.setVisibility(View.VISIBLE);
                }
            }
        });
    }

  

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);

        outState.putString(PENDING_ACTION_BUNDLE_KEY, pendingAction.name());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
        controlsContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        controlsContainer.setVisibility(View.VISIBLE);
        uiHelper.onDestroy();
    }

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (pendingAction != PendingAction.NONE &&
                (exception instanceof FacebookOperationCanceledException ||
                exception instanceof FacebookAuthorizationException)) {
                new AlertDialog.Builder(Facebook.this)
                    .setTitle(R.string.cancelled)
                    .setMessage(R.string.permission_not_granted)
                    .setPositiveButton(R.string.ok, null)
                    .show();
            pendingAction = PendingAction.NONE;
        } else if (state == SessionState.OPENED_TOKEN_UPDATED) {
            handlePendingAction();
        }
        updateUI();
        
    }

    private void updateUI() {
        Session session = Session.getActiveSession();
        boolean enableButtons = (session != null && session.isOpened());

       /* postStatusUpdateButton.setEnabled(enableButtons);
        postPhotoButton.setEnabled(enableButtons);
        pickFriendsButton.setEnabled(enableButtons);
        pickPlaceButton.setEnabled(enableButtons);*/

        if (enableButtons && user != null) {
            profilePictureView.setProfileId(user.getId());
            greeting.setText(getString(R.string.hello_user, user.getFirstName()));
        	Log.e("viende",":"+vieneDe);
          	acepta.setVisibility(RelativeLayout.VISIBLE);

            if(vieneDe.equals("Invita")){
              	mensaje.setText("Espere unos segundos, estamos validando la cuenta");
            	Log.e("viende","invita");
            	vieneDe="otra";//para que no vuela a entrar
    			onClickPickFriends();
        		/*finish();//termina la activad de Facebook para que al regresar no pase por esta
        		Intent intent = new Intent(this, Lista.class);
        		intent.putExtra("viene", "facebook");
				intent.putExtra("friends", 1);//si va abrir el popup de seleccionar amigo
				startActivity(intent);*/
    		}else if(vieneDe.equals("postea")){
              	mensaje.setText("Espere unos segundos, estamos validando la cuenta");
            	Log.e("viende","post");
            	vieneDe="otra";//para que no vuela a entrar
            	 postStatusUpdate();
    		}
        } else {
            profilePictureView.setProfileId(null);
            greeting.setText(null);
        }
    }

    @SuppressWarnings("incomplete-switch")
    private void handlePendingAction() {
        PendingAction previouslyPendingAction = pendingAction;
        // These actions may re-set pendingAction if they are still pending, but we assume they
        // will succeed.
        pendingAction = PendingAction.NONE;
        
        switch (previouslyPendingAction) {
            case POST_PHOTO:
                postPhoto();
                break;
            case POST_STATUS_UPDATE:
                postStatusUpdate();
             
                break;
        }
    }

    private interface GraphObjectWithId extends GraphObject {
        String getId();
    }

    private void showPublishResult(String message, GraphObject result, FacebookRequestError error) {
        String title = null;
        String alertMessage = null;
        if (error == null) {
            title = getString(R.string.success);
            String id = result.cast(GraphObjectWithId.class).getId();
            alertMessage = getString(R.string.successfully_posted_post, message, id);
        } else {
            title = getString(R.string.error);
            alertMessage = error.getErrorMessage();
        }

        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(alertMessage)
                .setPositiveButton(R.string.ok, null)
                .show();
    }

    private void onClickPostStatusUpdate() {
        performPublish(PendingAction.POST_STATUS_UPDATE);
    }

    private void postStatusUpdate() {
    	Session session = Session.getActiveSession();

        if (session != null){

            // Check for publish permissions    
            List<String> permissions = session.getPermissions();
            if (!isSubsetOf(PERMISSIONS, permissions)) {
                pendingPublishReauthorization = true;
                Session.NewPermissionsRequest newPermissionsRequest = new Session
                        .NewPermissionsRequest(this, PERMISSIONS);
            session.requestNewPublishPermissions(newPermissionsRequest);
                return;
            }
            JSONObject json = userFunctions.getInfoMesa(idMesa);
			try {//si la respuesta de KEY_Succes contiene algo
				if (json.getString("success") != null) {
					String res = json.getString("success"); 					
					if(Integer.parseInt(res) == 1){//si se accedio
						JSONObject json_mesa = json.getJSONObject("mesa");
						restaurante = json_mesa.getString("restaurante");
						
					}else{
						// Error al cargar los datos
						//mensajeError.setText("Usuario y/o contraseña incorrectos");
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			JSONObject json2 = userFunctions.getAmigos(idMesa);
    		try {//si la respuesta de KEY_Succes contiene algo
    			if (json2.getString("success") != null) {
    				String res = json2.getString("success"); 					
    				if(Integer.parseInt(res) == 1){//si se accedio
    					amigos = json2.getString("amigos");
    					Log.e("amigos de la base", amigos);
    				}else{
    					// Error al cargar los datos
    					//mensajeError.setText("Usuario y/o contraseña incorrectos");
    				}
    			}
    		} catch (JSONException e) {
    			e.printStackTrace();
    		}
            Bundle postParams = new Bundle();
            final String message =  "Comiendo en: " + restaurante;
            postParams.putString("message", message);
            if(!amigos.equals("")){
	            postParams.putString("tags", amigos);
            }
            postParams.putString("place", "110852402276862");

            Request.Callback callback= new Request.Callback() {
                public void onCompleted(Response response) {
                	mensaje.setText("Posteado con exito, dale click en continuar para seguir usando la aplicación");
                	/*showPublishResult(message, response.getGraphObject(), response.getError());*/
                   // JSONObject graphResponse = (JSONObject) response
                                              // .getGraphObject();
                                               //.getInnerJSONObject();
                    /*String postId = null;
                    try {
                        postId = graphResponse.getString("id");
                    } catch (JSONException e) {
                        Log.i("Mensaje",
                            "JSON error "+ e.getMessage());
                    }*/
                    FacebookRequestError error = response.getError();
                    if (error != null) {
                       Log.e("error",error.getErrorMessage());
                        } else {
                            Log.e("errornull","a");
 
                    }
                   
                }
            };
            
            Request request = new Request(session, "me/feed", postParams, 
                                  HttpMethod.POST, callback);

            RequestAsyncTask task = new RequestAsyncTask(request);
            task.execute();
        }
        
        /*if (user != null && hasPublishPermission()) {
            final String message = getString(R.string.status_update, user.getFirstName(), (new Date().toString()),"623811006");
            Request request = Request
                    .newStatusUpdateRequest(Session.getActiveSession(), message, new Request.Callback() {
                        @Override
                        public void onCompleted(Response response) {
                            showPublishResult(message, response.getGraphObject(), response.getError());
                        }
                    });
            request.executeAsync();
        } else {
            pendingAction = PendingAction.POST_STATUS_UPDATE;
        }*/
    }
    private boolean isSubsetOf(Collection<String> subset, Collection<String> superset) {
        for (String string : subset) {
            if (!superset.contains(string)) {
                return false;
            }
        }
        return true;
    }
    private void onClickPostPhoto() {
        performPublish(PendingAction.POST_PHOTO);
    }

    private void postPhoto() {
        if (hasPublishPermission()) {
            Bitmap image = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher);
            Request request = Request.newUploadPhotoRequest(Session.getActiveSession(), image, new Request.Callback() {
                @Override
                public void onCompleted(Response response) {
                    showPublishResult(getString(R.string.photo_post), response.getGraphObject(), response.getError());
                }
            });
            request.executeAsync();
        } else {
            pendingAction = PendingAction.POST_PHOTO;
        }
    }

    private void showPickerFragment(PickerFragment<?> fragment) {
        fragment.setOnErrorListener(new PickerFragment.OnErrorListener() {
            @Override
            public void onError(PickerFragment<?> pickerFragment, FacebookException error) {
                showAlert(getString(R.string.error), error.getMessage());
            }
        });

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
        Log.e("la vista es","==="+vista);
        if(vista != 1){
        controlsContainer.setVisibility(View.GONE);
        }
       // Log.e("la vista es","==="+controlsContainer.getVisibility());

        // We want the fragment fully created so we can use it immediately.
        fm.executePendingTransactions();

        fragment.loadData(false);
    }

    private void onClickPickFriends() {
        final FriendPickerFragment fragment = new FriendPickerFragment();

        setFriendPickerListeners(fragment);

        showPickerFragment(fragment);

    }

    private void setFriendPickerListeners(final FriendPickerFragment fragment) {
        fragment.setOnDoneButtonClickedListener(new FriendPickerFragment.OnDoneButtonClickedListener() {
            @Override
            public void onDoneButtonClicked(PickerFragment<?> pickerFragment) {
                onFriendPickerDone(fragment);
            }
        });
    }

    private void onFriendPickerDone(FriendPickerFragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack();

        String results = "";

        Collection<GraphUser> selection = fragment.getSelection();
        if (selection != null && selection.size() > 0) {
        	JSONObject json = userFunctions.getAmigos(idMesa);
    		try {//si la respuesta de KEY_Succes contiene algo
    			if (json.getString("success") != null) {
    				String res = json.getString("success"); 					
    				if(Integer.parseInt(res) == 1){//si se accedio
    					amigos = json.getString("amigos");
    					Log.e("amigos de la base", amigos);
    				}else{
    					// Error al cargar los datos
    					//mensajeError.setText("Usuario y/o contraseña incorrectos");
    				}
    			}
    		} catch (JSONException e) {
    			e.printStackTrace();
    		}
            ArrayList<String> names = new ArrayList<String>();
            for (GraphUser user : selection) {
            	if(nombresFacebook.equals("")){
					nombresFacebook = user.getName().toString();
				}else{
    				nombresFacebook = nombresFacebook + ", " + user.getName().toString();
				}
            	if(amigos.equals(""))//si es el primer usuario que se agrega solo se pone ese sin como
				{amigos = user.getId();}
				else//sino todo lo que ya esta , el nuevo usuario
				{amigos = amigos + ","+ user.getId();}
            }
			Log.e("id-usuario-antes", ":" +nombresFacebook);
			Log.e("id-usuario-antes", ":" +amigos);
        } else {
			Log.e("id-usuario-antes", "Ningun amigos seleccionada");
        }
        userFunctions.updateAmigos(idMesa, amigos);
        this.finish();
        Intent intent = new Intent(this, Lista.class);
    	    intent.putExtra("viene", "facebook");
    		intent.putExtra("selecciono", nombresFacebook);
    		intent.putExtra("amigos", amigos);
    		 this.finish();
    		startActivity(intent);
    		
        //showAlert(getString(R.string.you_picked), results);
    }

    private void onPlacePickerDone(PlacePickerFragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack();

        String result = "";

        GraphPlace selection = fragment.getSelection();
        if (selection != null) {
            result = selection.getName();
            Log.e("lugar id", ":"+selection.getId());
        } else {
            result = getString(R.string.no_place_selected);
        }

        showAlert(getString(R.string.you_picked), result);
    }

    private void onClickPickPlace() {
        final PlacePickerFragment fragment = new PlacePickerFragment();
        fragment.setLocation(SEATTLE_LOCATION);
        fragment.setTitleText(getString(R.string.pick_seattle_place));

        setPlacePickerListeners(fragment);

        showPickerFragment(fragment);
    }

    private void setPlacePickerListeners(final PlacePickerFragment fragment) {
        fragment.setOnDoneButtonClickedListener(new PlacePickerFragment.OnDoneButtonClickedListener() {
            @Override
            public void onDoneButtonClicked(PickerFragment<?> pickerFragment) {
                onPlacePickerDone(fragment);
            }
        });
        fragment.setOnSelectionChangedListener(new PlacePickerFragment.OnSelectionChangedListener() {
            @Override
            public void onSelectionChanged(PickerFragment<?> pickerFragment) {
                if (fragment.getSelection() != null) {
                    onPlacePickerDone(fragment);
                }
            }
        });
    }

    private void showAlert(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.ok, null)
                .show();
    }

    private boolean hasPublishPermission() {
        Session session = Session.getActiveSession();
        return session != null && session.getPermissions().contains("publish_actions");
    }

    private void performPublish(PendingAction action) {
        Session session = Session.getActiveSession();
        if (session != null) {
            pendingAction = action;
            if (hasPublishPermission()) {
                // We can do the action right away.
                handlePendingAction();
            } else {
                // We need to get new permissions, then complete the action when we get called back.
                session.requestNewPublishPermissions(new Session.NewPermissionsRequest(this, PERMISSIONS));
            }
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
		//cargaMenu();
		uiHelper.onResume();
		
        updateUI();
		// Normal case behavior follows
	}
	/**
	 * Metodo: cargaMenu(),
	 * Metodo que personaliza la vista del ActionBar con el color, titulo, y opciones
	 */
	void cargaMenu(){
		ActionBar actionBar = getActionBar();//obtiene el ActionBar
		//actionBar.setDisplayHomeAsUpEnabled(true);//habilita la opcion de regresar a la actividad anterios
		actionBar.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.bar_color));//pone color gris
		actionBar.setTitle("Facebook    ");//pone el titulo

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