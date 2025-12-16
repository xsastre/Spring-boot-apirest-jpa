# Makefile for Sensor Simulator (C)

CC = gcc
CFLAGS = -Wall -Wextra -O2
LDFLAGS = -lcurl
TARGET = sensor_simulator
SRC = sensor_simulator.c

all: $(TARGET)

$(TARGET): $(SRC)
	$(CC) $(CFLAGS) -o $(TARGET) $(SRC) $(LDFLAGS)

clean:
	rm -f $(TARGET)

run: $(TARGET)
	./$(TARGET)

.PHONY: all clean run
