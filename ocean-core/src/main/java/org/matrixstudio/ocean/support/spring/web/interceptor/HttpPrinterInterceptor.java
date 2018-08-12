package org.matrixstudio.ocean.support.spring.web.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.DispatcherType;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HttpPrinterInterceptor implements HandlerInterceptor {

    /**
     * Logger for print.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpPrinterInterceptor.class);
    private static final String LINE_BREAK = "\n";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Only print REQUEST DispatcherType
        if (request.getDispatcherType() != DispatcherType.REQUEST) {
            return true;
        }

        StringBuilder builder = new StringBuilder(255);

        // Append general info
        builder.append(LINE_BREAK);
        builder.append("[ General ]").append(LINE_BREAK);
        builder.append("Request URL: ").append(request.getRequestURL()).append(LINE_BREAK);
        builder.append("Request Method: ").append(request.getMethod()).append(LINE_BREAK);
        builder.append("Remote Address: ").append(request.getRemoteAddr()).append(LINE_BREAK);

        builder.append(LINE_BREAK);

        // Append http headers
        if (request.getHeaderNames() != null && request.getHeaderNames().hasMoreElements()) {
            builder.append("[ Header ]").append(LINE_BREAK);
            List<String> headers = Collections.list(request.getHeaderNames()).stream().map(headerName -> {
                List<String> newHeaderNames = Stream.of(headerName.split("-")).map(StringUtils::capitalize).collect(Collectors.toList());
                return String.join("-", newHeaderNames);
            }).map(headerName -> headerName + ": " + request.getHeader(headerName)).collect(Collectors.toList());
            builder.append(String.join(LINE_BREAK, headers)).append(LINE_BREAK);
        }

        builder.append(LINE_BREAK);

        // Append query string parameters
        if (request.getParameterNames() != null && request.getParameterNames().hasMoreElements()) {
            builder.append("[ Query String Parameters ]").append(LINE_BREAK);
            List<String> queryParameters = Collections.list(request.getParameterNames())
                    .stream()
                    .map(parameterName -> parameterName + ": " + request.getParameter(parameterName))
                    .collect(Collectors.toList());
            builder.append(String.join(LINE_BREAK, queryParameters)).append(LINE_BREAK);
        }

        // Append request payload
        if (request.getInputStream() != null && request.getInputStream().isReady()) {
            builder.append("[ Request Payload ]").append(LINE_BREAK);
            ServletInputStream inputStream = request.getInputStream();

            byte[] buffer = new byte[1024];
            int line = 0;
            while ((line = inputStream.read(buffer)) > 0) {
                builder.append(new String(buffer, 0, line));
            }

            builder.append(LINE_BREAK);
        }

        LOGGER.debug("HTTP Request Info: {}", builder.toString());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // Do nothing
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        StringBuilder builder = new StringBuilder(255);

        builder.append(LINE_BREAK);
        builder.append("[ General ]").append(LINE_BREAK);
        builder.append("Status: ").append(response.getStatus()).append(LINE_BREAK);
        builder.append("Character-Encoding: ").append(response.getCharacterEncoding()).append(LINE_BREAK);

        builder.append(LINE_BREAK);

        if (!CollectionUtils.isEmpty(response.getHeaderNames())) {
            builder.append("[ Header ]").append(LINE_BREAK);
            List<String> headers = response.getHeaderNames().stream().map(headerName -> {
                List<String> newHeaderNames = Stream.of(headerName.split("-")).map(StringUtils::capitalize).collect(Collectors.toList());
                return String.join("-", newHeaderNames);
            }).map(headerName -> headerName + ": " + response.getHeader(headerName)).collect(Collectors.toList());
            builder.append(String.join(LINE_BREAK, headers)).append(LINE_BREAK);
        }

        builder.append(LINE_BREAK);
        LOGGER.debug("HTTP Response Info: {}", builder.toString());
    }
}
