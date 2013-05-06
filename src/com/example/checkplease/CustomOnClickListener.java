package com.example.checkplease;

import android.view.View;
import android.view.View.OnClickListener;

/**
 * Clase que maneja la position enviada
 * @author Mario Trujillo
 *
 */
public class CustomOnClickListener implements OnClickListener {

	private int position;
	
	/**
	 * Metodo constructor de la clase CustomOnClickListener
	 * @param position
	 */
	public CustomOnClickListener(int position) {
		this.position = position;
	}

	@Override
	public void onClick(View arg0) {

	}

	/**
	 * Metodo que regresa la position del objeto creado
	 * @return
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * Metodo que cambia la posicion del objeto por la recibida
	 * @param position
	 */
	public void setPosition(int position) {
		this.position = position;
	}
}
