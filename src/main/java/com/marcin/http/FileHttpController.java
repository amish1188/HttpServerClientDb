package com.marcin.http;

import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

class FileHttpController implements HttpController {
    private HttpServer httpServer;

    public FileHttpController(HttpServer httpServer) {
        this.httpServer = httpServer;
    }

    private static final org.slf4j.Logger Logger = LoggerFactory.getLogger(FileHttpController.class);

    @Override
    public void handle(String requestPath, Map<String, String> queryParameters, OutputStream outputStream) throws IOException {
        File file= new File(httpServer.getFileLocation() + requestPath);
        Logger.debug("Requesting file {}", file);
        if(file.isDirectory()) {
            file = new File(file, "index.html");
        }
        if(file.exists()) {
            long length = file.length();
            outputStream.write(("HTTP/1.1 200 OK\r\n" +
                                "Content-Length: " + length + "\r\n" +
                                "Connection: close\r\n" +
                                "\r\n").getBytes());
            // loading body to the http package, under the header
            try(FileInputStream fileInputStream = new FileInputStream(file)) {
                fileInputStream.transferTo(outputStream);
            }
        }
    }
}
