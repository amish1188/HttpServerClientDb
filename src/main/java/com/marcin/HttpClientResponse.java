package com.marcin;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

class HttpClientResponse {

    private Socket socket;
    private String statusLine;
    private Map<String, String> headers = new HashMap<>();
    private String body;

    public HttpClientResponse(Socket socket) {
        this.socket = socket;
    }

    public void invoke() throws IOException {
        this.statusLine = readLine(socket);
        System.out.print(this.statusLine);
        String headerLine;

        while(!(headerLine=readLine(socket)).isEmpty()) {
            System.out.println(getClass().getSimpleName() + ": " + headerLine);
            int colonPos = headerLine.indexOf(':');
            this.headers.put(headerLine.substring(0, colonPos), headerLine.substring(colonPos + 1).trim());
        }

        if(headers.containsKey("Content-Length")) {
            StringBuilder body = new StringBuilder();
            for(int i = 0; i < getContentLength(); i++) {
                body.append((char)socket.getInputStream().read());
            }
            this.body = body.toString();
        }

    }

    private static String readLine(Socket socket) throws IOException {
        int c;
        StringBuilder line = new StringBuilder();
        while((c = socket.getInputStream().read()) != -1) {
            if(c == '\r') {
                c = socket.getInputStream().read();
                if (c != '\n') {
                    System.err.println("Unexpected character! " + ((char) c));
                }
                return line.toString();
            }
            line.append((char)c);
        }
        return line.toString();
    }

    public int getStatusCode() {
        return Integer.parseInt(statusLine.split(" ")[1]);
    }

    public String getHeader(String headerName) {
        return headers.get(headerName);
    }

    public int getContentLength() {
        return Integer.parseInt(getHeader("Content-Length"));
    }

    public String getBody() {
        return body;
    }
}
