package com.example.toytrader;

import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class SnackbarHelper {
    private final static String TAG = "FIREBASE_HELPER";
    private static SnackbarHelper snackbarHelper = null;

    private SnackbarHelper() {}

    public static void showMessage(String msg, View v) {
        Snackbar snackbar = Snackbar.make(v, msg, Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}
