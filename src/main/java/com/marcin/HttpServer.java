package com.marcin;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {

    private static ServerSocket serverSocket;

    public HttpServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public static void main(String[] args) throws IOException {
        new HttpServer(8080).start();
    }

    public static void start() throws IOException {
        while(true) {
            Socket socket = serverSocket.accept();
            String statusLine = readLine(socket);
            String line;
            while(!(line= readLine(socket)).isEmpty()) {

                System.out.println("Line: " + line);
            }

            System.out.println("Done");

            String requestTarget = statusLine.split(" ")[1];
            int questionPos = requestTarget.indexOf('?');
            String statusCode = "200";
            String location = null;
            String body = "Hello World!";

            if(questionPos > 0) {
                String query = requestTarget.substring(questionPos + 1);
                int equalsPos = query.indexOf('=');
                String paramName = query.substring(0, equalsPos);
                String paramValue = query.substring(equalsPos + 1);
                if(paramName.equals("status")) {
                    statusCode = paramValue;
                } else if(paramName.equals("Location")) {
                    location = paramValue;
                } else if(paramName.equals("body")) {
                    body = paramValue;
                }
            }

            socket.getOutputStream().write(("HTTP/1.1 " + statusCode + " OK\r\n").getBytes());
            socket.getOutputStream().write("Content-Type: text/html; charset=utf-8\r\n".getBytes());
            socket.getOutputStream().write(("Content-Length: "+ body.length() + "\r\n").getBytes());
            socket.getOutputStream().write("Connection: close\r\n".getBytes());
            if(location != null){
                socket.getOutputStream().write(("Location: " + location + "\r\n").getBytes());
            }
            socket.getOutputStream().write("\r\n".getBytes());
            socket.getOutputStream().write((body + "\r\n").getBytes());
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

    public int getActualPort() {
        return serverSocket.getLocalPort();
    }
}
