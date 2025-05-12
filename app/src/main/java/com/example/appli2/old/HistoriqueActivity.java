package com.example.appli2.old;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.appli2.GasData;
import com.example.appli2.GasDataCollection;
import com.example.appli2.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// UNDO COMMENTS SECTION BEFORE USE

public class HistoriqueActivity extends AppCompatActivity {

    private ListView listViewHistorique;
    SimpleAdapter adapter;
    private Button buttonSupprimer;
    private TextView textViewNb;
    private static int itemSlctPos;
    private Spinner spinner_tri;
    static final String allGasDataFileName = "allGasData.json";

    LinearLayout linearLayoutHistorique;


    GasDataCollection gasdataCollec;

            /******************************************************
             *                   O N C R E A T E                  *
             ******************************************************/
/*
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_historique);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // views
        listViewHistorique = findViewById(R.id.listViewHistorique);
        buttonSupprimer = findViewById(R.id.buttonSupprimerHistorique);
        textViewNb = findViewById(R.id.textViewNbPlein);

        spinner_tri = findViewById(R.id.spinnerTriHistorique);

        gasdataCollec = new GasDataCollection(this);

        textViewNb.setText(String.valueOf(gasdataCollec.getGasDataSize()));
        refreshHistoryList();

        // linearLayoutHistorique = findViewById(R.id.linearLayoutHistorique);



*/

                        /******************************************************
                        *                   L I S T E N E R S                 *
                        *******************************************************/

/*
        buttonSupprimer.setOnClickListener(v -> {
            deleteGasData();
        });

        // activate the delete button only when an item is selected
        listViewHistorique.setOnItemClickListener((parent, arg_view, position, id) ->
        {
            buttonSupprimer.setEnabled(true);
            itemSlctPos = position;

//            Log.d("DEBUG", "itemSlctPos : "+itemSlctPos);
//            Log.d("DEBUG", allGasData.get(itemSlctPos).toString());

        });

        spinner_tri.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                refreshHistoryList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

 */

            /******************************************************
            *           I N S T A N C E     M E T H O D           *
            *******************************************************/

/*
    public void refreshHistoryList()
    {
        // update gas data collection from stored file
        gasdataCollec.updateGasDataFromFile();

        // get index of sorting type selected
        int sort_type = spinner_tri.getSelectedItemPosition();

        List<Map<String, String>> data = new ArrayList<>();

        for (GasData e : gasdataCollec.getAllGasData(sort_type))
        {
            Map<String, String> item = new HashMap<>();
            item.put("title", e.getFirstLineStr());
            item.put("subtitle", e.getSecondLineStr());
            data.add(item);
        }

        // Création de l’adaptateur
        adapter = new SimpleAdapter(
                this,
                data,
                android.R.layout.simple_list_item_2, // Layout intégré avec 2 lignes
                new String[] {"title", "subtitle"},   // Clés de la map
                new int[] {android.R.id.text1, android.R.id.text2} // Views cibles
        );

        // attention with simple_list_item_1 when checking items
        // Associer l'adaptateur au ListView
        listViewHistorique.setAdapter(adapter);
        // listViewHistorique.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        adapter.notifyDataSetChanged(); // update listView
    }


    private void deleteGasData()
    {

        new AlertDialog.Builder(this)
                .setTitle("Confirmation")
                .setMessage("\nConfirmer la suppression ?")
                .setPositiveButton("Supprimer", (dialog, which) -> {

                    // delete previously clicked item
                    gasdataCollec.removeGasData(itemSlctPos);
                    refreshHistoryList();

                })
                .setNegativeButton("Annuler", (dialog, which) -> dialog.dismiss())
                .show();


    }

*/

}