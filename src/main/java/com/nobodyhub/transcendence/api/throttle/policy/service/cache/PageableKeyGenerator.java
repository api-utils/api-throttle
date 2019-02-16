package com.nobodyhub.transcendence.api.throttle.policy.service.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Used for generating key for mehtod:
 * - has only one parameter,  and
 * - is of type {@link org.springframework.data.domain.PageRequest}
 */
@Slf4j
@Component("pageableKeyGenerator")
public class PageableKeyGenerator implements KeyGenerator {
    @Override
    public Object generate(Object target, Method method, Object... params) {
        if (params.length == 1 && params[0] instanceof PageRequest) {
            return ((PageRequest) params[0]).toString();
        }
        throw new PageableKeyGeneratorError(method);
    }
}

class PageableKeyGeneratorError extends Error {
    PageableKeyGeneratorError(Method method) {
        super(String.format("Unable to generate key for method:[%s]", method));
    }
}
