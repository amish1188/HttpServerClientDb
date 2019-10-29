package com.marcin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpServerTest {



     private HttpServer server;

    @BeforeEach
    void setUp() throws IOException {
        server = new HttpServer(0);
        server.startServer();
    }


    @Test
    void shouldReturn200() throws IOException {
        HttpClient client = new HttpClient("localhost", server.getPort(), "/echo");
        HttpClientResponse response = client.executeRequest();
        assertEquals(200, response.getStatusCode());
    }

    @Test
    void shouldReturn401() throws IOException {
        HttpClient client = new HttpClient("localhost", server.getPort(), "/echo?status=401");
        HttpClientResponse response = client.executeRequest();
        assertEquals(401, response.getStatusCode());
    }

    @Test
    void shouldReturnResponseHeader() throws IOException {
        HttpClient client = new HttpClient("localhost", server.getPort(), "/echo?status=302&Location=http://www.example.com");
        HttpClientResponse response = client.executeRequest();
        assertEquals("http://www.example.com", response.getHeader("Location"));
    }

    @Test
    void shouldReturnContent() throws IOException {
        HttpClient client = new HttpClient("localhost", server.getPort(), "/echo?body=HiKutas");
        HttpClientResponse response = client.executeRequest();
        assertEquals("7", response.getHeader("Content-Length"));
        assertEquals("HiKutas", response.getBody());
    }

    @Test
    void shoulFileFromDisk() throws IOException {
        Files.writeString(Paths.get("target/mytestfile.txt"), "Hello Kristiania");
        server.setFileLocation("target");
        HttpClient client = new HttpClient("localhost", server.getPort(), "/mytestfile.txt");
        HttpClientResponse response = client.executeRequest();
        assertEquals("Hello Kristiania", response.getBody());
    }


    /*
    @Test
    void shouldReturnFailStatusCode() throws IOException {

        HttpClient client = new HttpClient("localhost", server.getActualPort(), "/echo?status=401");
        HttpClientResponse response = client.executeRequest();
        assertEquals(401,response.getStatusCode());
    }

    @Test
    void shouldReturnRequestedHeader() throws IOException {
        HttpClient client = new HttpClient("localhost", server.getActualPort(), "/echo?Location=http://example.com");
        HttpClientResponse response = client.executeRequest();
        assertEquals(200,response.getStatusCode());
        assertEquals("http://example.com",response.getHeader("Location"));
    }

    @Test
    void shouldReturnRequestedBody() throws IOException {
        HttpClient client = new HttpClient("localhost", server.getActualPort(), "/echo?body=HelloKutas");
        HttpClientResponse response = client.executeRequest();
        assertEquals("HelloKutas",response.getBody());
    }

    @Test
    void shouldReturnFileFromDisk() throws IOException {
        Files.writeString(Paths.get("target/mytestfile.txt"), "Hello Kristiania");
        server.setFileLocation("target");
        HttpClient client = new HttpClient("localhost", server.getActualPort(),"/mytestfile.txt");
        HttpClientResponse response = client.executeRequest();
        assertEquals("Hello Kristiania", response.getBody());
    } */

}
