# LinkUP 🚀

### A Modern Student Networking Platform Built with Spring Boot

LinkUP is a backend-driven student networking platform that enables students to discover, connect, and build meaningful academic and professional relationships based on shared interests, skills, and educational backgrounds.

The project is designed to simulate a production-grade social networking system while demonstrating enterprise-level backend development practices using Spring Boot.

---

## 🎯 Project Vision

Students often struggle to find peers with similar interests, technical skills, career goals, or project ideas.

LinkUP solves this problem by providing:

- Secure user authentication
- Personalized student profiles
- Intelligent user discovery
- Connection request management
- Advanced search and filtering
- Scalable REST APIs

The platform acts as a professional networking ecosystem tailored for students.

---

## ✨ Key Features

### 🔐 Authentication & Security

- User Registration
- User Login
- JWT Authentication
- Password Encryption using BCrypt
- Role-Based Authorization
- Protected APIs

---

### 👤 Profile Management

- Create Profile
- Update Profile
- View Profiles
- Delete Profile
- Academic Information
- Skills & Interests Management

---

### 🔍 Advanced User Discovery

Discover students using powerful search capabilities.

#### Search By

- Name
- Skills
- Interests
- Bio

#### Filter By

- City
- Branch
- Academic Year
- Age Range

#### Sort By

- Name
- Age
- Profile Creation Date

#### Pagination

Efficiently handle thousands of users through paginated API responses.

Example:

```http
GET /api/users/discover?page=0&size=10&sort=createdAt,desc
```

---

### 🤝 Connection System

A complete networking workflow.

- Send Connection Requests
- Accept Requests
- Reject Requests
- Withdraw Requests
- View Pending Requests
- View Sent Requests
- View Connections
- Remove Connections

Connection Status Flow:

```text
NOT_CONNECTED
      ↓
REQUEST_SENT
      ↓
PENDING
      ↓
CONNECTED
```

---

### 📊 Dashboard & Analytics

Track networking activity.

- Total Connections
- Pending Requests
- Sent Requests
- Platform Statistics

---

### 🛡️ Admin Module

Administrative controls for platform management.

- View Users
- Manage Users
- Platform Statistics
- User Monitoring

---

## 🏗️ System Architecture

```text
Client
   │
   ▼
REST Controllers
   │
   ▼
Service Layer
   │
   ▼
Repository Layer
   │
   ▼
MySQL Database
```

The project follows a clean layered architecture that promotes:

- Scalability
- Maintainability
- Separation of Concerns
- Testability

---

## 🗄️ Database Design

### User

```text
id
fullName
email
password
age
gender
city
college
branch
year
bio
skills
interests
profilePictureUrl
createdAt
```

### Role

```text
ROLE_USER
ROLE_ADMIN
```

### ConnectionRequest

```text
id
sender
receiver
status
createdAt
```

### Connection

```text
id
user1
user2
connectedAt
```

---

## 🚀 REST APIs

### Authentication

```http
POST /api/auth/register
POST /api/auth/login
GET  /api/auth/me
```

### User Management

```http
GET    /api/users/{id}
PUT    /api/users/profile
DELETE /api/users/profile
```

### User Discovery

```http
GET /api/users/discover
```

Supports:

```http
?page=0
&size=10
&sort=age,desc
&city=Delhi
&branch=CSE
&year=3
&keyword=spring
```

### Connection Requests

```http
POST   /api/connections/send/{receiverId}

PUT    /api/connections/accept/{requestId}

PUT    /api/connections/reject/{requestId}

DELETE /api/connections/request/{requestId}

GET    /api/connections/pending

GET    /api/connections/sent
```

### Connections

```http
GET    /api/connections/my-connections

DELETE /api/connections/remove/{connectionId}

GET    /api/connections/status/{userId}
```

---
**MORE TO BE ADDED if needed**
## 🧠 Spring Boot Concepts Demonstrated

### Core Development

- RESTful API Design
- Dependency Injection
- Layered Architecture
- DTO Pattern

### Persistence Layer

- Spring Data JPA
- Hibernate ORM
- Entity Relationships
- JPQL Queries
- Native Queries

### Security

- Spring Security
- JWT Authentication
- BCrypt Password Encryption
- Role-Based Access Control

### Advanced Backend Concepts

- Pagination
- Sorting
- Searching
- Filtering
- Dynamic Query Generation
- JPA Specifications

### API Quality

- Bean Validation
- Global Exception Handling
- Standardized API Responses

### Documentation & Testing

- Swagger/OpenAPI
- JUnit 5
- Mockito
- MockMvc

---

## 📈 Future Enhancements

- Profile Image Upload
- Email Verification
- Real-Time Notifications
- WebSocket Messaging
- Friend Recommendations
- Redis Caching
- Docker Deployment
- CI/CD Pipeline
- Microservices Architecture

---

## 👨‍💻 Author

**Himanshu Bagga**

**Contact- 8448031291**

Computer Science Engineering Student  
Java | Spring Boot | React | Data Structures & Algorithms

---

### Current Status

🚧 Actively Under Development

Building one feature at a time while following industry-standard backend development practices.