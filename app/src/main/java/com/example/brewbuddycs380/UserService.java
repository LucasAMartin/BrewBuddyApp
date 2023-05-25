package com.example.brewbuddycs380;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class AccountTakenException extends Exception {
    public AccountTakenException(String message) {
        super(message);
    }
}

class UserServiceException extends Exception {
    public UserServiceException(String message) {
        super(message);
    }
}

public class UserService {
    // Connection details for the MySQL server
    static final String URL = "jdbc:mysql://sql9.freemysqlhosting.net/sql9619545";
    static final String USER = "sql9619545";
    static final String PASS = "TALaShDLMD";

    // Method to create a new account with the given username and password
    public static boolean createAccount(final String username, final String password) throws AccountTakenException, UserServiceException {
        // Have to use executor because database won't connect on main network thread for some reason
        // This object makes a new thread just for the database
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Boolean> future = executor.submit(() -> {
            try (Connection connection = DriverManager.getConnection(URL, USER, PASS)) {
                // Check if the username is already taken
                String checkSql = "SELECT * FROM sql9619545.logins WHERE username = ?";
                PreparedStatement checkStatement = connection.prepareStatement(checkSql);
                checkStatement.setString(1, username);
                ResultSet checkResultSet = checkStatement.executeQuery();
                if (checkResultSet.next()) {
                    // Username is already taken
                    throw new AccountTakenException("Username is already taken");
                }

                // Hash the password using the SHA-512 algorithm
                String hashedPassword = hashPassword(password);
                // SQL statement to insert a new record into the logins table
                String sql = "INSERT INTO sql9619545.logins (username, password) VALUES (?, ?)";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, username);
                statement.setString(2, hashedPassword);
                int rowsInserted = statement.executeUpdate();
                return rowsInserted > 0;
            } catch (SQLException e) {
                throw new UserServiceException("SQLException: " + e.getMessage());
            } catch (NoSuchAlgorithmException e) {
                throw new UserServiceException("NoSuchAlgorithmException: " + e.getMessage());
            }
        });
        try {
            return future.get();
        } catch (Exception e) {
            if (e.getCause() instanceof AccountTakenException) {
                throw (AccountTakenException) e.getCause();
            } else if (e.getCause() instanceof UserServiceException) {
                throw (UserServiceException) e.getCause();
            }
            throw new UserServiceException("Unknown exception: " + e.getMessage());
        } finally {
            executor.shutdown();
        }
    }

    // Method to log in with the given username and password
    public static boolean login(final String username, final String password) throws UserServiceException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Boolean> future = executor.submit(() -> {
            try (Connection connection = DriverManager.getConnection(URL, USER, PASS)) {
                // Hash the password using the SHA-512 algorithm
                String hashedPassword = hashPassword(password);
                // SQL statement to query the logins table for a record with the given username and hashed password
                String sql = "SELECT * FROM sql9619545.logins WHERE username = ? AND password = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, username);
                statement.setString(2, hashedPassword);
                ResultSet resultSet = statement.executeQuery();
                return resultSet.next();
            } catch (SQLException e) {
                throw new UserServiceException("SQLException: " + e.getMessage());
            } catch (NoSuchAlgorithmException e) {
                throw new UserServiceException("NoSuchAlgorithmException: " + e.getMessage());
            }
        });
        try {
            return future.get();
        } catch (Exception e) {
            if (e.getCause() instanceof UserServiceException) {
                throw (UserServiceException) e.getCause();
            }
            throw new UserServiceException("Unknown exception: " + e.getMessage());
        } finally {
            executor.shutdown();
        }
    }

    // Helper method to hash a password using the SHA-512 algorithm
    private static String hashPassword(String password) throws NoSuchAlgorithmException {
        // Get an instance of the SHA-512 message digest
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(password.getBytes());

        byte[] digest = md.digest();
        StringBuilder sb = new StringBuilder();
        // Convert each byte of the message digest to a two-digit hexadecimal string
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }
        // Return the resulting hexadecimal string
        return sb.toString();
    }
}

