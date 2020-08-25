package br.com.goin.feature.search.category.model.api

object SearchFilterQuery {
    val FILTER_RESULTS = "query searchFilter(\$input: SearchFilterInput!, \$paginate: Int!, \$limit: Int!) {\n" +
            "   searchFilter(input: \$input, paginate: \$paginate, limit: \$limit) {\n" +
            "       currentPage\n" +
            "       totalPages\n" +
            "       totalItems\n" +
            "       events {\n" +
            "           id\n" +
            "           categoryType\n" +
            "           name\n" +
            "           subcategories\n" +
            "           subcategoriesInfo{\n" +
            "               name\n" +
            "                       }\n" +
            "           image\n" +
            "           city\n" +
            "           placeAddress\n" +
            "           latitude\n" +
            "           longitude\n" +
            "           lowestPrice\n" +
            "           rating\n" +
            "           state\n" +
            "           startDate\n" +
            "       }\n" +
            "       clubs {\n" +
            "           id\n" +
            "           name\n" +
            "           city\n" +
            "           address\n" +
            "           latitude\n" +
            "           subcategories\n" +
            "           subcategoriesInfo{\n" +
            "               name\n" +
            "                       }\n" +
            "           longitude\n" +
            "           logoImage\n" +
            "    coverImage\n" +
            "           rating\n" +
            "           priceRating\n" +
            "           state\n" +
            "       }\n" +
            "      }\n" +
            "   }"

    val GET_SUBCATEGORIES = "query subcategories(\$categoryId: ID!) {\n" +
            "  subcategories (categoryId: \$categoryId){\n" +
            "    subcategoryId\n" +
            "    categoryId\n" +
            "    name\n" +
            "    image\n" +
            "  }\n" +
            "}"

}