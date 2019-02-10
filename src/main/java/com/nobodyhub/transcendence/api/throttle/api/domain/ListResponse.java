package com.nobodyhub.transcendence.api.throttle.api.domain;

import lombok.Data;

import java.util.List;

/**
 * Response contains a list of data
 *
 * @param <T>
 */
@Data
public class ListResponse<T> {
    private List<T> data;

    public static <T> ListResponse<T> of(List<T> data) {
        ListResponse<T> resp = new ListResponse<>();
        resp.setData(data);
        return resp;
    }
}
