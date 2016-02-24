# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table page (
  id                        bigint not null,
  title                     varchar(255),
  description               varchar(10000),
  text                      varchar(100000),
  image                     varchar(1000),
  categories                varchar(1000),
  url                       varchar(255),
  time                      timestamp,
  constraint uq_page_title unique (title),
  constraint pk_page primary key (id))
;

create sequence page_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists page;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists page_seq;

