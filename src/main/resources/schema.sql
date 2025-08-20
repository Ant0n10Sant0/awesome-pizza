CREATE SEQUENCE IF NOT EXISTS ingredient_type_id_seq START 1000 INCREMENT 1;

CREATE TABLE IF NOT EXISTS ingredient_type (
	id bigint PRIMARY KEY DEFAULT nextval('ingredient_type_id_seq'),
	code varchar(30) UNIQUE,
	rec_ver integer NOT NULL DEFAULT 0,
	tsi timestamp DEFAULT now(),
	tsu timestamp DEFAULT now(),
	log_del boolean DEFAULT false
);

CREATE TABLE IF NOT EXISTS ingredient (
	id bigserial PRIMARY KEY,
	code varchar(30) UNIQUE,
	descr varchar(100),
	ingredient_type_id bigint NOT NULL,
	rec_ver integer NOT NULL DEFAULT 0,
	tsi timestamp DEFAULT now(),
	tsu timestamp DEFAULT now(),
	log_del boolean DEFAULT false,
	CONSTRAINT fk_ingredient_ingredienttypeid FOREIGN KEY (ingredient_type_id) REFERENCES ingredient_type (id)
);

CREATE SEQUENCE IF NOT EXISTS pizza_base_id_seq START 1000 INCREMENT 1;

CREATE TABLE IF NOT EXISTS pizza_base (
	id bigint PRIMARY KEY DEFAULT nextval('pizza_base_id_seq'),
	code varchar(30) UNIQUE,
	descr varchar(100),
	rec_ver integer NOT NULL DEFAULT 0,
	tsi timestamp DEFAULT now(),
	tsu timestamp DEFAULT now(),
	log_del boolean DEFAULT false
);

CREATE TABLE IF NOT EXISTS pizza (
	id bigserial PRIMARY KEY,
	code varchar(30) UNIQUE,
	descr varchar(100),
	base_id bigint NOT NULL,
	rec_ver integer NOT NULL DEFAULT 0,
	tsi timestamp DEFAULT now(),
	tsu timestamp DEFAULT now(),
	log_del boolean DEFAULT false,
	CONSTRAINT fk_pizza_baseid FOREIGN KEY (base_id) REFERENCES pizza_base (id)
);

CREATE TABLE IF NOT EXISTS pizza_ingredient_link (
	id bigserial PRIMARY KEY,
	ingredient_id bigint NOT NULL,
	pizza_id bigint NOT NULL,
	CONSTRAINT fk_pizza_ingr_link_ingredientid FOREIGN KEY (ingredient_id) REFERENCES ingredient (id),
	CONSTRAINT fk_pizza_ingr_link_pizzaid FOREIGN KEY (pizza_id) REFERENCES pizza (id),
	CONSTRAINT indx_pizza_ingr_link_pizzaingr UNIQUE (pizza_id, ingredient_id)
);

CREATE SEQUENCE IF NOT EXISTS pizza_order_status_id_seq START 1000 INCREMENT 1;

CREATE TABLE IF NOT EXISTS pizza_order_status (
	id bigint PRIMARY KEY DEFAULT nextval('pizza_order_status_id_seq'),
	code varchar(30) UNIQUE,
	descr varchar(100),
	rec_ver integer NOT NULL DEFAULT 0,
	tsi timestamp DEFAULT now(),
	tsu timestamp DEFAULT now(),
	log_del boolean DEFAULT false
);

CREATE TABLE IF NOT EXISTS pizza_order (
	id bigserial PRIMARY KEY,
	code varchar(50) UNIQUE,
	status_id bigint NOT NULL,
	table_nr integer,
	delivery_city varchar(100),
	delivery_street varchar(100),
	delivery_zip varchar(12),
	rec_ver integer NOT NULL DEFAULT 0,
	tsi timestamp DEFAULT now(),
	tsu timestamp DEFAULT now(),
	CONSTRAINT fk_pizza_order_statusid FOREIGN KEY (status_id) REFERENCES pizza_order_status (id)
);

CREATE TABLE IF NOT EXISTS order_pizza_link (
	id bigserial PRIMARY KEY,
	order_id bigint NOT NULL,
	pizza_id bigint NOT NULL,
	extra varchar(200),
	CONSTRAINT fk_order_pizza_link_orderid FOREIGN KEY (order_id) REFERENCES pizza_order (id),
	CONSTRAINT fk_order_pizza_link_pizzaid FOREIGN KEY (pizza_id) REFERENCES pizza (id),
	CONSTRAINT indx_order_pizza_link_ordpizza UNIQUE (order_id, pizza_id)
);