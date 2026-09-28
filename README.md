# Spring-Boot-Java-Project

# 🌱 GreenLog — Tree Plantation Drive Tracker

GreenLog is a Spring Boot REST API designed to help college sustainability clubs manage tree plantation drives and monitor the survival of planted trees over time.

The system records plantation drives, trees, volunteers, and periodic survival check-ins while enforcing business rules to maintain data integrity.

## ✨ Features

- 🌱 Create and manage plantation drives
- 🌳 Register planted trees
- 👥 Manage volunteers
- 📋 Record periodic survival check-ins
- 📊 Calculate survival rates by plantation drive
- 🌿 Calculate survival rates by tree species
- 📅 Find trees due for their next check-in
- 🏆 Generate volunteer leaderboard
- ✅ Input validation
- 🛡️ Global exception handling
- 🔒 Business-rule enforcement
- 🗄️ MySQL database integration

## 🛡️ Business Rules

### Dead trees cannot receive further check-ins

Once a tree is marked as `DEAD`, additional check-ins are rejected with a `400 Bad Request`.

### Tree status updates automatically

When a survival check-in is recorded:

```text
ALIVE → Tree status becomes ALIVE
DEAD  → Tree status becomes DEAD
```

## 🏗️ Architecture

```text
              REST Client
                   │
                   ▼
            Controller Layer
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

## 🛠️ Tech Stack

- Java 17
- Spring Boot 3.1.5
- Spring Web
- Spring Data JPA
- Hibernate
- Spring Validation
- MySQL
- Maven
- Lombok
- Postman

## 📂 Project Structure

```text
greenlog/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/example/greenlog/
│       │       ├── GreenLogApplication.java
│       │       ├── controller/
│       │       ├── service/
│       │       ├── repository/
│       │       ├── model/
│       │       ├── dto/
│       │       └── exception/
│       │
│       └── resources/
│           └── application.properties
│
├── pom.xml
└── README.md
```

## 🗄️ Database Design

GreenLog uses MySQL with the database:

```text
greenlog
```

Main entities:

```text
PlantationDrive
       │
       │ 1
       │
       │ *
      Tree
       │
       ├──────── Volunteer
       │
       │
       │ 1
       │
       │ *
    CheckIn
```

### PlantationDrive

Stores plantation drive information such as:

- Name
- Location
- Drive date
- Description

### Tree

Stores:

- Species
- Location
- Date planted
- Status
- Next check-in date
- Plantation drive
- Volunteer

### CheckIn

Stores:

- Check-in date
- Survival status
- Notes
- Tree

### Volunteer

Stores:

- Name
- Email
- Phone

## 🔌 REST API

### Plantation Drives

```http
POST   /api/drives
GET    /api/drives
GET    /api/drives/{id}
PUT    /api/drives/{id}
DELETE /api/drives/{id}
```

### Trees

```http
POST   /api/trees
GET    /api/trees
GET    /api/trees/{id}
PUT    /api/trees/{id}
DELETE /api/trees/{id}
```

### Survival Check-ins

```http
POST /api/checkins
GET  /api/checkins
GET  /api/checkins/{id}
```

### Volunteers

```http
POST   /api/volunteers
GET    /api/volunteers
GET    /api/volunteers/{id}
PUT    /api/volunteers/{id}
DELETE /api/volunteers/{id}
```

### Statistics

```http
GET /api/drives/{id}/survival-rate
GET /api/trees/survival-rate/species/{species}
GET /api/trees/due-checkins
GET /api/volunteers/leaderboard
```

## 📊 Survival Rate

The survival rate is calculated using:

```text
Survival Rate =
(Alive Trees / Total Trees) × 100
```

Example:

```json
{
  "totalTrees": 100,
  "aliveTrees": 82,
  "deadTrees": 18,
  "survivalRate": 82.0
}
```

## 🏆 Volunteer Leaderboard

Volunteers are ranked based on the number of trees they have planted.

Example:

```json
[
  {
    "volunteerId": 1,
    "volunteerName": "Vishal",
    "treesPlanted": 25
  },
  {
    "volunteerId": 2,
    "volunteerName": "Arun",
    "treesPlanted": 18
  }
]
```

## ⚙️ Setup

### 1. Clone the repository

```bash
git clone https://github.com/YOUR_USERNAME/greenlog.git
cd greenlog
```

### 2. Create the MySQL database

```sql
CREATE DATABASE greenlog;
```

### 3. Configure the database

Update:

```text
src/main/resources/application.properties
```

Example:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/greenlog
spring.datasource.username=${DB_USERNAME:root}
spring.datasource.password=${DB_PASSWORD:your_password}

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

server.port=1555
```

### 4. Build the project

```bash
mvn clean install
```

### 5. Run the application

```bash
mvn spring-boot:run
```

The application runs at:

```text
http://localhost:1555
```

## 🧪 Testing

The API can be tested using Postman.

Recommended testing flow:

```text
1. Create Volunteer
        ↓
2. Create Plantation Drive
        ↓
3. Add Tree
        ↓
4. Create ALIVE Check-in
        ↓
5. Verify Tree Status
        ↓
6. Create DEAD Check-in
        ↓
7. Verify Tree Status
        ↓
8. Attempt another Check-in
        ↓
9. Verify HTTP 400
        ↓
10. Test Survival Rate
        ↓
11. Test Due Check-ins
        ↓
12. Test Volunteer Leaderboard
```

## 📈 Future Enhancements

- 📍 Map-based tree locations
- 📷 Photo upload for survival check-ins
- 🔔 Check-in reminders
- 📊 Web dashboard
- 📄 PDF/CSV reports
- 🔎 Advanced search and filtering
- 🔐 Authentication and role-based access
- 📱 Mobile application
- 🌍 Environmental impact tracking

## 👨‍💻 Project Information

**Project Name:** GreenLog  
**Project Type:** Spring Boot REST API  
**Domain:** Environmental Sustainability  
**Language:** Java 17  
**Framework:** Spring Boot 3.1.5  
**Database:** MySQL  
**Build Tool:** Maven

---

## 🌱 Purpose

GreenLog provides a structured platform for managing tree plantation drives, tracking planted trees, monitoring their survival, and recognizing volunteer contributions.

**Plantation Drives → Trees → Volunteers → Survival Check-ins**
