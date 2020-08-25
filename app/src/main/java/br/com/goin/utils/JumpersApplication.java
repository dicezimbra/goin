package br.com.goin.utils;


import androidx.multidex.MultiDexApplication;
import androidx.emoji.text.EmojiCompat;
import androidx.emoji.bundled.BundledEmojiCompatConfig;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import br.com.goin.AppNavigationController;
import br.com.goin.BuildConfig;

import io.branch.referral.Branch;
import io.fabric.sdk.android.Fabric;

public class JumpersApplication extends MultiDexApplication {

    public static JumpersApplication application;

    public void onCreate(){
        super.onCreate();
        application = this;

        //Init navegation between modules
        AppNavigationController.Companion.init();
        AppEventsLogger.activateApp(this);

        // Initialize the Branch object
        Branch.getAutoInstance(this);


        Crashlytics crashlyticsKit = new Crashlytics.Builder()
                .core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
                .build();

        Fabric.with(this, crashlyticsKit);
        EmojiCompat.init(new BundledEmojiCompatConfig(getApplicationContext()));
    }

}
