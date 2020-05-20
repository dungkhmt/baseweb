update party_customer
set party_type_id = 'PARTY_DISTRIBUTOR'
where party_id = '875704ac-4026-11ea-9be4-54bf64436441';

update party_customer
set party_type_id = 'PARTY_RETAIL_OUTLET'
where party_id = '875704ac-4026-11ea-9be4-54bf64436441';

-----------
get list of distributors

select *
from party_customer
where party_type_id = 'PARTY_DISTRIBUTOR';

----------
get list of retail outlets

select *
from party_customer
where party_type_id = 'PARTY_RETAIL_OUTLET';

