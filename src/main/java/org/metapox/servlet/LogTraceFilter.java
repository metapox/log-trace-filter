package org.metapox.servlet;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.invoke.MethodHandles;

import org.slf4j.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogTraceFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final String TRACE_PARENT_HEADER = "traceparent";
    private final String TRACE_PARENT_KEY = "traceparent";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        try {
            HttpServletRequest httpRequest = (HttpServletRequest) req;
            MDC.put(TRACE_PARENT_KEY, httpRequest.getHeader(TRACE_PARENT_HEADER));
            chain.doFilter(req, response);
        } catch (Exception e) {
            log.error("Trace filter Error", e);
        } finally {
            MDC.remove(TRACE_PARENT_KEY);
        }
    }

    @Override
    public void destroy() {
    }
}