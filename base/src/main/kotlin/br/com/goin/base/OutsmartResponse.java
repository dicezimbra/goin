package br.com.goin.base;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ruderos on 8/18/17.
 */

public class OutsmartResponse<T> {

    String status;
    String message;
    String name;
    T data;

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
        public String message;
        public String name;
        public List<JSONObject> locations;
        public List<String> path;
    }

}
