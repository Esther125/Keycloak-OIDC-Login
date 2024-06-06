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