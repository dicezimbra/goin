package br.com.legacy.managers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import br.com.goin.component.session.user.UserModel;
import br.com.goin.component.session.user.UserSessionInteractor;
import br.com.legacy.utils.LongVersionSignature;

import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by appsimples on 9/4/17.
 */

public class SessionManager {

    private static final SessionManager ourInstance = new SessionManager();

    public static SessionManager getInstance() {
        return ourInstance;
    }

    public String getCurrentUserId(Context activity) {
        return UserSessionInteractor.Companion.getInstance().fetchUser().getId();
    }


    public UserModel getCurrentUser(Context activity) {
        return UserSessionInteractor.Companion.getInstance().fetchUser();
    }

    public UserModel getCurrentUser() {
        return UserSessionInteractor.Companion.getInstance().fetchUser();
    }

    public void setCurrentUser(UserModel user, Activity activity) {
        UserSessionInteractor.Companion.getInstance().save(user);
    }

    HashMap<ImageType, LongVersionSignature> signatures = new HashMap<>();

    public enum ImageType {
        MyProfile,
    }

    public void forceRenewSignature(Activity activity, ImageType type) {
        SharedPreferences preferences = activity.getPreferences(Context.MODE_PRIVATE);
        Long newSignature = Calendar.getInstance().getTimeInMillis();
        preferences.edit().putLong(type.name(), newSignature).apply();
        signatures.put(type, new LongVersionSignature(newSignature));
    }

    public LongVersionSignature getSignature(Activity activity, ImageType type) {
        LongVersionSignature signature = signatures.get(type);
        if (signature != null) return signature;

        Long timestamp = activity.getPreferences(Context.MODE_PRIVATE)
                .getLong(type.name(), 0l);

        LongVersionSignature longVersionSignature;
        if (timestamp > 0) {
            longVersionSignature = new LongVersionSignature(timestamp);
        } else {
            longVersionSignature = new LongVersionSignature(Calendar.getInstance().getTimeInMillis());
        }

        signatures.put(type, longVersionSignature);
        return longVersionSignature;
    }

}
