INSERT INTO roles (id, name) VALUES
                                      (1, 'ADMIN'),
                                      (2, 'USER');
INSERT INTO users (username, password, email, enabled) VALUES
                                                           ('admin', '$2a$10$wEkBtSGacysu1N2I8zOQYeg1fugzkrhS7k.GaBMRTjmEex5P2WV4m', 'admin@example.com', 1);


INSERT INTO user_roles (user_id, role_id) VALUES
                                              (1, 1);