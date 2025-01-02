package com.user.dao;

import com.user.model.User;
import com.user.model.Role;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/online_job_portal"; // Database URL
    private static final String USERNAME = "root"; 
    private static final String PASSWORD = "your_password"; // MySQL password

    // Establish connection to the database
    private Connection getConnection() throws SQLException {
        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("MySQL JDBC driver not found");
        }
    }

    // Add User to the database
    public void addUser(User user) {
        String sql = "INSERT INTO Users (username, password, email, role_id, created_at) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setInt(4, user.getRole().ordinal() + 1); // Get role id (role.ordinal() maps to the appropriate value in the Roles table)
            stmt.setTimestamp(5, new Timestamp(user.getCreatedAt().getTime()));

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User added successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get User by ID
    public User getUserById(int id) {
        User user = null;
        String sql = "SELECT * FROM Users WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                user = new User(
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("email"),
                    Role.valueOf(rs.getString("role"))
                );
                user.setId(rs.getInt("id"));
                user.setCreatedAt(rs.getTimestamp("created_at"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    // Get all users
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM Users";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                User user = new User(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email"),
                        Role.valueOf(rs.getString("role"))
                );
                user.setId(rs.getInt("id"));
                user.setCreatedAt(rs.getTimestamp("created_at"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    // Update a user's data
    public void updateUser(User user) {
        String sql = "UPDATE Users SET username = ?, password = ?, email = ?, role_id = ?, created_at = ? WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setInt(4, user.getRole().ordinal() + 1); // Convert Enum to id
            stmt.setTimestamp(5, new Timestamp(user.getCreatedAt().getTime()));
            stmt.setInt(6, user.getId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User updated successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete a user by ID
    public void deleteUser(int id) {
        String sql = "DELETE FROM Users WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User deleted successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
