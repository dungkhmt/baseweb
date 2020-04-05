-- update data type
alter table product_facility
    alter column atp_inventory_count type int using atp_inventory_count::int;

alter table product_facility
    alter column last_inventory_count type int using last_inventory_count::int;

alter table shipment_item
    add scheduled_quantity int;

alter table shipment_item
    add completed_quantity int;