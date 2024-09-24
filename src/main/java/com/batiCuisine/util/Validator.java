package com.batiCuisine.util;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Validator {
    public Date convertToDate(String dateInput) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            java.util.Date parsed = format.parse(dateInput);
            return new Date(parsed.getTime());
        } catch (ParseException e) {
            throw new RuntimeException("Erreur de format de date, veuillez utiliser jj/mm/aaaa");
        }
    }
}
