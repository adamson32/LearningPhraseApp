ALTER TABLE phrase_group
    ADD user_id INT;

ALTER TABLE phrase_group
    ADD CONSTRAINT fk_phrase_group_users
        FOREIGN KEY (user_id)
            REFERENCES users(id);
