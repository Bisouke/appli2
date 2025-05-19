package com.example.appli2;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

// BUG : CRASH SANS LE FICHIER AllGasData.json
// Attention, lorsque le fichier allGasData.json est supprimé, la liste n'est plus créée, cela fait planter
// toute l'application


public class GasDataCollection {

    // gas data collection sorting defines
    public static final int EARLIEST_TOP = 0;
    public static final int OLDEST_TOP = 1;

    private List<GasData> allGasData;

    static final String GASDATAFILENAME = "allGasData.json";
    private Context context = null;



    public GasDataCollection(Context arg_context)
    {
        context = arg_context;
        updateGasDataFromFile();
    }

    /*******************************************************
     *           GAS DATA COLLECTION INTERFACE             *
     *******************************************************/

    public HashMap<Integer, Float> getMonthExpense()
    {

        HashMap<Integer, Float> map = new HashMap<>();

        // pour chaque mois
        for(int month_var = 1; month_var <= 12; month_var++)
        {
            float monthExpense = 0f;
            // pour chaque gasdata sauvegardé
            for(GasData gasdata : allGasData)
            {
                // si gasdata month == actual month variable
                if(gasdata.getDate().getMonthValue() == month_var)
                    // ajouter le prix du plein au total du mois
                    monthExpense += gasdata.getTotalPrice();
            }
            map.put(month_var, monthExpense);
        }

        return map;
    }

    public void addGasData(GasData arg)
    {
        // add a GasData to the collection
         allGasData.add(arg);
         saveGasDataFile();
    }

    public void removeGasData(int arg_index)
    {
        allGasData.remove(arg_index);
        saveGasDataFile();
    }

    public void removeGasData(GasData arg_gasdata)
    {
        allGasData.remove(arg_gasdata);
        saveGasDataFile();
    }

    public List<GasData> getAllGasData(int arg_order)
    {

        // copy the gas data collection list
        List<GasData> allGasDataCopy = new ArrayList<>(allGasData);

        // comparator oldest top
        Comparator<GasData> comparator_oldest_top = new Comparator<GasData>() {
            @Override
            public int compare(GasData a, GasData b) {
                return a.getDatetime().compareTo(b.getDatetime());
            }
        };

        // comparator youngest top
        Comparator<GasData> comparator_youngest_top = new Comparator<GasData>() {
            @Override
            public int compare(GasData a, GasData b) {
                return b.getDatetime().compareTo(a.getDatetime());
            }
        };

        // sorting
        if (arg_order == OLDEST_TOP)
        {
            Collections.sort(allGasDataCopy, comparator_oldest_top);
        }
        else if (arg_order == EARLIEST_TOP)
        {
            Collections.sort(allGasDataCopy, comparator_youngest_top);
        }
        else
        // security : no sorting selected
        {
            return new ArrayList<>();
            // return empty list
        }

        // return the sorted copy
        return allGasDataCopy;

    }

    public int getGasDataSize()
    {
        return allGasData.size();
    }

    /*******************************************************
     *              GAS DATA FILE READ WRITE               *
     *******************************************************/


    void sort ()
    {

    }

    public void updateGasDataFromFile()
    {
        String json_str = null;
        try
        {
            FileInputStream fis = context.openFileInput(GASDATAFILENAME);
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
        }

        // Convert String Json into GasData collection
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        Type listType = new TypeToken<List<GasData>>(){}.getType(); // wtf is this ? but it's working

        allGasData = gson.fromJson(json_str, listType);

        // DANGER sécurité temporaire un peu a la zeub
        if (allGasData == null)
        {
            // create an empty list to avoid NPE
            allGasData = new ArrayList<>();
        }


    }
    public void saveGasDataFile()
    {
        // gson builder
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();

        // convert collection to json
        String json = gson.toJson(allGasData);

        // List<GasData> --> memory
        // write to file
        try {
            FileOutputStream fos = context.openFileOutput(GASDATAFILENAME, Context.MODE_PRIVATE);
            fos.write(json.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
