package com.wellsfargo.rarconsumer.response;

/**
 *
 */


import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * @author
 *
 */
public class Metadata implements Serializable {

    private String version;
    private String status;
    @JsonProperty(value = "http_status")
    private Integer httpStatus;
    @JsonProperty(value = "result_count")
    private Integer resultCount;

    @JsonProperty(value = "message")
    private String message;


    public Metadata() {
    }

    public Metadata(String version, Integer httpStatus, Integer resultCount) {
        this(version, httpStatus, resultCount, null);
    }

    public Metadata(String version, Integer httpStatus, Integer resultCount, String message) {
        this.version = version;
        this.httpStatus = httpStatus;
        this.resultCount = resultCount;
        this.message = message;
    }

    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the httpStatus
     */

    public Integer getHttpStatus() {
        return httpStatus;
    }

    /**
     * @param httpStatus the httpStatus to set
     */
    public void setHttpStatus(Integer httpStatus) {
        this.httpStatus = httpStatus;
    }

    /**
     * @return the resultCount
     */
    public Integer getResultCount() {
        return resultCount;
    }

    /**
     * @param resultCount the resultCount to set
     */
    public void setResultCount(Integer resultCount) {
        this.resultCount = resultCount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}

