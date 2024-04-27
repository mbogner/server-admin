create table clients
(
    id                      serial primary key,
    created_at              timestamp without time zone not null default now_utc(),
    updated_at              timestamp without time zone
        constraint clients_create_before_update check (created_at <= updated_at),
    lock_version            integer                     not null
        constraint clients_lock_version_positive check (lock_version >= 0),

    key                     uuid                        not null unique
        constraint clients_key unique,
    name                    varchar(255)                not null,

    last_heartbeat          timestamp without time zone,

    metadata_last_request   timestamp without time zone,
    metadata_ts             timestamp without time zone,
    metadata_schema         varchar(255),
    metadata_schema_version varchar(32),
    metadata                jsonb,

    constraint clients_key_name unique (key, name)
);

CREATE TABLE clients_history PARTITION OF db_history
    FOR VALUES IN ('clients');

CREATE TRIGGER clients_audit_table
    AFTER INSERT OR UPDATE OR DELETE
    ON clients
    FOR EACH ROW
EXECUTE PROCEDURE audit_table();

create trigger clients_check_created_at_unchanged
    before update
    on clients
    for each row
execute procedure check_created_at_unchanged();

create trigger clients_update_updated_at
    before update
    on clients
    for each row
execute procedure update_updated_at();

create trigger clients_increment_lock_version
    before update
    on clients
    for each row
execute procedure increment_lock_version();