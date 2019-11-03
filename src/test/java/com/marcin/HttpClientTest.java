package com.marcin;

import com.marcin.http.HttpClient;
import com.marcin.http.HttpClientResponse;
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

    @Test
    void shouldReadContentLength() throws IOException {
        HttpClient client = new HttpClient("urlecho.appspot.com", 80, "/echo?body=12345678");
        HttpClientResponse response = client.executeRequest();
        assertEquals(8,response.getContentLength());
    }

    @Test
    void shouldReadBody() throws IOException {
        HttpClient client = new HttpClient("urlecho.appspot.com", 80, "/echo?body=HiKutas");
        HttpClientResponse response = client.executeRequest();
        assertEquals("HiKutas",response.getBody());
    }
}
