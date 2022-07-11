package org.metapox.filters;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import org.slf4j.MDC;

public class LogTraceFilter implements Filter {
    private final string TRACEID_KEY = "trace_id";
    private final string SPAN_KEY = "span_id";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("Hello Init!!");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        System.out.println("Hello filter!!");
    }

    @Override
    public void destroy() {
        System.out.println("Hello destory!!");
    }
}