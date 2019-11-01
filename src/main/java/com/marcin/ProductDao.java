package com.marcin;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ProductDao extends AbstractDao<Product> {

    public ProductDao(DataSource dataSource) {
        super(dataSource);
    }

    public void insert(Product product) throws SQLException {
        insert(product, "insert into PRODUCTS (name) values (?)");
    }

    @Override
    protected void mapToStatement(Product product, PreparedStatement statement) throws SQLException {
        statement.setString(1,product.getName());
    }

    public List<Product> listAll() throws SQLException {
        return listAll("select * from PRODUCTS");
    }

    @Override
    protected Product mapFromResultSet(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setName(rs.getString("name"));
        return product;
    }
}
