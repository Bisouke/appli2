package com.example.appli2;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.w3c.dom.Text;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

// BUG : crash à l'ouverture de l'activité avec AllGasData.json vide.

public class ActivityHistorique2 extends AppCompatActivity {

    private Button buttonSupprimer;
    private TextView textViewNbPlein;
    private Spinner spinnerTriHistorique;
    LinearLayout linearLayoutHistorique, itemLinearLayout;
    GasDataCollection gasdataCollec;
    @Nullable
    View selected_view;

    // View list containing each inflated views
    private List<View> inflated_view_list;


            /******************************************************
             *                   O N C R E A T E                  *
             ******************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_historique2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Spinner
        spinnerTriHistorique = findViewById(R.id.spinnerTriHistorique);
        // Button
        buttonSupprimer = findViewById(R.id.buttonSupprimerHistorique);
        // TextView
        textViewNbPlein = findViewById(R.id.textViewNbPlein);
        // LinearLayout
        linearLayoutHistorique = findViewById(R.id.linearLayoutHistorique);

        // for gas data database access
        gasdataCollec = new GasDataCollection(this);

        // affiche le nombre de plein enregistrés
        textViewNbPlein.setText(String.valueOf(gasdataCollec.getGasDataSize()));

        // refreshHistoryList();



        View.OnClickListener listener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

//                int color, alpha = 0;
//
//                // get background color
//                Drawable background = v.getBackground();
//
//                if (background instanceof ColorDrawable) {
//                    // convert Drawable to ColorDrawable
//                    color = ((ColorDrawable) background).getColor();
//                    // get alpha channel
//                    alpha = Color.alpha(color);
//                }

                // remove background color for each inflated view
                for (View view : inflated_view_list)
                {
                    // alpha to 0 : invisible color
                    view.setBackgroundColor(Color.argb(0,149,183,253));
                }

                // set background color on the clicked view
                v.setBackgroundColor(Color.argb(127,149,183,253));

                // change selected view
                selected_view = v;

                // enable delete button
                buttonSupprimer.setEnabled(true);
            }
        };


        //          INFLATE NEW VIEW IN LINEAR LAYOUT

        // create the inflater
        LayoutInflater inflater = LayoutInflater.from(this);

        // get the gas data list from GasDataCollection class
        List<GasData> list = gasdataCollec.getAllGasData(GasDataCollection.EARLIEST_TOP);

        inflated_view_list = new ArrayList<>();

        // CREATE, ADD, MODIFY THE INFLATED VIEW
        for (GasData gasdata : list)
        // for each gas data
        {
            // inflate the view "item_historique_plein"
            View inflated_view = inflater.inflate(R.layout.item_historique_plein,
                    linearLayoutHistorique, false);

            // add the view to the list view
            inflated_view_list.add(inflated_view);

            // FIND INNER VIEWS
            // ligne 1
            TextView textViewItemHist_ligne1 =
                    inflated_view.findViewById(R.id.textViewItemHist_ligne1);
            // ligne 2
            TextView textViewItemHist_ligne2 =
                    inflated_view.findViewById(R.id.textViewItemHist_ligne2);
            // prix
            TextView textViewItemHist_prix =
                    inflated_view.findViewById(R.id.textViewItemHist_prix);
            // temps
            TextView textViewItemHist_temps =
                    inflated_view.findViewById(R.id.textViewItemHist_temps);
            // color
            View textViewItemHist_color =
                    inflated_view.findViewById(R.id.textViewItemHist_color);

            // MODIFY INNER VIEWS VALUES

            // volume + fuel
            textViewItemHist_ligne1.setText(gasdata.getVolume() + "L de " + gasdata.getFuel());

            // liter price + date
            textViewItemHist_ligne2.setText(gasdata.getPrice_liter() + " €/L  "
                    + gasdata.getDateStr());

            // price
            textViewItemHist_prix.setText(gasdata.getTotal_price() + " €");

            // time elapsed
            LocalDate current_time = LocalDate.now();
            long daysBetween = ChronoUnit.DAYS.between(gasdata.getDate(), current_time);
            long monthsBetween = ChronoUnit.MONTHS.between(gasdata.getDate(), current_time);
            long yearsBetween = ChronoUnit.YEARS.between(gasdata.getDate(), current_time);

            if (daysBetween == 0)
            {
                // use "today"
                textViewItemHist_temps.setText("aujourd'hui");
            }
            else if (daysBetween <= 31)
            {
                // use days
                textViewItemHist_temps.setText("il y a " + daysBetween + " jours");
            }
            else if (monthsBetween < 12)
            {
                // use months
                textViewItemHist_temps.setText("il y a " + monthsBetween + " mois");
            }
            else
            {
                // use year
                textViewItemHist_temps.setText("il y a " + yearsBetween + " ans");
            }



            // fuel color
            switch (gasdata.getFuel())
            {
                case "SP98":
                    textViewItemHist_color.setBackgroundColor(
                            Color.parseColor("#44ba56")); // SP98 green
                    break;

                case "S95":
                    textViewItemHist_color.setBackgroundColor(
                            Color.parseColor("#44ba56")); // SP95 green
                    break;

                case "SP95E10":
                    textViewItemHist_color.setBackgroundColor(
                            Color.parseColor("#44ba56")); // SP95 green
                    break;

                case "E85":
                    textViewItemHist_color.setBackgroundColor(
                            Color.parseColor("#0170dd")); // E85 blue
                    break;

                case "DIESEL":
                    textViewItemHist_color.setBackgroundColor(
                            Color.parseColor("#fedc01")); // Diesel yellow
                    break;
            }

            // add the view to the linear layout
            linearLayoutHistorique.addView(inflated_view);

            // set click listener
            inflated_view.setOnClickListener(listener);

        }




                        /******************************************************
                        *                   L I S T E N E R S                 *
                        *******************************************************/


        buttonSupprimer.setOnClickListener(v -> {
            // deleteGasData();
        });

        spinnerTriHistorique.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

            /******************************************************
            *           I N S T A N C E     M E T H O D           *
            *******************************************************/


    public void refreshHistoryList()
    {
//        // update gas data collection from stored file
//        gasdataCollec.updateGasDataFromFile();
//
//        // get index of sorting type selected
//        int sort_type = spinner_tri.getSelectedItemPosition();
//
//        List<Map<String, String>> data = new ArrayList<>();
//
//        for (GasData e : gasdataCollec.getAllGasData(sort_type))
//        {
//            Map<String, String> item = new HashMap<>();
//            item.put("title", e.getFirstLineStr());
//            item.put("subtitle", e.getSecondLineStr());
//            data.add(item);
//        }
//
//        // Création de l’adaptateur
//        adapter = new SimpleAdapter(
//                this,
//                data,
//                android.R.layout.simple_list_item_2, // Layout intégré avec 2 lignes
//                new String[] {"title", "subtitle"},   // Clés de la map
//                new int[] {android.R.id.text1, android.R.id.text2} // Views cibles
//        );
//
//        // attention with simple_list_item_1 when checking items
//        // Associer l'adaptateur au ListView
//        listViewHistorique.setAdapter(adapter);
//        // listViewHistorique.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
//        adapter.notifyDataSetChanged(); // update listView
    }



}