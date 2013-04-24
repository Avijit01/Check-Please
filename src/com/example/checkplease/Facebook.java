package com.example.checkplease;

import java.util.ArrayList;
import java.util.Collection;

import com.example.checkplease.libreria.UserFunctions;
import com.facebook.FacebookException;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.FriendPickerFragment;
import com.facebook.widget.LoginButton;
import com.facebook.widget.PickerFragment;
import com.facebook.widget.UserSettingsFragment;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ActionBar.OnNavigationListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
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

    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home://se cierra el menu
			Facebook.this.finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}


	private UserSettingsFragment userSettingsFragment;
    private Button regresa, acepta, rechaza;
    ImageView fondo;
    private ViewGroup controlsContainer;
    private static final int PICK_FRIENDS_ACTIVITY = 1;
    ArrayList<String> actions = new ArrayList<String>();//arreglo que guardara las acciones de menu del action bar

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_fragment_activity);
		
		fondo = (ImageView)findViewById(R.id.imageView3);
		final TextView mensajeFace = (TextView)findViewById(R.id.mensaje);
		
		acepta = (Button)findViewById(R.id.sigue);
		rechaza = (Button)findViewById(R.id.regresa);
		Session session = Session.getActiveSession();
		if (session == null) {
			Toast.makeText(getApplicationContext(),"session",Toast.LENGTH_SHORT).show();
			 acepta.setVisibility(RelativeLayout.INVISIBLE);
	    		rechaza.setVisibility(RelativeLayout.INVISIBLE);
	    		mensajeFace.setVisibility(RelativeLayout.INVISIBLE);
		}else{
        if (session.isOpened()) {
			Toast.makeText(getApplicationContext(),"open",Toast.LENGTH_SHORT).show();

        	  acepta.setVisibility(RelativeLayout.VISIBLE);
	    		rechaza.setVisibility(RelativeLayout.VISIBLE);
	    		mensajeFace.setVisibility(RelativeLayout.VISIBLE);
        }else{
			Toast.makeText(getApplicationContext(),"session",Toast.LENGTH_SHORT).show();

        	 acepta.setVisibility(RelativeLayout.INVISIBLE);
	    		rechaza.setVisibility(RelativeLayout.INVISIBLE);
	    		mensajeFace.setVisibility(RelativeLayout.INVISIBLE);
        	
        }
		}
		
		
		
      //  fondo.setBackgroundColor(getResources().getColor(R.color.com_facebook_blue));
	    FragmentManager fragmentManager = getSupportFragmentManager();

	    userSettingsFragment = (UserSettingsFragment) fragmentManager.findFragmentById(R.id.login_fragment);
	    
	    userSettingsFragment.setSessionStatusCallback(new Session.StatusCallback() {
	        @Override
	        public void call(Session session, SessionState state, Exception exception) {
	            Log.d("LoginUsingLoginFragmentActivity", String.format("New session state: %s", state.toString()));
	            Log.d("LoginUsingLoginFragmentActivity", String.format("New session state: %s", session.toString()));
	            
	            acepta.setVisibility(RelativeLayout.VISIBLE);
	    		rechaza.setVisibility(RelativeLayout.VISIBLE);
	    		mensajeFace.setVisibility(RelativeLayout.VISIBLE);
	        }
	    });
	        
	    acepta.setOnClickListener(new  View.OnClickListener(){
        	public void onClick(View view){

        		Intent intent = new Intent(view.getContext(), Entra.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        		startActivity(intent);
        		finish();
        	}
        });	   
	    rechaza.setOnClickListener(new  View.OnClickListener(){
        	public void onClick(View view){
        		Session session = Session.getActiveSession();
    	        if (!session.isClosed()) {
    	            session.closeAndClearTokenInformation();
    	        }
        		Facebook.this.finish();
        	}
        });
	}
	
	
    private void showPickerFragment(PickerFragment<?> fragment) {
        fragment.setOnErrorListener(new PickerFragment.OnErrorListener() {
            @Override
            public void onError(PickerFragment<?> pickerFragment, FacebookException error) {
                showAlert(getString(R.string.aceptar), error.getMessage());
            }
        });
        Log.d("5", "ONCLICK");

        FragmentManager fm = getSupportFragmentManager();
        Log.d("6", "ONCLICK");
		//setContentView(R.layout.pick_friends_activity);

        fm.beginTransaction()
                .replace(R.id.fragment_container2, fragment)
                .addToBackStack(null)
                .commit();

        //controlsContainer.setVisibility(View.GONE);
        Log.d("7", "ONCLICK");

        // We want the fragment fully created so we can use it immediately.
        fm.executePendingTransactions();
        Log.d("8", "ONCLICK");

        fragment.loadData(false);
    }

    private void onClickPickFriends() {
    	 FriendPickerApplication application = (FriendPickerApplication) getApplication();
         application.setSelectedUsers(null);

         Intent intent = new Intent(this, FriendPicker.class);
         // Note: The following line is optional, as multi-select behavior is the default for
         // FriendPickerFragment. It is here to demonstrate how parameters could be passed to the
         // friend picker if single-select functionality was desired, or if a different user ID was
         // desired (for instance, to see friends of a friend).
         FriendPicker.populateParameters(intent, null, true, true);
         startActivityForResult(intent, PICK_FRIENDS_ACTIVITY);
       /* final FriendPickerFragment fragment = new FriendPickerFragment();
        Log.d("1", "ONCLICK");

        setFriendPickerListeners(fragment);
        Log.d("2", "ONCLICK");

        showPickerFragment(fragment);*/
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        userSettingsFragment.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
   /* public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICK_FRIENDS_ACTIVITY:
                displaySelectedFriends(resultCode);
                break;
            default:
                Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
                break;
        }
    }*/

    private void displaySelectedFriends(int resultCode) {
        String results = "";
        FriendPickerApplication application = (FriendPickerApplication) getApplication();

        Collection<GraphUser> selection = application.getSelectedUsers();
        if (selection != null && selection.size() > 0) {
            ArrayList<String> names = new ArrayList<String>();
            for (GraphUser user : selection) {
                names.add(user.getName());
            }
            results = TextUtils.join(", ", names);
        } else {
            results = "<No friends selected>";
        }
		Toast.makeText(getApplicationContext(),results,Toast.LENGTH_SHORT).show();

       // mensajeFace.setText(results);
    }

    private void setFriendPickerListeners(final FriendPickerFragment fragment) {
        fragment.setOnDoneButtonClickedListener(new FriendPickerFragment.OnDoneButtonClickedListener() {
        	
            @Override
            public void onDoneButtonClicked(PickerFragment<?> pickerFragment) {
                Log.d("3", "SETFREIDNONCLICK");

                onFriendPickerDone(fragment);
            }
            
        });
    }

    private void onFriendPickerDone(FriendPickerFragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack();

        String results = "";
        Log.d("4", "ONCLICK");

        Collection<GraphUser> selection = fragment.getSelection();
        if (selection != null && selection.size() > 0) {
            ArrayList<String> names = new ArrayList<String>();
            for (GraphUser user : selection) {
                names.add(user.getName());
            }
            results = TextUtils.join(", ", names);
        } else {
            results = getString(R.string.rechazar);
        }

        showAlert(getString(R.string.seleccion), results);
    }

	
    private void showAlert(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.aceptar, null)
                .show();
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
	    actionBar.setTitle("Facebook   ");
	    
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
