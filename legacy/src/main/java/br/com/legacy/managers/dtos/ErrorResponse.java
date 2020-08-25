package br.com.legacy.managers.dtos;

/**
 * Created by ruderos on 8/18/17.
 */

public class ErrorResponse {
    public int statusCode;
    public String error;
    public String message;

    public ErrorResponse() {}

    public ErrorResponse(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
