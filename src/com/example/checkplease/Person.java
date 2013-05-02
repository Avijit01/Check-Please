package com.example.checkplease;

public class Person {
	private int id;
	private String name;
	private String picture;
    private float total;
    private float totalTip;
    private boolean paid;
    
    public Person(int id, String name, float total, boolean paid, String picture) {
    	this.id = id;
    	this.picture = picture;
    	this.total = total;
    	this.paid = paid;
    	this.name = name;
    }
    
    public Person(int id, String name, float total, boolean paid) {
    	this.id = id;
    	this.total = total;
    	this.paid = paid;
    	this.name = name;
    	picture = "null";
    }
    
    public Person(int id, String name) {
    	this.id = id;
    	this.name = name;
    	picture = "null";
    	total = 0.0f;
    	paid = false;
    }
    
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public float getTotal() {
		return total;
	}
	public void setTotal(float total) {
		this.total = total;
	}

	public boolean isPaid() {
		return paid;
	}

	public void setPaid(boolean paid) {
		this.paid = paid;
	}

	public float getTotalTip() {
		return totalTip;
	}

	public void setTotalTip(float totalTip) {
		this.totalTip = totalTip;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		return id + " " + name + " " + total + " " + paid;
	}
}
