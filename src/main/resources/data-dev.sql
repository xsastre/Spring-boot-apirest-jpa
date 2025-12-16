-- Sample data for development environment (H2)
-- This data is automatically loaded when running in dev profile

INSERT INTO sensors (name, location, temperature, humidity, pressure, measurement_time, created_at) VALUES
('Sensor-001', 'Warehouse A', 22.5, 65.3, 1013.25, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Sensor-002', 'Warehouse A', 23.1, 68.7, 1012.80, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Sensor-003', 'Office Floor 1', 21.8, 55.2, 1014.10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Sensor-004', 'Office Floor 2', 22.3, 58.9, 1013.95, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Sensor-005', 'Data Center', 18.5, 45.0, 1015.20, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
