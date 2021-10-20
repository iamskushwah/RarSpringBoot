package com.wellsfargo.rarconsumer.exception;

import com.wellsfargo.rarconsumer.response.Metadata;

/**
 * @author
 */
public class TooManyRequestException extends ApplicationException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * @param applicationCode
     * @param metadata
     */
    public TooManyRequestException(String applicationCode, Metadata metadata) {
        super(applicationCode, metadata);
    }

}

