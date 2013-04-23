package com.example.checkplease;

public class Person {
	private String picture;
    private float total;
    private float totalTip;
    private boolean paid;
    
    public Person(String picture, float total, boolean paid) {
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
}
