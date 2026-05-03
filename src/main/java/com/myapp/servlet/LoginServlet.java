package com.myapp.servlet;

import com.myapp.db.DBConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.mindrot.jbcrypt.BCrypt;

/**
 * LoginServlet.java
 * ------------------
 * Handles user login.
 *
 * FLOW:
 *   1. User submits the login form (POST /login)
 *   2. We look up the user by email in the database
 *   3. We check if their email has been verified
 *   4. We use BCrypt to compare the entered password with the stored hash
 *   5. If valid → create a session and redirect to dashboard
 *   6. If invalid → show an error on the login page
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    /**
     * GET /login → Show the login page
     */
    @Override
    protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    /**
     * POST /login → Process the login form
     */
    @Override
    protected void doPost(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        // Step 1: Read form inputs
        String email = request.getParameter("email").trim().toLowerCase();
        String password = request.getParameter("password");

        if (email.isEmpty() || password.isEmpty()) {
            request.setAttribute("error", "Email and password are required.");
            request
                .getRequestDispatcher("/login.jsp")
                .forward(request, response);
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            // Step 2: Find user by email
            String sql =
                "SELECT id, name, password, is_verified FROM users WHERE email = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                // No user found with that email
                request.setAttribute("error", "Invalid email or password.");
                request
                    .getRequestDispatcher("/login.jsp")
                    .forward(request, response);
                return;
            }

            // Step 3: Check if email is verified
            boolean isVerified = rs.getBoolean("is_verified");
            if (!isVerified) {
                request.setAttribute(
                    "error",
                    "Please verify your email before logging in. Check your inbox."
                );
                request
                    .getRequestDispatcher("/login.jsp")
                    .forward(request, response);
                return;
            }

            // Step 4: Compare entered password with stored BCrypt hash
            String storedHash = rs.getString("password");
            if (!BCrypt.checkpw(password, storedHash)) {
                // Password does not match
                request.setAttribute("error", "Invalid email or password.");
                request
                    .getRequestDispatcher("/login.jsp")
                    .forward(request, response);
                return;
            }

            // Step 5: Password matches! Create a session for the logged-in user
            HttpSession session = request.getSession();
            session.setAttribute("userId", rs.getInt("id"));
            session.setAttribute("userName", rs.getString("name"));
            session.setAttribute("userEmail", email);

            // Step 6: Redirect to the dashboard (protected page)
            response.sendRedirect(request.getContextPath() + "/dashboard.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute(
                "error",
                "Something went wrong. Please try again."
            );
            request
                .getRequestDispatcher("/login.jsp")
                .forward(request, response);
        }
    }
}
