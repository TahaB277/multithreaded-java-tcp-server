# TCP Temperature Server Assignment

## 📝 Description
This project is a multithreaded TCP server that manages real-time temperature data for cities. It supports two types of clients:

- **Producer Clients**: Submit temperature readings for cities.
- **Consumer Clients**: Request temperature data for a specific city, the full list, or the average of all cities.

## 📦 Project Structure

- `server.java`: Multithreaded TCP server that handles temperature data and logs client interactions.
- `consumerProducer.java`: Thread class that handles both producer and consumer requests.
- `consumer.java`: Client for retrieving temperature data.
- `producer.java`: Client for submitting temperature updates.
- `memoryLock.java`: Singleton lock used for thread-safe access to shared memory.
- `logFileLock.java`: Singleton lock used for thread-safe logging to the server log file.
- `Logs/`: Directory where server logs are stored with timestamped filenames.

## 🚀 How to Run

### 1. Compile the Java Files
```bash
javac *.java
```

### 2. Start the Server
```bash
java server
```
- This will create a log file in the `Logs` folder and wait for client connections.

### 3. Run a Consumer Client
```bash
java consumer
```
- Enter the server's IP address (e.g., `localhost`).
- Available commands:
  - `LIST`: Show all cities and their temperatures.
  - `AVG`: Show average temperature.
  - `<City>`: Get temperature for a specific city.
  - `.` (dot): Exit the client.

### 4. Run a Producer Client
```bash
java producer
```
- Enter the server's IP address.
- Input format:
  - `<CityName> <Temperature>` (e.g., `Beirut 24.5`)
  - `.` (dot): Exit the client.

## 🧠 Features

- ✅ Multithreaded TCP server
- ✅ Shared memory using `HashMap<String, Double>`
- ✅ Thread-safe access via custom singleton lock (`memoryLock`)
- ✅ Logging all server activity to a timestamped file
- ✅ LIST and AVG commands for consumers
- ✅ Input validation for temperature range (-50°C to 60°C)

## 📁 Example Log Snippet (from `Logs/2025-04-10_19-42-30.txt`)
```
2025-04-10T19:43:02.124	Consumer /127.0.0.1:54321 is connected
2025-04-10T19:43:05.567	Consumer /127.0.0.1:54321 requested LIST
2025-04-10T19:44:18.873	Producer /127.0.0.1:54322 updated Beirut to 24.6°C
2025-04-10T19:45:00.123	Producer /127.0.0.1:54322 is DISCONNECTED
```
