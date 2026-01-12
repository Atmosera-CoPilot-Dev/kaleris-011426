/*
This code has several issues that need to be addressed. Use Copilot to help you fix them.
*/

const mysql = require('mysql');

class LoginManager {
    constructor(dbConfig) {
        this.connection = mysql.createConnection(dbConfig);
    }

    connectToDb() {
        this.connection.connect((err) => {
            if (err) {
                console.error('Error connecting to the database:', err.stack);
                return;
            }
            console.log('Connected to the database');
        });
    }

    login(username, password) {
        this.connectToDb();

        const query = `SELECT * FROM users WHERE username = '${username}' AND password = '${password}'`;
        this.connection.query(query, (error, results) => {
            if (error) {
                console.error('Error executing query:', error.stack);
                return;
            }

            if (results.length > 0) {
                console.log('Login successful');
            } else {
                console.log('Invalid username or password');
            }
        });
    }
}

// Example usage:
const dbConfig = {
    host: 'localhost',
    user: 'root',
    password: 'password',
    database: 'testdb'
};

const loginManager = new LoginManager(dbConfig);
loginManager.login('admin', 'password123');