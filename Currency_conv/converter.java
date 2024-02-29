package Currency_conv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class converter {
    private static final String API_URL = "https://api.exchangerate-api.com/v4/latest/";
    
    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            
            // Currency selection
            System.out.print("Enter base currency code: ");
            String baseCurrency = reader.readLine().toUpperCase();
            System.out.print("Enter target currency code: ");
            String targetCurrency = reader.readLine().toUpperCase();
            
            // Fetch currency rates
            JSONObject exchangeRates = fetchExchangeRates(baseCurrency);
            double conversionRate = exchangeRates.getJSONObject("rates").getDouble(targetCurrency);
            
            // Amount input
            System.out.print("Enter amount to convert: ");
            double amount = Double.parseDouble(reader.readLine());
            
            // Currency conversion
            double convertedAmount = amount * conversionRate;
            
            // Display result
            System.out.printf("%.2f %s = %.2f %s%n", amount, baseCurrency, convertedAmount, targetCurrency);
            
            reader.close();
        } catch (IOException e) {
            System.out.println("An error occurred while processing input.");
            e.printStackTrace();
        }
    }
    
    private static JSONObject fetchExchangeRates(String baseCurrency) throws IOException {
        URL url = new URL(API_URL + baseCurrency);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        return new JSONObject(response.toString());
    }
}
