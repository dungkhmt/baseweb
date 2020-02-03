create trigger order_last_updated_stamp
before update
on order_header
for each row execute procedure set_last_updated_stamp()