<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%-- ✅ SESSION GUARD: Redirect to login if user is not logged in --%>
<%
    if (session.getAttribute("userId") == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    String userName  = (String) session.getAttribute("userName");
    String userEmail = (String) session.getAttribute("userEmail");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Dashboard</title>
    <style>
        * { box-sizing: border-box; margin: 0; padding: 0; }
        body { font-family: 'Segoe UI', sans-serif; background: #f0f2f5; }
        header {
            background: #1976D2;
            color: white;
            padding: 16px 32px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        header h1 { font-size: 20px; }
        header a {
            color: white;
            text-decoration: none;
            background: rgba(255,255,255,0.2);
            padding: 8px 16px;
            border-radius: 6px;
            font-size: 14px;
        }
        header a:hover { background: rgba(255,255,255,0.3); }
        .container { max-width: 700px; margin: 60px auto; padding: 0 20px; }
        .welcome-card {
            background: white;
            padding: 40px;
            border-radius: 12px;
            box-shadow: 0 4px 20px rgba(0,0,0,0.08);
            text-align: center;
        }
        .avatar {
            width: 72px; height: 72px;
            background: #1976D2;
            border-radius: 50%;
            display: flex; align-items: center; justify-content: center;
            font-size: 28px; color: white; font-weight: bold;
            margin: 0 auto 20px;
        }
        h2 { color: #333; margin-bottom: 8px; }
        p { color: #666; font-size: 15px; }
        .badge {
            display: inline-block;
            background: #E8F5E9;
            color: #2E7D32;
            padding: 4px 12px;
            border-radius: 20px;
            font-size: 13px;
            margin-top: 12px;
        }
    </style>
</head>
<body>

<header>
    <h1>My App</h1>
    <a href="${pageContext.request.contextPath}/logout">Log Out</a>
</header>

<div class="container">
    <div class="welcome-card">
        <%-- Show first letter of name as avatar --%>
        <div class="avatar"><%= userName.charAt(0) %></div>

        <h2>Welcome, <%= userName %>! 🎉</h2>
        <p>You have successfully logged in.</p>
        <p style="margin-top:8px;">Email: <strong><%= userEmail %></strong></p>
        <span class="badge">✅ Email Verified</span>
    </div>
</div>

</body>
</html>
