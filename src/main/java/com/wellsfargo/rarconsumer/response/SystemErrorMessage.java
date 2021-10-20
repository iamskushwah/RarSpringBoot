package com.wellsfargo.rarconsumer.response;


import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author
 */
public class SystemErrorMessage {

    @JsonProperty(value = "message")
    private String message;

    @JsonProperty(value = "error_code")
    private String errorCode;


    public SystemErrorMessage() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     * @param errorCode
     */
    public SystemErrorMessage(String message, String errorCode) {
        this.message = message;
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public String getErrorCode() {
        return errorCode;
    }

}

