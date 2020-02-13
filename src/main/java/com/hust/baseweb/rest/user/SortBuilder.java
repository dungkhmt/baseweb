package com.hust.baseweb.rest.user;

import java.util.ArrayList;
import java.util.List;

import com.hust.baseweb.model.querydsl.SortCriteria;
import com.hust.baseweb.utils.CommonUtils;

import org.springframework.data.domain.Sort;


public class SortBuilder {
	List<SortCriteria> sort;
	private final String fullNamepath = "wrappedPerson.fullName";
	private final String firstNamePath = "wrappedPerson.firstName";
	private final String middleNamePath = "wrappedPerson.middleName";
	private final String lastNamePath = "wrappedPerson.lastName";

	public SortBuilder() {
		super();
		this.sort = new ArrayList<SortCriteria>();
	}

	public void add(String key, boolean isAsc) {
		if (key.equals(fullNamepath)) {
			sort.add(new SortCriteria(lastNamePath, isAsc));
			sort.add(new SortCriteria(middleNamePath, isAsc));
			sort.add(new SortCriteria(firstNamePath, isAsc));
		} else {
			sort.add(new SortCriteria(key, isAsc));
		}
	}

	public Sort build() {
		return CommonUtils.buildSortbySortCriteria(sort);
	}
}