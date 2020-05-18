package com.hust.baseweb.applications.salesroutes.repo;

import com.hust.baseweb.applications.sales.entity.PartySalesman;
import com.hust.baseweb.applications.salesroutes.entity.SalesRouteDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;


public interface SalesRouteDetailRepo extends JpaRepository<SalesRouteDetail, UUID> {
    List<SalesRouteDetail> findByPartySalesmanAndExecuteDate(PartySalesman partySalesman, String executeDate);
    void deleteByPartySalesman(PartySalesman partySalesman);

    @Query(value = "select ul.user_login_id salesmanName,\n" +
                          "pro.retail_outlet_name retailOutletName,\n" +
                          "pd.distributor_name distributorname,\n" +
                          "srd.execute_date executeDate,\n" +
                          "srd.\"sequence\" orderOfVisit\n" +
                   "from sales_route_detail srd \n" +
                          "inner join user_login ul on srd.party_salesman_id = ul.party_id \n" +
                          "inner join party_retail_outlet pro on srd.party_retail_outlet_id = pro.party_id \n" +
                          "inner join party_distributor pd on srd.party_distributor_id = pd.party_id \n" +
                   "where srd.sales_route_planning_period_id = ?1",
           nativeQuery = true)
    List<GetSalesRouteDetailOfPlanPeriodOutputModel> getSalesRouteDetailOfPlanPeriod(UUID salesRoutePlanningPeriodId);

    interface GetSalesRouteDetailOfPlanPeriodOutputModel {
        String getSalesmanName();
        String getRetailOutletName();
        String getDistributorName();
        String getExecuteDate();
        int getOrderOfVisit();
    }
}
