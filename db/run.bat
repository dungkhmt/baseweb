set PGPASSWORD=1
psql -U postgres -d baseweb < schema.sql
psql -U postgres -d baseweb < seed.sql
psql -U postgres -d baseweb < demo.sql
