CREATE TRIGGER trg_UpdateGroupNextDate
ON phrases
AFTER INSERT, UPDATE
AS
BEGIN
    UPDATE group_phrases
    SET next_date = (
        SELECT MIN(next_date)
        FROM phrases
        WHERE group_phrases_id = inserted.group_phrases_id
    )
    FROM group_phrases
    INNER JOIN inserted ON group_phrases.id = inserted.group_phrases_id;
END;
