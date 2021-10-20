package com.wellsfargo.rarconsumer.request;


import lombok.*;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Pagination {

    private int pageNumber;
    private int pageSize;
    private String sort;

}
