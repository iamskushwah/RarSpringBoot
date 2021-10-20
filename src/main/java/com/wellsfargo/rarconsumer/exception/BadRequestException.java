package com.wellsfargo.rarconsumer.exception;

/**
 *
 */


import com.wellsfargo.rarconsumer.response.FieldMessage;
import com.wellsfargo.rarconsumer.response.Metadata;

import java.util.List;


/**
 * @author
 *
 */
public class BadRequestException extends ApplicationException {

    private static final long serialVersionUID = -9215466226316062049L;

    /**
     *
     * @param errorMessage
     *
     */
    public BadRequestException(String errorMessage) {
        super(errorMessage, true);
    }

    /**
     *
     * @param applicationCode
     * @param metadata
     */
    public BadRequestException(String applicationCode, Metadata metadata) {
        super(applicationCode, metadata);
    }

    public BadRequestException(String applicationCode, Metadata metadata, String userMessages, List<FieldMessage> fieldMessages) {
        super(applicationCode, metadata, userMessages, fieldMessages);
    }

    public BadRequestException(String applicationCode, Metadata metadata, String userMessages) {
        super(applicationCode, metadata, userMessages, null);
    }
}

