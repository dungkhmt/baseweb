get list orders

select o.order_id, o.order_date, oi.product_id,  p.product_name, oi.quantity from order_header as o, order_item as oi, product as p 
where o.order_id = oi.order_id and oi.product_id = p.product_id

--------------------
