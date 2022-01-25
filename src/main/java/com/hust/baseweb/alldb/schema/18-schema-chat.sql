CREATE TABLE chat_group
(
    group_id uuid NOT NULL DEFAULT uuid_generate_v1(),
    group_name character varying(500) COLLATE pg_catalog."default",
    created_by_user_login_id character varying(60) COLLATE pg_catalog."default",
    last_updated_stamp timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    created_stamp timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    last_message_time timestamp without time zone DEFAULT now(),
    CONSTRAINT pk_group_id PRIMARY KEY (group_id)
)

CREATE TABLE public.user_login_chat_group
(
    mapping_id uuid NOT NULL DEFAULT uuid_generate_v1(),
    user_login_id character varying(60) COLLATE pg_catalog."default",
    group_id uuid,
    CONSTRAINT pk_mapping_id PRIMARY KEY (mapping_id)
)


CREATE TABLE message
(
    msg_id uuid NOT NULL DEFAULT uuid_generate_v1(),
    message text COLLATE pg_catalog."default",
    created_by_user_login_id character varying(60) COLLATE pg_catalog."default",
    to_user_login_id character varying(60) COLLATE pg_catalog."default",
    to_group_id uuid,
    reply_to_message_id uuid,
    status_id character varying(200) COLLATE pg_catalog."default",
    last_updated_stamp timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    created_stamp timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_message_id PRIMARY KEY (msg_id)
)

