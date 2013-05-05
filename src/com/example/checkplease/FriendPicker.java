package com.example.checkplease;

import java.util.ArrayList;
import java.util.Collection;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.model.GraphUser;
import com.facebook.widget.FriendPickerFragment;
import com.facebook.widget.PickerFragment;

public class FriendPicker extends FragmentActivity {
    FriendPickerFragment friendPickerFragment;
	Collection<GraphUser> selection;
	String nombresFacebook = "";
	String amigos = "";

    // A helper to simplify life for callers who want to populate a Bundle with the necessary
    // parameters. A more sophisticated Activity might define its own set of parameters; our needs
    // are simple, so we just populate what we want to pass to the FriendPickerFragment.
    public static void populateParameters(Intent intent, String userId, boolean multiSelect, boolean showTitleBar) {
        intent.putExtra(FriendPickerFragment.USER_ID_BUNDLE_KEY, userId);
        intent.putExtra(FriendPickerFragment.MULTI_SELECT_BUNDLE_KEY, multiSelect);
        intent.putExtra(FriendPickerFragment.SHOW_TITLE_BAR_BUNDLE_KEY, showTitleBar);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pick_friends_activity);

        FragmentManager fm = getSupportFragmentManager();

        if (savedInstanceState == null) {
            // First time through, we create our fragment programmatically.
            final Bundle args = getIntent().getExtras();
            friendPickerFragment = new FriendPickerFragment(args);
            fm.beginTransaction()
                    .add(R.id.friend_picker_fragment, friendPickerFragment)
                    .commit();
        } else {
            // Subsequent times, our fragment is recreated by the framework and already has saved and
            // restored its state, so we don't need to specify args again. (In fact, this might be
            // incorrect if the fragment was modified programmatically since it was created.)
            friendPickerFragment = (FriendPickerFragment) fm.findFragmentById(R.id.friend_picker_fragment);
        }

        friendPickerFragment.setOnErrorListener(new PickerFragment.OnErrorListener() {
            @Override
            public void onError(PickerFragment<?> fragment, FacebookException error) {
                FriendPicker.this.onError(error);
            }
        });

        friendPickerFragment.setOnDoneButtonClickedListener(new PickerFragment.OnDoneButtonClickedListener() {
            @Override
            public void onDoneButtonClicked(PickerFragment<?> fragment) {
                // We just store our selection in the Application for other activities to look at.
                FriendPickerApplication application = (FriendPickerApplication) getApplication();
                application.setSelectedUsers(friendPickerFragment.getSelection());
        		selection = application.getSelectedUsers();
        		if (selection != null && selection.size() > 0) {
        			for (GraphUser user : selection) {
        				//Toast.makeText(getApplicationContext(),user.getId(),Toast.LENGTH_SHORT).show();
        				if(nombresFacebook.equals("")){
        					nombresFacebook = user.getName().toString();
        				}else{
            				nombresFacebook = nombresFacebook + "," + user.getName().toString();
        				}
        				//profilePictureView.setProfileId(user.getId());
        				if(amigos.equals(""))//si es el primer usuario que se agrega solo se pone ese sin como
        				{amigos = user.getId();}
        				else//sino todo lo que ya esta , el nuevo usuario
        				{amigos = amigos + ","+ user.getId();}

        				
        				Log.e("id-usuario-antes", ":" +user.getId());


        			}
        			//results = TextUtils.join(", ", names);
        		} else {
        			//results = "<No friends selected>";
        		}
        		Log.e("usuario",":"+selection);
                setResult(RESULT_OK, null);
                finish();
                termina();
				
            }
        });
    }
    /**
	 * Metodo: facebook,
	 * Metodo que realiza la accion de abrir la actividad de Facebook
	 */
	private void termina() {
		Intent intent = new Intent(this, Lista.class);
	//	intent.putExtra("viene", "Invita");
		//intent.putExtra("selecciono", selection);
		startActivity(intent);
		finish();
	}
   
    private void onError(Exception error) {
        new AlertDialog.Builder(this)
                //.setTitle(R.string.error_dialog_title)
                .setMessage(error.getMessage())
                //.setPositiveButton(R.string.ok_button, null)
                .show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            // Load data, unless a query has already taken place.
            friendPickerFragment.loadData(false);
        } catch (Exception ex) {
            onError(ex);
        }
    }
}

