package org.example;

import java.sql.*;

public class Main {

    // PostgreSQL connection details
    private static final String URL = "jdbc:postgresql://localhost:5432/assignment3";
    private static final String USER = "postgres";
    private static final String PASSWORD = "admin"; // replace with your actual pgAdmin password

    // Establish database connection
    private static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Retrieve and display all students
    public static void getAllStudents() {
        String sql = "SELECT * FROM app.students ORDER BY student_id";
        try (Connection conn = connect();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            System.out.printf("%-5s %-10s %-10s %-30s %-12s%n",
                    "ID", "First", "Last", "Email", "EnrollDate");
            System.out.println("---------------------------------------------------------");

            while (rs.next()) {
                System.out.printf("%-5d %-10s %-10s %-30s %-12s%n",
                        rs.getInt("student_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getDate("enrollment_date"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    // Add a new student
    public static void addStudent(String first, String last, String email, String date) {
        String sql = "SET search_path TO app, public; INSERT INTO students (first_name, last_name, email, enrollment_date) VALUES (?, ?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, first);
            ps.setString(2, last);
            ps.setString(3, email);
            ps.setDate(4, Date.valueOf(date));
            ps.executeUpdate();

            System.out.println("Student added successfully.");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Update student email by ID
    public static void updateStudentEmail(int id, String newEmail) {
        String sql = "SET search_path TO app, public; UPDATE students SET email = ? WHERE student_id = ?";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newEmail);
            ps.setInt(2, id);
            int rows = ps.executeUpdate();

            System.out.println("Updated " + rows + " row(s).");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Delete student by ID
    public static void deleteStudent(int id) {
        String sql = "SET search_path TO app, public; DELETE FROM students WHERE student_id = ?";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int rows = ps.executeUpdate();

            System.out.println("Deleted " + rows + " row(s).");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Main method
    public static void main(String[] args) {
        System.out.println("Initial students:");
        getAllStudents();

        System.out.println("\nAdding a new student...");
        addStudent("Chhavi", "Rajpal", "chhavi.rajpal@example.com", "2025-10-27");

        System.out.println("\nUpdating John's email...");
        updateStudentEmail(1, "johnny.doe@example.com");  // Update by ID

        System.out.println("\nDeleting Jim...");
        deleteStudent(3);  // Delete by ID

        System.out.println("\nFinal student list:");
        getAllStudents();
    }
}
