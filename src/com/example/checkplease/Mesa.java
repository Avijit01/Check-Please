package com.example.checkplease;

/**
 * Clase Objeto que genera cada uno de los datos obtenidos en la lista 
 * de ListMesa.java
 * @author Cesar Amaro
 *
 */
public class Mesa {

	private int idMesa; //id de la mesa del usuario
	private int position; //posicion de la persona en la lista de la mesa
	private String nombre; //nombre del comensal
	private float total; // total que pago el comensal
	private float propina;//propina que pago el comensal
	private int pago;//dato que si pago o no
	private String path;//imagen del usuaario
	
	/**
	 * Metodo contructor del objeto Mesa
	 * @param idMesa
	 * @param position
	 * @param nombre
	 * @param total
	 * @param pago
	 * @param path
	 */
	public Mesa( int idMesa, int position, String nombre, float total, int pago, String path){
		this.idMesa = idMesa;
		this.position = position;
		this.nombre = nombre;
		this.total = total;
		this.pago = pago;
		this.path = path;
	}	
	
	/**
	 * Metodo constructor del objeto Mesa con propina
	 * @param idMesa
	 * @param position
	 * @param nombre
	 * @param total
	 * @param propina
	 * @param pago
	 * @param path
	 */
	public Mesa( int idMesa, int position, String nombre, float total, float propina, int pago, String path){
		this.idMesa = idMesa;
		this.position = position;
		this.nombre = nombre;
		this.total = total;
		this.propina = propina;
		this.pago = pago;
		this.path = path;
	}
	/**
	 * Metodo set
	 * @param idMesa
	 */
	public void setIdMesa(int idMesa){
		this.idMesa = idMesa;
	}
	/**
	 * Metodo set
	 * @param position
	 */
	public void setPosition(int position){
		this.position = position;
	}
	/**
	 * Metodo set
	 * @param nombre
	 */
	public void setNombre(String nombre){
		this.nombre = nombre;
	}
	/**
	 * Metodo set
	 * @param total
	 */
	public void setTotal(float total){
		this.total = total;
	}
	/**
	 * Metodo set
	 * @param pago
	 */
	public void setPago(int pago){
		this.pago = pago;
	}
	/**
	 * 
	 * @param path
	 */
	public void setPath(String path){
		this.path = path;
	}
	
	/**
	 * Metodo get
	 * @return
	 */
	public int getIdMesa(){
		return idMesa;
	}
	/**
	 * Metodo get
	 * @return
	 */
	public int getPosition(){
		return position;
	}
	/**
	 * Metodo get
	 * @return
	 */
	public String getNombre(){
		return nombre;
	}
	/**
	 * Metodo get
	 * @return
	 */
	public float getTotal(){
		return total;
	}
	/**
	 * Metodo get
	 * @return
	 */
	public float getPropina(){
		return propina;
	}
	/**
	 * Metodo get
	 * @return
	 */
	public int getPago(){
		return pago;
	}
	/**
	 * Metodo get
	 * @return
	 */
	public String getPath(){
		return path;
	}
}
