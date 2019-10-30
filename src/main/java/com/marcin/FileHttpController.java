package com.marcin;

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

    @Override
    public void handle(String requestPath, OutputStream outputStream, Map<String, String> queryParameters) throws IOException {
        File file= new File(httpServer.getFileLocation() + requestPath);
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
