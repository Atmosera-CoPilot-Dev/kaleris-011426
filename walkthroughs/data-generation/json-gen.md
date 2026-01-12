## Data Generation with JSON
```
Generate a sample JSON array of 10 rows of sample data. The data should include the following keys: 
- employee ID (unique and random in the range 1 to 100),
- department name (randomly assigned to one of the following departments: HR, Engineering, Sales),
- first name,
- last name,
- hourly rate ($25.00 to $75.00, standard deviation of $7.65)
Use international naming conventions for the names.

```
Modify the response from the previous question to include a new key called "hoursWorked" that is a random number between 20 and 40.

```

```
Assuming the data you provided in your response is consumable over http://localhost:6543/employees, I want to consume this JSON data using C# with the following criteria:
- All types must be in their own file within a root namespace 'HR.Employees'.
- The result should produce a list of Employee objects and adhere to SOLID design principles.
- The result must include a minimal service and a contract.
- Securely handle user authentication for this service using JWT tokens.

```




