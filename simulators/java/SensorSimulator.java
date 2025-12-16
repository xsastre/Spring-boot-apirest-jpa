import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * Sensor Simulator - Simulates IoT sensor behavior
 * 
 * This program simulates sensor behavior by sending fictitious data to the IoT API:
 * - Sends data every ~30 seconds with ±10 seconds random variation
 * - Sends temperature, humidity, and/or pressure (1, 2, or 3 fields randomly)
 * - Sometimes skips sending data (simulating packet loss)
 */
public class SensorSimulator {
    
    private static final String API_URL = "http://localhost:8080/api/sensors";
    private static final String SENSOR_NAME = "Sensor-Simulator-Java";
    private static final String LOCATION = "Test-Lab";
    
    private static final int BASE_INTERVAL = 30; // seconds
    private static final int TIME_VARIATION = 10; // ±10 seconds
    
    private static final Random random = new Random();
    
    public static void main(String[] args) {
        System.out.println("=== IoT Sensor Simulator (Java) ===");
        System.out.println("API URL: " + API_URL);
        System.out.println("Sensor: " + SENSOR_NAME);
        System.out.println("Location: " + LOCATION);
        System.out.println("Interval: ~" + BASE_INTERVAL + "s (±" + TIME_VARIATION + "s)");
        System.out.println("Starting simulation...\n");
        
        int cycleCount = 0;
        
        while (true) {
            try {
                cycleCount++;
                log("Cycle " + cycleCount + " starting...");
                
                // Calculate random wait time (30 ± 10 seconds)
                int waitTime = BASE_INTERVAL + random.nextInt(2 * TIME_VARIATION + 1) - TIME_VARIATION;
                log("Waiting " + waitTime + " seconds...");
                Thread.sleep(waitTime * 1000L);
                
                // Simulate packet loss (10% chance)
                if (random.nextInt(10) == 0) {
                    log("PACKET LOSS - Skipping transmission");
                    System.out.println();
                    continue;
                }
                
                // Generate sensor data
                SensorData data = generateSensorData();
                
                // Send data to API
                sendData(data);
                System.out.println();
                
            } catch (InterruptedException e) {
                log("Simulation interrupted");
                break;
            } catch (Exception e) {
                log("ERROR: " + e.getMessage());
                System.out.println();
            }
        }
    }
    
    /**
     * Generate random sensor data with 1, 2, or 3 fields
     */
    private static SensorData generateSensorData() {
        // Decide how many fields to send (1, 2, or 3)
        int fieldCount = random.nextInt(3) + 1;
        
        SensorData data = new SensorData();
        data.name = SENSOR_NAME;
        data.location = LOCATION;
        
        if (fieldCount == 1) {
            // Send only one field
            int field = random.nextInt(3);
            switch (field) {
                case 0:
                    data.temperature = generateTemperature();
                    log("Generated data: Temperature=" + String.format("%.2f", data.temperature) + "°C");
                    break;
                case 1:
                    data.humidity = generateHumidity();
                    log("Generated data: Humidity=" + String.format("%.2f", data.humidity) + "%");
                    break;
                case 2:
                    data.pressure = generatePressure();
                    log("Generated data: Pressure=" + String.format("%.2f", data.pressure) + " hPa");
                    break;
            }
        } else if (fieldCount == 2) {
            // Send two fields
            int excluded = random.nextInt(3);
            if (excluded != 0) {
                data.temperature = generateTemperature();
            }
            if (excluded != 1) {
                data.humidity = generateHumidity();
            }
            if (excluded != 2) {
                data.pressure = generatePressure();
            }
            log("Generated data: " + formatData(data));
        } else {
            // Send all three fields
            data.temperature = generateTemperature();
            data.humidity = generateHumidity();
            data.pressure = generatePressure();
            log("Generated data: " + formatData(data));
        }
        
        return data;
    }
    
    /**
     * Generate random temperature (15-30°C)
     */
    private static double generateTemperature() {
        return 15.0 + random.nextDouble() * 15.0;
    }
    
    /**
     * Generate random humidity (30-80%)
     */
    private static double generateHumidity() {
        return 30.0 + random.nextDouble() * 50.0;
    }
    
    /**
     * Generate random pressure (980-1040 hPa)
     */
    private static double generatePressure() {
        return 980.0 + random.nextDouble() * 60.0;
    }
    
    /**
     * Format sensor data for display
     */
    private static String formatData(SensorData data) {
        StringBuilder sb = new StringBuilder();
        if (data.temperature != null) {
            sb.append("Temperature=").append(String.format("%.2f", data.temperature)).append("°C");
        }
        if (data.humidity != null) {
            if (sb.length() > 0) sb.append(", ");
            sb.append("Humidity=").append(String.format("%.2f", data.humidity)).append("%");
        }
        if (data.pressure != null) {
            if (sb.length() > 0) sb.append(", ");
            sb.append("Pressure=").append(String.format("%.2f", data.pressure)).append(" hPa");
        }
        return sb.toString();
    }
    
    /**
     * Send sensor data to API
     */
    private static void sendData(SensorData data) throws Exception {
        // Build JSON payload
        StringBuilder json = new StringBuilder("{");
        json.append("\"name\":\"").append(data.name).append("\",");
        json.append("\"location\":\"").append(data.location).append("\"");
        
        if (data.temperature != null) {
            json.append(",\"temperature\":").append(data.temperature);
        }
        if (data.humidity != null) {
            json.append(",\"humidity\":").append(data.humidity);
        }
        if (data.pressure != null) {
            json.append(",\"pressure\":").append(data.pressure);
        }
        
        json.append("}");
        
        // Send HTTP POST request
        URL url = new URL(API_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = json.toString().getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        
        int responseCode = conn.getResponseCode();
        if (responseCode == 201) {
            log("SUCCESS - Data sent to API");
        } else {
            log("WARNING - API returned code: " + responseCode);
        }
        
        conn.disconnect();
    }
    
    /**
     * Log message with timestamp
     */
    private static void log(String message) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println("[" + timestamp + "] " + message);
    }
    
    /**
     * Data class to hold sensor readings
     */
    static class SensorData {
        String name;
        String location;
        Double temperature;
        Double humidity;
        Double pressure;
    }
}
