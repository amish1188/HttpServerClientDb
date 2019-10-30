package com.marcin;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class HttpServer {

    private int port;
    private ServerSocket serverSocket;
    private String fileLocation;

    private HttpController defaultController = new FileHttpController(this);

    private Map <String, HttpController> controllers = new HashMap<>();

    public HttpServer(int port) throws IOException {
        this.port = port;
        serverSocket = new ServerSocket(port);
        controllers.put("/", new EchoHttpController());

    }

    public static void main(String[] args) throws IOException {
        HttpServer httpServer = new HttpServer(8080);
        httpServer.setFileLocation("src/main/resources");
        httpServer.startServer();
    }

    public void startServer() throws IOException {
        new Thread(() -> run()).start();
    }

    private void run() {
        while (true) {
            // this is a request line made by the client
            try (Socket socket = serverSocket.accept()) {
                StringBuilder line = new StringBuilder();
                String requestLine = null;
                int c;
                while ((c = socket.getInputStream().read()) != -1) {
                    if (c == '\r') {
                        c = socket.getInputStream().read();
                        if(requestLine == null) {
                            requestLine = line.toString();
                        }
                        System.out.println(line);
                        if (line.toString().isBlank()) {
                            break;
                        }
                        line = new StringBuilder();
                    }
                    line.append((char) c);
                }

                //this is request target which means anything after '?' or just main domain
                String requestTarget = requestLine.split(" ")[1];
                int questionPos = requestTarget.indexOf("?");
                String requestPath = questionPos == -1 ? requestTarget : requestTarget.substring(0, questionPos);
                Map<String, String> queryParameters = parseQueryParameters(requestTarget);

                controllers
                        .getOrDefault(requestPath, defaultController)
                        .handle(requestPath, socket.getOutputStream(), queryParameters);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Map<String, String> parseQueryParameters(String requestTarget) {
        Map <String, String> queryParameters = new HashMap<>();
        int questionPos = requestTarget.indexOf("?");
        if(questionPos > 0) {
            String query = requestTarget.substring(questionPos+1);
            for(String parameter : query.split("&")) {
                int equalsPos = parameter.indexOf("=");
                String paramName = parameter.substring(0, equalsPos);
                String paramValue = parameter.substring(equalsPos+1);
                queryParameters.put(paramName, paramValue);
            }
        }
        return queryParameters;
    }

    public int getPort(){
        return serverSocket.getLocalPort();
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public String getFileLocation() {
        return fileLocation;
    }

}



