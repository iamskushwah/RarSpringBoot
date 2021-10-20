package com.wellsfargo.rarconsumer.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ResponseApiDTO<T> implements Serializable {
	private static final long serialVersionUID = 6303563343644453315L;

	private Metadata metadata;
	private List<T> data;
	// private ApiErrorDTO error;
	@JsonProperty(value = "error")
	private ApiErrorMessage error;

	/**
	 * default constructor
	 **/
	public ResponseApiDTO() {
		super();
	}

	public ResponseApiDTO(ApiErrorMessage apiErrorMessage, Metadata metadata) {
		super();
		this.error = apiErrorMessage;
		this.metadata = metadata;
	}

	/**
	 * @param metadata
	 * @param data
	 */
	public ResponseApiDTO(Metadata metadata, List<T> data) {
		this.metadata = metadata;
		this.data = data;
	}

	/**
	 * @param metadata
	 * @param data
	 */
	public ResponseApiDTO(Metadata metadata, T data) {
		List<T> dataList = new ArrayList<T>();
		dataList.add(data);

		this.metadata = metadata;
		this.data = dataList;
	}

	/**
	 * @param data
	 * @param version    - the API version of the data included
	 * @param httpStatus - {@link org.springframework.http.HttpStatus HttpStatus}
	 *                   object that represents the status of the request
	 */
	public ResponseApiDTO(List<T> data, String version, HttpStatus httpStatus, String message) {
		if (data != null) {
			this.data = data;
			this.metadata = new Metadata(version, httpStatus.value(), data.size(), message);
		} else {
			this.metadata = new Metadata(version, httpStatus.value(), 0, message);
			// this.metadata = new Metadata(version,httpStatus.toString(), 0);

		}

	}

	/**
	 * @param data
	 * @param version    - the API version of the data included
	 * @param httpStatus - {@link org.springframework.http.HttpStatus HttpStatus}
	 *                   object that represents the status of the request
	 */
	public ResponseApiDTO(T data, String version, HttpStatus httpStatus, String message) {
		List<T> dataList = new ArrayList<T>();
		if (data != null) {
			dataList.add(data);
		}

		this.data = dataList;
		this.metadata = new Metadata(version, httpStatus.value(), dataList.size(), message);

	}

	public Metadata getMetadata() {
		return metadata;
	}

	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public ApiErrorMessage getError() {
		return error;
	}

	public void setError(ApiErrorMessage error) {
		this.error = error;
	}
}

