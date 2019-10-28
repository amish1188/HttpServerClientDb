package com.marcin;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpClientTest {

    @Test
    void shouldReturnStatusCode() throws IOException {
        HttpClient client = new HttpClient("urlecho.appspot.com", 80, "/echo");
        HttpClientResponse response = client.executeRequest();
        assertEquals(200, response.getStatusCode());
    }

    @Test
    void shouldReturnFailStatusCode() throws IOException {
        HttpClient client = new HttpClient("urlecho.appspot.com", 80, "/echo?status=401");
        HttpClientResponse response = client.executeRequest();
        assertEquals(401,response.getStatusCode());
    }

}
