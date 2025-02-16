create table user_email(
   users_chat_id bigint,
   emails_email varchar(100),
   primary key(users_chat_id, emails_email),
   foreign key(users_chat_id) references user(chat_id) on delete cascade,
   foreign key(emails_email) references email(email) on delete cascade
)