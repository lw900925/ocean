package org.matrixstudio.ocean.support.spring.web.servlet.wrapper;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class InputStreamRequestWrapper extends HttpServletRequestWrapper {

    private static final String LINE_BREAK = "\n";

    private final String requestBody;

    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request The request to wrap
     * @throws IllegalArgumentException if the request is null
     */
    public InputStreamRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);

        StringBuilder builder = new StringBuilder(255);
        request.getReader().lines().forEach(line -> builder.append(line).append(LINE_BREAK));
        requestBody = builder.toString();
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(requestBody.getBytes());
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return inputStream.available() == 0;
            }

            @Override
            public boolean isReady() {
                return !isFinished() && inputStream.available() > 0;
            }

            @Override
            public void setReadListener(ReadListener listener) {
                // No listener
            }

            @Override
            public int read() throws IOException {
                return inputStream.read();
            }
        };
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }

    public String getRequestBody() {
        return requestBody;
    }
}
