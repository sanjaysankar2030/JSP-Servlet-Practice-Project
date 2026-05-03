# Java Web App — JSP + Servlet + MySQL + Email Verification

A beginner-friendly Maven web application with user signup, login, and email verification.

---

## 📁 Project Structure

```
java-web-app/
├── pom.xml                                  ← Maven config & dependencies
├── database_setup.sql                       ← Run this to create MySQL DB
└── src/
    └── main/
        ├── java/
        │   └── com/myapp/
        │       ├── db/
        │       │   └── DBConnection.java    ← MySQL connection
        │       ├── servlet/
        │       │   ├── SignupServlet.java   ← Handles registration
        │       │   ├── LoginServlet.java    ← Handles login
        │       │   ├── VerifyServlet.java   ← Handles email verification
        │       │   └── LogoutServlet.java   ← Handles logout
        │       └── util/
        │           ├── SendEmail.java       ← JavaMail email sender
        │           └── TokenGenerator.java  ← UUID token generator
        └── webapp/
            ├── index.jsp                    ← Redirects to login
            ├── signup.jsp                   ← Signup form
            ├── login.jsp                    ← Login form
            ├── dashboard.jsp                ← Protected page after login
            └── WEB-INF/
                └── web.xml                  ← App configuration
```

---

## ✅ Prerequisites

Before you start, make sure you have installed:

1. **Java JDK 11+** — Download from https://adoptium.net
2. **Maven** — Download from https://maven.apache.org (or use VS Code extension)
3. **MySQL Server** — Download from https://dev.mysql.com/downloads/mysql/
4. **VS Code** with these extensions:
   - Extension Pack for Java (by Microsoft)
   - Maven for Java

---

## 🚀 Step-by-Step Setup

### Step 1: Set Up the Database

Open MySQL Workbench or a terminal and run:

```bash
mysql -u root -p < database_setup.sql
```

Or paste the contents of `database_setup.sql` into MySQL Workbench and run it.

This creates:
- A database called `myapp_db`
- A `users` table with: id, name, email, password, token, is_verified

---

### Step 2: Configure Database Credentials

Open `src/main/java/com/myapp/db/DBConnection.java` and change:

```java
private static final String DB_URL      = "jdbc:mysql://localhost:3306/myapp_db";
private static final String DB_USER     = "root";          // ← your MySQL username
private static final String DB_PASSWORD = "yourpassword";  // ← your MySQL password
```

---

### Step 3: Configure Email (Gmail)

Open `src/main/java/com/myapp/util/SendEmail.java` and change:

```java
private static final String FROM_EMAIL    = "youremail@gmail.com"; // ← your Gmail
private static final String EMAIL_PASSWORD = "your-app-password";  // ← App Password
```

**How to get a Gmail App Password:**
1. Go to your Google Account → Security
2. Enable 2-Step Verification (if not already enabled)
3. Go to: https://myaccount.google.com/apppasswords
4. Select "Mail" and "Windows Computer" → Generate
5. Copy the 16-character password and paste it above

---

### Step 4: Open in VS Code

```bash
# Open the project folder in VS Code
code java-web-app
```

VS Code will automatically detect it as a Maven project.

---

### Step 5: Build the Project

Open the VS Code terminal (`Ctrl + ~`) and run:

```bash
mvn clean package
```

This compiles all Java files and creates a `.war` file in the `target/` folder.

---

### Step 6: Run with Embedded Tomcat

```bash
mvn tomcat10:run
```

You should see:
```
[INFO] Starting Tomcat on port 8080
[INFO] Context path: /myapp
```

---

### Step 7: Open in Browser

Go to: **http://localhost:8080/myapp**

You'll be redirected to the login page automatically.

---

## 🔄 Application Flow

```
User visits /myapp
       ↓
  → /login page
       ↓ (no account)
  → /signup page
       ↓ (fill form)
  → SignupServlet saves user (unverified)
       ↓
  → Email sent with /verify?token=abc123
       ↓ (user clicks link)
  → VerifyServlet marks user as verified
       ↓
  → /login page (success message)
       ↓ (enter credentials)
  → LoginServlet checks password + verification
       ↓
  → /dashboard.jsp (logged in!)
       ↓ (click logout)
  → /logout → session cleared → /login
```

---

## 🐛 Troubleshooting

| Problem | Solution |
|---|---|
| `Communications link failure` | Make sure MySQL is running. Check port 3306. |
| `Access denied for user` | Check DB_USER and DB_PASSWORD in DBConnection.java |
| Email not received | Check spam folder. Make sure App Password is correct. |
| `ClassNotFoundException: com.mysql.cj.jdbc.Driver` | Run `mvn clean package` to download dependencies |
| `404 Not Found` | Make sure the context path is `/myapp` in your URL |
| Port 8080 already in use | Change `<port>8080</port>` in pom.xml to `8081` |

---

## 🔐 Security Notes

- **Passwords** are hashed with BCrypt (never stored in plain text)
- **Verification tokens** are UUID-based (cryptographically random)
- **SQL injection** is prevented by using `PreparedStatement`
- **Sessions** expire after 30 minutes of inactivity
