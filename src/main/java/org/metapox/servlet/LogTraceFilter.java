package org.metapox.servlet;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import org.slf4j.MDC;

public class LogTraceFilter implements Filter {
    private final String TRACE_PARENT = "traceparent";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest)req;
        try {
            MDC.put(TRACE_PARENT, httpRequest.getHeader(TRACE_PARENT));
            chain.doFilter(req, response);
        } finally {
            MDC.remove(TRACE_PARENT);
        }
    }

    @Override
    public void destroy() {
    }
}