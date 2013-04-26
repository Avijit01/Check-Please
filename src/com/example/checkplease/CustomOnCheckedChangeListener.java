package com.example.checkplease;

import java.util.ArrayList;

import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class CustomOnCheckedChangeListener implements OnCheckedChangeListener {
	private Person person;
	private int position;
	
	public CustomOnCheckedChangeListener(Person person) {
		this.person = person;
	}
	
	public CustomOnCheckedChangeListener(int position) {
		this.position = position;
	}
	
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}
	
	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		
	}
}
