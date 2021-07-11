package com.example.cartrader.helpers;

public class AppDataHelper {

    public static int convertFromKWtoHP(int enginePowerKW) {
        final float CONSTANT_VALUE = 0.73549875f;
        return Math.round(enginePowerKW / CONSTANT_VALUE);
    }

    public static String convertDateFormat(String dateInput) {
        String cleanedDate = dateInput.replace("-", "");
        String year = cleanedDate.substring(0, 4);
        String month = cleanedDate.substring(4, 6);
        String day = cleanedDate.substring(6, 8);
        return String.format("%s.%s.%s.", day, month, year);
    }

}
