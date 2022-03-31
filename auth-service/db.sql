create database AuthDB
  
create table User (
  id int unsigned auto_increment primary key,
  useraccountkey varchar(255) not null,
  email varchar(255) not null,
  mobile varchar(255) not null,
  username varchar(255) not null,
  firstname varchar(255) not null,
  lastname varchar(255),
  password varchar(1023) not null,
  authtoken varchar(1023) );
