create database netbookstore;
use netbookstore;
create table categorys(
	id varchar(100) primary key,
	name varchar(100) not null unique,
	description varchar(255)
);
create table books(
	id varchar(100) primary key,
	name varchar(100) not null unique,
	author varchar(100),
	price float(8,2),
	path varchar(100),
	filename varchar(100),
	description varchar(255),
	categoryId varchar(100),
	constraint category_id_fk foreign key(categoryId) references categorys(id)
);
create table customers(
	id varchar(100) primary key,
	username varchar(100) not null unique,
	password varchar(100) not null,
	nickname varchar(100),
	phonenum varchar(100),
	address varchar(100),
	email varchar(100)
);
create table orders(
	ordernum varchar(100) primary key,
	quantity int,
	money float(8,2),
	status int,
	customerId varchar(100),
	constraint customer_Id_fk foreign key(customerId) references customers(id)
);
create table orderitems(
	id varchar(100) primary key,
	quantity int,
	price float(8,2),
	bookId varchar(100),
	ordernum varchar(100),
	constraint bookId_fk foreign key(bookId) references books(id),
	constraint ordernum_fk foreign key(ordernum) references orders(ordernum)
);
##后台管理者
CREATE TABLE managers (
   id int(11) NOT NULL AUTO_INCREMENT,
   name varchar(50) NOT NULL,
   password varchar(50) NOT NULL,
   PRIMARY KEY (id)
 );