package br.com.legacy.managers.dtos;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ruderos on 8/18/17.
 */

public class OutsmartResponse<T> {

    @SerializedName("status") String status;
    @SerializedName("message") String message;
    @SerializedName("name") String name;
    @SerializedName("data") T data;

    @SerializedName("errors")
    public List<GraphqlErrors> errors = new ArrayList<>();

    public String getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public class GraphqlErrors {
        @SerializedName("message") public String message;
        @SerializedName("name")public String name;
        @SerializedName("locations")public List<JSONObject> locations;
        @SerializedName("path")public List<String> path;
    }

}
