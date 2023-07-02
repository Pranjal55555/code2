import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class BitcoinRateConverter {

    public static void main(String[] args) {
        String bitcoinRate = getBitcoinRate();
        String rateInWords = convertToWords(bitcoinRate);
        System.out.println(rateInWords);
    }

    private static String getBitcoinRate() {
        String apiUrl = "https://api.coindesk.com/v1/bpi/currentprice.json";
        StringBuilder response = new StringBuilder();

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Parse the JSON response and extract the rate
        String json = response.toString();
        int startIndex = json.indexOf("\"rate\":\"") + "\"rate\":\"".length();
        int endIndex = json.indexOf("\"", startIndex);
        String rate = json.substring(startIndex, endIndex);
        return rate;
    }

    private static String convertToWords(String number) {
        String[] units = { "", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten",
                "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen" };
        String[] tens = { "", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety" };

        // Remove commas and decimal part from the number
        number = number.replace(",", "").split("\\.")[0];

        int num = Integer.parseInt(number);
        StringBuilder words = new StringBuilder();

        if (num == 0) {
            return "Zero";
        }

        if (num >= 1000) {
            words.append(convertToWords(String.valueOf(num / 1000))).append(" Thousand ");
            num %= 1000;
        }

        if (num >= 100) {
            words.append(convertToWords(String.valueOf(num / 100))).append(" Hundred ");
            num %= 100;
        }

        if (num >= 20) {
            words.append(tens[num / 10]).append(" ");
            num %= 10;
        }

        if (num > 0) {
            words.append(units[num]).append(" ");
        }

        return words.toString().trim();
    }
}

