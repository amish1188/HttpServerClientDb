package com.marcin;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

class EchoHttpController implements HttpController {
    @Override
    public void handle(String requestPath, OutputStream outputStream, Map<String, String> queryParameters) throws IOException {
        String status = queryParameters.getOrDefault("status", "200");
        String location = queryParameters.getOrDefault("Location", null);
        String body = queryParameters.getOrDefault("body", "Hello World");

        outputStream.write(("HTTP/1.1 " + status + " OK\r\n" +
                "Content-Type: text/html\r\n" +
                "Content-Length: " + body.length() + "\r\n" +
                "Connection: close\r\n" +
                (location != null ? "Location: " + location + "\r\n" : "") +
                "\r\n" +
                body).getBytes());
        outputStream.flush();
    }
}
