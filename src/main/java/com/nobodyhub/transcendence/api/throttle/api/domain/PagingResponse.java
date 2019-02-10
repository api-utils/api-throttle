package com.nobodyhub.transcendence.api.throttle.api.domain;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * Response contains a page of data with paging information
 *
 * @param <T> result class
 */
@Data
public class PagingResponse<T> {
    /**
     * returned data
     */
    private List<T> data;
    /**
     * page size
     */
    private int pageSize;
    /**
     * current page number
     */
    private int pageNumber;
    /**
     * total number of pages
     */
    private int totalPages;
    /**
     * total number of records
     */
    private long totalRecords;

    public static <T> PagingResponse<T> of(@NonNull Page<T> page) {
        PagingResponse<T> resp = new PagingResponse<>();
        resp.setData(page.getContent());
        resp.setPageSize(page.getSize());
        resp.setPageNumber(page.getNumber());
        resp.setTotalPages(page.getTotalPages());
        resp.setTotalRecords(page.getTotalElements());
        return resp;
    }
}
