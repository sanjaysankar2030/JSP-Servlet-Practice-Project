<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sign Up</title>
    <style>
        /* ── Simple clean styling ── */
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
        input:focus { outline: none; border-color: #4CAF50; }
        button {
            width: 100%;
            padding: 12px;
            background: #4CAF50;
            color: white;
            border: none;
            border-radius: 8px;
            font-size: 16px;
            cursor: pointer;
            font-weight: 600;
            transition: background 0.2s;
        }
        button:hover { background: #388E3C; }
        .error {
            background: #FFEBEE;
            color: #C62828;
            padding: 10px 14px;
            border-radius: 8px;
            font-size: 14px;
            margin-bottom: 16px;
            border-left: 4px solid #C62828;
        }
        .footer { text-align: center; margin-top: 20px; font-size: 14px; color: #666; }
        .footer a { color: #4CAF50; text-decoration: none; font-weight: 500; }
        .footer a:hover { text-decoration: underline; }
    </style>
</head>
<body>

<div class="card">
    <h2>Create an Account</h2>
    <p class="subtitle">Sign up and verify your email to get started</p>

    <%-- ✅ Display error message if set by the servlet --%>
    <% String error = (String) request.getAttribute("error"); %>
    <% if (error != null) { %>
        <div class="error"><%= error %></div>
    <% } %>

    <%-- ✅ Form submits to /signup (handled by SignupServlet) --%>
    <form action="${pageContext.request.contextPath}/signup" method="post">

        <label for="name">Full Name</label>
        <input type="text" id="name" name="name" placeholder="John Doe" required>

        <label for="email">Email Address</label>
        <input type="email" id="email" name="email" placeholder="john@example.com" required>

        <label for="password">Password</label>
        <input type="password" id="password" name="password" placeholder="Min. 8 characters" required minlength="8">

        <button type="submit">Create Account</button>
    </form>

    <div class="footer">
        Already have an account? <a href="${pageContext.request.contextPath}/login">Log in</a>
    </div>
</div>

</body>
</html>
