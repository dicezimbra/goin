package br.com.legacy.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import br.com.goin.base.BaseApplication;
import br.com.goin.component.session.user.UserModel;
import br.com.legacy.managers.dtos.Club;
import br.com.legacy.managers.dtos.Feed;
import br.com.legacy.managers.dtos.Notification;
import br.com.legacy.managers.dtos.Post;
import br.com.legacy.managers.dtos.QueryReceiver;
import br.com.legacy.managers.dtos.QueryResponse;
import br.com.legacy.managers.dtos.SearchFilterResponse;
import br.com.legacy.managers.dtos.SubcategoriesList;
import br.com.legacy.managers.dtos.TicketDetails;
import br.com.legacy.managers.dtos.UserCardDetails;

import java.util.HashMap;
import java.util.List;

public class SharedPreferencesControl {

    SharedPreferences mPrefs;
    SharedPreferences.Editor prefsEditor;

    public SharedPreferencesControl(Context activity, String name) {
        this.mPrefs= activity.getSharedPreferences("goin_preference", Context.MODE_PRIVATE);
        prefsEditor = mPrefs.edit();
    }

    public void saveCache(String key, Object data) {
        Gson gson = new GsonBuilder().create();
        String listString = gson.toJson(data);
        prefsEditor.putString(key, listString);
        prefsEditor.apply();

    }

    public boolean sameOfCache(String key, Object data){
        Gson gson = new GsonBuilder().create();
        String fromServer = gson.toJson(data);
        String fromCache = mPrefs.getString(key, null);
        return fromServer.equals(fromCache);
    }

    public HashMap<String,List<UserCardDetails>> saveNotifications(String key) {
        String listItens = mPrefs.getString(key, null);
        Gson gson = new GsonBuilder().create();
        return  gson.fromJson(listItens, new TypeToken<HashMap<String,List<UserCardDetails>>>(){}.getType());
    }

    public HashMap<String,List<UserCardDetails>> loadNotifications(String key) {
        String listItens = mPrefs.getString(key, null);
        Gson gson = new GsonBuilder().create();
        return  gson.fromJson(listItens, new TypeToken<HashMap<String,List<UserCardDetails>>>(){}.getType());
    }

    public static HashMap<String,List<UserCardDetails>> loadFollow(String key) {
        String listItens = BaseApplication.Companion.getInstance().getContext().getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE).getString(key, null);
        Gson gson = new GsonBuilder().create();
        return  gson.fromJson(listItens, new TypeToken<HashMap<String,List<UserCardDetails>>>(){}.getType());
    }

    public QueryResponse loadCache(String key) {

        String listItens = mPrefs.getString(key, null);
        Gson gson = new GsonBuilder().create();
        return  gson.fromJson(listItens, QueryResponse.class);
    }

    public QueryReceiver loadCacheQueryReceiver(String key) {
        String listItens = mPrefs.getString(key, null);
        Gson gson = new GsonBuilder().create();
        return  gson.fromJson(listItens, QueryReceiver.class);
    }

    public HashMap<String, Feed> loadCacheFeed(String key) {
        String listItens = mPrefs.getString(key, null);
        Gson gson = new GsonBuilder().create();
        return  gson.fromJson(listItens, new TypeToken<HashMap<String, Feed>>(){}.getType());
    }

    public HashMap<String, List<Post>> loadCachePost(String key) {
        String listItens = mPrefs.getString(key, null);
        Gson gson = new GsonBuilder().create();
        return  gson.fromJson(listItens, new TypeToken<HashMap<String, List<Post>>>(){}.getType());
    }

    public UserModel loadCacheProfile(String key) {
        String listItens = mPrefs.getString(key, null);
        Gson gson = new GsonBuilder().create();
        return  gson.fromJson(listItens, UserModel.class);
    }

    public static UserModel loadProfileCached(String key) {
        SharedPreferences sharedPreferences = BaseApplication.Companion.getInstance().getContext().getSharedPreferences("goin_preference", Context.MODE_PRIVATE);
        String listItems = sharedPreferences.getString(key, null);
        Gson gson = new GsonBuilder().create();
        return  gson.fromJson(listItems, UserModel.class);
    }

    public SearchFilterResponse loadFilter(String key) {
        String listItens = mPrefs.getString(key, null);
        Gson gson = new GsonBuilder().create();
        return  gson.fromJson(listItens, SearchFilterResponse.class);
    }

    public Club loadCacheClubs(String key) {
        String listItens = mPrefs.getString(key, null);
        Gson gson = new GsonBuilder().create();
        return  gson.fromJson(listItens, Club.class);
    }

    public HashMap<String,List<Notification>> getNotifications(String key) {
        String listItens = mPrefs.getString(key, null);
        Gson gson = new GsonBuilder().create();
        return  gson.fromJson(listItens, new TypeToken<HashMap<String,List<Notification>>>(){}.getType());

    }

    public SubcategoriesList getSubcategories(String key) {
        String listItens = mPrefs.getString(key, null);
        Gson gson = new GsonBuilder().create();
        return  gson.fromJson(listItens, SubcategoriesList.class);
    }

    public HashMap<String,List<TicketDetails>> getEventTickets(String key) {
        String listItens = mPrefs.getString(key, null);
        Gson gson = new GsonBuilder().create();
        return  gson.fromJson(listItens, new TypeToken<HashMap<String,List<TicketDetails>>>(){}.getType());
    }

    public HashMap<String, List<TicketDetails>> getMyIndividualTickets(String key) {
        String listItens = mPrefs.getString(key, "");
        Gson gson = new GsonBuilder().create();
        return  gson.fromJson(listItens, new TypeToken<HashMap<String,List<TicketDetails>>>(){}.getType());
    }

    public void saveSimpleString(String key, String value) {
        prefsEditor.putString(key, value);
        prefsEditor.apply();
    }

    public String loadSimpleString(String key) {

        String value = mPrefs.getString(key,null);
        return value;
    }
}
