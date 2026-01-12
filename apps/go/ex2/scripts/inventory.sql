-- SQLite script to create an inventory management system

-- Step 1: Enable foreign key constraints
PRAGMA foreign_keys = ON;

-- Step 2: Create the suppliers table
CREATE TABLE suppliers (
    supplier_id INTEGER PRIMARY KEY AUTOINCREMENT,
    supplier_name TEXT NOT NULL,
    contact_name TEXT,
    phone TEXT
);

-- Step 3: Create the categories table
CREATE TABLE categories (
    category_id INTEGER PRIMARY KEY AUTOINCREMENT,
    category_name TEXT NOT NULL,
    supplier_id INTEGER NOT NULL,
    FOREIGN KEY (supplier_id) REFERENCES suppliers(supplier_id) ON DELETE CASCADE
);

-- Step 4: Create the products table
CREATE TABLE products (
    product_id INTEGER PRIMARY KEY AUTOINCREMENT,
    product_name TEXT NOT NULL,
    category_id INTEGER NOT NULL,
    price REAL,
    stock INTEGER,
    FOREIGN KEY (category_id) REFERENCES categories(category_id) ON DELETE CASCADE
);

-- Step 5: Create the product_list view
CREATE VIEW product_list AS
SELECT 
    p.product_id,
    p.product_name,
    p.price,
    p.stock,
    c.category_name,
    s.supplier_name
FROM 
    products p
JOIN 
    categories c ON p.category_id = c.category_id
JOIN 
    suppliers s ON c.supplier_id = s.supplier_id;

-- Step 6: Populate the suppliers table with sample data
INSERT INTO suppliers (supplier_name, contact_name, phone)
VALUES 
('Supplier A', 'John Doe', '123-456-7890'),
('Supplier B', 'Jane Smith', '987-654-3210');

-- Step 7: Populate the categories table with sample data
INSERT INTO categories (category_name, supplier_id)
VALUES 
('Electronics', 1),
('Furniture', 2);

-- Step 8: Populate the products table with sample data
INSERT INTO products (product_name, category_id, price, stock)
VALUES 
('Laptop', 1, 1200.00, 50),
('Smartphone', 1, 800.00, 100),
('Chair', 2, 150.00, 200),
('Table', 2, 300.00, 150);




