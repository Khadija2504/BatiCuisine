package com.batiCuisine.util;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Validator {

    public static boolean isValidProjectName(String projectName) {
        if (projectName == null || projectName.trim().isEmpty()) {
            return false;
        }
        return projectName.matches("^[a-zA-Z0-9 ]{3,}$");
    }

    public static boolean isValidSurface(double surface) {
        return surface > 0;
    }

    public static boolean isValidMaterialName(String materialName) {
        if (materialName == null || materialName.trim().isEmpty()) {
            return false;
        }
        return materialName.matches("^[a-zA-Z0-9 ]{2,}$");
    }

    public static boolean isValidQuantity(double quantity) {
        return quantity > 0;
    }

    public static boolean isValidCost(double cost) {
        return cost >= 0;
    }

    public static boolean isValidQualityCoefficient(double coefficientQualite) {
        return coefficientQualite >= 1.0;
    }

    public static boolean isValidLaborType(String laborType) {
        if (laborType == null || laborType.trim().isEmpty()) {
            return false;
        }
        return laborType.matches("^[a-zA-Z ]{2,}$");
    }

    public static boolean isValidHours(double hours) {
        return hours > 0;
    }

    public static boolean isValidProductivityFactor(double facteurProductivite) {
        return facteurProductivite >= 1.0;
    }

    public static boolean isValidPercentage(double percentage) {
        return percentage >= 0 && percentage <= 100;
    }

    public static boolean isValidString(String str, int minLength) {
        if (str == null || str.trim().isEmpty()) {
            return false;
        }
        return str.matches("^[a-zA-Z0-9 ]{" + minLength + ",}$");
    }

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
