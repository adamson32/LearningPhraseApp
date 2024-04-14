
CREATE TABLE users (
                       id int PRIMARY KEY IDENTITY(1,1),
                       username NVARCHAR(50) UNIQUE NOT NULL,
                       password NVARCHAR(255) NOT NULL,
                       email NVARCHAR(255) UNIQUE NOT NULL,
                       enabled bit
);

CREATE TABLE roles (
                       id INT PRIMARY KEY,
                       name VARCHAR(50) NOT NULL
);

CREATE TABLE user_roles (
                            user_id INT,
                            role_id INT,
                            PRIMARY KEY (user_id, role_id),
                            FOREIGN KEY (user_id) REFERENCES users(id),
                            FOREIGN KEY (role_id) REFERENCES roles(id)
);

