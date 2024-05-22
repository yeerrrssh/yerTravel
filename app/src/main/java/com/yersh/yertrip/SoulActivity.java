package com.yersh.yertrip;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SoulActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_soul);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        final ImageView landmarksCard = findViewById(R.id.landmarksCard);
        final ImageView shoppingCard = findViewById(R.id.shoppingCard);
        final ImageView hikesCard = findViewById(R.id.hikesCard);
        final ImageView foodCard = findViewById(R.id.foodCard);

        final LinearLayout notes = findViewById(R.id.grayCard2);


        landmarksCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SoulActivity.this, SoulLandmarksActivity.class);

                startActivity(intent);
                finish();
            }
        });
/**
        shoppingCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SoulActivity.this, SoulShoppingActivity.class);

                startActivity(intent);
                finish();
            }
        });

        hikesCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SoulActivity.this, SoulHikesActivity.class);

                startActivity(intent);
                finish();
            }
        });

        foodCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SoulActivity.this, SoulFoodActivity.class);

                startActivity(intent);
                finish();
            }
        });
         */

        notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SoulActivity.this, NotesActivity.class);

                startActivity(intent);
                finish();
            }
        });


        ImageView backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SoulActivity.this, MainMenuActivity.class);

                startActivity(intent);
                finish();
            }
        });
    }
}