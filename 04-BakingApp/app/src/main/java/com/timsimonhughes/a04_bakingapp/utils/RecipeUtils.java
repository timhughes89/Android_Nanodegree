package com.timsimonhughes.a04_bakingapp.utils;

public class RecipeUtils {

    private static final String KEY_MEASUREMENT_CUP = "CUP";
    private static final String KEY_MEASUREMENT_TSP = "TSP";
    private static final String KEY_MEASUREMENT_TBLSP = "TBLSP";
    private static final String KEY_MEASUREMENT_K = "K";
    private static final String KEY_MEASUREMENT_G = "G";
    private static final String KEY_MEASUREMENT_OZ = "OZ";
    private static final String KEY_MEASUREMENT_UNIT = "UNIT";

    public static String formatMeasurement(String measurementValue) {

        String formattedMeasurementString = null;

        switch (measurementValue) {
            case KEY_MEASUREMENT_CUP:
                formattedMeasurementString = "Cup";
                break;
            case KEY_MEASUREMENT_TSP:
                formattedMeasurementString = "Tsp";
                break;
            case KEY_MEASUREMENT_TBLSP:
                formattedMeasurementString = "Tblsp";
                break;
            case KEY_MEASUREMENT_K:
                formattedMeasurementString = "Kg";
                break;
            case KEY_MEASUREMENT_G:
                formattedMeasurementString = "G";
                break;
            case KEY_MEASUREMENT_OZ:
                formattedMeasurementString = "Oz";
                break;
            case KEY_MEASUREMENT_UNIT:
                formattedMeasurementString = "";
                break;
        }
        return formattedMeasurementString;
    }

    public static String formatQuantity(String quantityMeasure) {

        String formattedQuantityString = null;

        if (quantityMeasure.contains(".0")) {
            formattedQuantityString = quantityMeasure.replace(".0", "");
        } else {
            formattedQuantityString = quantityMeasure;
        }

        return formattedQuantityString;
    }
}
