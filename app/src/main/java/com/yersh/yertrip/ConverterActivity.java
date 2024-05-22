package com.yersh.yertrip;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Arrays;
import java.util.List;

public class ConverterActivity extends AppCompatActivity {

    String[] values1 = { "USD", "EUR", "RUB" };
    String[] values2 = { "KRW"};

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

        Spinner spinner1 = findViewById(R.id.firstSpinner);
        ArrayAdapter<String> adapter1 = new ArrayAdapter(this, R.layout.spinner_list, values1);
        adapter1.setDropDownViewResource(R.layout.spinner_list);
        spinner1.setAdapter(adapter1);

        Spinner spinner2 = findViewById(R.id.secondSpinner);
        ArrayAdapter<String> adapter2 = new ArrayAdapter(this, R.layout.spinner_list, values2);
        adapter2.setDropDownViewResource(R.layout.spinner_list);
        spinner2.setAdapter(adapter2);

        ImageView backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConverterActivity.this, MainMenuActivity.class);

                startActivity(intent);
                finish();
            }
        });
    }
}