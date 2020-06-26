package com.hust.baseweb.applications.salesroutes.repo;

import com.hust.baseweb.applications.salesroutes.entity.SalesRouteConfigRetailOutlet;
import com.hust.baseweb.applications.salesroutes.entity.SalesRoutePlanningPeriod;
import com.hust.baseweb.applications.salesroutes.model.salesrouteconfigcustomer.GetSalesRouteConfigRetailOutletsOM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface SalesRouteConfigRetailOutletRepo extends JpaRepository<SalesRouteConfigRetailOutlet, UUID> {

    List<SalesRouteConfigRetailOutlet> findBySalesRoutePlanningPeriod(SalesRoutePlanningPeriod salesRoutePlanningPeriod);

    /**
     * @author AnhTuan-AiT (anhtuan0126104@gmail.com)
     */
    @Query(value = "select cast(srcro.sales_route_config_retail_outlet_id as varchar) salesRouteConfigRetailOutletId, " +
                   "pro.retail_outlet_code retailOutletCode, " +
                   "pro.retail_outlet_name retailOutletName, " +
                   "ul.user_login_id salesmanName, " +
                   "cast(ul.party_id as varchar) partySalesmanId, " +
                   "pd.distributor_name distributorName, " +
                   "cast(srcro.visit_frequency_id as varchar) visitFrequencyId, " +
                   "srvf.description visitFrequency, " +
                   "coalesce(src.days,'Chưa thiết lập') visitConfig, " +
                   "srvf.repeat_week repeatWeek " +
                   "from sales_route_config_retail_outlet srcro " +
                   "inner join sales_route_visit_frequency srvf on srcro.visit_frequency_id = srvf.visit_frequency_id " +
                   "left join sales_route_config  src on srcro.sales_route_config_id = src.sales_route_config_id  " +
                   "inner join retail_outlet_salesman_vendor rosv on srcro.retail_outlet_salesman_vendor_id = rosv.retail_outlet_salesman_vendor_id " +
                   "inner join party_retail_outlet pro on rosv.party_retail_outlet_id = pro.party_id " +
                   "inner join user_login ul on rosv.party_salesman_id = ul.party_id " +
                   "inner join party_distributor pd on rosv.party_vendor_id = pd.party_id " +
                   "where srcro.sales_route_planning_period_id = ?1",
           nativeQuery = true)
    List<GetSalesRouteConfigRetailOutletsOM> getSalesRoutesConfigRetailOutlets(UUID salesRoutePlanningPeriodId);

    /**
     * @author AnhTuan-AiT (anhtuan0126104@gmail.com)
     */
    @Modifying
    @Transactional
    @Query(value = "update sales_route_config_retail_outlet set " +
                   "visit_frequency_id = ?2, " +
                   "sales_route_config_id = cast(cast(?3 as text) as uuid), " +
                   "start_execute_week = cast(cast(?4 as text) as integer) " +
                   "where sales_route_config_retail_outlet_id = ?1",
           nativeQuery = true)
    void updateSalesRoutesConfigRetailOutlet(
        UUID salesRouteConfigRetailOutletId,
        String visitFrequencyId,
        UUID salesRouteConfigId,
        Integer startExecuteWeek
    );
}
