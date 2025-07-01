package com.creamy.cinema.util;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String DEFAULT_DATABASE = "cinema_db";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    private final Connection connection;
    private final ArrayList<Statement> statements;
    private final ArrayList<ResultSet> resultSets;

    public DBConnection() throws CinemaException {
        this(DEFAULT_DATABASE);
    }

    public DBConnection(String db) throws CinemaException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(URL + db, USERNAME, PASSWORD);
            statements = new ArrayList<>();
            resultSets = new ArrayList<>();
        } catch (SQLException | ClassNotFoundException e) {
            throw new CinemaException("Error while connecting to database.", e);
        }
    }

    public PreparedStatement prepareStatement(String query) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(query);
        statements.add(statement);
        return statement;
    }

    public PreparedStatement prepareInsertStatement(String query) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statements.add(statement);
        return statement;
    }

    public ResultSet executeStatement(PreparedStatement statement) throws SQLException {
        ResultSet resultSet = statement.executeQuery();
        resultSets.add(resultSet);
        return resultSet;
    }

    public int executeInsertStatement(PreparedStatement statement) throws SQLException {
        int affectedRows = statement.executeUpdate();

        if (affectedRows == 0) {
            throw new SQLException("Insert failed, no rows affected.");
        }

        ResultSet generatedKeys = statement.getGeneratedKeys();
        resultSets.add(generatedKeys);

        if (generatedKeys.next()) {
            return generatedKeys.getInt(1);
        } else {
            throw new SQLException("Insert failed, no generated keys.");
        }
    }

    public void close() {
        for (ResultSet resultSet: resultSets) {
            try {
                if (resultSet != null && !resultSet.isClosed()) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                CinemaLogger.getLogger().log(Level.WARNING, "Error while closing result set.", e);
            }
        }
        for (Statement statement: statements) {
            try {
                if (statement != null && !statement.isClosed()) {
                    statement.close();
                }
            } catch (SQLException e) {
                CinemaLogger.getLogger().log(Level.WARNING, "Error while closing statement.", e);
            }
        }
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            CinemaLogger.getLogger().log(Level.WARNING, "Error while closing connection.", e);
        }
    }
}
