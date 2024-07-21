package org.example.tinderapp.service;

import org.example.tinderapp.domain.entity.User;
import org.example.tinderapp.domain.repository.UserRepositoryImpl;
import org.example.tinderapp.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private static final String JDBC_URL = "jdbc:postgresql://localhost:5433/postgres";
    private static final String JDBC_USERNAME = "postgres";
    private static final String JDBC_PASSWORD = "postgres";
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private static final String INSERT_SQL = "INSERT INTO users (name, surname, user_name, email, phone_number, password) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SELECT_BY_USERNAME_SQL = "SELECT * FROM users WHERE user_name = ?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM users";
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM users WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM users WHERE id = ?";
    private static final String UPDATE_SQL = "UPDATE users SET name = ?, surname = ?, user_name = ?, email = ?, phone_number = ?, password = ? WHERE id = ?";
    private static final String PARTIAL_UPDATE_SQL = "UPDATE users SET name = COALESCE(?, name), surname = COALESCE(?, surname), user_name = COALESCE(?, user_name), email = COALESCE(?, email), phone_number = COALESCE(?, phone_number), password = COALESCE(?, password) WHERE id = ?";
    private static final String SELECT_BY_USERNAME_AND_PASSWORD_SQL = "SELECT * FROM users WHERE user_name = ? AND password = ?";

    private final UserRepositoryImpl userRepositoryImpl;

    public UserService(UserRepositoryImpl userRepositoryImpl) {
        this.userRepositoryImpl = userRepositoryImpl;
    }

    public Long getUserId(String username, String password) throws UserNotFoundException {
        return userRepositoryImpl.getUserId(username, password);
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD);
    }

    public User register(String name, String surname, String username, String email, String phoneNumber, String password) throws SQLException {
        User user = new User();

        if (isUsernameTaken(username)) {
            throw new SQLException("Registration failed, username already exists");
        }

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, surname);
            preparedStatement.setString(3, username);
            preparedStatement.setString(4, email);
            preparedStatement.setString(5, phoneNumber);
            preparedStatement.setString(6, password);

            int rows = preparedStatement.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Registration failed, fields are not filled correctly");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getLong(1));
                    user.setName(name);
                    user.setSurname(surname);
                    user.setUserName(username);
                    user.setEmail(email);
                    user.setPhoneNumber(phoneNumber);
                    user.setPassword(password);
                } else {
                    throw new SQLException("Registration failed, ID not obtained");
                }
            }
        } catch (SQLException e) {
            logger.error("Error occurred during user registration: ", e);
            throw new SQLException("Error occurred during registration", e);
        }
        return user;
    }

    private boolean isUsernameTaken(String username) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_USERNAME_SQL)) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            logger.error("Error occurred while checking if username is taken: ", e);
            throw new SQLException("Error occurred while checking username", e);
        }
    }

    public boolean login(String username, String password) throws SQLException {
        try {
            return getUserId(username, password) != null;
        } catch (UserNotFoundException e) {
            logger.error("User not found: ", e);
            return false;
        }
    }

    public void deleteUser(Long id) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SQL)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error occurred while deleting user with ID {}: ", id, e);
            throw new RuntimeException("Failed to delete user with ID " + id, e);
        }
    }

    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_ALL_SQL)) {
            while (resultSet.next()) {
                User user = extractUserFromResultSet(resultSet);
                users.add(user);
            }
        } catch (SQLException e) {
            logger.error("Error occurred while retrieving users: ", e);
            throw new RuntimeException("Failed to retrieve users", e);
        }
        return users;
    }

    private User extractUserFromResultSet(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("id"));
        user.setName(resultSet.getString("name"));
        user.setSurname(resultSet.getString("surname"));
        user.setUserName(resultSet.getString("user_name"));
        user.setEmail(resultSet.getString("email"));
        user.setPhoneNumber(resultSet.getString("phone_number"));
        user.setPassword(resultSet.getString("password"));
        return user;
    }
}
