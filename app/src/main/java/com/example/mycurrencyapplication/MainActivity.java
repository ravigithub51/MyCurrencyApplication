package com.example.mycurrencyapplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    Button button;
    EditText currencyToBeConverted;
    EditText currencyConverted;
    Spinner convertToDropdown;
    Spinner convertFromDropdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currencyConverted = findViewById(R.id.currency_converted);
        currencyToBeConverted = findViewById(R.id.currency_to_be_converted);
        convertToDropdown = findViewById(R.id.to);
        convertFromDropdown = findViewById(R.id.from);
        button = findViewById(R.id.button);

        String[] dropDownList = {"USD", "INR", "EUR", "NZD"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, dropDownList);

        convertToDropdown.setAdapter(adapter);
        convertFromDropdown.setAdapter(adapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    makeExchangeRateRequest();
                } catch (IOException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "Failed to get exchange rate", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void makeExchangeRateRequest() throws IOException {
        new AsyncTask<Void, Void, JsonObject>() {
            @Override
            protected JsonObject doInBackground(Void... voids) {
                String apiKey = "9bffc5cfa84b8eb7ff3cb758";
                String fromCurrency = convertFromDropdown.getSelectedItem().toString();
                String url_str = "https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/" + fromCurrency;

                try {
                    URL url = new URL(url_str);
                    HttpURLConnection request = (HttpURLConnection) url.openConnection();
                    request.connect();

                    // Convert to JSON
                    InputStreamReader inputStreamReader = new InputStreamReader(request.getInputStream());
                    return new com.google.gson.JsonParser().parse(inputStreamReader).getAsJsonObject();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(JsonObject root) {
                super.onPostExecute(root);
                if (root != null) {
                    String req_result = root.get("result").getAsString();
                    if (req_result.equals("success")) {
                        JsonObject rates = root.getAsJsonObject("conversion_rates");
                        double currency = Double.valueOf(currencyToBeConverted.getText().toString());
                        double multiplier = rates.get(convertToDropdown.getSelectedItem().toString()).getAsDouble();
                        double result = currency * multiplier;
                        currencyConverted.setText(String.valueOf(result));
                    } else {
                        Toast.makeText(MainActivity.this, "Failed to get exchange rate", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Failed to get exchange rate", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }
}
