# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table users (
  id                        integer primary key AUTOINCREMENT,
  username                  varchar(80) not null,
  password                  varchar(255),
  email                     varchar(120),
  data_cadastro             timestamp,
  data_atualizacao          timestamp,
  is_cliente                integer(1))
;




# --- !Downs

PRAGMA foreign_keys = OFF;

drop table users;

PRAGMA foreign_keys = ON;

