package br.com.goin.feature.search.event.model.api

object SearchQueries {

    var SEARCH_EVENT = "query searchByExpression (\$expression: String!) {" +
            "   searchByExpression (expression: \$expression) {\n" +
            "   id\n" +
            "   name\n" +
            "   categorization\n" +
            "   description\n" +
            "   logoImage\n" +
            "   city\n" +
            "   state\n" +
            "   rating\n" +
            "   placeName\n" +
            "   startDate\n" +
            "   categoryType\n" +
            "   latitude\n" +
            "   longitude\n" +
            "}\n" +
            "}"


    var SEARCH_BY_NAME = "query GetEventByName(\$name: String!, \$categoryId: String, \$paginate: Int, \$limit: Int) {\n" +
            "   getEventByName(name: \$name, categoryId: \$categoryId, paginate: \$paginate, limit: \$limit) {" +
            "    id\n" +
            "    name\n" +
            "    description\n" +
            "    categorization\n" +
            "    logoImage\n" +
            "    city\n" +
            "    state\n" +
            "    rating\n" +
            "    placeName\n" +
            "    categoryType\n" +
            "    startDate\n" +
            "    score\n" +
            "    }" +
            "}"
}
