package com.example.appli2;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ActivityHistorique2 extends AppCompatActivity {

    private Button buttonSupprimer;
    private TextView textViewNbPlein;
    private Spinner spinnerTriHistorique;
    LinearLayout linearLayoutHistorique, itemLinearLayout;
    GasDataCollection gasdataCollec;


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



        //          INFLATE NEW VIEW IN LINEAR LAYOUT

        LayoutInflater inflater = LayoutInflater.from(this);

        // create the view
        View view1 = inflater.inflate(R.layout.item_historique_plein,
                linearLayoutHistorique, false);

        // add the view to the linear layout
        linearLayoutHistorique.addView(view1);

        LinearLayout ll = view1.findViewById(R.id.itemLinearLayout);

        ll.setBackgroundColor(Color.argb(100,149,183,253));

        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DEBUG","TEST");
            }
        });



        // create the view
        View view2 = inflater.inflate(R.layout.item_historique_plein,
                linearLayoutHistorique, false);

        // add the view to the linear layout
        linearLayoutHistorique.addView(view2);

        LinearLayout ll2 = view2.findViewById(R.id.itemLinearLayout);

        // ll2.setBackgroundColor(Color.argb(100,149,183,253));




        // create and add multiple history items

        for (int i=0; i<30; i++)
        {
            View view3 = inflater.inflate(R.layout.item_historique_plein,
                    linearLayoutHistorique, false);
            linearLayoutHistorique.addView(view3);
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
                refreshHistoryList();
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