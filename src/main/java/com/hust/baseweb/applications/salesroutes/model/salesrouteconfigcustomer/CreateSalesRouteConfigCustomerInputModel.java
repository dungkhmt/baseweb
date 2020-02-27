package com.hust.baseweb.applications.salesroutes.model.salesrouteconfigcustomer;

import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
public class CreateSalesRouteConfigCustomerInputModel {
	private UUID salesRouteConfigId;
	private UUID partyCustomerId;
	private UUID partySalesmanId;
	private String startExecuteDate;// format YYYY-MM-DD
	
}
