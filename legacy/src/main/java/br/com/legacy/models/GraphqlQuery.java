package br.com.legacy.models;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by appsimples on 9/15/17.
 */

public class GraphqlQuery {

    @SerializedName("query")
    String query;

    @SerializedName("variables")
    Map<String, Object> variables;

    public GraphqlQuery(String query, Map<String, Object> variables) {
        this.query = query;
        this.variables = variables;
    }

    public static Builder builder(String query) {
        return new Builder(query);
    }

    public static final class Builder {

        private Map<String, Object> variables;
        private String query;

        public Builder(String query) {
            this.query = query;
        }

        public Builder var(String key, Object value) {
            if (variables == null) {
                variables = new HashMap<>();
            }
            variables.put(key, value);
            return this;
        }

        public GraphqlQuery build() {
            return new GraphqlQuery(this.query, this.variables);
        }
    }
}
