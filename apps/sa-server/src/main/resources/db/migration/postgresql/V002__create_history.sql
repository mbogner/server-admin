-- general history table for tables that requires audit functionality.
--
-- for adding audit for a table named 'users' add a partition like this (pattern is <table_name>_history):
-- CREATE TABLE users_history PARTITION OF db_history FOR VALUES IN ('users');

CREATE TABLE db_history
(
    id         bigserial                   not null,
    table_name varchar(32)                 not null,

    created_at timestamp without time zone not null default now_utc(),
    changed_by varchar(32)                 not null default session_user,

    operation  varchar(16)                 not null,

    data_pk    varchar(36)                 not null,      -- uuid has 36 chars
    data_old   text,                                      -- trigger is creating valid json. no need for extra steps. using text for best insert performance
    data_new   text,

    constraint pk_db_history primary key (id, table_name) -- required to have table_name in the pk for partitioning
) PARTITION BY LIST (table_name) WITHOUT OIDS;

-- Trigger for adding audit functionality to a table. Here how to do so for a table name users.
-- CREATE TABLE <TABLE>_history PARTITION OF db_history
--     FOR VALUES IN ('<TABLE>');
-- CREATE TRIGGER audit_<TABLE>
--     AFTER INSERT OR UPDATE OR DELETE
--     ON <TABLE>
--     FOR EACH ROW
-- EXECUTE PROCEDURE audit_table();
CREATE FUNCTION audit_table()
    RETURNS TRIGGER AS
$$
DECLARE
    pk_column VARCHAR;
    pk_value  VARCHAR;
BEGIN
    -- get id column name dynamically into variable pk_column
    SELECT a.attname
    INTO pk_column
    FROM pg_index i
             JOIN pg_attribute a ON a.attrelid = i.indrelid AND a.attnum = ANY (i.indkey)
    WHERE i.indrelid = TG_RELID::regclass
      AND i.indisprimary;

    IF TG_OP = 'INSERT' THEN
        IF NEW.id IS NULL THEN
            RAISE EXCEPTION 'id has to be set for trigger';
        END IF;
        -- read id column name into pk_value
        EXECUTE format('SELECT ($1).%I', pk_column) USING NEW INTO pk_value;
        INSERT INTO db_history (table_name, operation, data_pk, data_new)
        VALUES (TG_TABLE_NAME::regclass::text, TG_OP, pk_value, row_to_json(NEW, false));
        RETURN NEW;
    END IF;
    IF TG_OP = 'UPDATE' THEN
        IF OLD.id != NEW.id THEN
            RAISE EXCEPTION 'id update not allowed';
        END IF;
        -- read id column name into pk_value
        EXECUTE format('SELECT ($1).%I', pk_column) USING OLD INTO pk_value;
        INSERT INTO db_history (table_name, operation, data_pk, data_old, data_new)
        VALUES (TG_TABLE_NAME::regclass::text, TG_OP, pk_value, row_to_json(OLD, false), row_to_json(NEW, false));
        RETURN NEW;
    END IF;
    IF TG_OP = 'DELETE' THEN
        -- read id column name into pk_value
        EXECUTE format('SELECT ($1).%I', pk_column) USING OLD INTO pk_value;
        INSERT INTO db_history (table_name, operation, data_pk, data_old)
        VALUES (TG_TABLE_NAME::regclass::text, TG_OP, pk_value, row_to_json(OLD, false));
        RETURN OLD;
    END IF;
    RAISE EXCEPTION 'operation not supported by trigger';
END;
$$ LANGUAGE plpgsql;