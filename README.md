# Distributed System Final Project
## Keycloak OIDC Login Implementation

### Prerequisites
Before you start, ensure you have the following installed on your system:
- Docker
- Docker Compose

### Running Keycloak Server
To run the Keycloak server, follow these steps:

```bash
# Navigate to the Keycloak directory 
cd keycloak

# Build the Docker images
docker-compose build

# Start the Keycloak server
docker-compose up -d
```

Once the Keycloak server is successfully started, it will be accessible via: https://localhost:8443/

You can use the default admin account to log in to the admin page. Here are the credentials:
- **Username:** `admin`
- **Password:** `password`

You can visit the following URL to test the login functionality for `myrealm` users:
https://localhost:8443/realms/myrealm/account?loginStyle=popup&client_id=myclient&response_type=code
- **Username:** `user1`
- **Password:** `user1password`



## Jpetstore
### Project Information

- Project URL: [mybatis-spring-boot-jpetstore](https://github.com/kazuki43zoo/mybatis-spring-boot-jpetstore.git)
- Reference: [Baeldung - Spring Boot Keycloak](https://www.baeldung.com/spring-boot-keycloak)
- Keycloak is configured to run on `localhost:8180`
  - Start Keycloak with the command: `bin\kc.bat start-dev --http-port=8180`

### Environment Setup

  1. **Download and Install Java 17**
     - Ensure you have JDK 17 installed. JDK 20/22 are too new and will not work; you need to downgrade to JDK 17.
     - Verify your Java installation:

       ```bash
       javac -version
       ```
  2. **Set JAVA_HOME**
     - Ensure `JAVA_HOME` is set to your JDK 17 installation directory.

### Running Jpetstore

  **Navigate to the project directory and run the application:**
     
  ```bash
    cd mybatis-spring-boot-jpetstore
     ./mvnw clean spring-boot:run
  ```
    
### Modification
  1. **Pom.xml**
     - Purpose: Use the Spring Security OAuth2.0 Client to connect to the Keycloak server..
     - Dependencies added to lines 156-164
    
  2. **\src\main\resources\application.properties**
     - Keycloak settings start from line 22

  3. **src\main\java\com\kazuki43zoo\jpetstore\config\WebSecurityConfig.java**
     - Changes start from line 72
     
  

