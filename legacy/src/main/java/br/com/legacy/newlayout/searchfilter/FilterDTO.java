package br.com.legacy.newlayout.searchfilter;

import java.io.Serializable;

public class FilterDTO implements Serializable {

    private String categoryId;
    private String subcategoryId;
    private String query;
    private String categoryName;
    private Long startCalendar;
    private Long endCalendar;
    private String city;
    private String priceRange;
    private String state;

    public boolean hasFilter() {
        return subcategoryId != null || query != null
                || startCalendar != null
                || endCalendar != null
                || city != null
                || state != null;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getSubcategoryId() {
        return subcategoryId;
    }

    public void setSubcategoryId(String subcategoryId) {
        this.subcategoryId = subcategoryId;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getStartCalendar() {
        return startCalendar;
    }

    public void setStartCalendar(Long startCalendar) {
        this.startCalendar = startCalendar;
    }

    public Long getEndCalendar() {
        return endCalendar;
    }

    public void setEndCalendar(Long endCalendar) {
        this.endCalendar = endCalendar;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public float getPriceRange() {
        try {
            if (priceRange != null) {
                return Float.valueOf(priceRange.replace(",", "."));
            }
        } catch (Exception e) {

        }
        return 0.0f;
    }

    public void setPriceRange(String priceRange) {
        this.priceRange = priceRange;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
