package com.timsimonhughes.popularmovies_p2.utils;

public class LanguageUtils {

    //TODO Use @GET request for the language config to replace the hard-coded abbreviations below.
    private static final String LAN_ENGLISH_ABBREVIATION = "en";
    private static final String LAN_JAPANESE_ABBREVIATION = "ja";
    private static final String LAN_FRENCH_ABBREVIATION = "fr";
    private static final String LAN_GERMAN_ABBREVIATION = "gr";
    private static final String LAN_SPANISH_ABBREVIATION = "es";
    private static final String LAN_ITALIAN_ABBREVIATION = "it";
    private static final String LAN_TAMIL_ABBREVIATION = "ta";

    private static final String LAN_ENGLISH = "English";
    private static final String LAN_JAPANESE = "Japanese";
    private static final String LAN_FRENCH = "French";
    private static final String LAN_GERMAN = "German";
    private static final String LAN_SPANISH = "Spanish";
    private static final String LAN_ITALIAN = "Italian";
    private static final String LAN_TAMIL = "Tamil";

    public static String formatLanguageAbbreviation(String languageAbbreviation) {

        switch (languageAbbreviation) {
            case LAN_ENGLISH_ABBREVIATION:
                languageAbbreviation = LAN_ENGLISH;
                break;
            case LAN_JAPANESE_ABBREVIATION:
                languageAbbreviation = LAN_JAPANESE;
                break;
            case LAN_FRENCH_ABBREVIATION:
                languageAbbreviation = LAN_FRENCH;
                break;
            case LAN_GERMAN_ABBREVIATION:
                languageAbbreviation = LAN_GERMAN;
                break;
            case LAN_SPANISH_ABBREVIATION:
                languageAbbreviation = LAN_SPANISH;
                break;
            case LAN_ITALIAN_ABBREVIATION:
                languageAbbreviation = LAN_ITALIAN;
                break;
            case LAN_TAMIL_ABBREVIATION:
                languageAbbreviation = LAN_TAMIL;
                break;
        }

        return languageAbbreviation;
    }

}
