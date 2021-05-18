package io.lw900925.ocean.restful.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class DefaultAccessDeniedHandler implements AccessDeniedHandler {

    private ObjectMapper objectMapper;

    public DefaultAccessDeniedHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        Map<String, Object> responseBody = ImmutableMap.<String, Object> builder()
                .put("timestamp", System.currentTimeMillis())
                .put("status", HttpServletResponse.SC_FORBIDDEN)
                .put("error", "Access Denied")
                .put("message", exception.getMessage())
                .put("path", request.getRequestURI())
                .build();

        String strResponseBody = objectMapper.writeValueAsString(responseBody);
        response.getWriter().println(strResponseBody);
    }
}
