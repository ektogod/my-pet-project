create table email(
    email varchar(100) primary key,
    name varchar(100) not null,
    code varchar(20) not null,
    is_verified bit(1) not null,
    chat_id bigint,
    foreign key(chat_id) references user(chat_id) on delete cascade
)