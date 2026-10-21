USE shopsphere;

INSERT INTO categories (name, description)
SELECT 'Electronics', 'Everyday personal technology'
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Electronics');

INSERT INTO categories (name, description)
SELECT 'Home', 'Useful pieces for home and travel'
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Home');

INSERT INTO categories (name, description)
SELECT 'Fashion', 'Comfortable everyday essentials'
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Fashion');

INSERT INTO products (category_id, name, brand, description, price, discount, stock, status)
SELECT category_id, 'Studio Wireless Headphones', 'Northstar',
       'Over-ear wireless headphones for work and travel.', 3499.00, 10, 24, TRUE
FROM categories
WHERE name = 'Electronics'
  AND NOT EXISTS (SELECT 1 FROM products WHERE name = 'Studio Wireless Headphones');

INSERT INTO products (category_id, name, brand, description, price, discount, stock, status)
SELECT category_id, 'USB-C Fast Charger', 'Voltline',
       'Compact 30W USB-C charger for compatible devices.', 1299.00, 0, 40, TRUE
FROM categories
WHERE name = 'Electronics'
  AND NOT EXISTS (SELECT 1 FROM products WHERE name = 'USB-C Fast Charger');

INSERT INTO products (category_id, name, brand, description, price, discount, stock, status)
SELECT category_id, 'Everyday Backpack', 'Fieldwork',
       'Lightweight backpack with a padded laptop sleeve.', 2199.00, 15, 18, TRUE
FROM categories
WHERE name = 'Fashion'
  AND NOT EXISTS (SELECT 1 FROM products WHERE name = 'Everyday Backpack');

INSERT INTO products (category_id, name, brand, description, price, discount, stock, status)
SELECT category_id, 'Ceramic Table Lamp', 'Hearth & Form',
       'A simple ceramic lamp for a bedside or reading corner.', 1899.00, 5, 12, TRUE
FROM categories
WHERE name = 'Home'
  AND NOT EXISTS (SELECT 1 FROM products WHERE name = 'Ceramic Table Lamp');

INSERT INTO products (category_id, name, brand, description, price, discount, stock, status)
SELECT category_id, 'Stainless Travel Bottle', 'Fieldwork',
       'Insulated stainless steel bottle for daily use.', 799.00, 0, 32, TRUE
FROM categories
WHERE name = 'Home'
  AND NOT EXISTS (SELECT 1 FROM products WHERE name = 'Stainless Travel Bottle');

INSERT INTO products (category_id, name, brand, description, price, discount, stock, status)
SELECT category_id, 'Cotton Crew T-Shirt', 'Northstar',
       'Soft cotton crew-neck T-shirt for everyday wear.', 999.00, 10, 28, TRUE
FROM categories
WHERE name = 'Fashion'
  AND NOT EXISTS (SELECT 1 FROM products WHERE name = 'Cotton Crew T-Shirt');

UPDATE products SET image_url = '/images/product1.jpg'
WHERE name = 'Studio Wireless Headphones';

UPDATE products SET image_url = '/images/product2.jpg'
WHERE name = 'USB-C Fast Charger';

UPDATE products SET image_url = '/images/product3.jpg'
WHERE name = 'Everyday Backpack';

UPDATE products SET image_url = '/images/product4.jpg'
WHERE name = 'Ceramic Table Lamp';

UPDATE products SET image_url = '/images/product5.jpg'
WHERE name = 'Stainless Travel Bottle';

UPDATE products SET image_url = '/images/product1.jpg'
WHERE name = 'Cotton Crew T-Shirt';