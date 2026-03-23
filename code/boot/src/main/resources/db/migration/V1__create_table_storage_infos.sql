create table storage_infos
(
    id          uuid primary key         default gen_random_uuid(),
    bucket_name varchar(255) not null,
    object_name varchar(255) not null,
    complete    boolean      not null    default false,
    created_at  timestamp with time zone default current_timestamp
);