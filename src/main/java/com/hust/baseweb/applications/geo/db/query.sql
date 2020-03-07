get postal_address and geo_point where geo_point have no lat/lng

select pa.contact_mech_id, pa.address,gp.geo_point_id, gp.latitude,gp.longitude 
from postal_address as pa, geo_point as gp where pa.geo_point_id = gp.geo_point_id and gp.latitude is null;
----------------
