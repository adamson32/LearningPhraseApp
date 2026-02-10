CREATE TRIGGER trg_UpdateGroupNextDate
ON phrases
AFTER INSERT, UPDATE
AS
BEGIN
    UPDATE phrase_group
    SET next_date = (
        SELECT MIN(next_date)
        FROM phrases
        WHERE phrase_group_id = inserted.phrase_group_id
    )
    FROM phrase_group
    INNER JOIN inserted ON phrase_group.id = inserted.phrase_group_id;
END;
