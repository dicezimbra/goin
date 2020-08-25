package br.com.legacy.utils;

import android.app.Activity;
import com.google.android.material.snackbar.Snackbar;
import android.view.View;

import br.com.R;

public class SnackBarUtils {
    public static void showSnackBar(Activity activity, View parentLayout, String message) {
        Snackbar snackbar = Snackbar.make(parentLayout, message, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary));
        snackbar.show();
    }
}