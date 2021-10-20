package com.wellsfargo.rarconsumer.exception;

/**
 *
 */


/**
 * @author
 *
 */
public enum RestErrorsEnum {

    /*Happy Path*/
    SUCCESS("200", "", "Success"),

    /*Client had issues*/
    BAD_REQUEST("400", "This is Bad Request", "Bad Request"),
    AUTHENTICATION_REQUIRED("401", "Not Valid User", "Authentication Required"),
    FORBIDDEN("403", "This is Forbidden", "Forbidden"),
    NOT_FOUND("404", "Request resource not found", "Not Found"),
    TOO_MANY_REQUESTS("429", "Too Many Requests, Wait", "Too Many Requests"),


    /*Server had issues*/
    INTERNAL_SERVER_ERROR("500", "System Error, Contact Adminstrator", "Internal Server Error");

    private final String httpStatusCode;
    private final String userMessage;
    private final String systemMessage;

    RestErrorsEnum(String httpStatusCode, String userMessage, String systemMessage) {
        this.httpStatusCode = httpStatusCode;
        this.userMessage = userMessage;
        this.systemMessage = systemMessage;
    }

    public String getHttpStatusCode() {
        return httpStatusCode;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public String getSystemMessage() {
        return systemMessage;
    }
}

