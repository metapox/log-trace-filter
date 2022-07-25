package org.metapox.servlet;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.MockitoAnnotations;
import org.mockito.MockedStatic;
import org.slf4j.MDC;

@ExtendWith(MockitoExtension.class)
public class LogTraceFilterTest {
    final String dummyTraceparent = "00-4bf92f3577b34da6a3ce929d0e0e4736-00f067aa0ba902b7-00";
    private AutoCloseable closeable;
    @InjectMocks
    private LogTraceFilter logTraceFilter;

    @Mock
    private HttpServletRequest request;

    @Mock
    private ServletResponse response;

    @Mock
    private FilterChain filterChain;

    @BeforeEach
    public void openMocks() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void releaseMocks() throws Exception {
        closeable.close();
    }

    @Test
    // target doFilter
    // requestのtraceparent ヘッダーがMDCのputに渡されること
    public void testDoFilter_checkMdcPut() throws IOException, ServletException{
        // making mock
        try(MockedStatic<MDC> mdcMock = Mockito.mockStatic(MDC.class)) {
            Mockito.when(request.getHeader("traceparent")).thenReturn(dummyTraceparent);

            logTraceFilter.doFilter(request, response, filterChain);
            mdcMock.verify(() -> MDC.put("traceparent", dummyTraceparent));
        }
    }

    @Test
    // target doFilter
    // chain.dofilterが適切な引数で呼ばれること
    public void testDoFilter_checkChainDoFilter() throws IOException, ServletException{
        logTraceFilter.doFilter(request, response, filterChain);
        Mockito.verify(filterChain).doFilter(request, response);
    }

    @Test
    // target doFilter
    // 正常実行時にMDC remove が呼ばれること
    public void testDoFilter_checkMdcRemove() throws IOException, ServletException{
        try(MockedStatic<MDC> mdcMock = Mockito.mockStatic(MDC.class)) {
            Mockito.when(request.getHeader("traceparent")).thenReturn(dummyTraceparent);

            logTraceFilter.doFilter(request, response, filterChain);
            mdcMock.verify(() -> MDC.remove("traceparent"));
        }
    }

    @Test
    // target doFilter
    // エラー検出時にMDC remove が呼ばれること
    public void testDoFilter_check() throws IOException, ServletException{
        try(MockedStatic<MDC> mdcMock = Mockito.mockStatic(MDC.class)) {
            Mockito.when(request.getHeader("traceparent"))
                    .thenThrow(new IllegalStateException("Error occurred"));

            logTraceFilter.doFilter(request, response, filterChain);
            mdcMock.verify(() -> MDC.remove("traceparent"));
        }
    }
}
