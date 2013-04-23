package com.example.checkplease;

import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class CustomOnCheckedChangeListener implements OnCheckedChangeListener {
	private Person person;
	
	public CustomOnCheckedChangeListener(Person person) {
		this.person = person;
	}
	
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		
	}
}
