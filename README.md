# 🌤 Live Weather Checker — Java Desktop App

A modern desktop weather application built with **Core Java (Swing)** and **Maven**, fetching real-time weather data from the [OpenWeatherMap API](https://openweathermap.org/api).

---

## 📸 Screenshots

<img width="697" height="888" alt="image" src="https://github.com/user-attachments/assets/e27caa4a-d9e4-4930-8f2d-d94d753d1ac2" />


<!-- Replace the line below with your actual screenshot path after uploading to GitHub -->
![Weather App Screenshot](screenshots/app-preview.png)

---

## ✨ Features

- 🌡 **Temperature** — Real-time current temperature in °C
- 💧 **Humidity** — Relative humidity percentage
- 🤔 **Feels Like** — Apparent/perceived temperature
- 📅 **Next Day Forecast** — Estimated next-day high temperature
- 🎨 **Modern Glassmorphism UI** — Dark navy gradient with glass-effect cards and buttons
- ⚡ **Live API Integration** — Powered by OpenWeatherMap REST API

---

## 🛠 Tech Stack

| Technology | Purpose |
|---|---|
| Java (Swing) | GUI / Desktop Interface |
| Maven | Build tool & dependency management |
| OpenWeatherMap API | Real-time weather data |
| Google Gson 2.11.0 | JSON parsing |
| HttpURLConnection | HTTP networking |

---

## 📁 Project Structure

```
WeatherAppMaven/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── weatherapp/
│                   ├── WeatherApp2.java        # Main UI entry point
│                   ├── model/
│                   │   └── WeatherData.java    # Data model
│                   ├── service/
│                   │   └── WeatherService.java # API logic
│                   └── util/
│                       └── ApiClient.java      # HTTP client
├── pom.xml                                     # Maven config
└── README.md
```

---

## ⚙️ Prerequisites

Before running the project, make sure you have:

- ✅ **Java JDK 17** or higher — [Download here](https://www.oracle.com/java/technologies/downloads/)
- ✅ **Apache Maven 3.6+** — [Download here](https://maven.apache.org/download.cgi)
- ✅ **An OpenWeatherMap API Key** — [Get free key here](https://home.openweathermap.org/users/sign_up)
- ✅ An IDE like **Eclipse** or **IntelliJ IDEA** (optional but recommended)

---

## 🚀 Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/YOUR_USERNAME/WeatherAppMaven.git
cd WeatherAppMaven
```

### 2. Add your API Key

Open `src/main/java/com/weatherapp/service/WeatherService.java` and replace:

```java
private static final String API_KEY = "YOUR_API_KEY_HERE";
```

### 3. Build the project

```bash
mvn clean install
```

### 4. Run the application

```bash
mvn exec:java -Dexec.mainClass="com.weatherapp.WeatherApp2"
```

Or simply run `WeatherApp2.java` directly from your IDE.

---

## 📦 Dependencies

Defined in `pom.xml`:

```xml
<dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
    <version>2.11.0</version>
</dependency>
```

---

## ⚠️ Important Security Note

> **Never commit your real API key to GitHub.**
> Add a `.env` file or use environment variables for production use.
> If accidentally pushed, regenerate your key immediately at [OpenWeatherMap](https://home.openweathermap.org/api_keys).

---


## 📄 License

This project is for educational purposes.

---

> Built with ☕ Java and lots of dedication.
