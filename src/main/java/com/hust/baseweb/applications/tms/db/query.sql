get drivers

select dr.party_id, u.user_login_id, ps.first_name, ps.middle_name, ps.last_name
from party_driver as dr,
     user_login as u,
     person as ps
where ps.party_id = dr.party_id
  and dr.party_id = u.party_id;

------------------------
get driver and assigned trips

select p.last_name,
       p.middle_name,
       p.first_name,
       u.user_login_id,
       dt.delivery_trip_id,
       dt.distance,
       dt.total_weight
from delivery_trip as dt,
     person as p,
     user_login as u
where dt.driver_id = p.party_id
  and u.party_id = dt.driver_id;

-----------------------
