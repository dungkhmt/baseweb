get list orders

select o.order_id, o.order_date, oi.product_id, p.product_name, oi.quantity, orl.role_type_id, orl.party_id
from order_header as o,
     order_item as oi,
     product as p,
     order_role as orl
where o.order_id = oi.order_id
  and oi.product_id = p.product_id
  and orl.order_id = o.order_id;

--------------------
