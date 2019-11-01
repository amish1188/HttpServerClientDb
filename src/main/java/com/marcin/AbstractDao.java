package com.marcin;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDao<Entity> {
    protected DataSource dataSource;

    public AbstractDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public long insert(Entity  q, String sql) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                mapToStatement(q, statement);
                statement.executeUpdate();

                ResultSet generatedKey = statement.getGeneratedKeys();
                generatedKey.next();
                return generatedKey.getLong("id");
            }
        }
    }

    protected abstract void mapToStatement(Entity  q, PreparedStatement statement) throws SQLException;

    public List<Entity > listAll(String sql) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet rs = statement.executeQuery()) {
                    List<Entity > result = new ArrayList<>();
                    while(rs.next()) {
                        result.add(mapFromResultSet(rs));
                    }
                    return result;
                }
            }
        }
    }

    protected abstract Entity  mapFromResultSet(ResultSet rs) throws SQLException;

    public Entity retrieve(Long id, String sql) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1,id);
                try (ResultSet rs = statement.executeQuery()) {
                    if(rs.next()) {
                        return mapFromResultSet(rs);
                    } else {
                    return null;
                    }
                }
            }
        }
    }
}
