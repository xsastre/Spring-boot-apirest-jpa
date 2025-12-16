-- PostgreSQL specific schema creation for Production
-- Run this script manually on your PostgreSQL database before deploying to production

-- Create database (run this as postgres superuser)
-- CREATE DATABASE iot_sensors_db;

-- Connect to the database and create schema
-- \c iot_sensors_db;

-- Drop table if exists (be careful in production!)
DROP TABLE IF EXISTS sensors CASCADE;

-- Create sensors table
CREATE TABLE sensors (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    location VARCHAR(255) NOT NULL,
    temperature DOUBLE PRECISION NOT NULL,
    humidity DOUBLE PRECISION NOT NULL,
    pressure DOUBLE PRECISION NOT NULL,
    measurement_time TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

-- Create indexes for better query performance
CREATE INDEX idx_sensors_location ON sensors(location);
CREATE INDEX idx_sensors_name ON sensors(name);
CREATE INDEX idx_sensors_measurement_time ON sensors(measurement_time);

-- Insert some initial data (optional)
INSERT INTO sensors (name, location, temperature, humidity, pressure, measurement_time, created_at) VALUES
('Sensor-001', 'Warehouse A', 22.5, 65.3, 1013.25, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Sensor-002', 'Warehouse A', 23.1, 68.7, 1012.80, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Sensor-003', 'Office Floor 1', 21.8, 55.2, 1014.10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Sensor-004', 'Office Floor 2', 22.3, 58.9, 1013.95, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Sensor-005', 'Data Center', 18.5, 45.0, 1015.20, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Grant permissions (adjust username as needed)
-- GRANT ALL PRIVILEGES ON DATABASE iot_sensors_db TO your_user;
-- GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO your_user;
-- GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO your_user;
