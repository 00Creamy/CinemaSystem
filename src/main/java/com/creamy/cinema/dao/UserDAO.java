package com.creamy.cinema.dao;

import com.creamy.cinema.models.User;
import com.creamy.cinema.util.CinemaException;
import com.creamy.cinema.util.DBConnection;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserDAO {
    private static final int SALT_LENGTH = 16;
    private static final int INTERATIONS = 25565;

    public static  void init() {
        DBConnection connection = null;
        try {
            connection = new DBConnection();
            User adminUser = requestUserByUsername(connection, "creamy");
            if (adminUser == null) {
                User user = new User();
                user.setUsername("creamy");
                user.setPassword("dausazam");
                user.setAccessLevel(User.AccessLevel.ADMIN);
                user.setName("Muhammad Nor Firdaus Bin Norazam");
                user.setEmail("dausazam@gmail.com");
                user.setPhoneNo("01162618610");
                createUser(connection, user);
            }
        } catch (CinemaException e) {
            if (connection != null) connection.close();
        }
    }

    public static void createUser(DBConnection connection, User user) throws CinemaException {
        try {
            PreparedStatement statement = connection.prepareInsertStatement("INSERT INTO user (username, password, salt, level, name, email, phone_no) VALUES (?, ?, ?, ?, ?, ?, ?)");

            byte[] salt = generateSalt();
            byte[] hashedPassword = hashPassword(user.getPassword(), salt);

            statement.setString(1, user.getUsername());
            statement.setBytes(2, hashedPassword);
            statement.setBytes(3, salt);
            statement.setInt(4, user.getAccessLevel().getLevel());
            statement.setString(5, user.getName());
            statement.setString(6, user.getEmail());
            statement.setString(7, user.getPhoneNo());

            int userId = connection.executeInsertStatement(statement);
            user.setUserId(userId);
        } catch (SQLException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new CinemaException("Error when trying to create user.", e);
        }
    }

    public static List<User> requestUsers(DBConnection connection) throws CinemaException {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM user");
            ArrayList<User> users = new ArrayList<>();
            ResultSet resultSet = connection.executeStatement(statement);
            while (resultSet.next()) {
                users.add(getUserFromResultSet(resultSet));
            }
            return users;
        } catch (SQLException e) {
            throw new CinemaException("Error when trying to request user.", e);
        }
    }

    public static User requestUserByUserId(DBConnection connection, int userId) throws CinemaException {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM user WHERE user_id=?");
            statement.setInt(1, userId);

            ResultSet resultSet = connection.executeStatement(statement);
            if (resultSet.next()) {
                return getUserFromResultSet(resultSet);
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new CinemaException("Error when trying to request user.", e);
        }
    }

    public static User requestUserByUsername(DBConnection connection, String username) throws CinemaException {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM user WHERE username=?");
            statement.setString(1, username);

            ResultSet resultSet = connection.executeStatement(statement);
            if (resultSet.next()) {
                return getUserFromResultSet(resultSet);
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new CinemaException("Error when trying to request user.", e);
        }
    }

    public static User requestUserByUsernamePassword(DBConnection connection, String username, String password) throws CinemaException {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM user WHERE username=?");
            statement.setString(1, username);

            ResultSet resultSet = connection.executeStatement(statement);
            if (resultSet.next()) {
                byte[] salt = resultSet.getBytes("salt");
                byte[] storedHash = resultSet.getBytes("password");
                byte[] hashedPassword = hashPassword(password, salt);
                if (Arrays.equals(storedHash, hashedPassword)) {
                    return getUserFromResultSet(resultSet);
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch (SQLException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new CinemaException("Error when trying to request user.", e);
        }
    }

    public static boolean updateUser(DBConnection connection, User user) throws CinemaException {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE user SET session_version=?, username=?, level=?, name=?, email=?, phone_no=?, deleted=? WHERE user_id=?");
            statement.setInt(1, user.getSessionVersion());
            statement.setString(2, user.getUsername());
            statement.setInt(3, user.getAccessLevel().getLevel());
            statement.setString(4, user.getName());
            statement.setString(5, user.getEmail());
            statement.setString(6, user.getPhoneNo());
            statement.setBoolean(7, user.isDeleted());
            statement.setInt(8, user.getUserId());

            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new CinemaException("Error when trying to update user.", e);
        }
    }

    public static boolean updateUserPassword(DBConnection connection, User user) throws CinemaException {
        try {
            byte[] salt = generateSalt();
            byte[] hashedPassword = hashPassword(user.getPassword(), salt);

            PreparedStatement statement = connection.prepareStatement("UPDATE user SET password=?, salt=? WHERE user_id=?");
            statement.setBytes(1, hashedPassword);
            statement.setBytes(2, salt);
            statement.setInt(3, user.getUserId());

            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new CinemaException("Error when trying to update user.", e);
        }
    }

    private static byte[] hashPassword(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, INTERATIONS, 512);
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        return secretKeyFactory.generateSecret(spec).getEncoded();
    }

    private static byte[] generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    private static User getUserFromResultSet(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setUserId(resultSet.getInt("user_id"));
        user.setSessionVersion(resultSet.getInt("session_version"));
        user.setUsername(resultSet.getString("username"));
        user.setAccessLevel(User.AccessLevel.getLevelByLevel(resultSet.getInt("level")));
        user.setName(resultSet.getString("name"));
        user.setEmail(resultSet.getString("email"));
        user.setPhoneNo(resultSet.getString("phone_no"));
        user.setDeleted(resultSet.getBoolean("deleted"));
        return user;
    }
}
