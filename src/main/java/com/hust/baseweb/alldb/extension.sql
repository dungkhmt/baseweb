--  uuid_generate_v1()
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";



-- Extension: unaccent

-- DROP EXTENSION unaccent;

CREATE EXTENSION unaccent
	SCHEMA "public"
	VERSION 1.1;
