# User Case Point Application

A Spring Boot application for managing user case points.

## Prerequisites

- Java 17 or higher
- Maven
- MySQL (local development)

## Local Development

1. Clone this repository
2. Configure your local MySQL database in `application.properties` or use the default settings
3. Run the application with:

```bash
./mvnw spring-boot:run
```

## Deployment on Render.com

This application is configured for deployment on Render.

### Required Environment Variables

For the application to connect to the database, the following environment variables must be set in your Render dashboard:

- `MYSQL_URL`: The JDBC URL for your MySQL database
  - Format: `jdbc:mysql://host:port/database_name?useSSL=false&allowPublicKeyRetrieval=true`
- `MYSQL_ROOT_USER`: The database username
- `MYSQL_ROOT_PASSWORD`: The database password

### Steps to Deploy

1. Create a Render account if you don't have one
2. Set up a MySQL database:
   - You can use Render's PostgreSQL service, or
   - Use an external MySQL provider like PlanetScale, AWS RDS, or Digital Ocean
   - Make note of the database URL, username, and password

3. Deploy your application on Render:
   - Go to your Render dashboard
   - Click "New" → "Web Service"
   - Connect your GitHub repository
   - Choose "Docker" as the environment
   - Set the required environment variables (MYSQL_URL, MYSQL_ROOT_USER, MYSQL_ROOT_PASSWORD)
   - Deploy the service

4. Verify the deployment:
   - Check the logs for successful connection to the database
   - Access the health check endpoint at `/health` to confirm the service is running

## Troubleshooting

### Database Connection Issues

If you encounter database connection errors:

1. Verify that the environment variables are correctly set
2. Check if your database service is accessible from Render
   - Some database providers require configuring network access
3. Try modifying the connection pool settings in `application.properties`
4. Make sure your database is properly initialized

### Port Configuration

The application is configured to use the port specified by Render in the PORT environment variable. By default, it uses port 8080.

## License

[Your License]
