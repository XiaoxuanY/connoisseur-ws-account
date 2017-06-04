drop table if exists cns_user cascade;
drop table if exists auth_token cascade;
drop table if exists rating cascade;
drop table if exists bookmark cascade;
drop table if exists comment cascade;

create table cns_user (id serial primary key, email varchar(255) not null, password varchar(255) not null, user_name varchar(255) not null, first_name varchar(255) not null, last_name varchar(255) not null, status int4 not null);
create table auth_token (id serial primary key, expiration int8 not null, token varchar(255), ttl int default 360000, user_id int8 not null references cns_user(id));
create table rating (id serial, user_id int8 not null references cns_user(id), restaurant_id varchar(255) not null, rating int4 not null, primary key (user_id, restaurant_id));
create table bookmark (id serial, user_id int8 not null references cns_user(id), restaurant_id varchar(255) not null, primary key (user_id, restaurant_id));
create table comment (id serial, user_id int8 not null references cns_user(id), restaurant_id varchar(255) not null, comment varchar(511) not null, primary key (user_id, restaurant_id));

insert into cns_user(id,email,first_name,last_name,password,status,user_name) values(0,'admin@diningconnoisseur.com','Connoisseur','admin','68bfa06a9a7d874ba3e27416acd948057c13dabe774a3fde1c362f31298453a7',0, 'Admin');
