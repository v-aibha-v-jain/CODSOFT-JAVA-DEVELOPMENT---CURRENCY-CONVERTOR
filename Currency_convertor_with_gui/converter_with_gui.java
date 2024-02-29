package Currency_convertor_with_gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class converter_with_gui extends JFrame {
    private static final String API_URL = "https://api.exchangerate-api.com/v4/latest/";

    private JLabel baseCurrencyLabel, targetCurrencyLabel, amountLabel, resultLabel;
    private JComboBox<String> baseCurrencyComboBox, targetCurrencyComboBox;
    private JTextField amountTextField;
    private JButton convertButton;

    public converter_with_gui() {
        setTitle("Currency Converter");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // Initialize components
        baseCurrencyLabel = new JLabel("Base currency:");
        targetCurrencyLabel = new JLabel("Target currency:");
        amountLabel = new JLabel("Amount:");
        resultLabel = new JLabel("");
        String[] currencies = {
        	    "USD", "EUR", "GBP", "JPY", "AUD", "CAD", "CHF", "CNY", "SEK", "NZD", 
        	    "MXN", "SGD", "HKD", "NOK", "KRW", "TRY", "RUB", "INR", "BRL", "ZAR", 
        	    "DKK", "PLN", "THB", "IDR", "HUF", "CZK", "ILS", "CLP", "PHP", "AED"
        };
        baseCurrencyComboBox = new JComboBox<>(currencies);
        targetCurrencyComboBox = new JComboBox<>(currencies);
        amountTextField = new JTextField(10);
        convertButton = new JButton("Convert");
        convertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String baseCurrency = (String) baseCurrencyComboBox.getSelectedItem();
                    String targetCurrency = (String) targetCurrencyComboBox.getSelectedItem();

                    JSONObject exchangeRates = fetchExchangeRates(baseCurrency);
                    double conversionRate = exchangeRates.getJSONObject("rates").getDouble(targetCurrency);

                    double amount = Double.parseDouble(amountTextField.getText());
                    double convertedAmount = amount * conversionRate;

                    resultLabel.setText(String.format("%.2f %s = %.2f %s", amount, baseCurrency, convertedAmount, targetCurrency));
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "An error occurred while fetching exchange rates.", "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid amount.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(baseCurrencyLabel)
                        .addComponent(targetCurrencyLabel)
                        .addComponent(amountLabel))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(baseCurrencyComboBox)
                        .addComponent(targetCurrencyComboBox)
                        .addComponent(amountTextField)
                        .addComponent(convertButton)
                        .addComponent(resultLabel))
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(baseCurrencyLabel)
                        .addComponent(baseCurrencyComboBox))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(targetCurrencyLabel)
                        .addComponent(targetCurrencyComboBox))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(amountLabel)
                        .addComponent(amountTextField))
                .addComponent(convertButton)
                .addComponent(resultLabel)
        );
    }

    private JSONObject fetchExchangeRates(String baseCurrency) throws IOException {
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new converter_with_gui().setVisible(true);
            }
        });
    }
}
