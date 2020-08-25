package br.com.legacy.models;

import java.io.Serializable;

public class SearchModel implements Serializable {

    public String name;
    public String id;
    public String type;
    public String image;
    public Float rating;

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public SearchModel(String name, String id, String type, String image, Float rating) {
        this.name = name;
        this.rating = rating;
        this.id = id;
        this.type = type;
        this.image = image;
    }

    public SearchModel(String name, String id, String type, String image) {
        this.name = name;
        this.id = id;
        this.type = type;
        this.image = image;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
