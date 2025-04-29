# Deployment Guide for User Case Point on Render

This document provides guidance for deploying the User Case Point application on Render.com.

## Prerequisites

1. A Render.com account
2. A MySQL database instance (either through Render or an external provider like AWS RDS)

## Setting Up Your Database

1. Set up a MySQL database instance (version 8.0+ recommended).
2. Note the following credentials:
   - Database URL (in format: `jdbc:mysql://hostname:port/database_name?parameters`)
   - Username
   - Password

## Deploying to Render

1. Push your changes to your Git repository.
2. Log in to your Render dashboard and create a new Web Service.
3. Connect your Git repository and select the repository containing your User Case Point application.
4. Choose "Docker" as the environment.
5. Configure the following environment variables in the Render dashboard under "Environment":

   | Key | Value |
   |-----|-------|
   | MYSQL_URL | Your full MySQL JDBC URL |
   | MYSQL_ROOT_USER | Your MySQL username |
   | MYSQL_ROOT_PASSWORD | Your MySQL password |
   | PORT | 10000 |
   | DATABASE_ENABLED | true |

   Note: Make sure your MYSQL_URL includes the necessary parameters: `?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true`

6. If you need to run the application initially without a database, set:
   - DATABASE_ENABLED = false (application will start without requiring database connectivity)
   - SPRING_PROFILES_ACTIVE = disable-database

7. Save the settings and deploy your application.

## Troubleshooting

1. **Database Connection Issues**:
   - Verify the database credentials are correct.
   - Ensure your database is accessible from Render's servers (check IP whitelisting if needed).
   - Check the logs in the Render dashboard for detailed error messages.

2. **Port Configuration Issues**:
   - The application is configured to run on port 8080 by default.
   - Render dynamically assigns a port via the PORT environment variable, which our Dockerfile is configured to use.

3. **Hibernate Dialect Issues**:
   - The application is configured to use MySQL dialect by default.
   - If you're using a different database, update the `spring.jpa.properties.hibernate.dialect` property.

## Local Testing

To test the Docker build locally before deploying:

```powershell
docker build -t usercasepoint .
docker run -p 8080:8080 -e MYSQL_URL=your_db_url -e MYSQL_ROOT_USER=your_user -e MYSQL_ROOT_PASSWORD=your_password usercasepoint
```
