package br.com.legacy.managers.Iugu;

public class IuguParameterModel {
    public String account_id;
    public String method;
    public DataObject data;
    public boolean test;

    public static class DataObject {
        public String number;
        public String verification_value;
        public String first_name;
        public String last_name;
        public String month;
        public String year;
    }
}
