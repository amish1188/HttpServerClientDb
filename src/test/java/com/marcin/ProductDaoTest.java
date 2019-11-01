package com.marcin;

import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductDaoTest {

    private Random random = new Random();

    @Test
    void shouldListSavedProducts() throws SQLException {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUrl("jdbc:h2:mem:productTest;DB_CLOSE_DELAY=-1");

        Flyway.configure().dataSource(dataSource).load().migrate();

        ProductDao productDao = new ProductDao(dataSource);
        Product product = sampleProduct();

        productDao.insert(product);
        assertThat(productDao.listAll())
                .extracting(Product::getName)
                .contains(product.getName());
    }

    private Product sampleProduct() {
        Product product = new Product();
        product.setName(pickOne(new String [] {"apple", "banana", "joy", "pleasure"}));
        return product;
    }

    private String pickOne(String[] alternatives) {
        return alternatives[random.nextInt(alternatives.length)];
    }
}
