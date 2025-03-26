import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class WeatherApp {
    
    // Use your actual API Key here
    private static final String API_KEY = "your_actual_api_key_here";
    private static final String API_URL = "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric";
    
    public static void main(String[] args) {
        String city = "London";
        String response = getWeatherData(city);
        
        if (response != null) {
            parseAndDisplayWeather(response);
        } else {
            System.out.println("Failed to get weather data");
        }
    }
    
    private static String getWeatherData(String city) {
        try {
            // Encode city name for URL
            String encodedCity = URLEncoder.encode(city, "UTF-8");
            String urlString = String.format(API_URL, encodedCity, API_KEY);
            URL url = new URL(urlString);
            
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            
            // Check response code first
            if (connection.getResponseCode() != 200) {
                System.out.println("Error: HTTP " + connection.getResponseCode());
                return null;
            }
            
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            
            return response.toString();
            
        } catch (Exception e) {
            System.out.println("Error fetching weather data: " + e.getMessage());
            return null;
        }
    }
    
    // Rest of the code remains the same...
    private static void parseAndDisplayWeather(String response) {
        try {
            String cityName = extractValue(response, "\"name\":\"", "\"");
            String temperature = extractValue(response, "\"temp\":", ",");
            String humidity = extractValue(response, "\"humidity\":", ",");
            String description = extractValue(response, "\"description\":\"", "\"");

            System.out.println("Weather Information for " + cityName + ":");
            System.out.println("Temperature: " + temperature + "°C");
            System.out.println("Humidity: " + humidity + "%");
            System.out.println("Description: " + description);
            
        } catch (Exception e) {
            System.out.println("Error parsing weather data: " + e.getMessage());
        }
    }
    
    private static String extractValue(String json, String key, String delimiter) {
        int startIndex = json.indexOf(key) + key.length();
        int endIndex = json.indexOf(delimiter, startIndex);
        return json.substring(startIndex, endIndex).trim();
    }
}
