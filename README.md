# WeatherApplication

## Overview
The `WeatherApplication` is a Spring Boot-based service designed to fetch and provide weather information for Melbourne. It integrates with two external weather APIs, WeatherStack (primary) and OpenWeatherMap (failover), to ensure high availability and reliability. The application is designed with scalability, caching, and failover mechanisms in mind.

---

## Features
- Fetches weather data (temperature in Celsius and wind speed) for Melbourne.
- Primary provider: WeatherStack.
- Failover provider: OpenWeatherMap.
- Caching mechanism to reduce API calls and improve performance.
- Unified response format for both providers.
- Proper logging and exception handling.
- Adheres to SOLID principles for maintainable and scalable code.

---

## Prerequisites
- Java 17 or higher
- Maven 3.8 or higher
- Git
- An IDE (e.g., IntelliJ IDEA) or a text editor
- Internet connection (to access external APIs)

---

## How to Clone and Run the Application Locally

### 1. Clone the Repository
```bash
git clone <repository-url>
cd WeatherApplication
```

### 2. Configure API Keys
The application requires API keys for WeatherStack and OpenWeatherMap. These keys are currently stored in the `application.properties` file.

1. Open the file `src/main/resources/application.properties`.
2. Replace the placeholders with your API keys:
   ```properties
   weatherstackprovider.api.key=YOUR_WEATHERSTACK_API_KEY
   openweathermapprovider.access.key=YOUR_OPENWEATHERMAP_API_KEY
   ```

### 3. Build the Application
Run the following command to build the application:
```bash
mvn clean install
```

### 4. Check if Port 8080 is Busy
Before running the application, check if port 8080 is already in use:
```bash
lsof -i :8080
```

If a process is running on port 8080, you can kill it using the following command:
```bash
kill -9 <PID>
```
Replace `<PID>` with the process ID shown in the output of the `lsof` command.

### 5. Run the Application
Start the application using the following command:
```bash
mvn spring-boot:run
```

### 6. Test the Application
You can test the application using `curl` or any API testing tool (e.g., Postman).  
Example:
```bash
curl "http://localhost:8080/v1/weather?city=melbourne"
```

Expected Response:
```json
{
  "wind_speed": 20,
  "temperature_degrees": 29
}
```

---

## Application Design

### **1. Adherence to SOLID Principles**
- **Single Responsibility Principle (SRP):**  
  Each class has a single responsibility. For example, `WeatherStackProvider` and `OpenWeatherMapProvider` handle API-specific logic, while `WeatherServiceImpl` manages failover and caching.
  
- **Open/Closed Principle (OCP):**  
  The `WeatherProvider` interface allows adding new providers without modifying existing code.

- **Dependency Inversion Principle (DIP):**  
  Providers are injected into the service layer using Spring's dependency injection.

### **2. Caching**
- Implemented using Spring's `@Cacheable` annotation.
- Cache expiration is set to 3 seconds to reduce API calls while ensuring fresh data.

### **3. Failover Mechanism**
- If the primary provider (WeatherStack) fails, the application automatically switches to the failover provider (OpenWeatherMap).

### **4. Logging and Exception Handling**
- Meaningful log messages are added at different levels (`INFO`, `ERROR`) for better traceability.
- Custom exception handling ensures graceful degradation in case of failures.

---

## Further Enhancements

1. **Secure API Key Storage:**
   - Move API keys from `application.properties` to a secure vault or environment variables.
   - Use tools like AWS Secrets Manager, Azure Key Vault, or HashiCorp Vault for secure storage.
   - Example for environment variables:
     ```properties
     weatherstackprovider.api.key=${WEATHERSTACK_API_KEY}
     openweathermapprovider.access.key=${OPENWEATHERMAP_API_KEY}
     ```

2. **Circuit Breaker Pattern:**
   - Use libraries like Resilience4j to implement a circuit breaker for better fault tolerance.

3. **Health Check Endpoint:**
   - Add a `/health` endpoint to monitor the application's health.

4. **Dynamic City Support:**
   - Currently, the city is hardcoded to Melbourne. Extend the application to support dynamic city input.

5. **Improved Caching:**
   - Use a more advanced caching library like Caffeine for better control over cache policies.

6. **Testing:**
   - Add more unit and integration tests to cover edge cases and failover scenarios.

---

## Trade-offs
- **Caching:** A simple caching mechanism is used to meet the requirements. A more advanced solution like Redis could be considered for production.
- **API Key Storage:** Keys are stored in `application.properties` for simplicity but should be moved to a secure storage solution for production.

---

## Conclusion
The `WeatherApplication` is a robust and scalable solution for fetching weather data. With the suggested enhancements, it can be further improved to meet enterprise-grade requirements.
