insert into product_type(product_type_id, description) values('FINISHED_GOOD','finised goods');

insert into uom_type(uom_type_id,description) values('WEIGHT_MEASURE','Weight'),
('LENGTH_MEASURE','Length'),
('UNIT_MEASURE','Unit'),
('CURRENCY_MEASURE','Currency Measure')
;


insert into uom(uom_id,uom_type_id,description) values('WT_kg','WEIGHT_MEASURE','Kg'),
('WT_g','WEIGHT_MEASURE','g'),
('WT_package','UNIT_MEASURE','gói'),
('WT_box','UNIT_MEASURE','hộp'),
('WT_jar','UNIT_MEASURE','lọ'),
('CUR_vnd','CURRENCY_MEASURE','VND')
;

insert into role_type(role_type_id) values
('BILL_TO_CUSTOMER'),
('BILL_FROM_VENDOR'),
('SHIP_FROM_VENDOR'),
('SUPPLIER_AGENT'),
('SHIP_TO_CUSTOMER'),
('SALES_EXECUTIVE'),
('SALESADMIN_EMPL'),
('SALES_EMPLOYEE')
;


insert into product(product_id,product_type_id,product_name,quantity_uom_id) values
('20201260001','FINISHED_GOOD','Nước mắm chinsu','WT_jar'),
('20201260002','FINISHED_GOOD','Tương ớt chinsu','WT_jar'),
('20201260003','FINISHED_GOOD','Sữa tươi','WT_box'),
('20201260004','FINISHED_GOOD','Mỳ koreno','WT_package'),
('20201260005','FINISHED_GOOD','Mỳ hảo hảo','WT_package'),
('20201260006','FINISHED_GOOD','Mỳ udon','WT_package'),
('20201260007','FINISHED_GOOD','Dầu ăn tường an','WT_jar'),
('20201260008','FINISHED_GOOD','Dầu ăn hướng dương','WT_jar'),
;

