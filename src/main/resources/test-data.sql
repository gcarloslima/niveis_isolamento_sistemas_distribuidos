-- public.categories definition

-- Drop table

-- DROP TABLE public.categories;

CREATE TABLE public.categories (
	id int8 GENERATED BY DEFAULT AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE) NOT NULL,
	description varchar(255) NULL,
	"name" varchar(255) NULL,
	CONSTRAINT categories_pkey PRIMARY KEY (id)
);


-- public.clients definition

-- Drop table

-- DROP TABLE public.clients;

CREATE TABLE public.clients (
	id int8 GENERATED BY DEFAULT AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE) NOT NULL,
	address varchar(255) NULL,
	cep varchar(255) NULL,
	city varchar(255) NULL,
	country varchar(255) NULL,
	fax varchar(255) NULL,
	"name" varchar(255) NULL,
	phone varchar(255) NULL,
	"role" varchar(255) NULL,
	CONSTRAINT clients_pkey PRIMARY KEY (id)
);


-- public.orders definition

-- Drop table

-- DROP TABLE public.orders;

CREATE TABLE public.orders (
	id int8 GENERATED BY DEFAULT AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE) NOT NULL,
	order_date date NULL,
	client_id int8 NULL,
	CONSTRAINT orders_pkey PRIMARY KEY (id),
	CONSTRAINT fkm2dep9derpoaehshbkkatam3v FOREIGN KEY (client_id) REFERENCES public.clients(id)
);


-- public.products definition

-- Drop table

-- DROP TABLE public.products;

CREATE TABLE public.products (
	id int8 GENERATED BY DEFAULT AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE) NOT NULL,
	image_url varchar(255) NULL,
	"name" varchar(255) NULL,
	price numeric(38, 2) NULL,
	stock int4 NULL,
	category_id int8 NULL,
	CONSTRAINT products_pkey PRIMARY KEY (id),
	CONSTRAINT fkog2rp4qthbtt2lfyhfo32lsw9 FOREIGN KEY (category_id) REFERENCES public.categories(id)
);


-- public.order_details definition

-- Drop table

-- DROP TABLE public.order_details;

CREATE TABLE public.order_details (
	id int8 GENERATED BY DEFAULT AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE) NOT NULL,
	discount numeric(38, 2) NULL,
	quantity int4 NULL,
	order_id int8 NULL,
	product_id int8 NULL,
	CONSTRAINT order_details_pkey PRIMARY KEY (id),
	CONSTRAINT fk4q98utpd73imf4yhttm3w0eax FOREIGN KEY (product_id) REFERENCES public.products(id),
	CONSTRAINT fkjyu2qbqt8gnvno9oe9j2s2ldk FOREIGN KEY (order_id) REFERENCES public.orders(id)
);

insert into clients(id, name) values (1, 'Carlos');