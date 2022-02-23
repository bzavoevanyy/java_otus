create table phone (
    phone_id bigserial primary key,
    number varchar(50),
    client_id bigint,
    foreign key (client_id) references client(id)
)