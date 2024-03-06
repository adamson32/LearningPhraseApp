ALTER TABLE group_phrases
    ADD user_id INT;

ALTER TABLE group_phrases
    ADD CONSTRAINT fk_groupPhrases_users
        FOREIGN KEY (user_id)
            REFERENCES users(id);
