# Distributed System Final Project
## Keycloak OIDC Login Implementation

### Prerequisites
Before you start, ensure you have the following installed on your system:
- Docker
- Docker Compose

### Running Keycloak Server (Auth Server)
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

### Running JPetStore (OIDC Client)
```bash
# Navigate to the JPetStore directory 
cd jpetstore

./mvnw clean spring-boot:run

# Build a jar file
./mvnw clean package -DskipTests=true

# Run java command
java -jar target/mybatis-spring-boot-jpetstore-2.0.0-SNAPSHOT.jar
```

Once the JPetStore server is successfully started, it will be accessible via: http://localhost:8080/

### Expected Result for Current Stage

After entering the JPetStore homepage, click on "Login with OIDC" in the top menu. Log in using the default credentials:

- Username: `user1`
- Password: `user1password`

You should be able to log in successfully and be redirected to a page that displays the complete user information from the Keycloak server.
### Reference
- OIDC Login flow: https://docs.google.com/presentation/d/1CiAiuay5rd1KDDnYwOyu6ud9xk5ZetSQDOMp9DYUKjs/edit?pli=1#slide=id.g8bb7b0e120_0_0