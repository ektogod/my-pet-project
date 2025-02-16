create table user_city(
    users_chat_id bigint,
    cities_city varchar(50),
    cities_country varchar(50),
    primary key(users_chat_id, cities_city, cities_country),
    foreign key(users_chat_id) references user(chat_id) on delete cascade,
    foreign key(cities_city, cities_country) references city(city, country) on delete cascade
)