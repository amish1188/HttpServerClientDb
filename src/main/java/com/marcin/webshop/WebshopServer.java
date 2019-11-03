package com.marcin.webshop;

import com.marcin.http.HttpServer;
import org.flywaydb.core.Flyway;
import org.postgresql.ds.PGSimpleDataSource;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class WebshopServer {

    public static void main(String[] args) throws IOException {

        Properties properties = new Properties();
        try (FileReader fileReader = new FileReader("db.properties")) {
            properties.load(fileReader);
        }

        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:5432/httpdb");
        dataSource.setUser("httpdb");
        dataSource.setPassword("123mnbj-09890dh");

        Flyway.configure().dataSource(dataSource).load().migrate();


        HttpServer server = new HttpServer(8080);
        server.setFileLocation("src/main/resources/webshop");
        server.addController("/api/products", new ProductsController(new ProductDao(dataSource)));
        server.startServer();
    }


}
