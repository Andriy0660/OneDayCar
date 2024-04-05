create table users
(
    id         bigserial primary key,
    first_name varchar(20)      not null,
    last_name  varchar(20)      not null,
    email      varchar(70)      not null,
    password   varchar(500)     not null,
    phone      varchar(30),
    balance    double precision not null
);


create table cars
(
    id            bigserial primary key,
    owner_id      bigint           not null,
    vendor        varchar(30)      not null,
    model         varchar(30)      not null,
    car_type      varchar(30)      not null,
    year          int              not null,
    location      varchar(100)     not null,
    description   varchar(200),
    price_for_day double precision not null,
    is_disabled   boolean          not null,
    foreign key (owner_id) references users (id)
);

create table car_bookings
(
    id         bigserial primary key,
    car_id     bigint not null,
    renter_id  bigint not null,
    owner_id   bigint not null,
    start_date date   not null,
    end_date   date   not null,
    status     varchar(10),
    foreign key (car_id) references cars (id),
    foreign key (renter_id) references users (id)

);

create table car_requests
(
    id            bigserial primary key,
    renter_id     bigint           not null,
    owner_id      bigint           null,
    car_type      varchar(30)      not null,
    price_for_day double precision not null,
    location      varchar(100)     not null,
    start_date    date             not null,
    end_date      date             not null,
    status        varchar(10),
    min_year      int,
    foreign key (renter_id) references users (id),
    foreign key (owner_id) references users (id)
);