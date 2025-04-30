-- Create the database
CREATE DATABASE IF NOT EXISTS hotel_management;
USE hotel_management;

-- Create rooms table
CREATE TABLE IF NOT EXISTS rooms (
    room_id INT PRIMARY KEY,
    room_type VARCHAR(50),
    is_available BOOLEAN,
    price DOUBLE,
    capacity INT,
    amenities VARCHAR(255),
    needs_cleaning BOOLEAN,
    under_maintenance BOOLEAN
);

-- Create customers table
CREATE TABLE IF NOT EXISTS customers (
    customer_id INT PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100),
    phone VARCHAR(20),
    room_id INT,
    check_in_date DATE,
    check_out_date DATE,
    total_amount DOUBLE,
    checked_in BOOLEAN,
    payment_status VARCHAR(20),
    FOREIGN KEY (room_id) REFERENCES rooms(room_id)
); 