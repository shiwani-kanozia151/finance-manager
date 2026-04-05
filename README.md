# Finance Manager Backend

**GitHub Repository:** https://github.com/shiwani-kanozia151/finance-manager

## Overview

Finance Manager is a Spring Boot backend application for managing users, role-based access control, financial records, and dashboard analytics. It implements a clean, layered architecture with comprehensive access control and validation.

## Tech Stack

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Security with JWT**
- **Spring Data JPA** with Hibernate
- **H2 Database** (In-memory, development)
- **MySQL Driver** (For production)
- **Maven**
- **Lombok**

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- Git

### Installation

1. **Clone the repository:**

   ```bash
   git clone https://github.com/shiwani-kanozia151/finance-manager.git
   cd finance-manager
   ```

2. **Build the project:**

   ```bash
   mvn clean install
   ```

3. **Run the application:**

   ```bash
   mvn spring-boot:run
   ```

The API will be available at `http://localhost:8080`.

### Default Test Credentials

The application initializes with three sample users:

| Email | Password | Role |
|-------|----------|------|
| admin@finance.com | admin123 | ADMIN |
| analyst@finance.com | analyst123 | ANALYST |
| viewer@finance.com | viewer123 | VIEWER |

## Project Structure

```
src/
├── main/
│   ├── java/com/finance/
│   │   ├── FinanceManagerApplication.java      # Main Spring Boot entry point
│   │   ├── config/
│   │   │   ├── DataInitializer.java            # Initialize roles and sample data
│   │   │   ├── SecurityConfig.java             # Security and access control configuration
│   │   ├── controller/
│   │   │   ├── UserController.java             # User registration, login, profile
│   │   │   ├── FinancialRecordController.java  # CRUD operations for records
│   │   │   ├── DashboardController.java        # Dashboard summary endpoints
│   │   ├── dto/
│   │   │   ├── RegisterRequest.java            # User registration request
│   │   │   ├── LoginRequest.java               # User login request
│   │   │   ├── AuthResponse.java               # JWT token response
│   │   │   ├── UserResponse.java               # User profile response
│   │   │   ├── FinancialRecordRequest.java     # Record creation/update
│   │   │   ├── FinancialRecordResponse.java    # Record read response
│   │   │   ├── DashboardSummaryResponse.java   # Dashboard aggregated data
│   │   ├── entity/
│   │   │   ├── Role.java                       # Role entity (VIEWER, ANALYST, ADMIN)
│   │   │   ├── User.java                       # User entity with role mapping
│   │   │   ├── FinancialRecord.java            # Financial record entity
│   │   ├── repository/
│   │   │   ├── RoleRepository.java             # Role JPA repository
│   │   │   ├── UserRepository.java             # User JPA repository
│   │   │   ├── FinancialRecordRepository.java  # Record JPA repository with filters
│   │   ├── security/
│   │   │   ├── JwtProvider.java                # JWT token generation and validation
│   │   │   ├── JwtAuthenticationFilter.java    # JWT authentication filter
│   │   ├── service/
│   │   │   ├── RoleService.java                # Role management logic
│   │   │   ├── UserService.java                # User authentication and profile management
│   │   │   ├── FinancialRecordService.java     # CRUD and filtering for records
│   │   │   ├── DashboardService.java           # Aggregated data and analytics
│   ├── resources/
│   │   └── application.properties              # Spring Boot configuration
└── pom.xml                                     # Maven dependency management
```

## API Endpoints

### Authentication

#### Register User
```
POST /api/users/register
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "securepassword",
  "role": "ANALYST"
}

Response: 201 Created
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "type": "Bearer",
  "email": "john@example.com",
  "role": "ANALYST",
  "userId": 1
}
```

#### Login
```
POST /api/users/login
Content-Type: application/json

{
  "email": "john@example.com",
  "password": "securepassword"
}

Response: 200 OK
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "type": "Bearer",
  "email": "john@example.com",
  "role": "ANALYST",
  "userId": 1
}
```

### User Management

#### Get Own Profile
```
GET /api/users/profile
Authorization: Bearer {token}

Response: 200 OK
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com",
  "role": "ANALYST",
  "active": true,
  "createdAt": "2026-04-05T10:00:00"
}
```

#### Update User Profile
```
PUT /api/users/{id}
Authorization: Bearer {token}
Content-Type: application/json

{
  "name": "Jane Doe",
  "email": "jane@example.com"
}

Response: 200 OK
```

### Financial Records

#### Create Record
```
POST /api/records
Authorization: Bearer {token}
Content-Type: application/json

{
  "amount": 2500.50,
  "type": "INCOME",
  "category": "Salary",
  "recordDate": "2026-04-05",
  "description": "Monthly salary payment"
}

Response: 201 Created
{
  "id": 1,
  "amount": 2500.50,
  "type": "INCOME",
  "category": "Salary",
  "recordDate": "2026-04-05",
  "description": "Monthly salary payment",
  "createdAt": "2026-04-05T10:00:00"
}
```

#### Get All Records
```
GET /api/records
Authorization: Bearer {token}

Response: 200 OK
[...]
```

#### Get Record by ID
```
GET /api/records/{id}
Authorization: Bearer {token}

Response: 200 OK
{...}
```

#### Update Record
```
PUT /api/records/{id}
Authorization: Bearer {token}
Content-Type: application/json

{...}

Response: 200 OK
```

#### Delete Record (Soft Delete)
```
DELETE /api/records/{id}
Authorization: Bearer {token}

Response: 200 OK
```

#### Filter Records by Type
```
GET /api/records/filter/type?type=INCOME
Authorization: Bearer {token}

Response: 200 OK
[...]
```

#### Filter Records by Category
```
GET /api/records/filter/category?category=Salary
Authorization: Bearer {token}

Response: 200 OK
[...]
```

#### Filter Records by Date Range
```
GET /api/records/filter/date-range?startDate=2026-01-01&endDate=2026-03-31
Authorization: Bearer {token}

Response: 200 OK
[...]
```

#### Get Recent Records
```
GET /api/records/recent?limit=10
Authorization: Bearer {token}

Response: 200 OK
[...]
```

### Dashboard

#### Get Overall Summary
```
GET /api/dashboard/summary
Authorization: Bearer {token}

Response: 200 OK
{
  "totalIncome": 7500.00,
  "totalExpenses": 1430.00,
  "netBalance": 6070.00,
  "categoryWiseTotals": {
    "Salary": 5000.00,
    "Freelance": 2500.00,
    "Rent": 1200.00,
    "Groceries": 150.00,
    "Utilities": 80.00
  },
  "recentActivity": [...],
  "recordCount": 5,
  "period": "ALL_TIME"
}
```

#### Get Monthly Summary
```
GET /api/dashboard/summary/monthly?year=2026&month=4
Authorization: Bearer {token}

Response: 200 OK
{
  "totalIncome": 5000.00,
  "totalExpenses": 1430.00,
  "netBalance": 3570.00,
  "categoryWiseTotals": {...},
  "recentActivity": [...],
  "recordCount": 5,
  "period": "2026-04"
}
```

## Role-Based Access Control

The system implements three roles with different permission levels:

### VIEWER
- ✅ View dashboard summaries
- ❌ Cannot create, update, or delete records
- ❌ Cannot manage other users

### ANALYST
- ✅ View all own records
- ✅ Create, update, and delete own records
- ✅ View dashboard summaries
- ✅ Filter and analyze records
- ❌ Cannot manage users

### ADMIN
- ✅ Full access to all records
- ✅ Full access to all dashboard features
- ✅ Can manage user accounts
- ✅ Can activate/deactivate users

## Security Features

1. **JWT Authentication**
   - Tokens expire after 24 hours (configurable)
   - Secure password encoding with BCrypt
   - Token validation on every protected endpoint

2. **Role-Based Access Control**
   - Method-level authorization using Spring Security
   - Request-level authorization in controllers
   - Data-level authorization (users can only access their own records)

3. **Validation & Error Handling**
   - Input validation using Jakarta Validation
   - Meaningful error responses with HTTP status codes
   - Soft delete for financial records (not permanently removed)
   - User activity tracking with timestamps

## Features Implemented

✅ **User and Role Management**
   - User registration with role assignment
   - User login with JWT token generation
   - Role-based permissions (VIEWER, ANALYST, ADMIN)
   - User activation/deactivation

✅ **Financial Records Management**
   - Create, read, update, delete (CRUD) operations
   - Support for income and expense types
   - Categorization of records
   - Soft delete functionality
   - Audit timestamps (createdAt, updatedAt)

✅ **Advanced Filtering**
   - Filter by record type (INCOME/EXPENSE)
   - Filter by category
   - Filter by date range
   - Retrieve recent records with limit

✅ **Dashboard & Analytics**
   - Total income calculation
   - Total expenses calculation
   - Net balance computation
   - Category-wise totals
   - Recent activity tracking
   - Monthly summary reports

✅ **Access Control**
   - Middleware-based JWT validation
   - Role-based endpoint protection
   - User-specific data isolation
   - Admin management capabilities

✅ **Data Validation**
   - Input validation with detailed error messages
   - Amount validation (positive numbers only)
   - Email format validation
   - Required field validation
   - Record type enumeration validation

✅ **Data Persistence**
   - H2 in-memory database for development
   - MySQL support for production
   - Automatic schema generation via JPA
   - Transaction support

## Configuration

Edit `src/main/resources/application.properties` to customize:

```properties
# Server port
server.port=8080

# Database (H2 for development, change for production)
spring.datasource.url=jdbc:h2:mem:financedb

# JWT
app.jwt.secret=mySecretKeyForFinanceManagerApplicationThatMustBeAtLeast32Characters
app.jwt.expiration=86400000  # 24 hours in milliseconds
```

## Database Migration (MySQL)

For production use with MySQL:

1. Update `application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/finance_manager
   spring.datasource.username=root
   spring.datasource.password=yourpassword
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
   ```

2. Create the database:
   ```sql
   CREATE DATABASE finance_manager;
   ```

3. Restart the application

## Testing the API

### Using cURL

```bash
# Register
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{"name":"Test User","email":"test@example.com","password":"test123","role":"ANALYST"}'

# Login
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"test123"}'

# Create Record (replace TOKEN with the JWT token)
curl -X POST http://localhost:8080/api/records \
  -H "Authorization: Bearer TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"amount":1000,"type":"INCOME","category":"Salary","recordDate":"2026-04-05","description":"Test income"}'
```

### Using Postman

1. Import the endpoints into Postman
2. Set the `Authorization` header to Bearer type with your JWT token
3. Test each endpoint

## Key Design Decisions

1. **JPA/Hibernate ORM** - Chosen for robust object-relational mapping and automatic schema management
2. **LayeredArchitecture** - Separated controllers, services, repositories for maintainability
3. **Soft Delete** - Financial records marked as deleted, not permanently removed for audit trails
4. **Role-Based Access** - Implemented at multiple levels for security
5. **JWT Authentication** - Stateless, scalable authentication mechanism
6. **H2 Database** - Lightweight, in-memory for development; MySQL-compatible for production
7. **Lombok** - Reduces boilerplate code for entities and DTOs
8. **Spring Security** - Industry-standard framework for authentication and authorization

## Trade-offs Considered

- **H2 vs Persistent Database:** H2 chosen for development simplicity; production uses MySQL
- **JWT vs Session-Based:** JWT chosen for stateless scalability
- **Soft Delete vs Hard Delete:** Soft delete chosen to maintain audit trail
- **In-Memory DB:** Simplifies setup for evaluation; production requires external database
- **Basic Error Handling:** Implemented for clarity; production would benefit from custom exception classes

## Future Enhancements

- Unit tests and integration tests
- API documentation with Swagger/OpenAPI
- Pagination support for record listing
- Search functionality across records
- Export records to CSV/PDF
- Scheduled reports
- Email notifications
- Rate limiting
- Audit logging
- Caching with Redis

## Notes

This project is designed as a clean, maintainable backend following Spring Boot best practices. It emphasizes:

- Separation of concerns (entity, repository, service, controller layers)
- Data validation and error handling
- Security with role-based access control
- Clear business logic implementation
- Extensibility for future features