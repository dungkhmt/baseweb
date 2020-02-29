get list of salesman

select u.user_login_id, sm.party_id, p.last_name, p.middle_name, p.first_name from party_salesman as sm, user_login as u, person as p 
where p.party_id = sm.party_id and sm.party_id = u.party_id;

-------------
get all customers of a salesman:

select u.user_login_id, c.customer_name from customer_salesman as cs, party_salesman as sm, user_login as u, party_customer as c  
where sm.party_id = cs.party_salesman_id and u.party_id = sm.party_id and c.party_id = cs.party_customer_id

--------------
