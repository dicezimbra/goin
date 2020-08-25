package br.com.legacy.managers.Iugu;

public class IuguPaymentTokenResponse {
    public String code;
    public String id;
    public String method;
    public ExtraInfo extra_info;

    public class ExtraInfo {
        public String holder_name;
        public String display_number;
    }
}
