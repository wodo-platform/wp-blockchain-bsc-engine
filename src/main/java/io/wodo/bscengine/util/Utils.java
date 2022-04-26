package io.wodo.bscengine.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.springframework.beans.support.PagedListHolder.DEFAULT_PAGE_SIZE;

public class Utils {

    public static Pageable createPageableBySizeAndPage(Integer page, Integer size) {

        if (size == null || size > 10 || size < 0) {
            size = DEFAULT_PAGE_SIZE;
        }
        if (page == null || page < 0) {
            page = 0;
        }

        return PageRequest.of(page, size);
    }

    public static String getHttpHeadersFromRequestAsString(HttpServletRequest request) {
        HttpHeaders httpHeaders = Collections.list(request.getHeaderNames())
                .stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        h -> Collections.list(request.getHeaders(h)),
                        (oldValue, newValue) -> newValue,
                        HttpHeaders::new
                ));
        return httpHeaders.toString();
    }
}