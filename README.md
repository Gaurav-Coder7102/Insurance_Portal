# 🏥 Insurance Portal

A full-stack insurance management portal.

| Layer | Technology |
|---|---|
| Backend | Spring Boot 3.2.x + JWT |
| Frontend | Next.js |
| Database | PostgreSQL |

## 👥 Team
- **Gaurav** — Backend Developer
- **Rahul** — Backend Developer

---

## 🚀 Backend Setup

### Prerequisites
- Java 17+
- Maven 3.8+
- PostgreSQL 14+

### 1. Create Database
```sql
CREATE DATABASE insurance_portal_db;
```

### 2. Configure Local Properties
Create `backend/src/main/resources/application-local.properties` (this file is gitignored):
```properties
spring.datasource.password=your_postgres_password
```

### 3. Run the Application
```bash
cd backend
mvn spring-boot:run
```
Server starts on **http://localhost:9191**

---

## 🔌 API Endpoints

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| `POST` | `/api/auth/register` | ❌ | Register new user |
| `POST` | `/api/auth/login` | ❌ | Login → returns JWT |
| `GET` | `/api/auth/me` | ✅ Bearer Token | Get current user |

### Example — Login
```bash
curl -X POST http://localhost:9191/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"gaurav@insurance.com","password":"pass123"}'
```

---

## 🌿 Git Branching Strategy

```
main          ← stable, production-ready
develop       ← integration branch
feature/xxx   ← individual feature branches (Rahul / Gaurav)
```
