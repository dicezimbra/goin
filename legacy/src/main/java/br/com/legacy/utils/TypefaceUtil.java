package br.com.legacy.utils;

import android.content.Context;
import android.graphics.Typeface;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class TypefaceUtil {

    private static String Bold = "fonts/Quicksand-Bold.ttf";
    private static String Light = "fonts/Quicksand-Light.ttf";
    private static String Medium = "fonts/Quicksand-Medium.ttf";
    private static String Regular = "fonts/Quicksand-Regular.ttf";
    private static Typeface faceBold;
    private static Typeface faceLight;
    private static Typeface faceMedium;
    private static Typeface faceRegular;


    public static void initilize(Context context) {
        if (faceBold == null)
            faceBold = Typeface.createFromAsset(context.getAssets(), Bold);
        if (faceLight == null)
            faceLight = Typeface.createFromAsset(context.getAssets(), Light);
        if (faceMedium == null)
            faceMedium = Typeface.createFromAsset(context.getAssets(), Medium);
        if (faceRegular == null)
            faceRegular = Typeface.createFromAsset(context.getAssets(), Regular);
    }


    public static void boldFont(View... view) {
        for (View view1 : view) {
            setFont(view1, faceBold);
        }
    }

    public static void mediumFont(View... view) {
        for (View view1 : view) {
            setFont(view1, faceMedium);
        }
    }

    public static void lightFont(View... view) {
        for (View view1 : view) {
            setFont(view1, faceLight);
        }
    }

    public static void regularFont(View... view) {
        for (View view1 : view) {
            setFont(view1, faceRegular);
        }
    }

    private static void setFont(View view, Typeface font) {
        if (view instanceof Button)
            ((Button) view).setTypeface(font);
        else if (view instanceof EditText)
            ((EditText) view).setTypeface(font);
        else if (view instanceof TextView)
            ((TextView) view).setTypeface(font);
        else if (view instanceof TextInputLayout)
            ((TextInputLayout) view).setTypeface(font);
        else if (view instanceof androidx.appcompat.widget.AppCompatRadioButton)
            ((androidx.appcompat.widget.AppCompatRadioButton) view).setTypeface(font);
    }

    /**
     *
     * @param context
     * @param tabLayout
     *
     * Changing the style of tab layout
     *
     */
    public static void changeTabsFontMedium(Context context, TabLayout tabLayout) {

        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(Typeface.createFromAsset(context.getAssets(), Medium));
                }
            }
        }
    }

    public static void changeTabsFontRegular(Context context, TabLayout tabLayout) {

        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(Typeface.createFromAsset(context.getAssets(), Regular));
                }
            }
        }
    }




}
