package com.example.appli2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ActivityAccueil extends AppCompatActivity {

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_accueil);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) ->
        {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // for toasts
        context = this;

        // find elements
        Button button_ajout_carburant = findViewById(R.id.button_ajout_carburant);
        Button button_historique = findViewById(R.id.button_historique);
        Button button_statistiques = findViewById(R.id.button_statistiques);
        Button button_vehicules = findViewById(R.id.button_vehicules);

        button_ajout_carburant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(ActivityAccueil.this, ActivityPlein.class);
                startActivity(intent);
            }
        });

        button_historique.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(ActivityAccueil.this, ActivityHistorique2.class);
                startActivity(intent);
            }
        });

        button_statistiques.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                // Intent intent = new Intent(ActivityAccueil.this, ActivityStatistique.class);
                // startActivity(intent);
                Toast.makeText(context, "En développement", Toast.LENGTH_SHORT).show();
            }
        });

        button_vehicules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                // Intent intent = new Intent(ActivityAccueil.this, ActivityStatistique.class);
                // startActivity(intent);
                Toast.makeText(context, "En développement", Toast.LENGTH_SHORT).show();
            }
        });


    }
}