package com.wellsfargo.rarconsumer.exception;

import com.wellsfargo.rarconsumer.response.Metadata;

/**
 * @author
 */
public class CustomExceptionWarning extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private final String applicationCode;
    private final transient Metadata metadata;
    private final String warningMessage;
    private final transient Object[] data;

    /**
     * @param applicationCode
     * @param metadata
     * @param warningMessage
     * @param data
     */
    public CustomExceptionWarning(String applicationCode, Metadata metadata,
                                  String warningMessage, Object[] data) {
        super();
        this.applicationCode = applicationCode;
        this.metadata = metadata;
        this.warningMessage = warningMessage;
        this.data = data;
    }


    public String getApplicationCode() {
        return applicationCode;
    }


    public Metadata getMetadata() {
        return metadata;
    }


    public String getWarningMessage() {
        return warningMessage;
    }


    public Object[] getData() {
        return data;
    }


}

