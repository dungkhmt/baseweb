package com.hust.baseweb.applications.sales.model;

import com.hust.baseweb.applications.sales.model.salesman.SalesmanOutputModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ListSalesmanOutputModel {

    List<SalesmanOutputModel> list;
}
