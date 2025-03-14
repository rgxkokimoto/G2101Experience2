package com.example.g2int101experience;
import android.content.Context;
import android.content.SharedPreferences;

public class GuardarIdioma {

    private static final String PREF_NAME = "IdiomaPreferences";
    private static final String LANGUAGE_KEY = "language";

    // Guardar idioma
    public static void saveLanguage(Context context, String languageCode) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LANGUAGE_KEY, languageCode);
        editor.apply();
    }

    // Obtener idioma guardado
    public static String getSavedLanguage(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(LANGUAGE_KEY, "es"); // Default to Spanish
    }
}
