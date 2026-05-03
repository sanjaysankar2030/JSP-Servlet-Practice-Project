package com.myapp.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * LogoutServlet.java
 * -------------------
 * Destroys the user session and redirects to the login page.
 */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        // Invalidate (destroy) the session
        HttpSession session = request.getSession(false); // false = don't create new session
        if (session != null) {
            session.invalidate();
        }
        // Redirect to login page
        response.sendRedirect(
            request.getContextPath() + "/login.jsp?msg=logged_out"
        );
    }
}
