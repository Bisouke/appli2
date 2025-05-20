package com.example.appli2;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ActivityStatistique extends AppCompatActivity {

    GasDataCollection gasCollection;




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

        gasCollection = new GasDataCollection(this);

        /*******************************************************
         *           D A T A      P R O C E S S I N G          *
         *******************************************************/


        // STPORE SPLITED GASDATA FOR EACH MONTH
        // Map<java.lang.Integer, List<GasData>> dataMapByMonth = sortGasDataByMonth(allGasData);
        // key 0 --> all GasData from Jan
        // key 1 --> all GasData from Feb
        // key 2 --> all GasData from Mar

        // STORE EACH MONTH FUEL BILL
        // Map<java.lang.Integer, java.lang.Float> expenseByMonthMap = new HashMap<>();
        // key 0 --> all expenses in Jan
        // key 1 --> all expenses in Feb
        // key 2 --> all expenses in Mar

        // sum each month fuel bill
//        for (int key : dataMapByMonth.keySet())
//        {
//            List<GasData> monthlist = dataMapByMonth.get(key);
//
//            float sum = 0f;
//            if (monthlist != null)
//            {
//                for (GasData tmpGasData : monthlist)
//                {
//                    sum += tmpGasData.getTotalPrice();
//                }
//            }
//
//            expenseByMonthMap.put(key, sum);
//            // Log.d("DEBUG", key + " --> " + sum + " €");
//        }

        Log.d("DEBUG", gasCollection.getMonthExpense().toString());


        /*******************************************************
         *                 B A R      C H A R T     1          *
         *******************************************************/

        // 1️⃣ Trouver l'élément BarChart dans le layout
        BarChart barGraph1 = findViewById(R.id.barGraph1);

        // récupérer le prix dépensé chaque mois sur une année en carburant
        HashMap<Integer, Float> monthExpenseMap = gasCollection.getMonthExpense();

        // 2️⃣ Ajouter des données (dépenses sur 12 mois)
        ArrayList<BarEntry> entriesb1 = new ArrayList<>();
        // ajouter les données de chaque mois
        for (int i = 1; i <= 12; i++)
        {
            entriesb1.add(new BarEntry(i - 1, monthExpenseMap.getOrDefault(i, 0f)));
        }


        // 3️⃣ Créer un DataSet avec les entrées
        BarDataSet dataSetb1 = new BarDataSet(entriesb1, "Dépenses mensuelles (€)");
        dataSetb1.setColor(0xFF416BE9); // Couleur des barres
        dataSetb1.setValueTextColor(0xFF000000); // Couleur des valeurs
        dataSetb1.setValueTextSize(16f);

        // 4️⃣ Créer des BarData et associer au graphique
        BarData barData1 = new BarData(dataSetb1);
        barGraph1.setData(barData1);


        barGraph1.getDescription().setEnabled(false); // Désactive la description
        barGraph1.setDrawBarShadow(false); // Désactive l'ombre sous les barres

        XAxis xAxis2 = barGraph1.getXAxis();
        YAxis leftAxis2 = barGraph1.getAxisLeft();
        YAxis rightAxis2 = barGraph1.getAxisRight();

        xAxis2.setLabelRotationAngle(-45f);     // rotation des x labels (gain de places)
        xAxis2.setLabelCount(12);               // afficher tous les x labels
        xAxis2.setDrawGridLines(false);
        xAxis2.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis2.setTextSize(16f);

        xAxis2.setValueFormatter(new ValueFormatter()
        {
            @Override
            public String getFormattedValue(float value) {
                // Format pour afficher les mois
                switch ((int) value) {
                    case 0: return "Jan";
                    case 1: return "Fev";
                    case 2: return "Mar";
                    case 3: return "Avr";
                    case 4: return "Mai";
                    case 5: return "Jui";
                    case 6: return "Jul";
                    case 7: return "Aou";
                    case 8: return "Sep";
                    case 9: return "Oct";
                    case 10: return "Nov";
                    case 11: return "Dec";
                    default: return "";
                }
            }
        });

        leftAxis2.setEnabled(false);
        rightAxis2.setEnabled(false);



        leftAxis2.setAxisMinimum(0f); // Valeur minimale de l'axe Y



        barGraph1.getLegend().setEnabled(false);
        barGraph1.animateY(800); // Animation verticale
        barGraph1.invalidate(); // Rafraîchir l'affichage


        /*******************************************************
         *              L I N E         C H A R T              *
         *******************************************************/

        LineChart lineChart;

            lineChart = findViewById(R.id.lineChart);

            // Créer une liste de points (x, y)
            ArrayList<Entry> entries3 = new ArrayList<>();
            entries3.add(new Entry(0f, 0));
            entries3.add(new Entry(18f, 4));
            entries3.add(new Entry(365f, 4));

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

        // Triple BAR GRAPH pour chaque mois

//        // 1️⃣ Trouver l'élément BarChart dans le layout
//        BarChart barChart = findViewById(R.id.barGraph);
//
//
//        List<BarEntry> entriesGroup1 = new ArrayList<>();
//        List<BarEntry> entriesGroup2 = new ArrayList<>();
//        List<BarEntry> entriesGroup3 = new ArrayList<>();
//
//        // Remplir les données
//        entriesGroup1.add(new BarEntry(0, 4));
//        entriesGroup1.add(new BarEntry(1, 8));
//        entriesGroup1.add(new BarEntry(2, 6));
//        entriesGroup1.add(new BarEntry(3, 12));
//        entriesGroup1.add(new BarEntry(4, 4));
//        entriesGroup1.add(new BarEntry(5, 8));
//        entriesGroup1.add(new BarEntry(6, 6));
//        entriesGroup1.add(new BarEntry(7, 12));
//        entriesGroup1.add(new BarEntry(8, 4));
//        entriesGroup1.add(new BarEntry(9, 8));
//        entriesGroup1.add(new BarEntry(10, 6));
//        entriesGroup1.add(new BarEntry(11, 12));
//
//        entriesGroup2.add(new BarEntry(0, 5));
//        entriesGroup2.add(new BarEntry(1, 7));
//        entriesGroup2.add(new BarEntry(2, 9));
//        entriesGroup2.add(new BarEntry(3, 10));
//
//        entriesGroup3.add(new BarEntry(0, 5));
//        entriesGroup3.add(new BarEntry(1, 7));
//        entriesGroup3.add(new BarEntry(2, 9));
//        entriesGroup3.add(new BarEntry(3, 10));
//
//
//        BarDataSet set1 = new BarDataSet(entriesGroup1, "Groupe 1");
//        set1.setColor(getResources().getColor(android.R.color.holo_blue_light));
//        BarDataSet set2 = new BarDataSet(entriesGroup2, "Groupe 2");
//        set2.setColor(getResources().getColor(android.R.color.holo_orange_light));
//        BarDataSet set3 = new BarDataSet(entriesGroup3, "Groupe 3");
//        set2.setColor(getResources().getColor(android.R.color.holo_green_light));
//
//        BarData data = new BarData(set1, set2, set3);
//        float groupSpace = 0.2f;
//        float barSpace = 0.05f; // espace entre les barres d'un même groupe
//        float barWidth = 0.35f; // largeur de chaque barre
//        // (barSpace + barWidth) * 2 + groupSpace = 1.00 -> obligatoire
//
//        data.setBarWidth(barWidth);
//        barChart.setData(data);
//
//        // Définir les labels
//        String[] labels = {"Jan", "Feb", "Mar", "Apr", "A", "B", "C", "D","E","F","G","H"};
//        XAxis xAxis = barChart.getXAxis();
//        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
//        xAxis.setCenterAxisLabels(true);
//        xAxis.setGranularity(1f);
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//
//        YAxis leftAxis = barChart.getAxisLeft();
//        YAxis rightAxis = barChart.getAxisRight();
//
//        leftAxis.setEnabled(false);
//        rightAxis.setEnabled(false);
//
//        xAxis.setDrawGridLines(false);
//
//        // Important pour l'affichage correct
//        barChart.getXAxis().setAxisMinimum(0);
//        barChart.getXAxis().setAxisMaximum(0 + barChart.getBarData().getGroupWidth(groupSpace, barSpace) * labels.length);
//        barChart.groupBars(0, groupSpace, barSpace);
//        barChart.invalidate(); // rafraîchir



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