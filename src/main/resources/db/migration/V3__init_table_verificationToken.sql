CREATE TABLE verification_token (
                                   id int PRIMARY KEY IDENTITY(1,1),
                                   token NVARCHAR(255),
                                   user_id int,
                                   expiry_date DATETIME,
                                   FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE INDEX idx_token ON verification_token (token);

CREATE INDEX idx_expiryDate ON verification_token (expiry_date);
