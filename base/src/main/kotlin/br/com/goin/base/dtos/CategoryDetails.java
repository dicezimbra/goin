package br.com.goin.base.dtos;

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

    public String name;
    public String id;
    public String image;
    public String imageIcon;
    public Boolean selected;
    public Type type;
    public String imageWhite;
    public CategoryType categoryType;
    public String bannerCategory;
    public String actionId;

    public boolean isEvent(){
        return type != null && !Type.place.equals(type);
    }
}
