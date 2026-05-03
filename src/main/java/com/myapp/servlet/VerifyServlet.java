package com.myapp.servlet;

import com.myapp.db.DBConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * VerifyServlet.java
 * -------------------
 * Handles the email verification link.
 *
 * FLOW:
 *   1. User clicks the verification link in their email: /verify?token=abc123
 *   2. We read the token from the URL query parameter
 *   3. We look up the token in the database
 *   4. If found and not expired → mark user as verified (is_verified = true)
 *   5. Redirect user to login with a success message
 *   6. If token is invalid or already used → show an error
 */
@WebServlet("/verify")
public class VerifyServlet extends HttpServlet {

    /**
     * GET /verify?token=... → Process the verification link
     */
    @Override
    protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        // Step 1: Read the token from the URL
        String token = request.getParameter("token");

        if (token == null || token.isEmpty()) {
            request.setAttribute(
                "error",
                "Invalid verification link. No token provided."
            );
            request
                .getRequestDispatcher("/login.jsp")
                .forward(request, response);
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            // Step 2: Find the user with this token (and not already verified)
            String selectSQL =
                "SELECT id, name FROM users WHERE token = ? AND is_verified = false";
            PreparedStatement selectStmt = conn.prepareStatement(selectSQL);
            selectStmt.setString(1, token);
            ResultSet rs = selectStmt.executeQuery();

            if (!rs.next()) {
                // Token not found or already used
                request.setAttribute(
                    "error",
                    "This verification link is invalid or has already been used. Please sign up again or log in."
                );
                request
                    .getRequestDispatcher("/login.jsp")
                    .forward(request, response);
                return;
            }

            // Step 3: Mark the user as verified and clear the token
            String updateSQL =
                "UPDATE users SET is_verified = true, token = NULL WHERE token = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateSQL);
            updateStmt.setString(1, token);
            int rowsUpdated = updateStmt.executeUpdate();

            if (rowsUpdated > 0) {
                // Step 4: Verification successful! Redirect to login
                System.out.println(
                    "✅ Email verified for user: " + rs.getString("name")
                );
                response.sendRedirect(
                    request.getContextPath() + "/login.jsp?msg=verified"
                );
            } else {
                request.setAttribute(
                    "error",
                    "Verification failed. Please try again."
                );
                request
                    .getRequestDispatcher("/login.jsp")
                    .forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute(
                "error",
                "Something went wrong during verification: " + e.getMessage()
            );
            request
                .getRequestDispatcher("/login.jsp")
                .forward(request, response);
        }
    }
}
