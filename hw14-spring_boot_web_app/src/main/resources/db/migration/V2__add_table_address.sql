create table address
(
    client bigint primary key references client(id),
    street     varchar(50)
);
-- alter table client
--     add column address_id bigint,
--     add foreign key (address_id) references address

