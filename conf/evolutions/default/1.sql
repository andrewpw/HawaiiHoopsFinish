# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table teams (
  team_name                 varchar(255) not null,
  location                  varchar(255),
  gender                    varchar(255),
  skl_lvl                   varchar(255),
  roster                    varchar(255),
  description               clob,
  constraint pk_teams primary key (team_name))
;

create sequence teams_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists teams;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists teams_seq;

