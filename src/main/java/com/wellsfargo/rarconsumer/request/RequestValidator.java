package com.wellsfargo.rarconsumer.request;

import com.wellsfargo.rarconsumer.exception.BadRequestException;

public class RequestValidator {

    private static final String PAGINATION_PAGE_SIZE_NOT_NEGATIVE = "Page and Size cannot be less then zero";
    private static final String PAGINATION__SIZE_NOT_ZERO = "Size cannot be less then one";

    public static boolean validatePagination(Pagination pagination) {
        if(pagination.getPageSize() < 0  ||  pagination.getPageNumber() < 0) {
            throw new BadRequestException(PAGINATION_PAGE_SIZE_NOT_NEGATIVE);
        }
        if(pagination.getPageSize() < 1) {
            throw new BadRequestException(PAGINATION__SIZE_NOT_ZERO);
        }

        return true;
    }
}
