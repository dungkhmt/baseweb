-- delete delivery_trip
delete from delivery_trip_detail;
delete from delivery_trip_status;
delete from delivery_trip;

-- delete delivery_trip_plan
delete from shipment_item_delivery_plan;
delete from delivery_trip_detail;
delete from delivery_trip;
delete from vehicle_delivery_plan;
delete from delivery_plan;

-- delete shipment
delete from shipment_item_role;
delete from shipment_item_status;
delete from shipment_item;
delete from shipment;


-- delete order
delete from delivery_trip_detail;
delete from shipment_item_status;
delete from shipment_item_delivery_plan;
delete from shipment_item;
delete from order_item;
delete from order_role;
delete from order_status;
delete from order_header;