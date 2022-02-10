create table address
(
    address_id bigserial primary key,
    street     varchar(50)
);
alter table client
    add column address_id bigint,
    add foreign key (address_id) references address

