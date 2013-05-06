package com.example.checkplease;

import java.util.ArrayList;

import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * Clase que te manejar la posicion en la lista de la vista lista_usuarios.xml
 * @author Mario Trujillo
 */
public class CustomOnCheckedChangeListener implements OnCheckedChangeListener {
	private Person person;
	private int position;
	
	/**
	 * Metodo constructor de Persona de la clase CustomOnCheckedChangeListener
	 * @param person
	 */
	public CustomOnCheckedChangeListener(Person person) {
		this.person = person;
	}
	
	/**
	 * Metodo  constructor de posicion de la clase CustomOnCheckedChangeListener
	 * @param position
	 */
	public CustomOnCheckedChangeListener(int position) {
		this.position = position;
	}
	
	/**
	 * Metodo que te permite obtener la persona generada de este objeto
	 * @return
	 */
	public Person getPerson() {
		return person;
	}

	/**
	 * Metodo que cambia la perona por la recibida
	 * @param person
	 */
	public void setPerson(Person person) {
		this.person = person;
	}
	
	/**
	 * Meetodo que regresa la posicion del objeto
	 * @return
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * Metodo que modifica la posicion por la recibida
	 * @param position
	 */
	public void setPosition(int position) {
		this.position = position;
	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		
	}
}
