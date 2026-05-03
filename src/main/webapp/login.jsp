<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Log In</title>
    <style>
        * { box-sizing: border-box; margin: 0; padding: 0; }
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: #f0f2f5;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
        }
        .card {
            background: white;
            padding: 40px;
            border-radius: 12px;
            box-shadow: 0 4px 20px rgba(0,0,0,0.1);
            width: 100%;
            max-width: 420px;
        }
        h2 { color: #333; margin-bottom: 8px; }
        .subtitle { color: #666; margin-bottom: 24px; font-size: 14px; }
        label { display: block; margin-bottom: 4px; font-size: 14px; color: #444; font-weight: 500; }
        input {
            width: 100%;
            padding: 10px 14px;
            border: 1.5px solid #ddd;
            border-radius: 8px;
            font-size: 15px;
            margin-bottom: 16px;
            transition: border-color 0.2s;
        }
        input:focus { outline: none; border-color: #1976D2; }
        button {
            width: 100%;
            padding: 12px;
            background: #1976D2;
            color: white;
            border: none;
            border-radius: 8px;
            font-size: 16px;
            cursor: pointer;
            font-weight: 600;
            transition: background 0.2s;
        }
        button:hover { background: #1565C0; }
        .error {
            background: #FFEBEE;
            color: #C62828;
            padding: 10px 14px;
            border-radius: 8px;
            font-size: 14px;
            margin-bottom: 16px;
            border-left: 4px solid #C62828;
        }
        .success {
            background: #E8F5E9;
            color: #2E7D32;
            padding: 10px 14px;
            border-radius: 8px;
            font-size: 14px;
            margin-bottom: 16px;
            border-left: 4px solid #2E7D32;
        }
        .footer { text-align: center; margin-top: 20px; font-size: 14px; color: #666; }
        .footer a { color: #1976D2; text-decoration: none; font-weight: 500; }
        .footer a:hover { text-decoration: underline; }
    </style>
</head>
<body>

<div class="card">
    <h2>Welcome Back</h2>
    <p class="subtitle">Log in to your account</p>

    <%-- ✅ Display error from servlet --%>
    <% String error = (String) request.getAttribute("error"); %>
    <% if (error != null) { %>
        <div class="error"><%= error %></div>
    <% } %>

    <%-- ✅ Display success messages based on URL parameter --%>
    <% String msg = request.getParameter("msg"); %>
    <% if ("check_email".equals(msg)) { %>
        <div class="success">
            ✅ Account created! Please check your email and click the verification link before logging in.
        </div>
    <% } else if ("verified".equals(msg)) { %>
        <div class="success">
            ✅ Email verified successfully! You can now log in.
        </div>
    <% } else if ("logged_out".equals(msg)) { %>
        <div class="success">
            You have been logged out successfully.
        </div>
    <% } %>

    <%-- ✅ Form submits to /login (handled by LoginServlet) --%>
    <form action="${pageContext.request.contextPath}/login" method="post">

        <label for="email">Email Address</label>
        <input type="email" id="email" name="email" placeholder="john@example.com" required>

        <label for="password">Password</label>
        <input type="password" id="password" name="password" placeholder="Your password" required>

        <button type="submit">Log In</button>
    </form>

    <div class="footer">
        Don't have an account? <a href="${pageContext.request.contextPath}/signup">Sign up</a>
    </div>
</div>

</body>
</html>
