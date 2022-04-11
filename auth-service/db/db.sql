
create table User (
  id int unsigned auto_increment primary key,
  useraccountkey varchar(255) not null unique,
  email varchar(255) not null,
  mobile varchar(255) not null,
  username varchar(255) not null unique,
  firstname varchar(255) not null,
  lastname varchar(255),
  password varchar(1023) not null,
  authtoken varchar(1023) );
