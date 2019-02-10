package com.nobodyhub.transcendence.api.throttle.api.domain;

import lombok.Data;

/**
 * Response contains only data
 */
@Data
public class SingleResponse<T> {
    private T data;

    public static <T> SingleResponse<T> of(T data) {
        SingleResponse<T> resp = new SingleResponse<>();
        resp.setData(data);
        return resp;
    }
}
