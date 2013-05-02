package com.example.checkplease;

public class Mesa {
	
	private int idMesa;
	private int position;
	private String nombre;
	private float total;
	private int pago;
	private String path;
	
	public Mesa( int idMesa, int position, String nombre, float total, int pago, String path){
		this.idMesa = idMesa;
		this.position = position;
		this.nombre = nombre;
		this.total = total;
		this.pago = pago;
		this.path = path;
	}
	
	public void setIdMesa(int idMesa){
		this.idMesa = idMesa;
	}
	public void setPosition(int position){
		this.position = position;
	}
	public void setNombre(String nombre){
		this.nombre = nombre;
	}
	public void setTotal(float total){
		this.total = total;
	}
	public void setPago(int pago){
		this.pago = pago;
	}
	public void setPath(String path){
		this.path = path;
	}
	
	public int getIdMesa(){
		return idMesa;
	}
	public int getPosition(){
		return position;
	}
	public String getNombre(){
		return nombre;
	}
	public float getTotal(){
		return total;
	}
	public int getPago(){
		return pago;
	}
	public String getPath(){
		return path;
	}
}
