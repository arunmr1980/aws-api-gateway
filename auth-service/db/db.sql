
create table user (
  id int unsigned auto_increment primary key,
  user_account_key varchar(255) not null unique,
  email varchar(255) not null,
  mobile varchar(255) not null,
  username varchar(255) not null unique,
  firstname varchar(255) not null,
  lastname varchar(255),
  password varchar(1023) not null);

  create table user_device (
    user_account_key varchar(255) not null,
    device_key varchar(255) not null unique,
    device_name varchar(255) not null,
    access_token varchar(1023),
    refresh_token varchar(1023),
    access_token_expiry_datetime bigint unsigned,
    refresh_token_expiry_datetime bigint unsigned
  );

  alter table user_device add constraint PK_Device primary key(user_account_key, device_key);
