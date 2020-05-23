delete
from salesman_checkin_history;
delete
from sales_route_detail;
delete
from sales_route_config_customer;
delete
from customer_salesman;
delete
from party_contact_mech_purpose
where party_id in (select party_id from party_customer);
delete
from party_customer;
