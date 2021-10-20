package com.wellsfargo.rarconsumer.response;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author
 */
public class UserErrorMessage {

	@JsonProperty(value = "user_message_fields")
	private List<FieldMessage> fieldMessages;

	@JsonProperty(value = "user_message")
	private String userMessage;


	public UserErrorMessage() {
		super();
		// TODO Auto-generated constructor stub
	}


	/**
	 * @param userMessage
	 * @param fieldMessages
	 */
	public UserErrorMessage(String userMessage, List<FieldMessage> fieldMessages) {

		this.userMessage = userMessage;
		this.fieldMessages = fieldMessages;
	}


	public String getUserMessage() {
		return userMessage;
	}

	public List<FieldMessage> getFieldMessages() {
		return fieldMessages;
	}

	public void setFieldMessages(List<FieldMessage> fieldMessage) {
		this.fieldMessages = fieldMessage;
	}


}

