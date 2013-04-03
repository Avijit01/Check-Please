package com.example.checkplease;

public class Person {
	private String picture;
    private int total;
    private boolean paid;
    
    public Person(String picture, int total, boolean paid) {
    	this.picture = picture;
    	this.total = total;
    	this.paid = paid;
    }
    
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public boolean isPaid() {
		return paid;
	}
	public void setPaid(boolean paid) {
		this.paid = paid;
	}
    
    
}
