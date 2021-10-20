package com.wellsfargo.rarconsumer.response;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 *
 */
public class ApiErrorMessage {

	@JsonProperty(value = "user_error_message")
	private UserErrorMessage userErrorMessage;
	@JsonProperty(value = "system_error_message")
	private SystemErrorMessage systemErrorMessage;

	public ApiErrorMessage() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param systemErrorMessage
	 * @param userErrorMessage
	 */
	public ApiErrorMessage(SystemErrorMessage systemErrorMessage, UserErrorMessage userErrorMessage) {
		this(systemErrorMessage, userErrorMessage, null);
	}

	/**
	 * @param systemErrorMessage
	 * @param userErrorMessage
	 * @param errors
	 */
	public ApiErrorMessage(SystemErrorMessage systemErrorMessage, UserErrorMessage userErrorMessage,
                           List<String> errors) {
		this.systemErrorMessage = systemErrorMessage;
		this.userErrorMessage = userErrorMessage;

	}

	public ApiErrorMessage(SystemErrorMessage systemErrorMessage, UserErrorMessage userErrorMessage,
                           List<String> errors, List<String> userMessages) {
		this.systemErrorMessage = systemErrorMessage;
		this.userErrorMessage = userErrorMessage;

	}

	public SystemErrorMessage getSystemErrorMessage() {
		return systemErrorMessage;
	}

	public UserErrorMessage getUserErrorMessage() {
		return userErrorMessage;
	}

}

