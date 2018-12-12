package com.timsimonhughes.popularmovies_p2.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    public static String formatDate(String date) {

        Locale current = Locale.getDefault();

        if (date != null) {

            @SuppressLint("SimpleDateFormat") SimpleDateFormat inputPattern = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat outputPattern = new SimpleDateFormat("dd-MM-yyyy");

            try {
                Date newDate = inputPattern.parse(date);
                date = outputPattern.format(newDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return date;
    }
}
