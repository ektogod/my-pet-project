create table city(
    city varchar(50),
    country varchar(50),
    latitude double not null,
    longitude double not null,
    primary key(city, country)
)