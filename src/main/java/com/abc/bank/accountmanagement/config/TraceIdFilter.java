package com.abc.bank.accountmanagement.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class TraceIdFilter implements Filter {
    public static final String TRACE_ID_HEADER = "Trace-Id";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String traceId = request.getHeader(TRACE_ID_HEADER);
        if (traceId == null || traceId.isEmpty()) {
            traceId = UUID.randomUUID().toString();
        }
        MDC.put(TRACE_ID_HEADER, traceId);
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            clearMDC();
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    void clearMDC() {
        MDC.remove(TRACE_ID_HEADER);
    }
}
