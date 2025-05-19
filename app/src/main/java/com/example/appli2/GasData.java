package com.example.appli2;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.lang.Math;

// collection of data in a usable format
public class GasData
{

    private LocalDate date;
    private LocalTime time;
    private LocalDateTime datetime; // this attribute is used to sort properly all gasdata
    private String fuel;
    private int km;
    private float price_liter, volume, total_price;

    private static final DateTimeFormatter date_formatter = DateTimeFormatter.ofPattern("dd-MM-yy");

    private static final DateTimeFormatter hour_formatter = DateTimeFormatter.ofPattern("HH:mm");

    private static final DecimalFormat numberFormat1 = new DecimalFormat("#.0");
    private static final DecimalFormat numberFormat2 = new DecimalFormat("0.00");



    public GasData(LocalDate arg_date, LocalTime arg_time, String arg_fuel, int arg_km,
                   float arg_liter_price, float arg_total_price, float arg_volume)
    {
        this.date = arg_date;
        this.time = arg_time;
        this.fuel = arg_fuel;
        this.km = arg_km;
        this.price_liter = arg_liter_price;
        this.total_price = arg_total_price;
        this.volume = arg_volume;

        this.datetime = LocalDateTime.of(this.date, this.time);

        // Log.d("DEBUG", "datetime = " + this.datetime.toString());

    }

    public GasData(String arg_date, String arg_time, String arg_fuel, String arg_km,
                   String arg_price_liter, String arg_total_price)
    {

        // ARG_DATE
        try
        {
            date = LocalDate.parse(arg_date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (DateTimeParseException e)
        {
            throw new DateTimeParseException("Erreur date", arg_date ,e.getErrorIndex());
        }
        // Log.d("GASDATA", date.toString());

        // convert arg_time from string to LocalTime
        time = LocalTime.parse(arg_time, DateTimeFormatter.ofPattern("HH:mm"));
        // Log.d("GASDATA", time.toString());

        fuel = arg_fuel;
        // List<String> carburants = Arrays.asList("SP98", "SP95 E5", "SP95 E10", "E85", "Diesel");
        // carburants.contains(arg_carburant);

        // convert arg_km to int
        try {
            km = Integer.parseInt(arg_km);
        } catch (NumberFormatException e)
        {
            throw new NumberFormatException("Erreur kilométrage");
        }
        // convert arg_price_liter to float
        try {
            price_liter = Float.parseFloat(arg_price_liter);
        } catch (NumberFormatException e)
        {
            throw new NumberFormatException("Erreur prix au litre");
        }

        // convert arg_total_price to float
        try {
            total_price = Float.parseFloat(arg_total_price);
        } catch (NumberFormatException e)
        {
            throw new NumberFormatException("Erreur prix total");
        }

        // compute volume
        volume = total_price / price_liter;

//        Log.d("DEBUG", this.getFirstLineStr());
//        Log.d("DEBUG", this.getSecondLineStr());

    }

    @Override
    public String toString()
    {

        return "" + date.format(date_formatter) + " à " + time + "  - " + numberFormat1.format(total_price)
                + " € de " + fuel + "\n " + numberFormat2.format(price_liter) +
                " €/L soit " + numberFormat1.format(volume) + "L à " + km + " km";
    }

    public String getFirstLineStr()
    {

        return "" + date.format(date_formatter) + " à " + time + "  - "
                + numberFormat1.format(total_price) + " € de " + fuel;
    }

    public String getSecondLineStr()
    {
        return  numberFormat2.format(price_liter) + " €/L soit " + numberFormat1.format(volume)
                + "L à " + km + " km";
    }



    // GETTERS

    public LocalTime getTime() {
        return time;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public String getDateStr() {
        return date.format(date_formatter);
    }

    public String getTimeStr() {
        return time.format(hour_formatter);
    }

    public String getFuel() {
        return fuel;
    }

    public int getKm() {
        return km;
    }

    public float getPrice_liter() {
        return price_liter;
    }

    public String getPrice_literStr() {
        return numberFormat2.format(price_liter);
    }

    public float getVolume() {
        return volume;
    }

    public String getVolumeStr()
    // return string volume without decimal
    {
        return String.valueOf(Math.round(this.volume));
    }

    public float getTotalPrice() {
        return total_price;
    }

    public String getTotalPriceStr() {
        return String.valueOf(Math.round(this.total_price));
    }

//    Pour récupérer une valeur float entre 0 et 365 à partir d'un LocalDateTime

//    LocalDateTime now = LocalDateTime.now();
//
//    int dayOfYear = now.getDayOfYear(); // 1 à 365 (ou 366)
//    int hour = now.getHour();
//    int minute = now.getMinute();
//
//    // Ajout d'une fraction du jour
//    float fractionOfDay = (hour + minute / 60f) / 24f;
//
//    // Résultat final en float entre 0 et 365
//    float dayAsFloat = (dayOfYear - 1) + fractionOfDay;
//
//    System.out.println("Jour dans l'année (float) : " + dayAsFloat);

}