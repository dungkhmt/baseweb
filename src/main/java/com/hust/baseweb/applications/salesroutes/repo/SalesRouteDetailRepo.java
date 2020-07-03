package com.hust.baseweb.applications.salesroutes.repo;

import com.hust.baseweb.applications.sales.entity.PartySalesman;
import com.hust.baseweb.applications.salesroutes.entity.SalesRouteDetail;
import com.hust.baseweb.applications.salesroutes.model.salesroutedetail.GetSalesRouteDetailOfPlanPeriodOM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;


public interface SalesRouteDetailRepo
    extends JpaRepository<SalesRouteDetail, UUID>, CrudRepository<SalesRouteDetail, UUID> {

    List<SalesRouteDetail> findByPartySalesmanAndExecuteDate(PartySalesman partySalesman, String executeDate);

    void deleteByPartySalesman(PartySalesman partySalesman);

    @Query(value = "select ul.user_login_id salesmanName, " +
                   "pro.retail_outlet_name retailOutletName, " +
                   "pd.distributor_name distributorname, " +
                   "srd.execute_date executeDate, " +
                   "srd.\"sequence\" orderOfVisit " +
                   "from sales_route_detail srd " +
                   "inner join user_login ul on srd.party_salesman_id = ul.party_id " +
                   "inner join party_retail_outlet pro on srd.party_retail_outlet_id = pro.party_id " +
                   "inner join party_distributor pd on srd.party_distributor_id = pd.party_id " +
                   "where srd.sales_route_planning_period_id = ?1",
           nativeQuery = true)
    List<GetSalesRouteDetailOfPlanPeriodOM> getSalesRouteDetailOfPlanPeriod(UUID salesRoutePlanningPeriodId);
}
