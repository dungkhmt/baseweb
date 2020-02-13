package com.hust.baseweb.model.querydsl;


public class SortAndFiltersInput {
	SearchCriteria filters[];
	private SortCriteria sort[];
	public SearchCriteria[] getFilters() {
		return filters;
	}
	public void setFilters(SearchCriteria[] filters) {
		this.filters = filters;
	}

	public SortCriteria[] getSort() {
		return sort;
	}
	public void setSort(SortCriteria[] sort) {
		this.sort = sort;
	}
	public SortAndFiltersInput(SearchCriteria[] filters,
			SortCriteria[] sort) {
		super();
		this.filters = filters;
		this.sort = sort;
	}
	public SortAndFiltersInput() {
		super();
		// TODO Auto-generated constructor stub
	}
	

}