package com.marcin;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {
    public static void main(String[] args) throws IOException {
            try(ServerSocket serverSocket = new ServerSocket(8080)) {
                while(true) {
                    Socket socket = serverSocket.accept();
                    String line;
                    while(!(line= readLine(socket)).isEmpty()) {
                        System.out.println("Line: " + line);
                    }

                    System.out.println("Done");

                    socket.getOutputStream().write("HTTP/1.1 200 OK\r\n".getBytes());
                    socket.getOutputStream().write("Content-Type: text/html; charset=utf-8\r\n".getBytes());
                    socket.getOutputStream().write("Content-Length: 12\r\n".getBytes());
                    socket.getOutputStream().write("Connection: close\r\n".getBytes());
                    socket.getOutputStream().write("\r\n".getBytes());
                    socket.getOutputStream().write("Hello World!\r\n".getBytes());
                }
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
}
