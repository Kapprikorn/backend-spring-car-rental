-- Initialize database with test data

-- Users
INSERT INTO users (id, name, username, password, email, role)
VALUES (1, 'Admin User', 'admin', '$2a$10$hKDVYxLefVHV/vtuPhWD3OigtRyOykRLDdUAp80Z1crSoS1lFqaFS', 'admin@testaccount.com', 'ADMIN');

INSERT INTO users (id, name, username, password, email, role)
VALUES (2, 'Regular User', 'user', '$2a$10$ByIUiNaRfBKSV6urZoBBxuznb6j.FXdh/Lcu9wQZU.TMYJk5EeU2y', 'user@testaccount.com', 'USER');

-- Locations
INSERT INTO location (id, name, address, phone_number) 
VALUES (1, 'Amsterdam Car Rental', 'Damrak 1, 1012 LG Amsterdam', '+31 20 123 4567');

INSERT INTO location (id, name, address, phone_number) 
VALUES (2, 'Rotterdam Car Rental', 'Coolsingel 42, 3011 AD Rotterdam', '+31 10 987 6543');

-- Parking Lots
INSERT INTO parking_lot (id, name, location_id) 
VALUES (1, 'Amsterdam Main Lot', 1);

INSERT INTO parking_lot (id, name, location_id) 
VALUES (2, 'Amsterdam Secondary Lot', 1);

INSERT INTO parking_lot (id, name, location_id) 
VALUES (3, 'Rotterdam Main Lot', 2);

-- Parking Spaces
INSERT INTO parking_space (id, location, size, occupied, parking_lot_id) 
VALUES (1, 'Section B, Spot 1', 'Standard', true, 1);

INSERT INTO parking_space (id, location, size, occupied, parking_lot_id) 
VALUES (2, 'Section C, Spot 2', 'Large', true, 1);

INSERT INTO parking_space (id, location, size, occupied, parking_lot_id) 
VALUES (3, 'Section A, Spot 3', 'Standard', true, 1);

INSERT INTO parking_space (id, location, size, occupied, parking_lot_id) 
VALUES (4, 'Section B, Spot 4', 'Large', false, 1);

INSERT INTO parking_space (id, location, size, occupied, parking_lot_id) 
VALUES (5, 'Section C, Spot 5', 'Standard', false, 1);

INSERT INTO parking_space (id, location, size, occupied, parking_lot_id) 
VALUES (6, 'Section A, Spot 1', 'Standard', true, 2);

INSERT INTO parking_space (id, location, size, occupied, parking_lot_id) 
VALUES (7, 'Section B, Spot 2', 'Large', false, 2);

INSERT INTO parking_space (id, location, size, occupied, parking_lot_id) 
VALUES (8, 'Section C, Spot 3', 'Standard', false, 2);

INSERT INTO parking_space (id, location, size, occupied, parking_lot_id) 
VALUES (9, 'Section A, Spot 1', 'Standard', true, 3);

INSERT INTO parking_space (id, location, size, occupied, parking_lot_id) 
VALUES (10, 'Section B, Spot 2', 'Large', false, 3);

INSERT INTO parking_space (id, location, size, occupied, parking_lot_id) 
VALUES (11, 'Section C, Spot 3', 'Standard', false, 3);

INSERT INTO parking_space (id, location, size, occupied, parking_lot_id) 
VALUES (12, 'Section A, Spot 4', 'Large', false, 3);

-- Vehicle Photos (creating them first so we can reference them in vehicles)
INSERT INTO vehicle_photo (id, url, original_file_name, content_type, contents) 
VALUES (1, 'https://example.com/audi-a3.jpg', 'audi-a3.jpg', 'image/jpeg', NULL);

INSERT INTO vehicle_photo (id, url, original_file_name, content_type, contents) 
VALUES (2, 'https://example.com/bmw-x5.jpg', 'bmw-x5.jpg', 'image/jpeg', NULL);

INSERT INTO vehicle_photo (id, url, original_file_name, content_type, contents) 
VALUES (3, 'https://example.com/mercedes-c.jpg', 'mercedes-c.jpg', 'image/jpeg', NULL);

INSERT INTO vehicle_photo (id, url, original_file_name, content_type, contents) 
VALUES (4, 'https://example.com/vw-golf.jpg', 'vw-golf.jpg', 'image/jpeg', NULL);

INSERT INTO vehicle_photo (id, url, original_file_name, content_type, contents) 
VALUES (5, 'https://example.com/ford-focus.jpg', 'ford-focus.jpg', 'image/jpeg', NULL);

-- Vehicles
INSERT INTO vehicle (id, license_plate, make, model, status, price_per_day, parking_space_id, vehicle_photo_id) 
VALUES (1, 'AB-123-CD', 'Audi', 'A3', 'AVAILABLE', 75.0, 1, 1);

INSERT INTO vehicle (id, license_plate, make, model, status, price_per_day, parking_space_id, vehicle_photo_id) 
VALUES (2, 'EF-456-GH', 'BMW', 'X5', 'AVAILABLE', 120.0, 2, 2);

INSERT INTO vehicle (id, license_plate, make, model, status, price_per_day, parking_space_id, vehicle_photo_id) 
VALUES (3, 'IJ-789-KL', 'Mercedes', 'C-Class', 'MAINTENANCE', 95.0, 3, 3);

INSERT INTO vehicle (id, license_plate, make, model, status, price_per_day, parking_space_id, vehicle_photo_id) 
VALUES (4, 'MN-012-OP', 'Volkswagen', 'Golf', 'AVAILABLE', 65.0, 6, 4);

INSERT INTO vehicle (id, license_plate, make, model, status, price_per_day, parking_space_id, vehicle_photo_id) 
VALUES (5, 'QR-345-ST', 'Ford', 'Focus', 'RENTED_OUT', 60.0, 9, 5);

-- Update vehicle_photo with vehicle reference
UPDATE vehicle_photo SET vehicle_id = 1 WHERE id = 1;
UPDATE vehicle_photo SET vehicle_id = 2 WHERE id = 2;
UPDATE vehicle_photo SET vehicle_id = 3 WHERE id = 3;
UPDATE vehicle_photo SET vehicle_id = 4 WHERE id = 4;
UPDATE vehicle_photo SET vehicle_id = 5 WHERE id = 5;

-- Reservations
-- Past reservation
INSERT INTO reservation (id, start_date, end_date, total_price, user_id, vehicle_id) 
VALUES (1, CURRENT_DATE - 10, CURRENT_DATE - 7, 180.0, 2, 1);

-- Current reservation
INSERT INTO reservation (id, start_date, end_date, total_price, user_id, vehicle_id) 
VALUES (2, CURRENT_DATE - 2, CURRENT_DATE + 3, 300.0, 2, 5);

-- Future reservation
INSERT INTO reservation (id, start_date, end_date, total_price, user_id, vehicle_id) 
VALUES (3, CURRENT_DATE + 5, CURRENT_DATE + 12, 840.0, 2, 2);

-- Reset sequence values to continue after our manually inserted IDs
SELECT setval('users_id_seq', (SELECT MAX(id) FROM users));
SELECT setval('location_id_seq', (SELECT MAX(id) FROM location));
SELECT setval('parking_lot_id_seq', (SELECT MAX(id) FROM parking_lot));
SELECT setval('parking_space_id_seq', (SELECT MAX(id) FROM parking_space));
SELECT setval('vehicle_photo_id_seq', (SELECT MAX(id) FROM vehicle_photo));
SELECT setval('vehicle_id_seq', (SELECT MAX(id) FROM vehicle));
SELECT setval('reservation_id_seq', (SELECT MAX(id) FROM reservation));
