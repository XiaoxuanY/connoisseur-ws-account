drop table if exists cns_user cascade;
create table cns_user (email varchar(255) not null, password varchar(255) not null, user_name varchar(255) not null, first_name varchar(255) not null, last_name varchar(255) not null, status int4 not null, primary key(email));

drop table if exists auth_token cascade;
create table auth_token (id int8 not null, expiration int8 not null, token varchar(255), ttl int default 360000, email varchar(255) not null references cns_user(email), primary key(email));

drop table if exists rating cascade;
create table rating (email varchar(255) not null references cns_user(email), rest_id varchar(255) not null, rating int4 not null, constraint pk_rating primary key(email, rest_id));

drop table if exists bookmark cascade;
create table bookmark (email varchar(255) not null references cns_user(email), rest_id varchar(255) not null, constraint pk_bookmark primary key(email, rest_id));

drop table if exists comment cascade;
create table comment (email varchar(255) not null references cns_user(email), rest_id varchar(255) not null, comment varchar(511) not null, constraint pk_comment primary key(email, rest_id));