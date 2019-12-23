CREATE FUNCTION set_last_updated_stamp()
  RETURNS TRIGGER
LANGUAGE plpgsql
AS $$
BEGIN
  NEW.last_updated_stamp := now();
  RETURN NEW;
END;
$$;