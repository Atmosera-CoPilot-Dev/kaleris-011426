-- Script to design and implement the 'Inventory' database schema in Microsoft SQL Server 2019+, following best practices for structure, integrity, and automation.

/*

Requirements (concise design goals):
1. Consistent naming & keys: singular tables, primary key id, foreign keys <table>_id, constraint names fk_<table>_<column>.
2. Data integrity: enforce referential integrity; constrain lengths and use precise data types.
3. Performance: efficient queries with appropriate types; nonclustered indexes on foreign key columns.
4. Encapsulated logic: views, stored procedures, functions, triggers for reusable automation.
5. Testability: seed representative sample data to validate schema and generated scripts.

Steps (instructional prompts for Copilot):
1) If database Inventory exists, drop and recreate it; then USE it.
2) Create table suppliers with an identity/int primary key id and columns for name (required, 50), optional address (255), city (required, 50), state (2, required).
3) Create table categories linked to suppliers (supplier_id FK) with required short name (50), optional description (255).
4) Create table products linked to categories (category_id FK) with required name (50) and price decimal(10,2).
5) Insert sample seed data: suppliers first, then categories, then products (ensure FK order).
6) Add nonclustered indexes on products.category_id and categories.supplier_id.
7) Create view product_list joining products -> categories -> suppliers selecting ids and names per requirements.
8) Stored procedure get_product_list simply SELECT * FROM product_list.
9) Trigger on categories delete: when a category is deleted, set affected products.category_id to NULL or handle per chosen strategy (e.g., soft fail) — implement an update to NULL (requires category_id nullable) or cascade logic consistent with requirements.
10) Scalar function fn_total_products_in_category(categoryId) returning INT count from products.
11) Scalar function fn_total_products_for_supplier(supplierId) returning INT counting products through categories.
*/



