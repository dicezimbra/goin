package br.com.legacy.managers.dtos;

import java.io.Serializable;

public class SearchFilterInput implements Serializable {

    public String categoryId;
    public String subCategoriesId;
    public Long fromDate;
    public Long toDate;
    public String state;
    public String city;
    public String lowestPrice;
    public String highestPrice;

    public String to() {
        return categoryId + subCategoriesId + fromDate + toDate + state + city + lowestPrice + highestPrice;
    }
}
