package io.lw900925.ocean.restful.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class DefaultAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private ObjectMapper objectMapper;

    public DefaultAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        Map<String, Object> responseBody = ImmutableMap.<String, Object> builder()
                .put("timestamp", System.currentTimeMillis())
                .put("status", HttpServletResponse.SC_UNAUTHORIZED)
                .put("error", "Unauthorized")
                .put("message", exception.getMessage())
                .put("path", request.getRequestURI())
                .build();

        String strResponseBody = objectMapper.writeValueAsString(responseBody);
        response.getWriter().println(strResponseBody);
    }
}
