package com.marcin.webshop;

import com.marcin.http.HttpController;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Collectors;

public class ProductsController implements HttpController {

    private final ProductDao dao;

    public ProductsController(ProductDao dao){
        this.dao = dao;
    }

    @Override
    public void handle(String requestPath,Map<String, String> queryParameters, OutputStream outputStream) throws IOException {
        try {
            String status = "200";
            String body = getBody();
            outputStream.write(("HTTP/1.1 " + status + " OK\r\n" +
                    "Content-Type: text/html\r\n" +
                    "Content-Length: " + body.length() + "\r\n" +
                    "Connection: close\r\n" +
                    "\r\n" +
                    body).getBytes());
                    outputStream.flush();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    String getBody() throws SQLException {
        return dao.listAll().stream()
                .map(product -> String.format("<option id='%s'>%s</option>", product.getId(), product.getName()))
                .collect(Collectors.joining());
    }
}
