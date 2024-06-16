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
Run with Maven Command
```bash
# Navigate to the JPetStore directory 
cd jpetstore

./mvnw clean spring-boot:run
```
Run with Java command
```bash
# Build a jar file
./mvnw clean package -DskipTests=true

# Run java command
java -jar target/mybatis-spring-boot-jpetstore-2.0.0-SNAPSHOT.jar
```

Once the JPetStore server is successfully started, it will be accessible via: http://localhost:8080/

### Expected Result

After entering the JPetStore homepage, click on "Login in" in the top menu. Log in Keycloak server using the default credentials:

- Username: `user1`
- Password: `user1password`

You should be able to log in successfully and can use JPetStore normally.

### Possible Solution for : PKIX path building failed error
Export the SSL Certificate from Keycloak Container.
    
  ```bash
  # Access the Keycloak container's bash shell
  docker exec -it mykeycloak /bin/bash
  
  # Export the SSL certificate from the Keycloak server's keystore    
  keytool -export -alias server -keystore /opt/keycloak/conf/server.keystore -storepass password -file /tmp/server.crt
  
  # Locate the `server.crt` file within the Docker Desktop interface under the `tmp` directory of your container's image. 
  # Download this certificate file to your host machine.
   ```

Import the Certificate into the Java Keystore on Your Host:

  ```bash
  "C:\Program Files\Java\jdk-17\bin\keytool.exe" -importcert -file "[path\to\server.crt]" -alias "keycloak-server" -keystore "C:\Program Files\Java\jdk-17\lib\security\cacerts" -storepass changeit
  ```
**NOTICE:** The path to keytool.exe provided above is based on a typical installation of JDK 17 on Windows. If you have a different version of Java installed, or if Java is installed in a different location, you will need to adjust the path accordingly.
### Reference
- OIDC Login flow: https://docs.google.com/presentation/d/1CiAiuay5rd1KDDnYwOyu6ud9xk5ZetSQDOMp9DYUKjs/edit?pli=1#slide=id.g8bb7b0e120_0_0
