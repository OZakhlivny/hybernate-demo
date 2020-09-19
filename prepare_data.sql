BEGIN;

DROP DATABASE IF EXISTS sales;
CREATE DATABASE sales;
USE sales;

DROP TABLE IF EXISTS customers;
CREATE TABLE customers(
id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
name VARCHAR(128),

INDEX customers_name_idx(name)
);

DROP TABLE IF EXISTS products;
CREATE TABLE products(
id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
name VARCHAR(128),
cost DECIMAL(8,2),

INDEX products_name_idx(name)
);

DROP TABLE IF EXISTS orders;
CREATE TABLE orders(
id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
customer_id BIGINT UNSIGNED NOT NULL,
sale_time DATETIME DEFAULT NOW(),

FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE
);

DROP TABLE IF EXISTS order_products;
CREATE TABLE order_products(
id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
order_id BIGINT UNSIGNED NOT NULL,
product_id BIGINT UNSIGNED NOT NULL,
amount INT UNSIGNED,
cost DECIMAL(8,2),

FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);

INSERT INTO customers(name) VALUES ('Customer1'), ('Customer2'), ('Customer3'), ('Customer4');

INSERT INTO products(name, cost) VALUES ("Product1", 10.0), ("Product2", 5.5), ("Product3", 20), ("Product4", 15.5);

INSERT INTO orders(customer_id, sale_time) VALUES (1, '2020-09-01 12:00:00'), (3, '2020-09-10 14:00:00');

INSERT INTO order_products (order_id, product_id, amount, cost) VALUES (1, 1, 1, 10.0), (1, 2, 5, 5.5), (1, 4, 2, 15.5), (2, 1, 5, 10.0), (2, 2, 10, 5.5);

COMMIT;

