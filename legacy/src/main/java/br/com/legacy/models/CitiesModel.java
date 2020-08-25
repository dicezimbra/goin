package br.com.legacy.models;

public class CitiesModel {

    public String  city;

    public CitiesModel(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return city;
    }

}
