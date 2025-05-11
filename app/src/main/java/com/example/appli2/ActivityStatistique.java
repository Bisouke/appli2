package com.example.appli2;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ActivityStatistique extends AppCompatActivity {

    static final String allGasDataFileName = "allGasData.json";
    private List<GasData> allGasData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_statistique);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        /*******************************************************
         *           D A T A      P R O C E S S I N G          *
         *******************************************************/

        updateGasDataFromFile();

        // STPORE SPLITED GASDATA FOR EACH MONTH
        Map<java.lang.Integer, List<GasData>> dataMapByMonth = sortGasDataByMonth(allGasData);
        // key 0 --> all GasData from Jan
        // key 1 --> all GasData from Feb
        // key 2 --> all GasData from Mar

        // STORE EACH MONTH FUEL BILL
        Map<java.lang.Integer, java.lang.Float> expenseByMonthMap = new HashMap<>();
        // key 0 --> all expenses in Jan
        // key 1 --> all expenses in Feb
        // key 2 --> all expenses in Mar

        // sum each month fuel bill
        for (int key : dataMapByMonth.keySet())
        {
            List<GasData> monthlist = dataMapByMonth.get(key);

            float sum = 0f;
            if (monthlist != null)
            {
                for (GasData tmpGasData : monthlist)
                {
                    sum += tmpGasData.getTotalPrice();
                }
            }

            expenseByMonthMap.put(key, sum);
            // Log.d("DEBUG", key + " --> " + sum + " €");
        }


        /*******************************************************
         *                 B A R      C H A R T                *
         *******************************************************/

        // 1️⃣ Trouver l'élément BarChart dans le layout
        BarChart barChart = findViewById(R.id.barGraph);


        List<BarEntry> entriesGroup1 = new ArrayList<>();
        List<BarEntry> entriesGroup2 = new ArrayList<>();
        List<BarEntry> entriesGroup3 = new ArrayList<>();

        // Remplir les données
        entriesGroup1.add(new BarEntry(0, 4));
        entriesGroup1.add(new BarEntry(1, 8));
        entriesGroup1.add(new BarEntry(2, 6));
        entriesGroup1.add(new BarEntry(3, 12));
        entriesGroup1.add(new BarEntry(4, 4));
        entriesGroup1.add(new BarEntry(5, 8));
        entriesGroup1.add(new BarEntry(6, 6));
        entriesGroup1.add(new BarEntry(7, 12));
        entriesGroup1.add(new BarEntry(8, 4));
        entriesGroup1.add(new BarEntry(9, 8));
        entriesGroup1.add(new BarEntry(10, 6));
        entriesGroup1.add(new BarEntry(11, 12));

        entriesGroup2.add(new BarEntry(0, 5));
        entriesGroup2.add(new BarEntry(1, 7));
        entriesGroup2.add(new BarEntry(2, 9));
        entriesGroup2.add(new BarEntry(3, 10));

        entriesGroup3.add(new BarEntry(0, 5));
        entriesGroup3.add(new BarEntry(1, 7));
        entriesGroup3.add(new BarEntry(2, 9));
        entriesGroup3.add(new BarEntry(3, 10));


        BarDataSet set1 = new BarDataSet(entriesGroup1, "Groupe 1");
        set1.setColor(getResources().getColor(android.R.color.holo_blue_light));
        BarDataSet set2 = new BarDataSet(entriesGroup2, "Groupe 2");
        set2.setColor(getResources().getColor(android.R.color.holo_orange_light));
        BarDataSet set3 = new BarDataSet(entriesGroup3, "Groupe 3");
        set2.setColor(getResources().getColor(android.R.color.holo_green_light));

        BarData data = new BarData(set1, set2, set3);
        float groupSpace = 0.2f;
        float barSpace = 0.05f; // espace entre les barres d'un même groupe
        float barWidth = 0.35f; // largeur de chaque barre
        // (barSpace + barWidth) * 2 + groupSpace = 1.00 -> obligatoire

        data.setBarWidth(barWidth);
        barChart.setData(data);

        // Définir les labels
        String[] labels = {"Jan", "Feb", "Mar", "Apr", "A", "B", "C", "D","E","F","G","H"};
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis leftAxis = barChart.getAxisLeft();
        YAxis rightAxis = barChart.getAxisRight();

        leftAxis.setEnabled(false);
        rightAxis.setEnabled(false);

        xAxis.setDrawGridLines(false);

        // Important pour l'affichage correct
        barChart.getXAxis().setAxisMinimum(0);
        barChart.getXAxis().setAxisMaximum(0 + barChart.getBarData().getGroupWidth(groupSpace, barSpace) * labels.length);
        barChart.groupBars(0, groupSpace, barSpace);
        barChart.invalidate(); // rafraîchir



        /*******************************************************
         *                 P I E      C H A R T                *
         *******************************************************/


//        PieChart pieChart = findViewById(R.id.pieChart);
//
//        // 1. Créer les entrées
//        ArrayList<PieEntry> entries2 = new ArrayList<>();
//        entries2.add(new PieEntry(40f, "Diesel"));
//        entries2.add(new PieEntry(20f, "SP95E10"));
//        entries2.add(new PieEntry(10f, "SP95"));
//        entries2.add(new PieEntry(10f, "SP98"));
//        entries2.add(new PieEntry(30f, "E85"));
//
//        // 2. Créer un DataSet
//        PieDataSet dataSet2 = new PieDataSet(entries2, "Carburants");
//        dataSet2.setColors(
//                Color.parseColor("#fedc01"),    // Diesel   - yellow
//                Color.parseColor("#44ba56"),    // SP95E10  - green
//                Color.parseColor("#398e47"),    // SP95     - green
//                Color.parseColor("#006d44"),    // SP98     - green
//                Color.parseColor("#0170dd"));   // E85      - blue
//
//        // 3. Appliquer le dataset
//        PieData data = new PieData(dataSet2);
//        data.setValueTextSize(16f);
//        data.setValueTextColor(Color.WHITE);
//
//        // 4. Appliquer au PieChart
//        pieChart.setData(data);
//        pieChart.setUsePercentValues(false); // Optionnel
//        pieChart.setEntryLabelColor(Color.BLACK);
//        pieChart.setEntryLabelTextSize(14f);
//        pieChart.setCenterText("Litres");
//        pieChart.setCenterTextSize(18f);
//        pieChart.getDescription().setEnabled(false); // Enlever description par défaut
//        pieChart.animateY(800); // Animation
//        pieChart.getLegend().setTextSize(14f);


        /*******************************************************
         *              L I N E         C H A R T              *
         *******************************************************/

        LineChart lineChart;

            lineChart = findViewById(R.id.lineChart);

            // Créer une liste de points (x, y)
            ArrayList<Entry> entries3 = new ArrayList<>();
            entries3.add(new Entry(0, 2));
            entries3.add(new Entry(1, 4));
            entries3.add(new Entry(2, 1));
            entries3.add(new Entry(3, 7));
            entries3.add(new Entry(4, 3));

            // Créer un LineDataSet avec ces points
            LineDataSet dataSet3 = new LineDataSet(entries3, "Données Exemple");
            dataSet3.setColor(0xFF416BE9);
            dataSet3.setValueTextSize(12f);
            dataSet3.setValueTextColor(getResources().getColor(R.color.black));
            dataSet3.setLineWidth(2f);

            // Appliquer les données au graphique
            LineData lineData = new LineData(dataSet3);
            lineChart.setData(lineData);

            // Enlever la description par défaut
            Description description = new Description();
            description.setText("");

            lineChart.setDescription(description);


            lineChart.getXAxis().setDrawGridLines(false);
            lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
            lineChart.getAxisLeft().setEnabled(false);
            lineChart.getAxisRight().setEnabled(false);
            // Rafraîchir le graphique
            lineChart.invalidate();




        /*******************************************************
         *                 B A R      C H A R T                *
         *******************************************************/

        // 1️⃣ Trouver l'élément BarChart dans le layout
        BarChart barChart2 = findViewById(R.id.barGraph2);

        // 2️⃣ Ajouter des données (dépenses sur 12 mois)
        ArrayList<BarEntry> entriesb2 = new ArrayList<>();
        entriesb2.add(new BarEntry(0f, expenseByMonthMap.get(1))); // Janvier : 200€
        entriesb2.add(new BarEntry(1f, expenseByMonthMap.get(2))); // Février : 150€
        entriesb2.add(new BarEntry(2f, expenseByMonthMap.get(3))); // Mars : 180€
        entriesb2.add(new BarEntry(3f, expenseByMonthMap.get(4))); // Avril : 220€
        entriesb2.add(new BarEntry(4f, expenseByMonthMap.get(5))); // Mai : 160€
        entriesb2.add(new BarEntry(5f, expenseByMonthMap.get(6))); // Juin : 210€
        entriesb2.add(new BarEntry(6f, expenseByMonthMap.get(7))); // Juillet : 190€
        entriesb2.add(new BarEntry(7f, expenseByMonthMap.get(8))); // Août : 250€
        entriesb2.add(new BarEntry(8f, expenseByMonthMap.get(9))); // Septembre : 300€
        entriesb2.add(new BarEntry(9f, expenseByMonthMap.get(10))); // Octobre : 280€
        entriesb2.add(new BarEntry(10f, expenseByMonthMap.get(11))); // Novembre : 350€
        entriesb2.add(new BarEntry(11f, expenseByMonthMap.get(12))); // Décembre : 400€

        // 3️⃣ Créer un DataSet avec les entrées
        BarDataSet dataSetb2 = new BarDataSet(entriesb2, "Dépenses mensuelles (€)");
        dataSetb2.setColor(0xFF416BE9); // Couleur des barres
        dataSetb2.setValueTextColor(0xFF000000); // Couleur des valeurs
        dataSetb2.setValueTextSize(16f);

        // 4️⃣ Créer des BarData et associer au graphique
        BarData barData2 = new BarData(dataSetb2);
        barChart2.setData(barData2);


        barChart2.getDescription().setEnabled(false); // Désactive la description
        barChart2.setDrawBarShadow(false); // Désactive l'ombre sous les barres

        XAxis xAxis2 = barChart2.getXAxis();
        YAxis leftAxis2 = barChart2.getAxisLeft();
        YAxis rightAxis2 = barChart2.getAxisRight();

        leftAxis2.setEnabled(false);
        rightAxis2.setEnabled(false);

        xAxis2.setDrawGridLines(false);

        leftAxis2.setAxisMinimum(0f); // Valeur minimale de l'axe Y

        xAxis2.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis2.setTextSize(16f);
        xAxis2.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                // Format pour afficher les mois
                switch ((int) value) {
                    case 0: return "Jan";
                    case 1: return "Fév";
                    case 2: return "Mar";
                    case 3: return "Avr";
                    case 4: return "Mai";
                    case 5: return "Jui";
                    case 6: return "Jul";
                    case 7: return "Aoû";
                    case 8: return "Sep";
                    case 9: return "Oct";
                    case 10: return "Nov";
                    case 11: return "Déc";
                    default: return "";
                }
            }
        });

        barChart2.getLegend().setEnabled(false);
        barChart2.animateY(800); // Animation verticale
        barChart2.invalidate(); // Rafraîchir l'affichage


    }



    private boolean updateGasDataFromFile()
    {
        String json_str = null;
        try
        {
            FileInputStream fis = openFileInput(allGasDataFileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            json_str = sb.toString();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }

        // Convert String Json into GasData collection
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
                .create();
        Type listType = new TypeToken<List<GasData>>(){}.getType(); // wtf is this ? but it's working
        allGasData = gson.fromJson(json_str, listType);
        return true;

    }

    private Map<Integer, List<GasData>> sortGasDataByMonth(List<GasData> arg_listGasData)
    {
        Map<Integer, List<GasData>> localMapBarGraph = new HashMap<>();
        // key 0 --> all GasData from Jan
        // key 1 --> all GasData from Feb
        // key 2 --> all GasData from Mar
        // ...

        for (int month = 1; month <= 12; month++) {

            List<GasData> tempList = new ArrayList<>();

            for (GasData gasData : arg_listGasData)
            {
                if (gasData.getDate().getMonthValue() == month) {
                    tempList.add(gasData);
                }
            }

            localMapBarGraph.put(month, tempList);
        }

        return localMapBarGraph;
    }
}