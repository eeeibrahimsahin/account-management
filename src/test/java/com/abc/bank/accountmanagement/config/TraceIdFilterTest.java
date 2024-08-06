package com.abc.bank.accountmanagement.config;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;


import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = TraceIdFilter.class)
class TraceIdFilterTest {

    @SpyBean
    private TraceIdFilter traceIdFilter;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @MockBean
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        MDC.clear();
    }

    @Test
    @DisplayName("Filter should add provided Trace-Id to MDC when Trace-Id header is present")
    void testDoFilter_withTraceId() throws IOException, ServletException {
        request.addHeader(TraceIdFilter.TRACE_ID_HEADER, "12345");
        doNothing().when(traceIdFilter).clearMDC();

        traceIdFilter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertEquals("12345", MDC.get(TraceIdFilter.TRACE_ID_HEADER));
    }

    @Test
    @DisplayName("Filter should generate and add Trace-Id to MDC when Trace-Id header is blank")
    void testDoFilter_withBlankTraceId() throws IOException, ServletException {
        request.addHeader(traceIdFilter.TRACE_ID_HEADER, " ");
        doNothing().when(traceIdFilter).clearMDC();

        traceIdFilter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNotNull(MDC.get(TraceIdFilter.TRACE_ID_HEADER));
    }

    @Test
    @DisplayName("Filter should generate and add Trace-Id to MDC when Trace-Id header is absent")
    void testDoFilter_withoutTraceId() throws IOException, ServletException {
        doNothing().when(traceIdFilter).clearMDC();

        traceIdFilter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNotNull(MDC.get(TraceIdFilter.TRACE_ID_HEADER));
    }

    @Test
    @DisplayName("Test MDC is cleaned in finally block")
    public void testDoFilter_MDCRemovedInFinally() throws IOException, ServletException {
        request.addHeader(TraceIdFilter.TRACE_ID_HEADER, "12345");

        traceIdFilter.doFilter(request, response, filterChain);

        verify(traceIdFilter).clearMDC();
        assertNull(MDC.get(TraceIdFilter.TRACE_ID_HEADER));
    }
}
