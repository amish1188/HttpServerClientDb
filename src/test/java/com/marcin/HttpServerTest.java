package com.marcin;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpServerTest {
    @Test
    void shouldReturnFailStatusCode() throws IOException {
        int port = startServer();
        HttpClient client = new HttpClient("localhost", port, "/echo?status=401");
        HttpClientResponse response = client.executeRequest();
        assertEquals(401,response.getStatusCode());
    }

    @Test
    void shouldReturnRequestedHeader() throws IOException {
        int port = startServer();
        HttpClient client = new HttpClient("localhost", port, "/echo?Location=http://example.com");
        HttpClientResponse response = client.executeRequest();
        assertEquals(200,response.getStatusCode());
        assertEquals("http://example.com",response.getHeader("Location"));
    }

    @Test
    void shouldReturnRequestedBody() throws IOException {
        int port = startServer();
        HttpClient client = new HttpClient("localhost", port, "/echo?body=HelloKutas");
        HttpClientResponse response = client.executeRequest();
        assertEquals("HelloKutas",response.getBody());
    }

    private int startServer() throws IOException {
        HttpServer httpServer = new HttpServer(0);

        new Thread(() -> {
            try {
                httpServer.start();
            } catch (IOException e){
                e.printStackTrace();
            }
        }).start();
        return httpServer.getActualPort();
    }

}
