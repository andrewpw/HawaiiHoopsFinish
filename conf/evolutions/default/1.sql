# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table courts (
  id                        bigint not null,
  name                      varchar(255),
  type                      varchar(255),
  image                     varchar(255),
  website                   varchar(255),
  description               clob,
  address                   varchar(255),
  lat                       float,
  lng                       float,
  num_courts                bigint,
  court_size                varchar(255),
  court_surface             varchar(255),
  court_quality             varchar(255),
  lighted                   boolean,
  constraint pk_courts primary key (id))
;

create table game (
  name                      varchar(255) not null,
  time                      varchar(255),
  date                      varchar(255),
  location                  varchar(255),
  type                      varchar(255),
  frequency                 varchar(255),
  avg_skl_lvl               varchar(255),
  players                   varchar(255),
  date_created              varchar(255),
  date_edit                 varchar(255),
  update_count              integer,
  constraint pk_game primary key (name))
;

create table league (
  id                        bigint not null,
  name                      varchar(255),
  constraint pk_league primary key (id))
;

create table players (
  id                        bigint not null,
  name                      varchar(255),
  nickname                  varchar(255),
  home_court                varchar(255),
  skill                     varchar(255),
  position                  varchar(255),
  rating                    bigint,
  votes                     bigint,
  height                    varchar(255),
  weight                    varchar(255),
  bio                       varchar(255),
  looking_for               varchar(255),
  pic_url                   varchar(255),
  constraint pk_players primary key (id))
;

create table team (
  id                        bigint not null,
  image_url                 varchar(255),
  name                      varchar(255),
  constraint pk_team primary key (id))
;

create table users (
  id                        bigint not null,
  email                     varchar(255),
  user_name                 varchar(255),
  name                      varchar(255),
  password                  varchar(255),
  admin                     boolean,
  activation_key            varchar(255),
  timestamp                 timestamp,
  players_id                bigint,
  constraint uq_users_email unique (email),
  constraint pk_users primary key (id))
;


create table courts_players (
  courts_id                      bigint not null,
  players_id                     bigint not null,
  constraint pk_courts_players primary key (courts_id, players_id))
;

create table team_league (
  team_id                        bigint not null,
  league_id                      bigint not null,
  constraint pk_team_league primary key (team_id, league_id))
;
create sequence courts_seq;

create sequence game_seq;

create sequence league_seq;

create sequence players_seq;

create sequence team_seq;

create sequence users_seq;

alter table users add constraint fk_users_players_1 foreign key (players_id) references players (id) on delete restrict on update restrict;
create index ix_users_players_1 on users (players_id);



alter table courts_players add constraint fk_courts_players_courts_01 foreign key (courts_id) references courts (id) on delete restrict on update restrict;

alter table courts_players add constraint fk_courts_players_players_02 foreign key (players_id) references players (id) on delete restrict on update restrict;

alter table team_league add constraint fk_team_league_team_01 foreign key (team_id) references team (id) on delete restrict on update restrict;

alter table team_league add constraint fk_team_league_league_02 foreign key (league_id) references league (id) on delete restrict on update restrict;

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists courts;

drop table if exists courts_players;

drop table if exists game;

drop table if exists league;

drop table if exists team_league;

drop table if exists players;

drop table if exists team;

drop table if exists users;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists courts_seq;

drop sequence if exists game_seq;

drop sequence if exists league_seq;

drop sequence if exists players_seq;

drop sequence if exists team_seq;

drop sequence if exists users_seq;

