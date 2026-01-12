## Section 1- Planning a web api application using Go:

You are a senior software engineer with expertise in the Go programming language.

Assist me in planning a web api application using Go. The app will consist of 3 parts (packages) in module 'GoREST': a data library for a SQLite database (inventory.db), a web api, and integration testing. I need the following information before moving on to the initial design:
- Provide a detailed explanation of how to set up the application, including any dependencies and configuration files needed. 
- Describe and visually render the folder structure of the application and the purpose of each package.
- Include instructions for running the application locally and deploying it to a server. 
- Provide examples of how to use the web api, including sample requests and responses. 
- Include any additional information that would be helpful for someone looking to understand and work with the application.
- Provide a detailed explanation of how to set up the application, including any dependencies and configuration files needed.
- Include a section on best practices for API development and security considerations.


## Section 2- High-level design and implementation details:

1. Define a data library to contain a SQLite inventory database with an ORM (Object-Relational Mapping) that can handle migrations and seeding of the database. The database will be created by a user-provided sqlite script '/scripts/inventory.sql' that describes an sqlite database with 3 tables (suppliers, categories, products), a single view (product_list), and will reside in 'SQLite/inventory.db'. The ORM should be able to handle basic CRUD operations (Create, Read, Update, Delete) on the inventory items. The data library should also implement a caching mechanism to improve performance for frequently accessed data.

2. Define a web api that provides REST over GET and POST protocols. The web api should implement input validation for incoming requests. The web api should also implement error handling for invalid inputs and provide meaningful error messages to the client and appropriate HTTP status codes. The web api should also implement logging of requests and responses, and a health check endpoint that returns a 200 OK status code and a message indicating that the service is running.

3. Define a testing framework for integration testing which contains test methods against an in-memory mock of the database. The tests should cover all endpoints of the web api and ensure that the input validation and error handling are working as expected. The tests should also cover the rate limiting and CORS handling of the web api.

## Section 3- Future enhancements:

- Define a rate limiting middleware that limits the number of requests from a single IP address to 100 requests per minute. The middleware should also implement CORS handling to allow requests from any origin.

- Define a logging middleware that logs all incoming requests and outgoing responses to a file. The log should include the request method, URL, status code, and response time. The logging middleware should also support configurable log levels and log rotation to manage log file sizes.


