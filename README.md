# README

## Overview
This project implements a RESTful API for managing a simple blogging platform. The API supports creating, retrieving, and managing blog posts and their associated comments

## Swagger API Documentation
The following endpoints are available:

- **GET /api/posts**: Retrieve all blog posts.
- **POST /api/posts**: Create a new blog post.
- **GET /api/posts/{id}**: Get a blog post by ID.
- **POST /api/posts/{postId}/comments**: Add a comment to a blog post


> Improve Development Experience
- Error Handling
- Default validation is provided for basic error handling.
- Status codes include support for:
  - 2xx: Success
  - 4xx: Client errors
  - 5xx: Server errors
- HATEOAS
- Future enhancements include additional field validation using annotations like `@Size` or custom validators
- Non requirements like pagination, media types XML, text, DELETE, PATH, etc.
- Export swagger file for production

### API Versioning
- Currently, versioning is not implemented
- For minor changes, consider adding an `API-Version` to the headers
- For major changes, include `API-Version` in the path to phase out older versions

## Testing
- JUnit is used for unit testing.
- Future steps include load testing to simulate high traffic scenarios

## Logging and Monitoring
- Logback is configured using XML for customization
- Future plans include integrating:
  - Prometheus or Grafana for enhanced observability
  - AWS CloudWatch for detailed monitoring and alerts
- CloudWatch Alarms are used for redundancy checks
- Define SLAs for response times and error rates

## Notifications
- Automatically notify the BlogPost owner when new comments are added
- The code does not cover it notification, as well files, images, tags, metadata as other products

## Scalability
- the current API uses in-memory storage and basic request handling

### Cloud Deployment and Scaling
Assuming AWS as the cloud provider:
- **Caching**: Use AWS ElastiCache (Redis) for improved performance
- **Storage**: Transition to DynamoDB for scalable and reliable storage
- **Logging**: Utilize AWS CloudWatch for Lambda logs
- **Server**: Deploy the API as a serverless application using AWS Lambda
  - Configure AWS Spring support
  - Use GraalVM to reduce startup times and cold starts
  - Enable auto-scaling of Lambda functions
  - Implement throttling to optimize cost efficiency

### Security
- Use AWS API Gateway for caching and performance improvements
  - Implement rate-limiting and throttling.
- Secure the API with AWS Cognito for scalable authentication

### CI/CD Pipeline
- Use GitHub Actions for a streamlined deployment pipeline

### Redundancy
- AWS Lambda, DynamoDB, and Cognito handle multiple availability zones for high availability
- AWS Gateway operates as a regional service across multiple AZs
- Enable multi-AZ mode in ElastiCache
- Use AWS Backup for DynamoDB and, in the future, for S3 data

## Next Steps
The current implementation covers core requirements for production readiness. Future steps include:
- Transitioning to DynamoDB or another persistent storage solution
- Load testing and performance optimization
- Adding Prometheus/Grafana for advanced observability
- Enhancing notifications using AWS SES or similar services
- Building a frontend to consume the API

## Instructions to Run the Application
### Prerequisites
1. Install [Java 21](https://adoptium.net/) on your system.
2. Install [Maven](https://maven.apache.org/) for building and running the application.

### Steps to Run
1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd <repository-directory>
   ```
2. Build the project:
   ```bash
   mvn clean install
   ```
3. Run the application:
   ```bash
   mvn spring-boot:run
   ```
4. Access the API:
   - Swagger UI: `http://localhost:8080/swagger-ui.html`
   - API Base URL: `http://localhost:8080/api/posts`

### Running Tests
Run the unit tests using the following command:
```bash
mvn test
```

## Summary
This implementation provides a robust starting point for a blogging platform with a focus on development experience, scalability, low latency, and cost efficiency
