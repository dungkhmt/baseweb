package com.hust.baseweb.model.querydsl;

public class SortCriteria {
	String field;
	boolean isAsc;
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public boolean isAsc() {
		return isAsc;
	}
	public void setAsc(boolean isAsc) {
		this.isAsc = isAsc;
	}
	public SortCriteria(String field, boolean isAsc) {
		super();
		this.field = field;
		this.isAsc = isAsc;
	}
	public SortCriteria() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}