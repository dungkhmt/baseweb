insert into product_promo_type(product_promo_type_id) values('SALES_PROMO');

insert into order_adjustment_type(order_adjustment_type_id) values('DISCOUNT_ADJUSTMENT');

insert into enumeration_type(enumeration_type_id) values('PROD_PROMO_RULE');
insert into enumeration(enum_id,enum_type_id) values('PROD_PROMO_DISCOUNT_PERCENTAGE','PROD_PROMO_RULE');


insert into product_promo(product_promo_id,promo_name,product_promo_type_id,from_date) values
('4022313c-4ffe-11ea-82dd-54bf64436441','CT khuyến mại tết','SALES_PROMO','2020-02-15 10:00:00');

insert into product_promo_rule(product_promo_rule_id,product_promo_id,product_promo_rule_enum_id,json_params) values
('5c21abae-5000-11ea-82de-54bf64436441','4022313c-4ffe-11ea-82dd-54bf64436441','PROD_PROMO_DISCOUNT_PERCENTAGE','{"discountpercentage":0.05}');

insert into product_promo_product(product_promo_rule_id,product_id) values
('5c21abae-5000-11ea-82de-54bf64436441','20201260002'),
('5c21abae-5000-11ea-82de-54bf64436441','20201260003'),
('5c21abae-5000-11ea-82de-54bf64436441','20201260004'),
('5c21abae-5000-11ea-82de-54bf64436441','20201260005'),
('5c21abae-5000-11ea-82de-54bf64436441','20201260006'),
('5c21abae-5000-11ea-82de-54bf64436441','20201260007'),
('5c21abae-5000-11ea-82de-54bf64436441','20201260008');



