package br.com.goin.component.analytics.adjust;

public interface TagManager {
    String TOKEN_REGISTER = "gfa299";
    String TOKEN_WELCOME = "76p853";
    void trackAdjustEvent(String token);
}
