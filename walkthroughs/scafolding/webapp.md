
These steps build a web app that uses Node.js and Express with React components and Jest testing.

Step 1: Scaffold the web app that will use NodeJS and Express, then create the project structure and provide a basic setup for the server and client side.
Use these recommended versions:
- Node.js: 20.x
- Express: 4.18.x 
- React: 18.x 
- React Router: 6.x
- React Testing Library: 14.x
- Jest: 29.x 
Add the components as follows:
'Welcome' - A simple component that display a random welcome message.
'TodoList' - A component that displays a list of todo items with the ability to add and remove items.
- Configure Jest to work with the React components.

Step 2: Refactor to use routing for each component. For example the default url would be a welcome page, http://localhost:9000/Welcome would display the welcome component, etc.

Step 3: Add a simple API endpoint in the Express server that returns a list of todo items. The endpoint should be accessible at http://localhost:9000/api/todos.

Step 4: Modify the 'TodoList' component to fetch the list of todo items from the API endpoint created in Step 3 and display them. Ensure that the component can still add and remove items locally.

Step 5: Write unit tests using Jest for both the 'Welcome' and 'TodoList' components. Ensure that the tests cover rendering, adding, and removing items in the 'TodoList' component.

Step 6: Write integration tests using React Testing Library to verify that the 'TodoList' component correctly fetches data from the API endpoint and displays it. Use Jest and a testing library like React Testing Library for this purpose.