-- lay ra danh sach dai li ban le (id,ten, dia chi, toa do), bang party_retail_outlet
select ro.party_id,ro.retail_outlet_name, a.address, g.latitude, g.longitude
from party_retail_outlet as ro, postal_address as a, geo_point as g, party_contact_mech_purpose as pcmp
where a.geo_point_id = g.geo_point_id and pcmp.contact_mech_id = a.contact_mech_id and pcmp.party_id = ro.party_id;


-- lay ra danh sach nha phan phoi (id,ten, dia chi, toa do), bang party_distributor
select d.party_id,d.distributor_name, a.address, g.latitude, g.longitude
from party_distributor as d, postal_address as a, geo_point as g, party_contact_mech_purpose as pcmp
where a.geo_point_id = g.geo_point_id and pcmp.contact_mech_id = a.contact_mech_id and pcmp.party_id = d.party_id;


-- lay danh sach nhan vien ban hang (bang party_salesman)
select sm.party_id, p.first_name, p.middle_name, p.last_name, u.user_login_id from party_salesman as sm, person as p, user_login as u
where sm.party_id = p.party_id and u.party_id = sm.party_id;

--lay danh sach san pham (bang product)
select * from product;


--lay danh sach cau hinh vieng tham
select  srp.sales_route_planning_period_id, srp.description, srp.from_date, srp.to_date, u.party_id, u.user_login_id, ro.retail_outlet_name, d.distributor_name,
src.days, src.repeat_week, vf.description
from sales_route_config_retail_outlet as srcro, retail_outlet_salesman_vendor as rosv,
sales_route_config as src, party_salesman as sm, party_distributor as d, party_retail_outlet as ro, user_login as u, sales_route_visit_frequency as vf,
sales_route_planning_period as srp
where srp.sales_route_planning_period_id = srcro.sales_route_planning_period_id and
srcro.retail_outlet_salesman_vendor_id = rosv.retail_outlet_salesman_vendor_id and rosv.party_retail_outlet_id = ro.party_id
and rosv.party_salesman_id = sm.party_id and rosv.party_vendor_id = d.party_id and  u.party_id = sm.party_id
and vf.visit_frequency_id = src.visit_frequency_id ;

-- truy van thong tin tuyen vieng tham chi tiet (bang sales_route_detail)
select u.user_login_id, srd.execute_date, src.days, src.repeat_week, ro.retail_outlet_name, d.distributor_name
from sales_route_detail as srd, party_salesman as sm, party_distributor as d, party_retail_outlet as ro, user_login as u,
sales_route_config as src, sales_route_config_retail_outlet as srcro
where u.party_id = sm.party_id and srd.party_retail_outlet_id = ro.party_id and srd.party_salesman_id = sm.party_id
and srd.party_distributor_id = d.party_id and src.sales_route_config_id = srcro.sales_route_config_id
and srd.sales_route_config_retail_outlet_id = srcro.sales_route_config_retail_outlet_id
order by (u.user_login_id,srd.execute_date,d.distributor_name);