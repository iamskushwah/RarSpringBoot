package com.wellsfargo.rarconsumer.request;

public class RequestFormatter {

    public static Pagination getPagination(int pageNumber, int pageSize, String sort) {
        Pagination pagination = new Pagination();
        pagination.setPageNumber(pageNumber);
        pagination.setPageSize(pageSize);
        pagination.setSort(sort);
        return pagination;
    }
}
