package com.marcin;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public interface HttpController {
    void handle(String requestPath, OutputStream outputStream, Map<String, String> queryParameters) throws IOException;
}
