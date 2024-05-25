package com.yersh.yertrip;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.JsonObject;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConverterActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_converter);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        final EditText convertFrom = findViewById(R.id.GBPCurrencyValue);
        final TextView convertToValue = findViewById(R.id.convertToValue);
        final Spinner convertToSpinner = findViewById(R.id.convertToSpinner);

        String[] dropdownList = {"RUB", "USD", "EUR", "UAH", "GEL"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_currency_item, dropdownList);
        convertToSpinner.setAdapter(adapter);

        convertToSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                com.example.welcometotheuk.Retrofit.IRetrofitInterface retrofitInterface = com.example.welcometotheuk.Retrofit.RetrofitBuilder.getRetrofitInstance().create(com.example.welcometotheuk.Retrofit.IRetrofitInterface.class);
                Call<JsonObject> call = retrofitInterface.getExchangeCurrency("KRW");
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        JsonObject res = response.body();
                        JsonObject rates = res.getAsJsonObject("conversion_rates");

                        Double currency = Double.valueOf(convertFrom.getText().toString());
                        Double multiplier = Double.valueOf(rates.get(convertToSpinner.getSelectedItem().toString()).toString());

                        Double result = currency * multiplier;

                        convertToValue.setText(String.valueOf(result));
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}