-- let's use utc for sure
create or replace function now_utc() returns timestamp as
$$
select now() at time zone 'utc';
$$ language sql;

-- enforce that created at can't be changed
create or replace function check_created_at_unchanged()
    returns trigger as
$$
begin
    if OLD.created_at != NEW.created_at then
        raise exception 'attempted to update created_at' using hint = 'updated_at field can not be changed';
    end if;
    return new;
end;
$$ language plpgsql;

-- set updated_at to now if not set for an update
create or replace function update_updated_at()
    returns trigger as
$$
begin
    if NEW.updated_at is null or NEW.updated_at <= OLD.updated_at then
        NEW.updated_at = now_utc();
    end if;
    return new;
end;
$$ language plpgsql;

-- increment lock_version if not done
create or replace function increment_lock_version()
    returns trigger as
$$
begin
    if NEW.lock_version <= OLD.lock_version then
        NEW.lock_version = OLD.lock_version + 1;
    end if;
    return new;
end;
$$ language plpgsql;
