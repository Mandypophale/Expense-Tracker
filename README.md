# Expense Tracker Web Application

A clean, beginner-friendly, and professional Expense Tracker web application built using **Java 17, Spring Boot, Spring MVC, Spring Data JPA, Thymeleaf, and Bootstrap 5**.

This application is designed specifically for freshers/beginners to learn the **MVC Architecture** in Spring Boot without security complexities (using a clean session-based custom authentication filter).

---

## 🚀 Key Features

*   **Session-based Authentication**: Secure Register, Login, and Logout flows using Java HTTP Session.
*   **Aesthetic Responsive Dashboard**:
    *   Dynamic summary counters for Total Income, Total Expenses, and Net Balance (styles adapt dynamically based on status).
    *   Recent Transactions Table displaying the last 5 expense entries.
*   **Income Management**: View, log, edit, and delete cash inflows on a single unified UI page.
*   **Expense Management**: Full CRUD operations with standard field validation (e.g., positive amount restriction, email validation, etc.).
*   **Search & Filter**: Search expenses by title and filter them by category (Food, Travel, Shopping, Bills, Entertainment, Health, Education, Other).
*   **Premium Visual Polish**: Bootstrap 5 glassmorphic style cards, custom color themes, and automatic fade-away alert banners.

---

## 🛠️ Technology Stack

*   **Backend**: Java 17, Spring Boot 3.2.2, Spring MVC, Spring Data JPA, Hibernate
*   **Database**: MySQL
*   **Frontend**: Thymeleaf, HTML5, CSS3, JavaScript, Bootstrap 5 (with Bootstrap Icons)
*   **Build Tool**: Maven

---

## 📂 Project Structure

```text
src/main/java/com/expensetracker
│
├── ExpenseTrackerApplication.java  # Main Boot class
│
├── config
│   ├── WebMvcConfig.java          # Registers the Session Interceptor
│   └── SessionInterceptor.java    # Checks for logged-in user session
│
├── controller
│   ├── AuthController.java        # Handles register, login, and logout GET/POSTs
│   ├── DashboardController.java   # Computes summary stats and serves dashboard
│   ├── ExpenseController.java     # Handles full CRUD & filters for expenses
│   ├── IncomeController.java      # Handles full CRUD for incomes on a single page
│   └── ProfileController.java     # Manages user settings & password edits
│
├── dto
│   ├── UserRegisterDto.java       # DTO with user validation properties
│   ├── UserLoginDto.java          # DTO for logging in
│   ├── ExpenseDto.java            # DTO for adding/editing expenses
│   └── IncomeDto.java             # DTO for adding/editing income
│
├── entity
│   ├── User.java                  # Hibernate JPA user model
│   ├── Expense.java               # Hibernate JPA expense model
│   └── Income.java                # Hibernate JPA income model
│
├── repository
│   ├── UserRepository.java        # Database queries interface for Users
│   ├── ExpenseRepository.java     # Database queries interface for Expenses
│   └── IncomeRepository.java      # Database queries interface for Incomes
│
├── service
│   ├── UserService.java           # User service interface
│   ├── ExpenseService.java        # Expense service interface
│   ├── IncomeService.java         # Income service interface
│   └── impl
│       ├── UserServiceImpl.java   # Hashes passwords using SHA-256
│       ├── ExpenseServiceImpl.java
│       └── IncomeServiceImpl.java
│
└── exception
    └── GlobalExceptionHandler.java # Catches errors and displays details
```

---

## ⚙️ Setup & Installation Instructions

Follow these step-by-step instructions to set up and run the project locally.

### 1. Prerequisites
Make sure you have the following installed:
*   **Java Development Kit (JDK) 17**
*   **Apache Maven**
*   **MySQL Server**
*   Any standard IDE (IntelliJ IDEA, Eclipse, VS Code)

### 2. Database Configuration
1. Open your MySQL client (Command Line, Workbench, or DBeaver).
2. Execute the commands found in `schema.sql` at the root of the project to create the database and tables:
   ```sql
   CREATE DATABASE IF NOT EXISTS expense_tracker;
   ```
3. Set your local MySQL credentials in `src/main/resources/application.properties`:
   ```properties
   spring.datasource.username=YOUR_MYSQL_USERNAME
   spring.datasource.password=YOUR_MYSQL_PASSWORD
   ```

### 3. Build & Compile the Project
Open a terminal in the project directory and run:
```bash
mvn clean compile
```

### 4. Running the Application
To launch the Spring Boot development server, run:
```bash
mvn spring-boot:run
```

Once running, open your web browser and navigate to:
👉 **[http://localhost:8080](http://localhost:8080)**

---

## 🛡️ Security Details

For simplicity and ease of understanding for beginners, **Spring Security is NOT used**.
*   **Custom Login Enforcement**: A `SessionInterceptor` intercepts requests to URLs (excluding `/login`, `/register`, and static files) and checks if the HTTP Session attribute `"loggedUser"` is set. If not, it redirects the request to `/login`.
*   **Password Hashing**: Passwords are securely hashed using a custom `SHA-256` hashing logic inside `UserServiceImpl` before saving to the database. This ensures passwords are not stored in plaintext.
