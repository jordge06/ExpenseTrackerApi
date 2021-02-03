drop database expensetrackerdb;
drop user expensetracker;
create user expensetracker with password 'password';
create database expensetrackerdb with template=template0 owner=expensetracker;
\connect expensetrackerdb;
alter default privileges grant all on tables to expensetracker;
alter default privileges grant all on sequences to expensetracker;

create table et_users(
user_id INTEGER PRIMARY KEY NOT NULL,
first_name VARCHAR(20) NOT NULL,
last_name VARCHAR(20) NOT NULL,
email VARCHAR(30) NOT NULL,
password TEXT NOT NULL
);

create table et_categories(
category_id INTEGER PRIMARY KEY NOT NULL,
user_id INTEGER NOT NULL,
title VARCHAR(20) NOT NULL,
description VARCHAR(50) NOT NULL
);
alter table et_categories add constraint cat_users_fk
foreign key (user_id) references et_users(user_id);

create table et_transactions(
transaction_id INTEGER PRIMARY KEY NOT NULL,
category_id INTEGER NOT NULL,
user_id INTEGER NOT NULL,
amount NUMERIC(10,2) NOT NULL,
note VARCHAR(50) NOT NULL,
transaction_date BIGINT NOT NULL
);
alter table et_transactions add constraint transaction_cat_fk
foreign key (category_id) references et_categories(category_id);
alter table et_transactions add constraint trans_users_fk
foreign key (user_id) references et_users(user_id);

create sequence et_users_seq increment 1 start 1;
create sequence et_categories_seq increment 1 start 1;
create sequence et_transactions_seq increment 1 start 1000;
