-- =====================================================
-- Restaurant Management System Database Schema
-- =====================================================

-- Drop existing tables if they exist (in correct order to handle foreign keys)
DROP TABLE IF EXISTS dish_ingredient CASCADE;
DROP TABLE IF EXISTS order_item CASCADE;
DROP TABLE IF EXISTS reservations CASCADE;
DROP TABLE IF EXISTS order_table CASCADE;
DROP TABLE IF EXISTS dishes CASCADE;
DROP TABLE IF EXISTS category CASCADE;
DROP TABLE IF EXISTS ingredient CASCADE;
DROP TABLE IF EXISTS order_status CASCADE;
DROP TABLE IF EXISTS table_info CASCADE;
DROP TABLE IF EXISTS customers CASCADE;
DROP TABLE IF EXISTS staff CASCADE;

-- =====================================================
-- CREATE TABLES
-- =====================================================

-- Staff Table
CREATE TABLE staff (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    roles VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create index for staff username
CREATE INDEX idx_staff_username ON staff(username);

-- Customers Table
CREATE TABLE customers (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(15) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Category Table
CREATE TABLE category (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Dishes Table
CREATE TABLE dishes (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    price DECIMAL(10,2) NOT NULL CHECK (price > 0),
    category_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE CASCADE
);

-- Create indexes for dishes
CREATE INDEX idx_dish_name ON dishes(name);
CREATE INDEX idx_dish_category ON dishes(category_id);

-- Table Info Table
CREATE TABLE table_info (
    id BIGSERIAL PRIMARY KEY,
    table_number INTEGER NOT NULL UNIQUE CHECK (table_number > 0),
    capacity INTEGER NOT NULL CHECK (capacity > 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create index for table number
CREATE INDEX idx_table_number ON table_info(table_number);

-- Order Status Table
CREATE TABLE order_status (
    id BIGSERIAL PRIMARY KEY,
    status_name VARCHAR(50) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Orders Table
CREATE TABLE order_table (
    id BIGSERIAL PRIMARY KEY,
    order_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    table_id BIGINT,
    staff_id BIGINT NOT NULL,
    customer_id BIGINT,
    status_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (table_id) REFERENCES table_info(id) ON DELETE SET NULL,
    FOREIGN KEY (staff_id) REFERENCES staff(id) ON DELETE CASCADE,
    FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE SET NULL,
    FOREIGN KEY (status_id) REFERENCES order_status(id) ON DELETE CASCADE
);

-- Order Items Table
CREATE TABLE order_item (
    id BIGSERIAL PRIMARY KEY,
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    order_id BIGINT NOT NULL,
    dish_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES order_table(id) ON DELETE CASCADE,
    FOREIGN KEY (dish_id) REFERENCES dishes(id) ON DELETE CASCADE
);

-- Ingredients Table
CREATE TABLE ingredient (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    quantity_in_stock DECIMAL(10,2) NOT NULL DEFAULT 0,
    unit VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Dish Ingredients Table (Many-to-Many relationship)
CREATE TABLE dish_ingredient (
    id BIGSERIAL PRIMARY KEY,
    dish_id BIGINT NOT NULL,
    ingredient_id BIGINT NOT NULL,
    quantity DECIMAL(10,2) NOT NULL CHECK (quantity > 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (dish_id) REFERENCES dishes(id) ON DELETE CASCADE,
    FOREIGN KEY (ingredient_id) REFERENCES ingredient(id) ON DELETE CASCADE,
    UNIQUE(dish_id, ingredient_id)
);

-- Reservations Table
CREATE TABLE reservations (
    id BIGSERIAL PRIMARY KEY,
    customer_name VARCHAR(100) NOT NULL,
    customer_phone VARCHAR(15) NOT NULL,
    reservation_time TIMESTAMP NOT NULL,
    number_of_people INTEGER NOT NULL CHECK (number_of_people > 0),
    table_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (table_id) REFERENCES table_info(id) ON DELETE CASCADE
);

-- Create indexes for reservations
CREATE INDEX idx_reservation_time ON reservations(reservation_time);
CREATE INDEX idx_reservation_table ON reservations(table_id);
CREATE INDEX idx_reservation_phone ON reservations(customer_phone);

-- =====================================================
-- INSERT SAMPLE DATA
-- =====================================================

-- Insert Staff (10 entries with BCrypt encoded passwords)
INSERT INTO staff (name, username, password, roles) VALUES
('John Smith', 'admin', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', 'ADMIN'), -- password: password
('Sarah Johnson', 'manager1', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', 'MANAGER'), -- password: password
('Mike Wilson', 'waiter1', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', 'WAITER'), -- password: password
('Emily Davis', 'waiter2', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', 'WAITER'), -- password: password
('Robert Brown', 'chef1', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', 'CHEF'), -- password: password
('Lisa Garcia', 'chef2', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', 'CHEF'), -- password: password
('David Miller', 'cashier1', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', 'CASHIER'), -- password: password
('Jennifer Taylor', 'waiter3', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', 'WAITER'), -- password: password
('James Anderson', 'manager2', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', 'MANAGER'), -- password: password
('Maria Rodriguez', 'waiter4', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', 'WAITER'); -- password: password

-- Insert Customers (10 entries)
INSERT INTO customers (name, phone, email) VALUES
('Alice Cooper', '1234567890', 'alice.cooper@email.com'),
('Bob Johnson', '2345678901', 'bob.johnson@email.com'),
('Carol White', '3456789012', 'carol.white@email.com'),
('Daniel Green', '4567890123', 'daniel.green@email.com'),
('Eva Martinez', '5678901234', 'eva.martinez@email.com'),
('Frank Thompson', '6789012345', 'frank.thompson@email.com'),
('Grace Lee', '7890123456', 'grace.lee@email.com'),
('Henry Clark', '8901234567', 'henry.clark@email.com'),
('Iris Walker', '9012345678', 'iris.walker@email.com'),
('Jack Robinson', '0123456789', 'jack.robinson@email.com');

-- Insert Categories (10 entries)
INSERT INTO category (name) VALUES
('Appetizers'),
('Main Course'),
('Desserts'),
('Beverages'),
('Salads'),
('Soups'),
('Pizza'),
('Pasta'),
('Seafood'),
('Vegetarian');

-- Insert Table Info (10 entries)
INSERT INTO table_info (table_number, capacity) VALUES
(1, 2),
(2, 4),
(3, 4),
(4, 6),
(5, 2),
(6, 8),
(7, 4),
(8, 2),
(9, 6),
(10, 4);

-- Insert Order Status (10 entries)
INSERT INTO order_status (status_name) VALUES
('Pending'),
('Confirmed'),
('Preparing'),
('Ready'),
('Served'),
('Completed'),
('Cancelled'),
('On Hold'),
('Delivered'),
('Paid');

-- Insert Dishes (10 entries)
INSERT INTO dishes (name, price, category_id) VALUES
('Caesar Salad', 12.99, 5),
('Grilled Chicken Breast', 18.99, 2),
('Chocolate Lava Cake', 8.99, 3),
('Fresh Orange Juice', 4.99, 4),
('Margherita Pizza', 16.99, 7),
('Spaghetti Carbonara', 15.99, 8),
('Grilled Salmon', 22.99, 9),
('Vegetable Stir Fry', 13.99, 10),
('Tomato Basil Soup', 7.99, 6),
('Buffalo Wings', 11.99, 1);

-- Insert Ingredients (10 entries)
INSERT INTO ingredient (name, quantity_in_stock, unit) VALUES
('Chicken Breast', 50.0, 'kg'),
('Salmon Fillet', 25.0, 'kg'),
('Tomatoes', 30.0, 'kg'),
('Lettuce', 15.0, 'kg'),
('Mozzarella Cheese', 20.0, 'kg'),
('Pasta', 40.0, 'kg'),
('Olive Oil', 10.0, 'liters'),
('Garlic', 5.0, 'kg'),
('Onions', 25.0, 'kg'),
('Bell Peppers', 15.0, 'kg');

-- Insert Orders (10 entries)
INSERT INTO order_table (order_time, table_id, staff_id, customer_id, status_id) VALUES
('2024-08-12 12:30:00', 1, 3, 1, 5),
('2024-08-12 13:15:00', 2, 4, 2, 6),
('2024-08-12 14:00:00', 3, 3, 3, 3),
('2024-08-12 14:30:00', 4, 8, 4, 4),
('2024-08-12 15:00:00', 5, 4, 5, 2),
('2024-08-12 15:30:00', 6, 3, 6, 5),
('2024-08-12 16:00:00', 7, 8, 7, 6),
('2024-08-12 16:30:00', 8, 4, 8, 1),
('2024-08-12 17:00:00', 9, 3, 9, 3),
('2024-08-12 17:30:00', 10, 8, 10, 4);

-- Insert Order Items (10 entries)
INSERT INTO order_item (quantity, order_id, dish_id) VALUES
(2, 1, 1),
(1, 1, 3),
(1, 2, 2),
(2, 2, 4),
(1, 3, 5),
(1, 3, 6),
(2, 4, 7),
(1, 4, 8),
(3, 5, 9),
(2, 5, 10);

-- Insert Dish Ingredients (10 entries)
INSERT INTO dish_ingredient (dish_id, ingredient_id, quantity) VALUES
(1, 4, 0.2), -- Caesar Salad - Lettuce
(2, 1, 0.3), -- Grilled Chicken - Chicken Breast
(5, 5, 0.15), -- Margherita Pizza - Mozzarella
(6, 6, 0.1), -- Spaghetti Carbonara - Pasta
(7, 2, 0.25), -- Grilled Salmon - Salmon Fillet
(8, 10, 0.1), -- Vegetable Stir Fry - Bell Peppers
(9, 3, 0.2), -- Tomato Soup - Tomatoes
(10, 1, 0.4), -- Buffalo Wings - Chicken Breast
(2, 7, 0.02), -- Grilled Chicken - Olive Oil
(8, 9, 0.05); -- Vegetable Stir Fry - Onions

-- Insert Reservations (10 entries)
INSERT INTO reservations (customer_name, customer_phone, reservation_time, number_of_people, table_id) VALUES
('Michael Scott', '1111111111', '2024-08-15 19:00:00', 4, 2),
('Pam Beesly', '2222222222', '2024-08-15 20:00:00', 2, 1),
('Jim Halpert', '3333333333', '2024-08-16 18:30:00', 6, 4),
('Dwight Schrute', '4444444444', '2024-08-16 19:30:00', 8, 6),
('Angela Martin', '5555555555', '2024-08-17 18:00:00', 4, 3),
('Kevin Malone', '6666666666', '2024-08-17 20:30:00', 2, 5),
('Oscar Martinez', '7777777777', '2024-08-18 19:00:00', 6, 9),
('Stanley Hudson', '8888888888', '2024-08-18 18:30:00', 4, 7),
('Phyllis Vance', '9999999999', '2024-08-19 19:30:00', 2, 8),
('Creed Bratton', '0000000000', '2024-08-19 20:00:00', 4, 10);

-- =====================================================
-- CREATE VIEWS FOR COMMON QUERIES
-- =====================================================

-- View for Order Summary
CREATE OR REPLACE VIEW order_summary AS
SELECT 
    o.id as order_id,
    o.order_time,
    s.name as staff_name,
    ti.table_number,
    c.name as customer_name,
    os.status_name,
    SUM(oi.quantity * d.price) as total_amount
FROM order_table o
JOIN staff s ON o.staff_id = s.id
LEFT JOIN table_info ti ON o.table_id = ti.id
LEFT JOIN customers c ON o.customer_id = c.id
JOIN order_status os ON o.status_id = os.id
LEFT JOIN order_item oi ON o.id = oi.order_id
LEFT JOIN dishes d ON oi.dish_id = d.id
GROUP BY o.id, o.order_time, s.name, ti.table_number, c.name, os.status_name
ORDER BY o.order_time DESC;

-- View for Menu with Categories
CREATE OR REPLACE VIEW menu_view AS
SELECT 
    d.id,
    d.name as dish_name,
    d.price,
    c.name as category_name
FROM dishes d
JOIN category c ON d.category_id = c.id
ORDER BY c.name, d.name;

-- View for Table Status
CREATE OR REPLACE VIEW table_status AS
SELECT 
    ti.id,
    ti.table_number,
    ti.capacity,
    CASE 
        WHEN r.id IS NOT NULL AND r.reservation_time > NOW() THEN 'Reserved'
        WHEN o.id IS NOT NULL AND os.status_name IN ('Confirmed', 'Preparing', 'Ready', 'Served') THEN 'Occupied'
        ELSE 'Available'
    END as status
FROM table_info ti
LEFT JOIN reservations r ON ti.id = r.table_id 
    AND r.reservation_time BETWEEN NOW() AND NOW() + INTERVAL '2 hours'
LEFT JOIN order_table o ON ti.id = o.table_id 
    AND DATE(o.order_time) = CURRENT_DATE
LEFT JOIN order_status os ON o.status_id = os.id
ORDER BY ti.table_number;

-- =====================================================
-- USEFUL QUERIES FOR TESTING
-- =====================================================

-- Test query to verify data
SELECT 'Staff Count' as table_name, COUNT(*) as count FROM staff
UNION ALL
SELECT 'Customers Count', COUNT(*) FROM customers
UNION ALL
SELECT 'Categories Count', COUNT(*) FROM category
UNION ALL
SELECT 'Dishes Count', COUNT(*) FROM dishes
UNION ALL
SELECT 'Tables Count', COUNT(*) FROM table_info
UNION ALL
SELECT 'Order Status Count', COUNT(*) FROM order_status
UNION ALL
SELECT 'Orders Count', COUNT(*) FROM order_table
UNION ALL
SELECT 'Order Items Count', COUNT(*) FROM order_item
UNION ALL
SELECT 'Ingredients Count', COUNT(*) FROM ingredient
UNION ALL
SELECT 'Reservations Count', COUNT(*) FROM reservations;

-- =====================================================
-- INDEXES FOR PERFORMANCE
-- =====================================================

-- Additional indexes for better performance
CREATE INDEX IF NOT EXISTS idx_order_time ON order_table(order_time);
CREATE INDEX IF NOT EXISTS idx_order_status ON order_table(status_id);
CREATE INDEX IF NOT EXISTS idx_order_customer ON order_table(customer_id);
CREATE INDEX IF NOT EXISTS idx_order_staff ON order_table(staff_id);
CREATE INDEX IF NOT EXISTS idx_order_table ON order_table(table_id);

-- =====================================================
-- COMPLETION MESSAGE
-- =====================================================

SELECT 'Database schema created successfully with sample data!' as message;