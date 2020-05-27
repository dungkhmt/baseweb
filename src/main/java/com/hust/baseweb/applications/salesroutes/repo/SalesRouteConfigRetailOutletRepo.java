package com.hust.baseweb.applications.salesroutes.repo;

import com.hust.baseweb.applications.salesroutes.entity.SalesRouteConfigRetailOutlet;
import com.hust.baseweb.applications.salesroutes.entity.SalesRoutePlanningPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface SalesRouteConfigRetailOutletRepo extends JpaRepository<SalesRouteConfigRetailOutlet, UUID> {

    List<SalesRouteConfigRetailOutlet> findBySalesRoutePlanningPeriod(SalesRoutePlanningPeriod salesRoutePlanningPeriod);

    @Query(value = "select pro.retail_outlet_code retailOutletCode,\n" +
                   "pro.retail_outlet_name retailOutletName,\n" +
                   "ul.user_login_id salesmanName,\n" +
                   "pd.distributor_name distributorName,\n" +
                   "srvf.description visitFrequency,\n" +
                   "src.days visitConfig,\n" +
                   "src.repeat_week repeatWeek\n" +
                   "from sales_route_config_retail_outlet srcro\n" +
                   "inner join sales_route_visit_frequency srvf on srcro.visit_frequency_id = srvf.visit_frequency_id\n" +
                   "inner join sales_route_config  src on srcro.sales_route_config_id = src.sales_route_config_id \n" +
                   "inner join retail_outlet_salesman_vendor rosv on srcro.retail_outlet_salesman_vendor_id = rosv.retail_outlet_salesman_vendor_id \n" +
                   "inner join party_retail_outlet pro on rosv.party_retail_outlet_id = pro.party_id \n" +
                   "inner join user_login ul on rosv.party_salesman_id = ul.party_id \n" +
                   "inner join party_distributor pd on rosv.party_vendor_id = pd.party_id \n" +
                   "where srcro.sales_route_planning_period_id = ?1",
           nativeQuery = true)
    List<GetSalesRouteConfigRetailOutletsOutputModel> getSalesroutesConfigRetailOutlets(UUID salesRoutePlanningPeriodId);

    interface GetSalesRouteConfigRetailOutletsOutputModel {

        String getRetailOutletCode();

        String getRetailOutletName();

        String getSalesmanName();

        String getDistributorName();

        String getVisitFrequency();

        String getVisitConfig();

        int getRepeatWeek();
    }
}
