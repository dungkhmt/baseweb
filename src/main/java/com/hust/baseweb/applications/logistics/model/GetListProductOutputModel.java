package com.hust.baseweb.applications.logistics.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

import com.hust.baseweb.applications.logistics.entity.Product;

@Getter
@Setter
public class GetListProductOutputModel {
	private List<Product> products;

	public GetListProductOutputModel(List<Product> products) {
		super();
		this.products = products;
	}
	
}
