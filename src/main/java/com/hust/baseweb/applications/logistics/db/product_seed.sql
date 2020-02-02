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





