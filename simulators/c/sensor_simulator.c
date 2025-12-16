#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <unistd.h>
#include <curl/curl.h>

/**
 * Sensor Simulator in C - Simulates IoT sensor behavior
 * 
 * This program simulates sensor behavior by sending fictitious data to the IoT API:
 * - Sends data every ~30 seconds with ±10 seconds random variation
 * - Sends temperature, humidity, and/or pressure (1, 2, or 3 fields randomly)
 * - Sometimes skips sending data (simulating packet loss)
 */

#define API_URL "http://localhost:8080/api/sensors"
#define SENSOR_NAME "Sensor-Simulator-C"
#define LOCATION "Test-Lab"
#define BASE_INTERVAL 30
#define TIME_VARIATION 10

typedef struct {
    char name[100];
    char location[100];
    double temperature;
    double humidity;
    double pressure;
    int has_temperature;
    int has_humidity;
    int has_pressure;
} SensorData;

/**
 * Get current timestamp string
 */
void get_timestamp(char *buffer, size_t size) {
    time_t now = time(NULL);
    struct tm *t = localtime(&now);
    strftime(buffer, size, "%Y-%m-%d %H:%M:%S", t);
}

/**
 * Log message with timestamp
 */
void log_message(const char *message) {
    char timestamp[64];
    get_timestamp(timestamp, sizeof(timestamp));
    printf("[%s] %s\n", timestamp, message);
}

/**
 * Generate random double in range [min, max]
 */
double random_double(double min, double max) {
    return min + ((double)rand() / RAND_MAX) * (max - min);
}

/**
 * Generate random temperature (15-30°C)
 */
double generate_temperature() {
    return random_double(15.0, 30.0);
}

/**
 * Generate random humidity (30-80%)
 */
double generate_humidity() {
    return random_double(30.0, 80.0);
}

/**
 * Generate random pressure (980-1040 hPa)
 */
double generate_pressure() {
    return random_double(980.0, 1040.0);
}

/**
 * Generate sensor data with random fields
 */
void generate_sensor_data(SensorData *data) {
    strcpy(data->name, SENSOR_NAME);
    strcpy(data->location, LOCATION);
    
    data->has_temperature = 0;
    data->has_humidity = 0;
    data->has_pressure = 0;
    
    // Decide how many fields to send (1, 2, or 3)
    int field_count = (rand() % 3) + 1;
    
    if (field_count == 1) {
        // Send only one field
        int field = rand() % 3;
        switch (field) {
            case 0:
                data->temperature = generate_temperature();
                data->has_temperature = 1;
                printf("[%s] Generated data: Temperature=%.2f°C\n", "", data->temperature);
                break;
            case 1:
                data->humidity = generate_humidity();
                data->has_humidity = 1;
                printf("[%s] Generated data: Humidity=%.2f%%\n", "", data->humidity);
                break;
            case 2:
                data->pressure = generate_pressure();
                data->has_pressure = 1;
                printf("[%s] Generated data: Pressure=%.2f hPa\n", "", data->pressure);
                break;
        }
    } else if (field_count == 2) {
        // Send two fields
        int excluded = rand() % 3;
        if (excluded != 0) {
            data->temperature = generate_temperature();
            data->has_temperature = 1;
        }
        if (excluded != 1) {
            data->humidity = generate_humidity();
            data->has_humidity = 1;
        }
        if (excluded != 2) {
            data->pressure = generate_pressure();
            data->has_pressure = 1;
        }
    } else {
        // Send all three fields
        data->temperature = generate_temperature();
        data->has_temperature = 1;
        data->humidity = generate_humidity();
        data->has_humidity = 1;
        data->pressure = generate_pressure();
        data->has_pressure = 1;
    }
    
    // Log generated data
    if (field_count > 1) {
        char timestamp[64];
        get_timestamp(timestamp, sizeof(timestamp));
        printf("[%s] Generated data: ", timestamp);
        int first = 1;
        if (data->has_temperature) {
            printf("Temperature=%.2f°C", data->temperature);
            first = 0;
        }
        if (data->has_humidity) {
            if (!first) printf(", ");
            printf("Humidity=%.2f%%", data->humidity);
            first = 0;
        }
        if (data->has_pressure) {
            if (!first) printf(", ");
            printf("Pressure=%.2f hPa", data->pressure);
        }
        printf("\n");
    }
}

/**
 * Build JSON payload from sensor data
 */
void build_json(const SensorData *data, char *json, size_t size) {
    char temp[512];
    snprintf(json, size, "{\"name\":\"%s\",\"location\":\"%s\"", data->name, data->location);
    
    if (data->has_temperature) {
        snprintf(temp, sizeof(temp), ",\"temperature\":%.2f", data->temperature);
        strncat(json, temp, size - strlen(json) - 1);
    }
    if (data->has_humidity) {
        snprintf(temp, sizeof(temp), ",\"humidity\":%.2f", data->humidity);
        strncat(json, temp, size - strlen(json) - 1);
    }
    if (data->has_pressure) {
        snprintf(temp, sizeof(temp), ",\"pressure\":%.2f", data->pressure);
        strncat(json, temp, size - strlen(json) - 1);
    }
    
    strncat(json, "}", size - strlen(json) - 1);
}

/**
 * Send sensor data to API using cURL
 */
int send_data(const SensorData *data) {
    CURL *curl;
    CURLcode res;
    char json[1024] = {0};
    long response_code = 0;
    
    build_json(data, json, sizeof(json));
    
    curl = curl_easy_init();
    if (!curl) {
        log_message("ERROR: Failed to initialize cURL");
        return 0;
    }
    
    struct curl_slist *headers = NULL;
    headers = curl_slist_append(headers, "Content-Type: application/json");
    
    curl_easy_setopt(curl, CURLOPT_URL, API_URL);
    curl_easy_setopt(curl, CURLOPT_POSTFIELDS, json);
    curl_easy_setopt(curl, CURLOPT_HTTPHEADER, headers);
    curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, NULL); // Discard response body
    
    res = curl_easy_perform(curl);
    
    if (res != CURLE_OK) {
        char msg[256];
        snprintf(msg, sizeof(msg), "ERROR: cURL failed: %s", curl_easy_strerror(res));
        log_message(msg);
        curl_easy_cleanup(curl);
        curl_slist_free_all(headers);
        return 0;
    }
    
    curl_easy_getinfo(curl, CURLINFO_RESPONSE_CODE, &response_code);
    
    if (response_code == 201) {
        log_message("SUCCESS - Data sent to API");
    } else {
        char msg[256];
        snprintf(msg, sizeof(msg), "WARNING - API returned code: %ld", response_code);
        log_message(msg);
    }
    
    curl_easy_cleanup(curl);
    curl_slist_free_all(headers);
    
    return (response_code == 201);
}

int main() {
    printf("=== IoT Sensor Simulator (C) ===\n");
    printf("API URL: %s\n", API_URL);
    printf("Sensor: %s\n", SENSOR_NAME);
    printf("Location: %s\n", LOCATION);
    printf("Interval: ~%ds (±%ds)\n", BASE_INTERVAL, TIME_VARIATION);
    printf("Starting simulation...\n\n");
    
    // Initialize random seed
    srand(time(NULL));
    
    // Initialize cURL globally
    curl_global_init(CURL_GLOBAL_ALL);
    
    int cycle_count = 0;
    
    while (1) {
        cycle_count++;
        
        char msg[256];
        snprintf(msg, sizeof(msg), "Cycle %d starting...", cycle_count);
        log_message(msg);
        
        // Calculate random wait time (30 ± 10 seconds)
        int wait_time = BASE_INTERVAL + (rand() % (2 * TIME_VARIATION + 1)) - TIME_VARIATION;
        snprintf(msg, sizeof(msg), "Waiting %d seconds...", wait_time);
        log_message(msg);
        sleep(wait_time);
        
        // Simulate packet loss (10% chance)
        if (rand() % 10 == 0) {
            log_message("PACKET LOSS - Skipping transmission");
            printf("\n");
            continue;
        }
        
        // Generate and send sensor data
        SensorData data;
        generate_sensor_data(&data);
        send_data(&data);
        printf("\n");
    }
    
    curl_global_cleanup();
    return 0;
}
