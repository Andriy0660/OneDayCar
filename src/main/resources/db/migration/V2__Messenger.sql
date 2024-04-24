create table messages
(
    id          bigserial primary key,
    sender_id   bigint       not null,
    receiver_id bigint       not null,
    content     varchar(500) not null,
    time        timestamp    not null,

    foreign key (sender_id) references users (id),
    foreign key (receiver_id) references users (id)

);