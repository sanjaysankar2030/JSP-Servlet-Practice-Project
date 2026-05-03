package com.myapp.servlet;

import com.myapp.db.DBConnection;
import com.myapp.util.SendEmail;
import com.myapp.util.TokenGenerator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.mindrot.jbcrypt.BCrypt;

/**
 * SignupServlet.java
 * -------------------
 * Handles user registration (signup).
 *
 * FLOW:
 *   1. User submits the signup form (POST /signup)
 *   2. We check if the email already exists in the database
 *   3. We hash the password using BCrypt (NEVER store plain-text passwords!)
 *   4. We generate a unique email verification token
 *   5. We save the user in the database (is_verified = false)
 *   6. We send a verification email with a link containing the token
 *   7. We redirect the user to a "check your email" page
 */
@WebServlet("/signup")
public class SignupServlet extends HttpServlet {

    // ✅ Change this to your actual server address when deploying
    private static final String BASE_URL = "http://localhost:8080/myapp";

    /**
     * GET /signup → Show the signup form
     */
    @Override
    protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        request.getRequestDispatcher("/signup.jsp").forward(request, response);
    }

    /**
     * POST /signup → Process the signup form
     */
    @Override
    protected void doPost(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        // Step 1: Read form inputs
        String name = request.getParameter("name").trim();
        String email = request.getParameter("email").trim().toLowerCase();
        String password = request.getParameter("password");

        // Basic validation
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            request.setAttribute("error", "All fields are required.");
            request
                .getRequestDispatcher("/signup.jsp")
                .forward(request, response);
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            // Step 2: Check if email already exists
            String checkSQL = "SELECT id FROM users WHERE email = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSQL);
            checkStmt.setString(1, email);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                // Email already registered
                request.setAttribute(
                    "error",
                    "This email is already registered. Please log in."
                );
                request
                    .getRequestDispatcher("/signup.jsp")
                    .forward(request, response);
                return;
            }

            // Step 3: Hash the password securely with BCrypt
            // BCrypt automatically handles salting — each hash is unique even for same password
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

            // Step 4: Generate a unique verification token
            String token = TokenGenerator.generate();

            // Step 5: Insert user into the database (is_verified = false by default)
            String insertSQL =
                "INSERT INTO users (name, email, password, token, is_verified) VALUES (?, ?, ?, ?, false)";
            PreparedStatement insertStmt = conn.prepareStatement(insertSQL);
            insertStmt.setString(1, name);
            insertStmt.setString(2, email);
            insertStmt.setString(3, hashedPassword);
            insertStmt.setString(4, token);
            insertStmt.executeUpdate();

            // Step 6: Send verification email
            String verifyLink = BASE_URL + "/verify?token=" + token;
            String emailSubject = "Verify Your Email Address";
            String emailBody =
                "<h2>Hello " +
                name +
                "!</h2>" +
                "<p>Thank you for signing up. Please click the link below to verify your email:</p>" +
                "<a href='" +
                verifyLink +
                "' style='background:#4CAF50;color:white;padding:10px 20px;" +
                "text-decoration:none;border-radius:5px;'>Verify My Email</a>" +
                "<p>If you did not sign up, ignore this email.</p>";

            SendEmail.send(email, emailSubject, emailBody);

            // Step 7: Redirect to success message page
            response.sendRedirect(
                request.getContextPath() + "/login.jsp?msg=check_email"
            );
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute(
                "error",
                "Something went wrong: " + e.getMessage()
            );
            request
                .getRequestDispatcher("/signup.jsp")
                .forward(request, response);
        }
    }
}
