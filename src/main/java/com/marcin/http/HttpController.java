package com.marcin.http;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public interface HttpController {
    void handle(String requestPath, Map<String, String> queryParameters, OutputStream outputStream) throws IOException;
}
