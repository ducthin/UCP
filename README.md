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

## Deployment on Railway

This application is configured for deployment on Railway.

### Required Environment Variables

For the application to connect to the database, the following environment variables must be set in your Railway project:

- `MYSQL_URL`: The JDBC URL for your MySQL database
- `MYSQL_ROOT_USER`: The database username
- `MYSQL_ROOT_PASSWORD`: The database password

### Steps to Deploy

1. Create a Railway account if you don't have one
2. Add a MySQL service to your project:
   - Go to your Railway dashboard
   - Click "New Project" → "Provision MySQL"
   - Once created, navigate to the MySQL service and go to the "Variables" tab
   - Note the automatically generated variables (`MYSQL_URL`, `MYSQL_ROOT_USER`, and `MYSQL_ROOT_PASSWORD`)

3. Deploy your application:
   - Connect your GitHub repository or use the Railway CLI
   - Link your application service to the MySQL service:
     - In your application service, go to "Variables" tab
     - Click "Add from other service" and select your MySQL service

4. Verify that your application can connect to the database by checking the logs.

## Troubleshooting

### Database Connection Issues

If you encounter database connection errors:

1. Verify that the environment variables are correctly set
2. Check if your MySQL service is running
3. Ensure your application service is linked to the MySQL service

### Application Port

The application runs on port 8080 by default. Railway will automatically assign a public URL to your service.

## License

[Your License]
