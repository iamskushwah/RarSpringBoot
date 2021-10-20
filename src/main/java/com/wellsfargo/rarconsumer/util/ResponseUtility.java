package com.wellsfargo.rarconsumer.util;


import com.wellsfargo.rarconsumer.response.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;


public class ResponseUtility {

    private ResponseUtility() {
        throw new IllegalStateException("Utility class");
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static ResponseEntity<ResponseApiDTO> createSuccessResponse(Object object) {

        String version = "1.0";
        String message = "";
        HttpStatus httpStatus = HttpStatus.OK;
        return new ResponseEntity<>(new ResponseApiDTO(object, version, httpStatus, message), httpStatus);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static ResponseEntity<ResponseApiDTO> createSuccessResponse(List object) {

        String version = "1.0";
        String message = "";
        HttpStatus httpStatus = HttpStatus.OK;
        return new ResponseEntity<>(new ResponseApiDTO(object, version, httpStatus, message), httpStatus);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static ResponseEntity<ResponseApiDTO> createSuccessResponse(String message, Object object) {

        String version = "1.0";
        HttpStatus httpStatus = HttpStatus.OK;
        return new ResponseEntity<>(new ResponseApiDTO(object, version, httpStatus, message), httpStatus);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static ResponseEntity<ResponseApiDTO> createSystemErrorReponse(String message,HttpStatus httpStatus) {
        String version = "1.0";
        SystemErrorMessage systemError = new  SystemErrorMessage(message,null);
        ApiErrorMessage errorMessage = new ApiErrorMessage(systemError,null);
        return new ResponseEntity<>(new ResponseApiDTO(errorMessage,new Metadata(version, httpStatus.value(),null)), httpStatus);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static ResponseEntity<ResponseApiDTO> createUserErrorResponse(String message,HttpStatus httpStatus) {
        String version = "1.0";
        UserErrorMessage userError = new  UserErrorMessage(message,null);
        ApiErrorMessage errorMessage = new ApiErrorMessage(null,userError);
        return new ResponseEntity<>(new ResponseApiDTO(errorMessage,new Metadata(version, httpStatus.value(),null)), httpStatus);
    }

}

