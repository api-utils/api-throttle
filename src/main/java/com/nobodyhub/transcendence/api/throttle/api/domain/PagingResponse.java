package com.nobodyhub.transcendence.api.throttle.api.domain;

import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * Response contains a page of data with paging information
 *
 * @param <T> result class
 */
@Data
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class PagingResponse<T> {
    /**
     * returned data
     */
    private List<T> data = Lists.newArrayList();
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

    public static <T> PagingResponse<T> of(@NonNull PagingResponse<T> page) {
        PagingResponse<T> resp = new PagingResponse<>();
        resp.setData(page.getData());
        resp.setPageSize(page.getPageSize());
        resp.setPageNumber(page.getPageNumber());
        resp.setTotalPages(page.getTotalPages());
        resp.setTotalRecords(page.getTotalRecords());
        return resp;
    }

    public static <T> PagingResponse<T> of(@NonNull T data) {
        PagingResponse<T> resp = new PagingResponse<>();
        resp.getData().add(data);
        resp.setPageSize(1);
        resp.setPageNumber(1);
        resp.setTotalPages(1);
        resp.setTotalRecords(1);
        return resp;
    }
}
