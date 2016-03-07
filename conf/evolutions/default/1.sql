# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table page (
  id                        bigserial not null,
  title                     varchar(100),
  text                      varchar(100000),
  image                     varchar(1000),
  categories                varchar(1000),
  url                       varchar(255),
  time                      timestamp,
  constraint uq_page_url unique (url),
  constraint pk_page primary key (id))
;




# --- !Downs

drop table if exists page cascade;

