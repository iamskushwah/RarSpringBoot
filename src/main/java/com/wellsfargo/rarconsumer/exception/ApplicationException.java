package com.wellsfargo.rarconsumer.exception;


import com.wellsfargo.rarconsumer.response.FieldMessage;
import com.wellsfargo.rarconsumer.response.Metadata;

import java.util.List;


/**
 * @param
 * @author
 */
public class ApplicationException extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private String applicationCode = null;
	private transient Metadata metadata = null;
	private String userMessage = null;
	private List<FieldMessage> fieldMessages;
	private String systemErrorMessage;

	public ApplicationException(String errorMessage, boolean isUserMessage) {
		super();
		if(isUserMessage) {
			this.userMessage = errorMessage;
		} else {
			this.systemErrorMessage = errorMessage;
		}
	}

	/**
	 * @param applicationCode
	 * @param metadata
	 */
	public ApplicationException(String applicationCode, Metadata metadata) {
		super();
		this.applicationCode = applicationCode;
		this.metadata = metadata;
		this.userMessage = null;
	}

	public ApplicationException(String applicationCode, Metadata metadata, String userMessage, List<FieldMessage> fieldMessages) {
		super();
		this.applicationCode = applicationCode;
		this.metadata = metadata;
		this.userMessage = userMessage;
		this.fieldMessages = fieldMessages;
	}

	public String getApplicationCode() {
		return applicationCode;
	}

	public Metadata getMetadata() {
		return metadata;
	}

	public List<FieldMessage> getFieldMessages() {
		return fieldMessages;
	}

	public void setFieldMessages(List<FieldMessage> fieldMessages) {
		this.fieldMessages = fieldMessages;
	}

	public String getUserMessage() {
		return userMessage;
	}

	public String getSystemErrorMessage() {
		return systemErrorMessage;
	}

	public void setSystemErrorMessage(String systemErrorMessage) {
		this.systemErrorMessage = systemErrorMessage;
	}


}

