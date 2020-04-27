delete from party_contact_mech_purpose ;
delete
from delivery_trip;
delete
from delivery_plan;
delete
from facility;
delete from postal_address;
delete
from distance_travel_time_geo_points;
delete from geo_point;

-------------------
remove geo_point having no latitude/longitude

delete from shipment_item_delivery_plan;

delete from delivery_trip_detail;
delete from shipment_item;

delete from party_contact_mech_purpose where contact_mech_id in (select contact_mech_id  from postal_address 
where geo_point_id in (select geo_point_id from geo_point where latitude is null));


delete   from postal_address 
where geo_point_id in (select geo_point_id from geo_point where latitude is null);
------------------------