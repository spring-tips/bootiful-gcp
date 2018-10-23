drop table if exists reservations;

create table reservations  (
  id bigint not null auto_increment   ,
  name varchar (255) not null,
  primary key(id)
);