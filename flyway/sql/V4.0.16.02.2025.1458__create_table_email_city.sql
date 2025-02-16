create table email_city(
    emails_email varchar(100),
    cities_city varchar(50),
    cities_country varchar(50),
    primary key(emails_email, cities_city, cities_country),
    foreign key(emails_email) references email(email) on delete cascade,
    foreign key(cities_city, cities_country) references city(city, country) on delete cascade
)