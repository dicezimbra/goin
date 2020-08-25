package br.com.goin.component.model.category

object CategoriesQueries {

    var GET_CATEGORIES = "query GetCategories {\n" +
            "  categories {\n" +
            "    id\n" +
            "    name\n" +
            "    image\n" +
            "    imageIcon\n" +
            "    type\n" +
            "    categoryType\n" +
            "    imageWhite\n" +
            "    bannerCategory\n" +
            "    actionId\n" +
            "  }\n" +
            "}"
}