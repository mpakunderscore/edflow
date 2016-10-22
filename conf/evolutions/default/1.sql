# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table flow (
  id                        bigserial not null,
  title                     varchar(100),
  categories                varchar(1000),
  url                       varchar(255),
  time                      timestamp,
  constraint uq_flow_url unique (url),
  constraint pk_flow primary key (id))
;

create table page (
  id                        bigserial not null,
  title                     varchar(100),
  text                      varchar(100000),
  image                     varchar(1000),
  categories                varchar(1000),
  url                       varchar(255),
  words_count               integer,
  time                      timestamp,
  language                  varchar(10),
  constraint uq_page_url unique (url),
  constraint pk_page primary key (id))
;




# --- !Downs

drop table if exists flow cascade;

drop table if exists page cascade;

