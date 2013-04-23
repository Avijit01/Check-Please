package com.example.checkplease;

import java.util.Arrays;

import com.facebook.LoggingBehavior;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;

import com.facebook.widget.LoginButton;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainFragment extends Fragment{
	 private static final String URL_PREFIX_FRIENDS = "https://graph.facebook.com/me/friends?access_token=";

	    private TextView textInstructionsOrLink;
	    private Button buttonLoginLogout;
	    private Session.StatusCallback statusCallback = new SessionStatusCallback();

	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	        View view = inflater.inflate(R.layout.activity_main, container, false);

	        buttonLoginLogout = (Button) view.findViewById(R.id.sigue);
	        textInstructionsOrLink = (TextView) view.findViewById(R.id.error);

	        Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);

	        Session session = Session.getActiveSession();
	        if (session == null) {
	            if (savedInstanceState != null) {
	                session = Session.restoreSession(getActivity(), null, statusCallback, savedInstanceState);
	            }
	            if (session == null) {
	                session = new Session(getActivity());
	            }
	            Session.setActiveSession(session);
	            if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
	                session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
	            }
	        }

	        updateView();

	        return view;
	    }

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
	        Session.getActiveSession().onActivityResult(getActivity(), requestCode, resultCode, data);
	    }

	    @Override
	    public void onSaveInstanceState(Bundle outState) {
	        super.onSaveInstanceState(outState);
	        Session session = Session.getActiveSession();
	        Session.saveSession(session, outState);
	    }

	    private void updateView() {
	        Session session = Session.getActiveSession();
	        if (session.isOpened()) {
				Toast.makeText(getApplicationContext(),"entana de Facebook",Toast.LENGTH_SHORT).show();

	            textInstructionsOrLink.setText(URL_PREFIX_FRIENDS + session.getAccessToken());
	            buttonLoginLogout.setText("logoutRaul");
	            View view = null;
				Intent intent = new Intent(view.getContext(), MainActivity.class);
                startActivity(intent);
	            buttonLoginLogout.setOnClickListener(new View.OnClickListener() {
	                public void onClick(View view) { onClickLogout(); }
	            });
	          
	        } else {
	            textInstructionsOrLink.setText("instructions");
	            buttonLoginLogout.setText("login");
	            buttonLoginLogout.setOnClickListener(new View.OnClickListener() {
	                public void onClick(View view) { onClickLogin(); }
	            });
	        }
	    }

	    private Context getApplicationContext() {
			// TODO Auto-generated method stub
			return null;
		}

		private void onClickLogin() {
	        Session session = Session.getActiveSession();
	        if (!session.isOpened() && !session.isClosed()) {
	            session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
	        } else {
	            Session.openActiveSession(getActivity(), this, true, statusCallback);
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
	
}
