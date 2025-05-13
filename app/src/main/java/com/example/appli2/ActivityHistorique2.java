package com.example.appli2;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

// BUG : crash à l'ouverture de l'activité avec AllGasData.json vide.

public class ActivityHistorique2 extends AppCompatActivity {

    private Button buttonSupprimer;
    private TextView textViewNbPlein;
    private ToggleButton toggleButton_tri;
    private LinearLayout linearLayoutHistorique;
    private GasDataCollection gasdataCollec;
    @Nullable
    private View selected_view;
    private List<View> inflated_view_list; // containing each inflated views


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

        // Toggle button
        // toggleButton_tri = findViewById(R.id.toggleButton_tri);
        // Button
        // buttonSupprimer = findViewById(R.id.buttonSupprimerHistorique);
        // TextView
        // textViewNbPlein = findViewById(R.id.textViewNbPlein);
        // LinearLayout
        linearLayoutHistorique = findViewById(R.id.linearLayoutHistorique);

        // for gas data database access
        gasdataCollec = new GasDataCollection(this);

        // affiche le nombre de plein enregistrés
        // textViewNbPlein.setText(String.valueOf(gasdataCollec.getGasDataSize()));

        // refreshHistoryList();


        // inflateGasDataViews();


                        /******************************************************
                        *                   L I S T E N E R S                 *
                        *******************************************************/


//        buttonSupprimer.setOnClickListener(new View.OnClickListener()   {
//               @Override
//               public void onClick(View v)
//               {
//               }
//           });


//        toggleButton_tri.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v)
//            {
//                // get button state
//                boolean isSelected = toggleButton_tri.isSelected();
//
//                // change background ressrouce
//                if (isSelected)
//                {
//                    toggleButton_tri.setBackgroundResource(R.drawable.down_arrow);
//                }
//                else
//                {
//                    toggleButton_tri.setBackgroundResource(R.drawable.up_arrow);
//                }
//
//                // reverse button state for next click
//                toggleButton_tri.setSelected(!isSelected);
//
//                // update gas data list view
//                inflateGasDataViews();
//
//                // desselect gas data
//                selected_view = null;
//                buttonSupprimer.setEnabled(false);
//
//            }
//        });


    }

            /******************************************************
            *           I N S T A N C E     M E T H O D           *
            *******************************************************/


    public void inflateGasDataViews()
    // INFLATE NEW VIEWS IN LINEAR LAYOUT
    {

        // clean all existing views inside the vertical layout
        linearLayoutHistorique.removeAllViews();

        // click listener on each gas data view
        View.OnClickListener listener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                // SELECTION LOGIC for gas data view

                // remove background color for each gas data view
                for (View view : inflated_view_list)
                {
                    // alpha to 0 : invisible color
                    view.setBackgroundColor(Color.argb(0,149,183,253));
                }

                if (selected_view == v)
                // same view was clicked two times in a row
                // so deselect this gas data view
                {
                    // desselect
                    selected_view = null;
                    // disable delete button
                    buttonSupprimer.setEnabled(false);
                    // skip
                    return;
                }

                else
                // this gas data view was clicked for the first time
                {
                    // set background color on the clicked view
                    v.setBackgroundColor(Color.argb(127,149,183,253));

                    // change selected view
                    selected_view = v;

                    // enable delete button
                    buttonSupprimer.setEnabled(true);
                }

            }
        };

        // get the gas data list from GasDataCollection class
        List<GasData> gasDataList;

        // get gas data list accordingly to user sorting selection
        if (toggleButton_tri.isSelected())
            gasDataList = gasdataCollec.getAllGasData(GasDataCollection.OLDEST_TOP);
        else
            gasDataList = gasdataCollec.getAllGasData(GasDataCollection.EARLIEST_TOP);

        // create the inflater
        LayoutInflater inflater = LayoutInflater.from(this);

        // list of each gas data view
        inflated_view_list = new ArrayList<>();

        // CREATE, ADD, MODIFY THE INFLATED VIEW
        for (GasData gasdata : gasDataList)
        // for each gas data<
        {
            // inflate the view "item_historique_plein"
            View inflated_view = inflater.inflate(R.layout.item_historique_plein,
                    linearLayoutHistorique, false);

            // add the view to the list view
            inflated_view_list.add(inflated_view);

            // find and modify inner views

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
            textViewItemHist_ligne1.setText(gasdata.getVolumeStr() + "L de " + gasdata.getFuel());

            // liter price + date
            textViewItemHist_ligne2.setText(gasdata.getPrice_literStr() + " €/L  "
                    + gasdata.getDateStr());

            // price
            textViewItemHist_prix.setText(gasdata.getTotalPriceStr() + " €");

            // TIME ELAPSED
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
                if(daysBetween == 1)
                    textViewItemHist_temps.setText("il y a " + daysBetween + " jour");
                else
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
                if (yearsBetween == 1)
                    textViewItemHist_temps.setText("il y a " + yearsBetween + " an");
                else
                    textViewItemHist_temps.setText("il y a " + yearsBetween + " ans");
            }

            // FUEL COLOR

            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.RECTANGLE);
            drawable.setCornerRadius(32); // Rayon d’arrondi en pixels

            switch (gasdata.getFuel())
            {
                case "SP98":
                    drawable.setColor(Color.parseColor("#2f823c")); // SP98 green
                    break;

                case "SP95":
                case "SP95 E10":
                    drawable.setColor(Color.parseColor("#44ba56")); // SP95 and SP95E10 green
                    break;

                case "E85":
                    drawable.setColor(Color.parseColor("#0170dd")); // E85 blue
                    break;

                case "DIESEL":
                    drawable.setColor(Color.parseColor("#fedc01")); // Diesel yellow
                    break;
            }
            textViewItemHist_color.setBackground(drawable);


            // add the view to the linear layout
            linearLayoutHistorique.addView(inflated_view);

            // set click listener
            inflated_view.setOnClickListener(listener);

        }
    }



}