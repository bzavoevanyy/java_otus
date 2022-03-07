create table if not exists "users" (
    id bigserial primary key,
    name varchar(50),
    login varchar(50),
    password varchar
);
insert into "users" (name, login, password) VALUES ('admin', 'admin', '123456');
