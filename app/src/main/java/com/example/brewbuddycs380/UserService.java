/**
 * This class represents a user service for creating and managing user accounts.
 * It provides methods for creating new accounts, logging in, and managing user preferences.
 * The service interacts with a MySQL database using JDBC for data storage and retrieval.
 *
 * <p>To use this service, you need to set the connection details for the MySQL server by modifying
 * the URL, USER, and PASS constants in this class.
 *
 * <p>The service uses executor and future objects to perform database operations in separate threads
 * to avoid blocking the main network thread. The executor is shut down after each operation.
 *
 * <p>The service also provides helper methods for hashing passwords using the SHA-512 algorithm.
 */
package com.example.brewbuddycs380;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Custom exception class for indicating that an account is already taken.
 */
class AccountTakenException extends Exception {
    /**
     * Constructs a new AccountTakenException with the specified error message.
     *
     * @param message the error message
     */
    public AccountTakenException(String message) {
        super(message);
    }
}

/**
 * Custom exception class for indicating an error in the UserService.
 */
class UserServiceException extends Exception {
    /**
     * Constructs a new UserServiceException with the specified error message.
     *
     * @param message the error message
     */
    public UserServiceException(String message) {
        super(message);
    }
}

/**
 * The UserService class provides methods for creating, managing, and interacting with user accounts.
 */
public class UserService {
    // Connection details for the MySQL server
    static final String URL = "jdbc:mysql://btouleodk2fbxqykpyko-mysql.services.clever-cloud.com:3306/btouleodk2fbxqykpyko";
    static final String USER = "usy1lqwxdmnjkhyy";
    static final String PASS = "PafizbM2VI0g4D4Etsy3";
    private static String userName;
    public static ArrayList<Coffee> shoppingCart = new ArrayList<>();


    /**
     * Sets the username for the current session if it's not already set.
     *
     * @param input the username to set
     */
    public static void setUsername(String input) {
        if (userName == null)
            userName = input;
    }

    /**
     * Gets the username for the current session.
     *
     * @return the username
     */
    public static String getUsername() {
        return userName;
    }

    /**
     * Creates a new user account with the given username and password.
     *
     * @param username the username for the new account
     * @param password the password for the new account
     * @return true if the account was created successfully, false otherwise
     * @throws AccountTakenException  if the username is already taken
     * @throws UserServiceException  if an error occurs during the account creation process
     */
    public static boolean createAccount(final String username, final String password)
            throws AccountTakenException, UserServiceException {
        // Have to use executor because database won't connect on main network thread for some reason
        // This object makes a new thread just for the database
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Boolean> future = executor.submit(() -> {
            try (Connection connection = DriverManager.getConnection(URL, USER, PASS)) {
                // Check if the username is already taken
                String checkSql = "SELECT * FROM logins WHERE username = ?";
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
                String sql = "INSERT INTO logins (username, password) VALUES (?, ?)";
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

    /**
     * Saves user preferences for the specified username.
     *
     * @param username    the username for which to save the preferences
     * @param preferences the preferences to save
     * @return true if the preferences were saved successfully, false otherwise
     * @throws UserServiceException if an error occurs during the preference saving process
     */
    public static boolean saveUserPreferences(final String username, final String preferences)
            throws UserServiceException {
        // Have to use executor because database won't connect on main network thread for some reason
        // This object makes a new thread just for the database
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Boolean> future = executor.submit(() -> {
            try (Connection connection = DriverManager.getConnection(URL, USER, PASS)) {
                // SQL statement to update the userPrefs column for the given username
                String sql = "UPDATE logins SET userPrefs = ? WHERE username = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, preferences);
                statement.setString(2, username);
                int rowsUpdated = statement.executeUpdate();
                return rowsUpdated > 0;
            } catch (SQLException e) {
                throw new UserServiceException("SQLException: " + e.getMessage());
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

    /**
     * Retrieves the user preferences for the specified username.
     *
     * @param username the username for which to retrieve the preferences
     * @return the user preferences if found, or null if not found
     * @throws UserServiceException if an error occurs during the preference retrieval process
     */
    public static String getUserPreferences(final String username) throws UserServiceException {
        // Have to use executor because database won't connect on main network thread for some reason
        // This object makes a new thread just for the database
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(() -> {
            try (Connection connection = DriverManager.getConnection(URL, USER, PASS)) {
                // SQL statement to select the userPrefs column for the given username
                String sql = "SELECT userPrefs FROM logins WHERE username = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, username);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    return resultSet.getString("userPrefs");
                } else {
                    return null;
                }
            } catch (SQLException e) {
                throw new UserServiceException("SQLException: " + e.getMessage());
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

    /**
     * Logs in with the given username and password.
     *
     * @param username the username to log in
     * @param password the password to log in
     * @return true if the login is successful, false otherwise
     * @throws UserServiceException if an error occurs during the login process
     */
    public static boolean login(final String username, final String password) throws UserServiceException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Boolean> future = executor.submit(() -> {
            try (Connection connection = DriverManager.getConnection(URL, USER, PASS)) {
                // Hash the password using the SHA-512 algorithm
                String hashedPassword = hashPassword(password);
                // SQL statement to query the logins table for a record with the given username and hashed password
                String sql = "SELECT * FROM logins WHERE username = ? AND password = ?";
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

    /**
     * Hashes the given password using the SHA-512 algorithm.
     *
     * @param password the password to hash
     * @return the hashed password
     * @throws NoSuchAlgorithmException if the SHA-512 algorithm is not available
     */
    private static String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-512");
        byte[] hashedBytes = digest.digest(password.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : hashedBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    /**
     * adds coffee to the cart
     * @param coffee
     */
    public static void addToCart(Coffee coffee){
        shoppingCart.add(coffee);
    }

    /**
     * updates the user's preferences with the coffee's they selected.
     * @return
     */
    public static boolean checkOut(){
        if(shoppingCart.size()==0)
            return false;
        shoppingCart.clear();
        return true;
    }
}