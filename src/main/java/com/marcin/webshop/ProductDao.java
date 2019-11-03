package com.marcin.webshop;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ProductDao extends AbstractDao<Product> {

    public ProductDao(DataSource dataSource) {
        super(dataSource);
    }

    public Product retrieve(Long id) throws SQLException {
        return retrieve(id, "select * from PRODUCTS where id = ?");
    }

    public void insert(Product product) throws SQLException {
        long id = insert(product, "insert into PRODUCTS (name) values (?)");
        product.setId(id);
    }

    public List<Product> listAll() throws SQLException {
        return listAll("select * from PRODUCTS");
    }

    @Override
    protected void mapToStatement(Product product, PreparedStatement statement) throws SQLException {
        statement.setString(1,product.getName());
    }

    @Override
    protected Product mapFromResultSet(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setId(rs.getLong("id"));
        product.setName(rs.getString("name"));
        return product;
    }


}
