package br.com.goin.feature_all_categories.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by appsimples on 8/9/17.
 */

public class CategoryDetails implements Serializable {
    public enum Type {
        event, place
    }

    public enum CategoryType {
        EVENT, THEATER, PLACE, MOVIE
    }

    @SerializedName("name")
    public String name;
    @SerializedName("id")
    public String id;
    @SerializedName("image")
    public String image;
    @SerializedName("imageIcon")
    public String imageIcon;
    @SerializedName("selected")
    public Boolean selected;
    @SerializedName("type")
    public Type type;
    @SerializedName("imageWhite")
    public String imageWhite;
    @SerializedName("categoryType")
    public CategoryType categoryType;
    @SerializedName("bannerCategory")
    public String bannerCategory;
    @SerializedName("actionId")
    public String actionId;

    public boolean isEvent(){
        return type != null && !Type.place.equals(type);
    }
}
