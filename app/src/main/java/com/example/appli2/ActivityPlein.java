package com.example.appli2;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

// BUG : CRASH SANS LE FICHIER AllGasData.json
// Attention, lorsque le fichier allGasData est supprimé, cela fait planter l'activité, il n'est
// plus possible d'ajouter un plein, en plus de cela l'activité crash de tel façon qu'il est
// impossible à l'utilisateur de savoir qu'il y a eu un problème avec l'ajout du plein. Dangereux.

public class ActivityPlein extends AppCompatActivity {

    // UI
    private EditText editTextDate, editTextHeure, editTextKm, editTextPrixLitre, editTextPrixTotal;
    private Spinner spinnerCarburant;
    private TextView textViewVolumeCarburant;
    private Button buttonAjouterPlein;
    private GasDataCollection gasdatacollection;
    private Vibrator vibrator;
    private LinearLayout layoutKM, layoutPrixLitre, layoutPrixTotal;
    private Animation shake;
    private SharedPreferences sharedPreferences;

    private int mavariabledetestgit;
    private final List<String> list_carburant = Arrays.asList("SP98", "SP95", "SP95 E10", "E85", "DIESEL");



    /******************************************************
     *                   O N C R E A T E                  *
     ******************************************************/

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_plein);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // edit text
        editTextDate = findViewById(R.id.editTextDate);
        editTextHeure = findViewById(R.id.editTextHeure);
        editTextKm = findViewById(R.id.editTextKm);
        editTextPrixLitre = findViewById(R.id.editTextPrixLitre);
        editTextPrixTotal = findViewById(R.id.editTextPrixTotal);

        // text
        textViewVolumeCarburant = findViewById(R.id.textViewVolumeCarburant);

        // spinners
        spinnerCarburant = findViewById(R.id.spinnerCarburant);

        // button
        buttonAjouterPlein = findViewById(R.id.buttonAjouterPlein);

        // linear layout
        layoutKM = findViewById(R.id.layoutKM);
        layoutPrixLitre = findViewById(R.id.layoutPrixLitre);
        layoutPrixTotal = findViewById(R.id.layoutPrixTotal);

        gasdatacollection = new GasDataCollection(this);

        // vibration
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        // shared preferences
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
//
        // animation
        shake = new TranslateAnimation(0, 35, 0, 0); // secousse de 10px à gauche et à droite
        shake.setDuration(70); // Durée de l'animation
        shake.setRepeatCount(5); // Répéter l'animation 3 fois
        shake.setRepeatMode(Animation.REVERSE); // Inverser l'animation pour la faire aller de gauche à droite


        // Fill the date of the day
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // Le mois commence à 0 (janvier = 0)
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // set the date of the day
        editTextDate.setText(String.format("%02d/%02d/%d", day, month, year));

        // get the time
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // set actual time
        editTextHeure.setText(String.format("%02d:%02d", hour, minute));


        // remplissage du spinner carburant
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, list_carburant);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCarburant.setAdapter(adapter);

        // restaurer la derniere valeur de carburant selectionne
        spinnerCarburant.setSelection(sharedPreferences.getInt("index_carburant", 0));



        /*******************************************************
         *                   L I S T E N E R S                 *
         *******************************************************/


        buttonAjouterPlein.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                clickAjouterPlein();
            }
        });

        editTextPrixLitre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                updateVolume();
            }
        });

        editTextPrixTotal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                updateVolume();
            }
        });

        spinnerCarburant.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                // sauvegarde le changement de carburant dans SharedPrefences
                int index_carburant = parentView.getSelectedItemPosition();

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("index_carburant", index_carburant);
                editor.apply();

                String selectedItemStr = parentView.getItemAtPosition(position).toString();

                // change the background color of the spinner accordingly to the selected fuel
                switch (selectedItemStr) {
                    case ("DIESEL"):
                        spinnerCarburant.setBackgroundColor(
                                Color.parseColor("#fedc01")); // Diesel yellow
                        break;
                    case ("SP95 E10"):
                        spinnerCarburant.setBackgroundColor(
                                Color.parseColor("#44ba56")); // SP95E10 green
                        break;
                    case ("SP95"):
                        spinnerCarburant.setBackgroundColor(
                                Color.parseColor("#44ba56")); // SP95 green
                        break;
                    case ("SP98"):
                        spinnerCarburant.setBackgroundColor(
                                Color.parseColor("#44ba56")); // SP98 green
                        break;
                    case ("E85"):
                        spinnerCarburant.setBackgroundColor(
                                Color.parseColor("#0170dd")); // E85 blue
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });


    }
             /******************************************************
             *           I N S T A N C E     M E T H O D           *
             *******************************************************/







    public void updateVolume()
    {
        float volume = 0.0F;
        try
        {
            float prix_litre = Float.parseFloat(editTextPrixLitre.getText().toString());
            float prix_total = Float.parseFloat(editTextPrixTotal.getText().toString());
            volume = prix_total / prix_litre;
        }
        catch (NumberFormatException e)
        {
            textViewVolumeCarburant.setText(String.format("0,0 litres", volume));
            return;
        }

        textViewVolumeCarburant.setText(String.format("%.1f litres", volume));
    }

    public void clickAjouterPlein()
    {

        // CHECK and CONVERT inputs form the USER

        LocalDate tmp_date;
        LocalTime tmp_time;
        int tmp_km;
        // String tmp_fuel; // not tested
        float tmp_liter_price, tmp_total_price, tmp_volume;


        // check the DATE input
        try
        {
            tmp_date = LocalDate.parse(editTextDate.getText().toString(),
                    DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }
        catch (Exception e)
        {
            // show error message to the user
            Toast.makeText(this, "Date incorrect", Toast.LENGTH_SHORT).show();
            editTextDate.startAnimation(shake);
            return;
        }

        // check the TIME input
        try
        {
            tmp_time = LocalTime.parse(editTextHeure.getText().toString(),
                    DateTimeFormatter.ofPattern("HH:mm"));
        }
        catch (Exception e)
        {
            // show error message to the user
            Toast.makeText(this, "Heure incorrect", Toast.LENGTH_SHORT).show();
            editTextHeure.startAnimation(shake);
            return;
        }

        // check the KM input
        try
        {
            tmp_km = Integer.parseInt(editTextKm.getText().toString());
        }
        catch (Exception e)
        {
            // show error message to the user
            Toast.makeText(this, "Kilométrage incorrect", Toast.LENGTH_SHORT).show();
            layoutKM.startAnimation(shake);
            return;
        }

        // fuel checking is useless

        // check the PRICE/LITER input
        try
        {
            String txt_price_liter = editTextPrixLitre.getText().toString();
//            // in case user uses ',' instead of '.' we must convert the comma
//            txt_price_liter = txt_price_liter.replace(',','.');
            // convert to float
            tmp_liter_price = Float.parseFloat(txt_price_liter);

            // check if it is equal to 0
            if (tmp_liter_price == 0f) { throw new Exception("Prix au litre nul"); }
        }
        catch (Exception e)
        {
            // show error message to the user
            Toast.makeText(this, "Prix/L incorrect", Toast.LENGTH_SHORT).show();
            layoutPrixLitre.startAnimation(shake);
            return;
        }

        // check the TOTAL PRICE input
        try
        {
            String txt_total_price = editTextPrixTotal.getText().toString();
//            // in case user uses ',' instead of '.' we must convert the comma
//            txt_total_price = txt_total_price.replace(',','.');
            // convert to float
            tmp_total_price = Float.parseFloat(txt_total_price);
        }
        catch (Exception e)
        {
            // show error message to the user
            Toast.makeText(this, "Prix total incorrect", Toast.LENGTH_SHORT).show();
            layoutPrixTotal.startAnimation(shake);
            return;
        }

        // compute the volume of fuel
        tmp_volume = tmp_total_price / tmp_liter_price;


        // create the new GasData object from the user input
        GasData gasdata = new GasData(tmp_date,
                tmp_time,
                spinnerCarburant.getSelectedItem().toString(),
                tmp_km,
                tmp_liter_price,
                tmp_total_price,
                tmp_volume);

        // save new gas data
        gasdatacollection.addGasData(gasdata);

        vibrator.vibrate(300);
        Toast.makeText(this, "Plein sauvegardé", Toast.LENGTH_LONG).show();

        finish();   // exit the activity

    }



}