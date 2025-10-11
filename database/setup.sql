-- Medicine Inventory Management System Database Setup
-- Drop database if exists and create fresh
DROP DATABASE IF EXISTS medicine_inventory;
CREATE DATABASE medicine_inventory;
USE medicine_inventory;

-- ============================================
-- 1. USERS TABLE
-- Stores both supplier and hospital user accounts
-- ============================================
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    user_type ENUM('SUPPLIER', 'HOSPITAL') NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20),
    address TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,
    INDEX idx_username (username),
    INDEX idx_user_type (user_type)
);

-- ============================================
-- 2. MEDICINE CATEGORIES TABLE
-- For organizing medicines into categories
-- ============================================
CREATE TABLE medicine_categories (
    category_id INT PRIMARY KEY AUTO_INCREMENT,
    category_name VARCHAR(100) UNIQUE NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- 3. MEDICINES TABLE
-- Core medicine information with supplier relationship
-- ============================================
CREATE TABLE medicines (
    medicine_id INT PRIMARY KEY AUTO_INCREMENT,
    medicine_name VARCHAR(200) NOT NULL,
    category_id INT,
    supplier_id INT NOT NULL,
    description TEXT,
    manufacturer VARCHAR(100),
    unit_price DECIMAL(10, 2) NOT NULL CHECK (unit_price >= 0),
    stock_quantity INT NOT NULL DEFAULT 0 CHECK (stock_quantity >= 0),
    reorder_level INT DEFAULT 10,
    unit_of_measure VARCHAR(20) DEFAULT 'Units',
    requires_prescription BOOLEAN DEFAULT FALSE,
    batch_number VARCHAR(50),
    manufacturing_date DATE,
    expiry_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (category_id) REFERENCES medicine_categories(category_id) ON DELETE SET NULL,
    FOREIGN KEY (supplier_id) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_medicine_name (medicine_name),
    INDEX idx_supplier (supplier_id),
    INDEX idx_category (category_id),
    INDEX idx_expiry (expiry_date),
    CONSTRAINT chk_expiry_after_manufacture CHECK (expiry_date > manufacturing_date)
);

-- ============================================
-- 4. ORDERS TABLE
-- Hospital orders from suppliers
-- ============================================
CREATE TABLE orders (
    order_id INT PRIMARY KEY AUTO_INCREMENT,
    hospital_id INT NOT NULL,
    supplier_id INT NOT NULL,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    order_status ENUM('PENDING', 'APPROVED', 'SHIPPED', 'DELIVERED', 'CANCELLED') DEFAULT 'PENDING',
    total_amount DECIMAL(12, 2) NOT NULL DEFAULT 0,
    shipping_address TEXT NOT NULL,
    special_instructions TEXT,
    approved_date TIMESTAMP NULL,
    shipped_date TIMESTAMP NULL,
    delivered_date TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (hospital_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (supplier_id) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_hospital (hospital_id),
    INDEX idx_supplier (supplier_id),
    INDEX idx_status (order_status),
    INDEX idx_order_date (order_date)
);

-- ============================================
-- 5. ORDER ITEMS TABLE
-- Individual items within each order
-- ============================================
CREATE TABLE order_items (
    order_item_id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT NOT NULL,
    medicine_id INT NOT NULL,
    quantity INT NOT NULL CHECK (quantity > 0),
    unit_price DECIMAL(10, 2) NOT NULL CHECK (unit_price >= 0),
    subtotal DECIMAL(12, 2) GENERATED ALWAYS AS (quantity * unit_price) STORED,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
    FOREIGN KEY (medicine_id) REFERENCES medicines(medicine_id) ON DELETE RESTRICT,
    INDEX idx_order (order_id),
    INDEX idx_medicine (medicine_id)
);

-- ============================================
-- 6. SHOPPING CART TABLE
-- Temporary storage for hospital's cart items
-- ============================================
CREATE TABLE shopping_cart (
    cart_id INT PRIMARY KEY AUTO_INCREMENT,
    hospital_id INT NOT NULL,
    medicine_id INT NOT NULL,
    quantity INT NOT NULL CHECK (quantity > 0),
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (hospital_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (medicine_id) REFERENCES medicines(medicine_id) ON DELETE CASCADE,
    UNIQUE KEY unique_cart_item (hospital_id, medicine_id),
    INDEX idx_hospital (hospital_id)
);

-- ============================================
-- 7. ORDER STATUS HISTORY TABLE
-- Audit trail for order status changes
-- ============================================
CREATE TABLE order_status_history (
    history_id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT NOT NULL,
    old_status ENUM('PENDING', 'APPROVED', 'SHIPPED', 'DELIVERED', 'CANCELLED'),
    new_status ENUM('PENDING', 'APPROVED', 'SHIPPED', 'DELIVERED', 'CANCELLED') NOT NULL,
    changed_by INT NOT NULL,
    change_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    notes TEXT,
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
    FOREIGN KEY (changed_by) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_order (order_id)
);

-- ============================================
-- 8. STOCK MOVEMENT LOG TABLE
-- Track all inventory changes for audit purposes
-- ============================================
CREATE TABLE stock_movement_log (
    log_id INT PRIMARY KEY AUTO_INCREMENT,
    medicine_id INT NOT NULL,
    movement_type ENUM('ADDITION', 'SALE', 'ADJUSTMENT', 'RETURN', 'EXPIRED') NOT NULL,
    quantity_change INT NOT NULL,
    previous_stock INT NOT NULL,
    new_stock INT NOT NULL,
    reference_id INT NULL COMMENT 'References order_id for sales',
    performed_by INT NOT NULL,
    movement_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    notes TEXT,
    FOREIGN KEY (medicine_id) REFERENCES medicines(medicine_id) ON DELETE CASCADE,
    FOREIGN KEY (performed_by) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_medicine (medicine_id),
    INDEX idx_movement_type (movement_type),
    INDEX idx_movement_date (movement_date)
);

-- ============================================
-- TRIGGERS
-- ============================================

-- Trigger to update order total_amount when order items are inserted/updated
DELIMITER //
CREATE TRIGGER trg_update_order_total_after_insert
AFTER INSERT ON order_items
FOR EACH ROW
BEGIN
    UPDATE orders 
    SET total_amount = (
        SELECT SUM(subtotal) 
        FROM order_items 
        WHERE order_id = NEW.order_id
    )
    WHERE order_id = NEW.order_id;
END//

CREATE TRIGGER trg_update_order_total_after_update
AFTER UPDATE ON order_items
FOR EACH ROW
BEGIN
    UPDATE orders 
    SET total_amount = (
        SELECT SUM(subtotal) 
        FROM order_items 
        WHERE order_id = NEW.order_id
    )
    WHERE order_id = NEW.order_id;
END//

CREATE TRIGGER trg_update_order_total_after_delete
AFTER DELETE ON order_items
FOR EACH ROW
BEGIN
    UPDATE orders 
    SET total_amount = COALESCE((
        SELECT SUM(subtotal) 
        FROM order_items 
        WHERE order_id = OLD.order_id
    ), 0)
    WHERE order_id = OLD.order_id;
END//

-- Trigger to log order status changes
CREATE TRIGGER trg_log_order_status_change
AFTER UPDATE ON orders
FOR EACH ROW
BEGIN
    IF OLD.order_status != NEW.order_status THEN
        INSERT INTO order_status_history (order_id, old_status, new_status, changed_by, notes)
        VALUES (NEW.order_id, OLD.order_status, NEW.order_status, NEW.supplier_id, 
                CONCAT('Status changed from ', OLD.order_status, ' to ', NEW.order_status));
    END IF;
END//

-- Trigger to update order dates based on status
CREATE TRIGGER trg_update_order_dates
BEFORE UPDATE ON orders
FOR EACH ROW
BEGIN
    IF OLD.order_status != NEW.order_status THEN
        IF NEW.order_status = 'APPROVED' AND OLD.order_status = 'PENDING' THEN
            SET NEW.approved_date = CURRENT_TIMESTAMP;
        ELSEIF NEW.order_status = 'SHIPPED' THEN
            SET NEW.shipped_date = CURRENT_TIMESTAMP;
        ELSEIF NEW.order_status = 'DELIVERED' THEN
            SET NEW.delivered_date = CURRENT_TIMESTAMP;
        END IF;
    END IF;
END//

DELIMITER ;

-- ============================================
-- SAMPLE DATA
-- ============================================

-- Insert sample medicine categories
INSERT INTO medicine_categories (category_name, description) VALUES
('Antibiotics', 'Medications used to treat bacterial infections'),
('Pain Relief', 'Analgesics and pain management medications'),
('Cardiovascular', 'Heart and blood pressure medications'),
('Diabetes Care', 'Medications for managing diabetes'),
('Vitamins & Supplements', 'Nutritional supplements and vitamins'),
('Respiratory', 'Medications for respiratory conditions'),
('Gastrointestinal', 'Medications for digestive system'),
('Dermatology', 'Skin care and treatment medications');

-- Insert sample users (Suppliers and Hospitals)
INSERT INTO users (username, password, user_type, full_name, email, phone, address) VALUES
('supplier1', 'pass123', 'SUPPLIER', 'MediSupply Corp', 'contact@medisupply.com', '555-0101', '123 Pharma Street, Medical District'),
('supplier2', 'pass123', 'SUPPLIER', 'HealthFirst Distributors', 'info@healthfirst.com', '555-0102', '456 Medicine Avenue, Health Plaza'),
('supplier3', 'pass123', 'SUPPLIER', 'Global Pharma Solutions', 'sales@globalpharma.com', '555-0103', '789 Wellness Road, Pharma Park'),
('hospital1', 'pass123', 'HOSPITAL', 'City General Hospital', 'procurement@citygen.com', '555-0201', '100 Hospital Lane, Downtown'),
('hospital2', 'pass123', 'HOSPITAL', 'St. Mary Medical Center', 'pharmacy@stmary.com', '555-0202', '200 Care Boulevard, Uptown'),
('hospital3', 'pass123', 'HOSPITAL', 'Regional Health Institute', 'supplies@regional.com', '555-0203', '300 Health Drive, Suburbs');

-- Insert sample medicines for different suppliers
INSERT INTO medicines (medicine_name, category_id, supplier_id, description, manufacturer, unit_price, stock_quantity, reorder_level, unit_of_measure, requires_prescription, batch_number, manufacturing_date, expiry_date) VALUES
-- Supplier 1 medicines
('Amoxicillin 500mg', 1, 1, 'Broad-spectrum antibiotic capsules', 'PharmaCorp', 0.50, 5000, 500, 'Capsules', TRUE, 'AMOX-2024-001', '2024-01-15', '2026-01-15'),
('Paracetamol 500mg', 2, 1, 'Pain relief and fever reducer', 'MediLab', 0.10, 10000, 1000, 'Tablets', FALSE, 'PARA-2024-002', '2024-02-01', '2027-02-01'),
('Lisinopril 10mg', 3, 1, 'ACE inhibitor for hypertension', 'CardioMed', 0.75, 3000, 300, 'Tablets', TRUE, 'LISI-2024-003', '2024-01-20', '2026-01-20'),
('Metformin 500mg', 4, 1, 'Diabetes medication', 'DiabeCare', 0.30, 4000, 400, 'Tablets', TRUE, 'METF-2024-004', '2024-03-01', '2026-03-01'),
('Vitamin D3 1000IU', 5, 1, 'Vitamin supplement', 'VitaPlus', 0.20, 2000, 200, 'Capsules', FALSE, 'VITD-2024-005', '2024-02-15', '2026-02-15'),

-- Supplier 2 medicines
('Azithromycin 250mg', 1, 2, 'Macrolide antibiotic', 'AntiBio Inc', 1.20, 2500, 250, 'Tablets', TRUE, 'AZIT-2024-006', '2024-01-10', '2026-01-10'),
('Ibuprofen 400mg', 2, 2, 'Non-steroidal anti-inflammatory', 'PainAway', 0.15, 8000, 800, 'Tablets', FALSE, 'IBUP-2024-007', '2024-02-20', '2027-02-20'),
('Atorvastatin 20mg', 3, 2, 'Cholesterol management', 'HeartHealth', 0.90, 3500, 350, 'Tablets', TRUE, 'ATOR-2024-008', '2024-01-25', '2026-01-25'),
('Insulin Glargine 100U/ml', 4, 2, 'Long-acting insulin', 'DiabeSolutions', 25.00, 500, 50, 'Vials', TRUE, 'INSU-2024-009', '2024-03-05', '2025-03-05'),
('Multivitamin Complex', 5, 2, 'Complete vitamin formula', 'NutriCare', 0.35, 3000, 300, 'Tablets', FALSE, 'MULT-2024-010', '2024-02-10', '2026-02-10'),

-- Supplier 3 medicines
('Ciprofloxacin 500mg', 1, 3, 'Fluoroquinolone antibiotic', 'BioPharm', 0.80, 4000, 400, 'Tablets', TRUE, 'CIPR-2024-011', '2024-01-30', '2026-01-30'),
('Aspirin 75mg', 2, 3, 'Low-dose pain relief and blood thinner', 'CardioPlus', 0.05, 15000, 1500, 'Tablets', FALSE, 'ASPI-2024-012', '2024-03-01', '2028-03-01'),
('Amlodipine 5mg', 3, 3, 'Calcium channel blocker', 'VascuMed', 0.40, 5000, 500, 'Tablets', TRUE, 'AMLO-2024-013', '2024-02-05', '2026-02-05'),
('Gliclazide 80mg', 4, 3, 'Diabetes management', 'GlucoControl', 0.60, 2000, 200, 'Tablets', TRUE, 'GLIC-2024-014', '2024-01-18', '2026-01-18'),
('Vitamin C 1000mg', 5, 3, 'Immune support supplement', 'ImmuneBoost', 0.25, 4000, 400, 'Tablets', FALSE, 'VITC-2024-015', '2024-02-25', '2026-02-25'),
('Salbutamol Inhaler', 6, 3, 'Bronchodilator for asthma', 'RespiraCare', 8.50, 800, 80, 'Inhalers', TRUE, 'SALB-2024-016', '2024-03-10', '2026-03-10'),
('Omeprazole 20mg', 7, 3, 'Proton pump inhibitor', 'GastroMed', 0.45, 6000, 600, 'Capsules', FALSE, 'OMEP-2024-017', '2024-01-22', '2026-01-22'),
('Hydrocortisone Cream 1%', 8, 3, 'Topical corticosteroid', 'SkinCare Pro', 3.50, 1000, 100, 'Tubes', FALSE, 'HYDR-2024-018', '2024-02-28', '2026-02-28');

-- Insert sample orders
INSERT INTO orders (hospital_id, supplier_id, order_status, total_amount, shipping_address, special_instructions) VALUES
(4, 1, 'DELIVERED', 2500.00, '100 Hospital Lane, Downtown', 'Deliver to main pharmacy - Ground floor'),
(4, 2, 'SHIPPED', 1800.00, '100 Hospital Lane, Downtown', 'Fragile items - Handle with care'),
(5, 1, 'APPROVED', 3200.00, '200 Care Boulevard, Uptown', 'Urgent delivery required'),
(5, 3, 'PENDING', 1500.00, '200 Care Boulevard, Uptown', 'Contact pharmacy manager upon arrival'),
(6, 2, 'DELIVERED', 2100.00, '300 Health Drive, Suburbs', NULL);

-- Insert order items for the sample orders
INSERT INTO order_items (order_id, medicine_id, quantity, unit_price) VALUES
-- Order 1 items
(1, 1, 1000, 0.50),
(1, 2, 2000, 0.10),
(1, 3, 500, 0.75),
(1, 4, 1000, 0.30),

-- Order 2 items
(2, 6, 500, 1.20),
(2, 7, 1000, 0.15),
(2, 8, 300, 0.90),

-- Order 3 items
(3, 1, 2000, 0.50),
(3, 3, 1000, 0.75),
(3, 4, 2000, 0.30),

-- Order 4 items
(4, 11, 500, 0.80),
(4, 13, 800, 0.40),
(4, 15, 1000, 0.25),

-- Order 5 items
(5, 7, 2000, 0.15),
(5, 9, 50, 25.00),
(5, 10, 500, 0.35);

-- ============================================
-- USEFUL VIEWS FOR REPORTING
-- ============================================

-- View for low stock medicines
CREATE VIEW vw_low_stock_medicines AS
SELECT 
    m.medicine_id,
    m.medicine_name,
    mc.category_name,
    m.stock_quantity,
    m.reorder_level,
    u.full_name AS supplier_name,
    m.unit_price
FROM medicines m
JOIN users u ON m.supplier_id = u.user_id
LEFT JOIN medicine_categories mc ON m.category_id = mc.category_id
WHERE m.stock_quantity <= m.reorder_level AND m.is_active = TRUE;

-- View for expiring medicines (within 90 days)
CREATE VIEW vw_expiring_medicines AS
SELECT 
    m.medicine_id,
    m.medicine_name,
    m.batch_number,
    m.expiry_date,
    DATEDIFF(m.expiry_date, CURDATE()) AS days_until_expiry,
    m.stock_quantity,
    u.full_name AS supplier_name
FROM medicines m
JOIN users u ON m.supplier_id = u.user_id
WHERE m.expiry_date <= DATE_ADD(CURDATE(), INTERVAL 90 DAY)
    AND m.expiry_date > CURDATE()
    AND m.is_active = TRUE
ORDER BY m.expiry_date;

-- View for order summary with details
CREATE VIEW vw_order_summary AS
SELECT 
    o.order_id,
    o.order_date,
    o.order_status,
    h.full_name AS hospital_name,
    h.email AS hospital_email,
    s.full_name AS supplier_name,
    s.email AS supplier_email,
    o.total_amount,
    COUNT(oi.order_item_id) AS total_items,
    o.shipped_date,
    o.delivered_date
FROM orders o
JOIN users h ON o.hospital_id = h.user_id
JOIN users s ON o.supplier_id = s.user_id
LEFT JOIN order_items oi ON o.order_id = oi.order_id
GROUP BY o.order_id;

-- ============================================
-- USEFUL STORED PROCEDURES
-- ============================================

DELIMITER //

-- Procedure to place an order from shopping cart
CREATE PROCEDURE sp_place_order_from_cart(
    IN p_hospital_id INT,
    IN p_supplier_id INT,
    IN p_shipping_address TEXT,
    IN p_special_instructions TEXT,
    OUT p_order_id INT
)
BEGIN
    DECLARE v_error VARCHAR(255);
    
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SET p_order_id = -1;
    END;
    
    START TRANSACTION;
    
    -- Create the order
    INSERT INTO orders (hospital_id, supplier_id, shipping_address, special_instructions)
    VALUES (p_hospital_id, p_supplier_id, p_shipping_address, p_special_instructions);
    
    SET p_order_id = LAST_INSERT_ID();
    
    -- Move cart items to order items
    INSERT INTO order_items (order_id, medicine_id, quantity, unit_price)
    SELECT 
        p_order_id,
        sc.medicine_id,
        sc.quantity,
        m.unit_price
    FROM shopping_cart sc
    JOIN medicines m ON sc.medicine_id = m.medicine_id
    WHERE sc.hospital_id = p_hospital_id
        AND m.supplier_id = p_supplier_id;
    
    -- Clear the cart for this supplier
    DELETE FROM shopping_cart 
    WHERE hospital_id = p_hospital_id 
        AND medicine_id IN (
            SELECT medicine_id FROM medicines WHERE supplier_id = p_supplier_id
        );
    
    COMMIT;
END//

-- Procedure to update order status
CREATE PROCEDURE sp_update_order_status(
    IN p_order_id INT,
    IN p_new_status VARCHAR(20),
    IN p_changed_by INT,
    IN p_notes TEXT
)
BEGIN
    UPDATE orders 
    SET order_status = p_new_status
    WHERE order_id = p_order_id;
    
    INSERT INTO order_status_history (order_id, new_status, changed_by, notes)
    VALUES (p_order_id, p_new_status, p_changed_by, p_notes);
END//

-- Procedure to update medicine stock
CREATE PROCEDURE sp_update_medicine_stock(
    IN p_medicine_id INT,
    IN p_quantity_change INT,
    IN p_movement_type VARCHAR(20),
    IN p_performed_by INT,
    IN p_reference_id INT,
    IN p_notes TEXT
)
BEGIN
    DECLARE v_current_stock INT;
    DECLARE v_new_stock INT;
    
    SELECT stock_quantity INTO v_current_stock
    FROM medicines
    WHERE medicine_id = p_medicine_id;
    
    SET v_new_stock = v_current_stock + p_quantity_change;
    
    IF v_new_stock < 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Insufficient stock quantity';
    END IF;
    
    UPDATE medicines
    SET stock_quantity = v_new_stock
    WHERE medicine_id = p_medicine_id;
    
    INSERT INTO stock_movement_log (
        medicine_id, movement_type, quantity_change, 
        previous_stock, new_stock, reference_id, 
        performed_by, notes
    ) VALUES (
        p_medicine_id, p_movement_type, p_quantity_change,
        v_current_stock, v_new_stock, p_reference_id,
        p_performed_by, p_notes
    );
END//

-- Procedure to get supplier dashboard metrics
CREATE PROCEDURE sp_get_supplier_metrics(IN p_supplier_id INT)
BEGIN
    SELECT 
        COUNT(DISTINCT m.medicine_id) AS total_medicines,
        SUM(m.stock_quantity * m.unit_price) AS total_inventory_value,
        COUNT(CASE WHEN m.stock_quantity <= m.reorder_level THEN 1 END) AS low_stock_count,
        COUNT(CASE WHEN m.expiry_date <= DATE_ADD(CURDATE(), INTERVAL 90 DAY) THEN 1 END) AS expiring_soon_count,
        COUNT(DISTINCT o.order_id) AS total_orders,
        COUNT(CASE WHEN o.order_status = 'PENDING' THEN 1 END) AS pending_orders,
        COALESCE(SUM(CASE WHEN o.order_status = 'DELIVERED' THEN o.total_amount END), 0) AS total_revenue
    FROM medicines m
    LEFT JOIN orders o ON m.supplier_id = o.supplier_id
    WHERE m.supplier_id = p_supplier_id;
END//

DELIMITER ;

-- ============================================
-- INDEXES FOR PERFORMANCE
-- (Already included in table definitions above)
-- ============================================

-- Display success message
SELECT 'Database setup completed successfully!' AS Status;
SELECT COUNT(*) AS Total_Users FROM users;
SELECT COUNT(*) AS Total_Medicines FROM medicines;
SELECT COUNT(*) AS Total_Orders FROM orders;
SELECT COUNT(*) AS Total_Categories FROM medicine_categories;
