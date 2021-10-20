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
public class InternalServerException extends ApplicationException {

	/**
	 *
	 */
	private static final long serialVersionUID = 5018979763055703549L;

	public InternalServerException(String errorMessage) {
		super(errorMessage, true);
	}

	/**
	 *
	 * @param applicationCode
	 * @param metadata
	 */
	public InternalServerException(String applicationCode, Metadata metadata) {
		super(applicationCode, metadata);
	}

	public InternalServerException(String applicationCode, Metadata metadata, String userMessages, List<FieldMessage> fieldMessages) {
		super(applicationCode, metadata, userMessages, fieldMessages);
	}

	public InternalServerException(String applicationCode, Metadata metadata, String userMessages) {
		super(applicationCode, metadata, userMessages, null);
	}

}

