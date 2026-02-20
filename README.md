# DevOps Task Manager API

![CI Status](https://github.com/AdityaJareda/devops-task-manager/workflows/Complete%20CI%2FCD%20Pipeline/badge.svg)
![Docker Build](https://github.com/AdityaJareda/devops-task-manager/workflows/Docker%20Build%20and%20Push/badge.svg)
![Docker Image Size](https://img.shields.io/docker/image-size/adityajareda/devops-task-manager/latest)
![Docker Pulls](https://img.shields.io/docker/pulls/adityajareda/devops-task-manager)

A production-ready RESTful API for task management, demonstrating DevOps best practices with Spring Boot, Maven, and automated workflows.

## Table of Contents

- [Features](#features)
- [Technologies](#technologies)
- [Getting Started](#getting-started)
- [Docker Deployment](#docker-deployment)
- [API Endpoints](#api-endpoints)
- [Configuration](#configuration)
- [Testing](#testing)
- [Building](#building)
- [Project Structure](#project-structure)
- [Troubleshooting](#troubleshooting)

## Features

- CRUD operations for task management
- Task status tracking (completed/pending)
- Statistics endpoint for task metrics
- Bulk operations (complete all, clear completed)
- Health monitoring with Spring Boot Actuator
- RESTful design with standard HTTP methods
- In-memory data storage
- Maven-based build automation
- Configuration profiles for different environments
- Docker containerization with multi-stage builds
- Docker Compose for easy local development

## Technologies

**Backend:**
- Java 17
- Spring Boot 3.2.1
- Spring Web (REST API)
- Spring Boot Actuator (Monitoring)
- Maven 3.9+

**Testing:**
- JUnit 5
- Spring Boot Test

**Build Tools:**
- Maven Wrapper

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.9+ (or use included Maven wrapper)

### Installation

1. Clone the repository:
```bash
git clone https://github.com/YOUR-USERNAME/devops-task-manager.git
cd devops-task-manager
```

2. Build the project:
```bash
./mvnw clean package
```

3. Run the application:
```bash
./mvnw spring-boot:run
```

4. Verify the application is running:
```bash
curl http://localhost:8080/api/tasks/health
```

Expected response: `Task API is running!`

## Docker Deployment

### Prerequisites

- Docker installed
- Docker Compose installed (optional, for multi-container setup)

### Quick Start with Docker

#### Build Docker Image
```bash
docker build -t spring-boot-app:latest .
```

#### Run Container
```bash
docker run -d \
  --name task-api \
  -p 8080:8080 \
  spring-boot-app:latest
```

#### Verify Application
```bash
curl http://localhost:8080/api/tasks/health
```

#### View Logs
```bash
docker logs -f task-api
```

#### Stop Container
```bash
docker stop task-api
docker rm task-api
```

### Using Docker Compose

Docker Compose simplifies running the application with all its dependencies.

#### Start Services
```bash
docker-compose up -d
```

This will:
- Build the application image
- Start the Spring Boot container
- Configure networking and health checks

#### View Logs
```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f app
```

#### Stop Services
```bash
docker-compose down
```

#### Rebuild and Restart
```bash
docker-compose up -d --build
```

### Docker Image Details

**Multi-Stage Build:**
- **Build Stage**: Uses `maven:3.9-eclipse-temurin-17` (~500MB)
  - Compiles Java code
  - Downloads dependencies
  - Packages JAR file
- **Runtime Stage**: Uses `eclipse-temurin:17-jre-alpine` (~300MB)
  - Minimal JRE-only image
  - Includes only the application JAR
  - Runs as non-root user for security

**Optimizations:**
- Layer caching for Maven dependencies
- Separate layers for wrapper, dependencies, and source code
- Alpine-based runtime for smaller image size
- Non-root user execution

**Configuration:**
- Port: 8080 (configurable via environment variables)
- Health checks: Built-in via Spring Boot Actuator
- Resource limits: 512MB memory limit, 256MB reserved
- Restart policy: unless-stopped

## API Endpoints

### Base URL
```
http://localhost:8080/api/tasks
```

### Endpoints

#### Get All Tasks
```http
GET /api/tasks
```

Response:
```json
[
  {
    "id": 1,
    "title": "Learn Maven",
    "description": "Understand Maven build process",
    "completed": false
  }
]
```

#### Get Task by ID
```http
GET /api/tasks/{id}
```

Example:
```bash
curl http://localhost:8080/api/tasks/1
```

#### Create New Task
```http
POST /api/tasks
Content-Type: application/json
```

Request body:
```json
{
  "title": "Deploy to Production",
  "description": "Deploy the application to AWS",
  "completed": false
}
```

Example:
```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{"title":"New Task","description":"Task description","completed":false}'
```

#### Update Task
```http
PUT /api/tasks/{id}
Content-Type: application/json
```

Request body:
```json
{
  "title": "Updated Task Title",
  "description": "Updated description",
  "completed": true
}
```

Example:
```bash
curl -X PUT http://localhost:8080/api/tasks/1 \
  -H "Content-Type: application/json" \
  -d '{"title":"Learn Maven","description":"Completed!","completed":true}'
```

#### Delete Task
```http
DELETE /api/tasks/{id}
```

Example:
```bash
curl -X DELETE http://localhost:8080/api/tasks/1
```

#### Get Statistics
```http
GET /api/tasks/stats
```

Response:
```json
{
  "total": 5,
  "completed": 2,
  "pending": 3
}
```

#### Complete All Tasks
```http
POST /api/tasks/complete-all
```

Marks all tasks as completed.

Example:
```bash
curl -X POST http://localhost:8080/api/tasks/complete-all
```

Response: `All tasks marked as completed`

#### Clear Completed Tasks
```http
DELETE /api/tasks/clear-completed
```

Removes all completed tasks from the list.

Example:
```bash
curl -X DELETE http://localhost:8080/api/tasks/clear-completed
```

Response:
```json
{
  "message": "Completed tasks cleared",
  "removedCount": 2
}
```

#### Health Check
```http
GET /api/tasks/health
```

Response: `Task API is running!`

### Actuator Endpoints

#### Health Status
```bash
curl http://localhost:8080/actuator/health
```

Response:
```json
{
  "status": "UP",
  "components": {
    "diskSpace": {"status": "UP"},
    "ping": {"status": "UP"}
  }
}
```

#### Application Info
```bash
curl http://localhost:8080/actuator/info
```

#### Metrics
```bash
curl http://localhost:8080/actuator/metrics
```

## Configuration

### Application Properties

Located in `src/main/resources/application.properties`:
```properties
spring.application.name=DevOps Task Manager
server.port=8080
management.endpoints.web.exposure.include=health,info,metrics
management.endpoint.health.show-details=always
logging.level.root=INFO
logging.level.com.devops.internship=DEBUG
```

### Configuration Profiles

**Development Profile** (`application-dev.properties`):
```properties
server.port=8080
logging.level.com.devops.internship=DEBUG
```

**Production Profile** (`application-prod.properties`):
```properties
server.port=8080
logging.level.com.devops.internship=WARN
```

Run with specific profile:
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
```

## Testing

### Run All Tests
```bash
./mvnw test
```

### Run Tests with Coverage
```bash
./mvnw test jacoco:report
```

Coverage report location: `target/site/jacoco/index.html`

### Run Specific Test
```bash
./mvnw test -Dtest=TaskServiceTest
```

## Building

### Create JAR File
```bash
./mvnw clean package
```

Output: `target/spring-boot-app-1.0.0.jar`

### Run the JAR
```bash
java -jar target/spring-boot-app-1.0.0.jar
```

### Skip Tests (Faster Build)
```bash
./mvnw clean package -DskipTests
```

## Project Structure
```
spring-boot-app/
├── src/
│   ├── main/
│   │   ├── java/com/devops/internship/
│   │   │   ├── Application.java
│   │   │   ├── controller/
│   │   │   │   └── TaskController.java
│   │   │   ├── model/
│   │   │   │   └── Task.java
│   │   │   └── service/
│   │   │       └── TaskService.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application-dev.properties
│   │       └── application-prod.properties
│   └── test/
│       └── java/com/devops/internship/
│           └── AppTest.java
├── target/
├── .gitignore
├── mvnw
├── mvnw.cmd
├── pom.xml
└── README.md
```

## Troubleshooting

### Port Already in Use

Change the port in `application.properties`:
```properties
server.port=8081
```

### Build Failures

Clean Maven cache and rebuild:
```bash
./mvnw clean
rm -rf ~/.m2/repository
./mvnw clean install
```

### Application Won't Start

Check Java version:
```bash
java -version
```

Required: Java 17 or higher

### Permission Denied on Port 80

Ports below 1024 require root privileges. Use port 8080 or higher for development.

### Docker Issues

#### Image Build Fails

Clean Docker cache and rebuild:
```bash
docker system prune -a
docker build --no-cache -t spring-boot-app:latest .
```

#### Container Won't Start

Check logs:
```bash
docker logs task-api
```

#### Port Already in Use

Change host port mapping:
```bash
docker run -d -p 8081:8080 --name task-api spring-boot-app:latest
```

Or update `docker-compose.yml`:
```yaml
ports:
  - "8081:8080"
```

#### Application Not Accessible

Verify container is running:
```bash
docker ps
```

Check from inside container:
```bash
docker exec -it task-api sh
wget -O- http://localhost:8080/api/tasks/health
```

## Additional Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Maven Documentation](https://maven.apache.org/guides/)
- [REST API Best Practices](https://restfulapi.net/)
- [Spring Boot Actuator Guide](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html)

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/feature-name`)
3. Commit your changes (`git commit -m 'Add feature'`)
4. Push to the branch (`git push origin feature/feature-name`)
5. Open a Pull Request

## License

This project is licensed under the MIT License.

## Author

**Your Name**
- GitHub: [@AdityaJareda](https://github.com/AdityaJareda)
- LinkedIn: [Aditya Singh](www.linkedin.com/in/adityajareda)
- Email: adityajareda.work@gmail.com
