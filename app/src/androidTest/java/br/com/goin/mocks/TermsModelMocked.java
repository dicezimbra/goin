package br.com.goin.mocks;

public class TermsModelMocked {
    public static String halfPrice = "Meia Entrada";
    public static String userTerms = "Termos de Uso";
    public static String privacyPolicy = "Politica de Privacidade";
    public static String description = "Descrição da ";

    public static String termsMocked = "{" +
                                           "\"data\": " +
                                               "[" +
                                               "   {" +
                                               "      \"title\": \"" + halfPrice + "\"," +
                                               "      \"description\": \"" + description + halfPrice + "\"" +
                                               "   }," +
                                               "   {" +
                                               "      \"title\": \"" + userTerms + "\"," +
                                               "      \"description\": \"" + description + userTerms + "\"" +
                                               "   }," +
                                               "   {" +
                                               "      \"title\": \"" + privacyPolicy + "\"," +
                                               "      \"description\": \"" + description + privacyPolicy + "\"" +
                                               "   }" +
                                               "]" +
                                       "}";

    public static String emptyTerms= "{ \"data\": [] }";
}
