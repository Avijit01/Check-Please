package com.example.checkplease;

import android.view.View;
import android.view.View.OnClickListener;

public class CustomOnClickListener implements OnClickListener {

	private int position;
	
	public CustomOnClickListener(int position) {
		this.position = position;
	}
	
	@Override
	public void onClick(View arg0) {

	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}
}
