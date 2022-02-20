insert into status_item(status_id, status_type_id, status_code, description)
values ('ORDER_CREATED', 'ORDER_STATUS', 'CREATED', 'tạo mới'),
       ('ORDER_CANCELLED', 'ORDER_STATUS', 'CANCELLED', 'đã hủy'),
       ('ORDER_DELIVERING', 'ORDER_STATUS', null, 'don dang giao'),
       ('ORDER_DONE', 'ORDER_STATUS', null, 'don hoan thanh')
;

insert into order_item(order_id, order_item_seq_id, product_id, user_id, quantity, status_id)
values ('ORD0000000006', '0','20201260001', 'admin', 2, 'ORDER_CREATED'),
       ('ORD0000000007', '0', '20201260002', 'admin', 3, 'ORDER_DELIVERING'),
       ('ORD0000000008', '0', '20201260003', 'admin', 4, 'ORDER_DONE'),
       ('ORD0000000009', '0', '20201260003', 'admin', 4, 'ORDER_CANCELLED')
;
