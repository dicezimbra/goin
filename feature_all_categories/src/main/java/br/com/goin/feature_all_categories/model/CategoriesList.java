package br.com.goin.feature_all_categories.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Eric on 1/7/19.
 */

public class CategoriesList {
    @SerializedName("categories")
    public List<CategoryDetails> categories;
}
