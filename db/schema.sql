CREATE OR REPLACE FUNCTION updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = now();
    RETURN NEW;
END;
$$ language 'plpgsql';

DROP TABLE IF EXISTS  security_group_permission;
DROP TABLE IF EXISTS  user_login_security_group;
DROP TABLE IF EXISTS  security_permission;
DROP TABLE IF EXISTS  security_group;
DROP TABLE IF EXISTS  user_login;

CREATE TABLE user_login(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v1(),
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TRIGGER user_login_updated_at BEFORE UPDATE ON
    user_login FOR EACH ROW EXECUTE PROCEDURE updated_at_column();

CREATE TABLE security_group(
    id SMALLINT PRIMARY KEY,
    name VARCHAR(60) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE security_permission(
    id SMALLINT PRIMARY KEY,
    name VARCHAR(60) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE user_login_security_group(
    user_login_id UUID REFERENCES user_login(id),
    security_group_id SMALLINT REFERENCES security_group(id),
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    PRIMARY KEY (user_login_id, security_group_id)
);

CREATE TABLE security_group_permission(
    security_group_id SMALLINT REFERENCES security_group(id),
    security_permission_id SMALLINT REFERENCES security_permission(id),
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    PRIMARY KEY (security_group_id, security_permission_id)
);