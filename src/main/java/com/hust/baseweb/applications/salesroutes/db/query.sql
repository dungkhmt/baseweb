get all customers visited by a salesman on a day

select c.customer_name, u.user_login_id, srd.execute_date from sales_route_detail as srd, party_salesman as sm, user_login as u, party_customer as c
where u.party_id = sm.party_id and srd.party_salesman_id = sm.party_id and c.party_id = srd.party_customer_id 
and srd.execute_date = '2020-02-29'

------------------

get sales_route_config_customer info

select src.days, src.repeat_week, c.customer_name, u.user_login_id from sales_route_config as src, sales_route_config_customer as srcc, party_customer as c, 
party_salesman as sm , user_login as u 
where src.sales_route_config_id = srcc.sales_route_config_id and c.party_id = srcc.party_customer_id 
and sm.party_id = srcc.party_salesman_id and u.party_id = sm.party_id;