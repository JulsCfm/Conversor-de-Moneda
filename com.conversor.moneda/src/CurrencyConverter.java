import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class CurrencyConverter {

    private static final String API_KEY = "20b3712fb6a39868e17115aa";
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/" + API_KEY;

    public static double convert(String fromCurrency, String toCurrency, double amount) throws Exception {
        String urlStr = API_URL + "/latest/" + fromCurrency;
        URL url = new URL(urlStr);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.connect();

        BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
        reader.close();

        if (!jsonObject.get("result").getAsString().equals("success")) {
            throw new Exception("Failed to get exchange rate");
        }

        double exchangeRate = jsonObject.getAsJsonObject("conversion_rates").get(toCurrency).getAsDouble();
        return amount * exchangeRate;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Ingrese la moneda de origen (por ejemplo, USD): ");
        String fromCurrency = scanner.nextLine().toUpperCase();

        System.out.print("Ingrese la moneda de destino (por ejemplo, EUR): ");
        String toCurrency = scanner.nextLine().toUpperCase();

        System.out.print("Ingrese la cantidad a convertir: ");
        double amount = scanner.nextDouble();

        try {
            double convertedAmount = convert(fromCurrency, toCurrency, amount);
            System.out.println(amount + " " + fromCurrency + " = " + convertedAmount + " " + toCurrency);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}